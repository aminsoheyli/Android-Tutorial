package com.aminsoheyli.trelloclone.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.aminsoheyli.trelloclone.R
import com.aminsoheyli.trelloclone.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : BaseActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
        auth = FirebaseAuth.getInstance()
    }

    private fun initUi() {
        setupActionBar()
        binding.toolbarSignUpActivity.setNavigationOnClickListener { onBackPressed() }
        binding.btnSignUp.setOnClickListener { registerUser() }
    }

    private fun setupActionBar() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setSupportActionBar(binding.toolbarSignUpActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }
    }

    private fun registerUser() {
        val name = binding.etName.text.toString().trim { it <= ' ' }
        val email = binding.etEmail.text.toString().trim { it <= ' ' }
        val password = binding.etPassword.text.toString().trim { it <= ' ' }

        if (validateForm(name, email, password)) {
            showProgressDialog(resources.getString(R.string.please_wait))
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        val firebaseUser = task.result.user
                        val registeredEmail = firebaseUser?.email
                        Toast.makeText(
                            this,
                            "$name you have successfully registered the email address",
                            Toast.LENGTH_SHORT
                        ).show()
                        auth.signOut()
                        finish()
                    } else
                        Toast.makeText(
                            this@SignUpActivity, "Registration failed", Toast.LENGTH_SHORT
                        ).show()
                }
        }
    }

    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("Please enter name.")
                false
            }
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter email.")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter password.")
                false
            }
            else -> {
                true
            }
        }
    }
}
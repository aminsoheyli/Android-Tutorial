package com.aminsoheyli.trelloclone.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.aminsoheyli.trelloclone.R
import com.aminsoheyli.trelloclone.databinding.ActivitySignInBinding
import com.aminsoheyli.trelloclone.firebase.Firestore
import com.aminsoheyli.trelloclone.models.User
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : BaseActivity() {
    private val TAG = "SignInLogTag"
    private lateinit var binding: ActivitySignInBinding
    private lateinit var auth: FirebaseAuth

    companion object {
        const val KEY_USER = "user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
        auth = FirebaseAuth.getInstance()
    }

    private fun initUi() {
        setupActionBar()
        binding.btnSignIn.setOnClickListener { signInRegisteredUser() }
    }

    private fun setupActionBar() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setSupportActionBar(binding.toolbarSignInActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }
        binding.toolbarSignInActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun signInRegisteredUser() {
        val email = binding.etEmail.text.toString().trim { it <= ' ' }
        val password = binding.etPassword.text.toString().trim { it <= ' ' }
        if (validateForm(email, password)) {
            showProgressDialog(resources.getString(R.string.please_wait))
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful)
                        Firestore().signInUser(this)
                    else {
                        hideProgressDialog()
                        task.exception?.printStackTrace()
                        Toast.makeText(
                            baseContext, "Username/Password is incorrect", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    fun onUserSignInSuccess(user: User) {
        hideProgressDialog()
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(KEY_USER, user)
        startActivity(intent)
        finish()
    }

    private fun validateForm(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter email.")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter password.")
                false
            }
            else -> true
        }
    }
}
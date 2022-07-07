package com.aminsoheyli.tic_tac_toe_online

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


const val TAG_SIGN_IN = "MainActivity/Login"
const val TAG_SIGN_UP = "MainActivity/Signup"

class MainActivity : AppCompatActivity() {
    private lateinit var buttonInvite: Button
    private lateinit var buttonAccept: Button
    private lateinit var buttonLogin: Button
    private lateinit var editeTextYourEmail: EditText
    private lateinit var editTextInviteEmail: EditText

    // Firebase
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var auth: FirebaseAuth
    private lateinit var authListener: FirebaseAuth.AuthStateListener
    private var userEmail: String? = null
    val database = Firebase.database("https://tictactoeonline-dccd7-default-rtdb.firebaseio.com")
    val usersRef = database.getReference("users")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Firebase
        initFirebase()
        initUi()
    }

    private fun initFirebase() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        auth = FirebaseAuth.getInstance();
        authListener = FirebaseAuth.AuthStateListener {
            val user = it.currentUser
            if (user != null) {
                Log.d(TAG_SIGN_IN, "onAuthStateChanged: Signed_in: ${user.uid}")
                userEmail = user?.email
                buttonLogin.isEnabled = false
                editeTextYourEmail.setText(userEmail)
                editeTextYourEmail.isEnabled = false
                usersRef.child(beforeAt(userEmail.toString())).child("request")
                    .setValue(user?.uid)
            } else
                Log.d(TAG_SIGN_IN, "onAuthStateChanged: Signed_out:")
        }
    }

    private fun initUi() {
        buttonInvite = findViewById(R.id.button_invite)
        buttonAccept = findViewById(R.id.button_accept)
        buttonLogin = findViewById(R.id.button_login)
        editeTextYourEmail = findViewById(R.id.editText_your_email)
        editTextInviteEmail = findViewById(R.id.editText_invite_email)

        buttonInvite.setOnClickListener {
            Log.d("Invite", editeTextYourEmail.text.toString())
        }

        buttonAccept.setOnClickListener {
            Log.d("Accept", editeTextYourEmail.text.toString())
        }

        buttonLogin.setOnClickListener {
            userLogin(editeTextYourEmail.text.toString())
        }
    }

    private fun userLogin(email: String, password: String = "somerandpass1234") {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { taskSignUp ->
                Log.d(TAG_SIGN_UP, "createUserWithEmail:onComplete:" + taskSignUp.isSuccessful)
                if (!taskSignUp.isSuccessful) {
                    Log.w(TAG_SIGN_UP, "createUserWithEmail:failure", taskSignUp.exception)
                    Toast.makeText(applicationContext, "Sing up failed", Toast.LENGTH_SHORT).show()
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { taskSignIn ->
                            if (taskSignIn.isSuccessful)
                                Log.d(TAG_SIGN_IN, "signInWithEmail:success")
                            else
                                Log.w(TAG_SIGN_IN, "signInWithEmail:failure", taskSignIn.exception)
                        }
                }
            }
    }

    private fun beforeAt(email: String): String = email.split(Regex("@"))[0]

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        auth.addAuthStateListener(authListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authListener)
    }

    fun onButtonClick(view: View) {

    }
}
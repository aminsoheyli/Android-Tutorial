package com.aminsoheyli.tic_tac_toe_online

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


const val TAG_SIGN_IN = "Main/Login"
const val TAG_SIGN_UP = "Main/Signup"
const val TAG_INVITE_REQUEST = "Main/UserRequest"


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
    private var userUID: String? = null
    val database = Firebase.database("https://tictactoeonline-dccd7-default-rtdb.firebaseio.com")
    val usersRef = database.getReference("users")
    val ref = database.reference;


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
                userEmail = user.email
                userUID = user.uid
                Log.d(TAG_SIGN_IN, "onAuthStateChanged: Signed_in: $userUID")
                buttonLogin.isEnabled = false
                editeTextYourEmail.setText(userEmail)
                editeTextYourEmail.isEnabled = false
                handleIncomingRequest()
//                usersRef.child(beforeAt(userEmail.toString())).child("request")
//                    .setValue(userUID)
//                ref.child("users").child(beforeAt(userEmail!!)).child("request").setValue(userUID)
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
            usersRef.child(beforeAt(editTextInviteEmail.text.toString()))
                .child("request")
                .push()
                .setValue(userEmail)
            startGame(beforeAt(editTextInviteEmail.text.toString()) + ":" + beforeAt(userEmail!!))
        }

        buttonAccept.setOnClickListener {
            usersRef
                .child(beforeAt(editTextInviteEmail.text.toString())).child("request").push()
                .setValue(userEmail);
            startGame(beforeAt(userEmail!!) + ":" + beforeAt(editTextInviteEmail.text.toString()))
        }

        buttonLogin.setOnClickListener {
            userLogin(editeTextYourEmail.text.toString())
        }
    }

    private fun startGame(playerGameId: String) {
        ref.child("playing").child(playerGameId).removeValue()
    }

    private fun handleIncomingRequest() {
        // Read from the database
        usersRef.child(beforeAt(userEmail.toString())).child("request")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value is HashMap<*, *>) {
                        val dataTable = snapshot.value as HashMap<String, Any>
                        if (dataTable != null) {
                            var value = ""
                            for (key in dataTable.keys) {
                                value = dataTable[key] as String
                                Log.d(TAG_INVITE_REQUEST, value)
                                editTextInviteEmail.setText(value)
                                usersRef.child(beforeAt(userEmail!!))
                                    .child("request")
                                    .setValue(userUID)
                                changeBackgroundColor()
                                break
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun changeBackgroundColor() {
        editTextInviteEmail.setBackgroundColor(Color.GREEN)
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
        auth.addAuthStateListener(authListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authListener)
    }

    fun onButtonClick(view: View) {

    }
}
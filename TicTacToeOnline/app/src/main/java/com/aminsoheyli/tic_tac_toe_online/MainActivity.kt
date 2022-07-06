package com.aminsoheyli.tic_tac_toe_online

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : AppCompatActivity() {
    private lateinit var buttonInvite: Button
    private lateinit var buttonAccept: Button
    private lateinit var buttonLogin: Button
    private lateinit var editeTextYourEmail: EditText
    private lateinit var editTextYourFriendEmail: EditText

    // Firebase
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Firebase
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        initUi()
    }

    private fun initUi() {
        buttonInvite = findViewById(R.id.button_invite)
        buttonAccept = findViewById(R.id.button_accept)
        buttonLogin = findViewById(R.id.button_login)
        editeTextYourEmail = findViewById(R.id.editText_your_email)
        editTextYourFriendEmail = findViewById(R.id.editText_your_friend_email)

        buttonInvite.setOnClickListener {

        }

        buttonAccept.setOnClickListener {

        }

        buttonLogin.setOnClickListener {

        }
    }

    fun onButtonClick(view: View) {

    }
}
package com.aminsoheyli.firebase

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var editTextUsername: EditText
    lateinit var editTextPasssword: EditText
    lateinit var editTextAge: EditText
    lateinit var textViewMessage: TextView
    lateinit var buttonSignUp: Button
    lateinit var mAdView: AdView
    lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Realtime Database
        database = Firebase.database(getString(R.string.firebase_real_time_database_url))

        initUi()
        // Banner Ad by AdMob
        firebaseBannerAd()
    }

    private fun initUi() {
        editTextUsername = findViewById(R.id.editText_username)
        editTextPasssword = findViewById(R.id.editText_password)
        editTextAge = findViewById(R.id.editText_age)
        buttonSignUp = findViewById(R.id.button_signup)
        textViewMessage = findViewById(R.id.textView_message)

        editTextUsername.doOnTextChanged { _, _, _, _ -> onTextChanged() }
        editTextPasssword.doOnTextChanged { _, _, _, _ -> onTextChanged() }
        editTextAge.doOnTextChanged { _, _, _, _ -> onTextChanged() }


        buttonSignUp.setOnClickListener {
            if (editTextUsername.text.isEmpty() || editTextPasssword.text.isEmpty() || editTextAge.text.isEmpty()) {
                Toast.makeText(this, "Enter all fields", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val username = editTextUsername.text.toString()
            val password = editTextPasssword.text.toString()
            val age = editTextAge.text.toString().toInt()
            signUp(username, password, age)
        }

        initMessage()
    }

    private fun initMessage() {
        val msgRef = database.getReference("msg")

        msgRef.get().addOnSuccessListener {
            textViewMessage.setText(it.value as String)
        }

        msgRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue<String>()
                textViewMessage.setText(value)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to read value.", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun signUp(username: String, password: String, age: Int) {
        database.getReference("users").apply {
            child(username).child("username").setValue(username)
            child(username).child("password").setValue(password)
            child(username).child("age").setValue(age)
        }
    }

    private fun onTextChanged() {
        buttonSignUp.isEnabled =
            !(editTextUsername.text.isEmpty() || editTextPasssword.text.isEmpty() || editTextAge.text.isEmpty())
    }

    private fun firebaseBannerAd() {
        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }
}
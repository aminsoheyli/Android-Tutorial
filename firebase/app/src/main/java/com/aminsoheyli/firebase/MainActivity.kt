package com.aminsoheyli.firebase

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    lateinit var editTextUsername: EditText
    lateinit var editTextPasssword: EditText
    lateinit var editTextAge: EditText
    lateinit var textViewMessage: TextView
    lateinit var buttonSignUp: Button
    lateinit var mAdView: AdView
    lateinit var database: FirebaseDatabase

    lateinit var auth: FirebaseAuth
    lateinit var authListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = Firebase.database(getString(R.string.firebase_real_time_database_url))

        initFirebaseRemoteConfig()

        initFirebaseBannerAd()

        initFirebaseCrashReport()

        initFirebaseStorage()

        initFirebaseAuth()

        initUi()
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authListener)
    }

    private fun initFirebaseAuth() {
        auth = FirebaseAuth.getInstance()
        authListener = FirebaseAuth.AuthStateListener {
            val user = it.currentUser
            if (user != null)
                Log.d("SignIn", user.uid)
            else
                Log.d("SignOut", "User signed out")

        }
        findViewById<Button>(R.id.button_signin_anonymously).setOnClickListener {
            auth.signInAnonymously().addOnCompleteListener {
                if (!it.isSuccessful)
                    Log.w("ErrorLogin", it.exception)
            }
        }
    }


    private fun initFirebaseStorage() {
        val storage = Firebase.storage
        val imageViewTest = findViewById<ImageView>(R.id.imageView_cloud_test)
        imageViewTest.setOnClickListener {
            val storageRef = storage.getReferenceFromUrl("gs://fir-demo-52002.appspot.com")

            val testRef = storageRef.child("images/test.png")

            imageViewTest.isDrawingCacheEnabled = true
            imageViewTest.buildDrawingCache()
            val bitmap = (imageViewTest.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val data = baos.toByteArray()

            var uploadTask = testRef.putBytes(data)
            uploadTask.addOnFailureListener {
                Toast.makeText(this, "Failed to save on the cloud storage", Toast.LENGTH_LONG)
                    .show()
            }.addOnSuccessListener { taskSnapshot ->
                Toast.makeText(this, "Saved on the cloud storage", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initFirebaseCrashReport() {
        val crashButton = findViewById<Button>(R.id.button_test_crash)
        crashButton.setOnClickListener {
            Firebase.crashlytics.recordException(RuntimeException("Test Crash: recordException()"))
            throw RuntimeException("Test Crash: throw Exception()")
        }
    }

    private fun initFirebaseRemoteConfig() {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val minFetchInterval = 1L

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = minFetchInterval
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.fetch(minFetchInterval).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val price = remoteConfig.getString("price")
                Toast.makeText(applicationContext, price, Toast.LENGTH_LONG).show()
                remoteConfig.activate()
            }
        }
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

    private fun initFirebaseBannerAd() {
        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }
}
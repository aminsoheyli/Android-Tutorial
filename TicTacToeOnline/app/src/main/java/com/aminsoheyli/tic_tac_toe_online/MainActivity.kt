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
import java.util.*
import kotlin.collections.ArrayList


const val TAG_SIGN_IN = "Main/Login"
const val TAG_SIGN_UP = "Main/Signup"
const val TAG_INVITE_REQUEST = "Main/UserRequest"


class MainActivity : AppCompatActivity() {
    private lateinit var buttonInvite: Button
    private lateinit var buttonAccept: Button
    private lateinit var buttonLogin: Button
    private lateinit var editTextYourEmail: EditText
    private lateinit var editTextInviteEmail: EditText

    // Firebase
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var auth: FirebaseAuth
    private lateinit var authListener: FirebaseAuth.AuthStateListener
    private var userEmail = ""
    private var userUID = ""
    private var playerSession = ""
    private var mySign = "X"
    private var activePlayer = 1 // 1 for first , 2 for second
    private var player1 = ArrayList<Int>() // hold player 1 data
    private var player2 = ArrayList<Int>() // hold player 2 data

    private val database =
        Firebase.database("https://tictactoeonline-dccd7-default-rtdb.firebaseio.com")
    private val ref = database.reference
    private val usersRef = database.getReference("users")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Firebase
        initFirebase()
        initUi()
    }

    private fun initFirebase() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        auth = FirebaseAuth.getInstance()
        authListener = FirebaseAuth.AuthStateListener {
            val user = it.currentUser
            if (user != null) {
                userEmail = user.email.toString()
                userUID = user.uid
                Log.d(TAG_SIGN_IN, "onAuthStateChanged: Signed_in: $userUID")
                buttonLogin.isEnabled = false
                editTextYourEmail.setText(userEmail)
                editTextYourEmail.isEnabled = false
                usersRef.child(beforeAt(userEmail)).child("request").setValue(userUID)
                handleIncomingRequest()
            } else
                Log.d(TAG_SIGN_IN, "onAuthStateChanged: Signed_out:")
        }
    }

    private fun initUi() {
        buttonInvite = findViewById(R.id.button_invite)
        buttonAccept = findViewById(R.id.button_accept)
        buttonLogin = findViewById(R.id.button_login)
        editTextYourEmail = findViewById(R.id.editText_your_email)
        editTextInviteEmail = findViewById(R.id.editText_invite_email)

        buttonInvite.setOnClickListener {
            ref.child("users")
                .child(beforeAt(editTextInviteEmail.text.toString()))
                .child("request")
                .push()
                .setValue(userEmail)
            startGame(beforeAt(editTextInviteEmail.text.toString()) + ":" + beforeAt(userEmail))
            mySign = "X"
        }

        buttonAccept.setOnClickListener {
            ref.child("users")
                .child(beforeAt(editTextInviteEmail.text.toString()))
                .child("request")
                .push()
                .setValue(userEmail)
            startGame(beforeAt(userEmail) + ":" + beforeAt(editTextInviteEmail.text.toString()))
            mySign = "O"
        }

        buttonLogin.setOnClickListener {
            userLogin(editTextYourEmail.text.toString())
        }
    }

    private fun handleIncomingRequest() {
        // Read from the database
        ref.child("users").child(beforeAt(userEmail)).child("request")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value is HashMap<*, *>) {
                        val dataTable = snapshot.value as HashMap<String, Any>
                        if (dataTable != null) {
                            var value: String
                            for (key in dataTable.keys) {
                                value = dataTable[key] as String
                                Log.d(TAG_INVITE_REQUEST, value)
                                editTextInviteEmail.setText(value)
                                ref.child("users")
                                    .child(beforeAt(userEmail))
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
        if (view is Button && playerSession.isNotEmpty()) {
            var cellId = 0
            when (view.id) {
                R.id.button1 -> cellId = 1
                R.id.button2 -> cellId = 2
                R.id.button3 -> cellId = 3
                R.id.button4 -> cellId = 4
                R.id.button5 -> cellId = 5
                R.id.button6 -> cellId = 6
                R.id.button7 -> cellId = 7
                R.id.button8 -> cellId = 8
                R.id.button9 -> cellId = 9
            }
            ref.child("playing")
                .child(playerSession)
                .child("cellId:$cellId")
                .setValue(beforeAt(userEmail))
        }
    }

    private fun startGame(playerGameId: String) {
        playerSession = playerGameId
        ref.child("playing").child(playerGameId).removeValue()

        ref.child("playing").child(playerGameId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    player1.clear()
                    player2.clear()
                    activePlayer = 2
                    if (snapshot.value is HashMap<*, *>) {
                        val dataTable = snapshot.value as HashMap<String, Any>
                        if (dataTable != null) {
                            var value: String
                            for (key in dataTable.keys) {
                                value = dataTable[key] as String
                                activePlayer = if (value != beforeAt(userEmail))
                                    if (mySign == "X") 1 else 2
                                else
                                    if (mySign == "X") 2 else 1
                                val splitId = key.split(Regex(":"))
                                autoPlay(splitId[1].toInt())
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun playGame(cellId: Int, buSelected: Button) {
        Log.d("Player:", cellId.toString())
        if (activePlayer == 1) {
            buSelected.text = "X"
            buSelected.setBackgroundColor(Color.GREEN)
            player1.add(cellId)
        } else if (activePlayer == 2) {
            buSelected.text = "O"
            buSelected.setBackgroundColor(Color.BLUE)
            player2.add(cellId)
        }
        buSelected.isEnabled = false
        checkWinner()
    }

    private fun checkWinner() {
        var winner = -1
        //row 1
        if (player1.contains(1) && player1.contains(2) && player1.contains(3)) winner = 1
        if (player2.contains(1) && player2.contains(2) && player2.contains(3)) winner = 2
        //row 2
        if (player1.contains(4) && player1.contains(5) && player1.contains(6)) winner = 1
        if (player2.contains(4) && player2.contains(5) && player2.contains(6)) winner = 2
        //row 3
        if (player1.contains(7) && player1.contains(8) && player1.contains(9)) winner = 1
        if (player2.contains(7) && player2.contains(8) && player2.contains(9)) winner = 2
        //col 1
        if (player1.contains(1) && player1.contains(4) && player1.contains(7)) winner = 1
        if (player2.contains(1) && player2.contains(4) && player2.contains(7)) winner = 2
        //col 2
        if (player1.contains(2) && player1.contains(5) && player1.contains(8)) winner = 1
        if (player2.contains(2) && player2.contains(5) && player2.contains(8)) winner = 2
        //col 3
        if (player1.contains(3) && player1.contains(6) && player1.contains(9)) winner = 1
        if (player2.contains(3) && player2.contains(6) && player2.contains(9)) winner = 2
        if (winner != -1) {
            // We have winner
            setUnselectedButtonsDisable()
            if (winner == 1)
                Toast.makeText(this, "Player 1 is winner", Toast.LENGTH_LONG).show()
            else if (winner == 2)
                Toast.makeText(this, "Player 2 is winner", Toast.LENGTH_LONG).show()
        }
    }

    private fun setUnselectedButtonsDisable() {
        val selectedCells = (player1.clone() as ArrayList<Int>)
        selectedCells.addAll(player2)
        val unselectedCells = arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        unselectedCells.removeAll(selectedCells.toSet())
        unselectedCells.forEach { cellId ->
            val button = when (cellId) {
                1 -> findViewById<Button>(R.id.button1)
                2 -> findViewById(R.id.button2)
                3 -> findViewById(R.id.button3)
                4 -> findViewById(R.id.button4)
                5 -> findViewById(R.id.button5)
                6 -> findViewById(R.id.button6)
                7 -> findViewById(R.id.button7)
                8 -> findViewById(R.id.button8)
                9 -> findViewById(R.id.button9)
                else -> findViewById(R.id.button1)
            } as Button
            button.isEnabled = false
        }
    }


    fun autoPlay(cellId: Int) {
        val buttonSelected = when (cellId) {
            1 -> findViewById<Button>(R.id.button1)
            2 -> findViewById(R.id.button2)
            3 -> findViewById(R.id.button3)
            4 -> findViewById(R.id.button4)
            5 -> findViewById(R.id.button5)
            6 -> findViewById(R.id.button6)
            7 -> findViewById(R.id.button7)
            8 -> findViewById(R.id.button8)
            9 -> findViewById(R.id.button9)
            else -> findViewById(R.id.button1)
        } as Button
        playGame(cellId, buttonSelected)
    }
}
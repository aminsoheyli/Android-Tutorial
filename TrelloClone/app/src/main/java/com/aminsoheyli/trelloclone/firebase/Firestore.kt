package com.aminsoheyli.trelloclone.firebase

import android.util.Log
import com.aminsoheyli.trelloclone.models.User
import com.aminsoheyli.trelloclone.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Firestore {
    private val firestore = Firebase.firestore

    fun registerUser(userInfo: User, onRegisterSuccessListener: () -> Unit) {
        firestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                onRegisterSuccessListener.invoke()
            }.addOnFailureListener { e ->
                Log.e("Firestore", "Error writing document", e)
            }
    }

    fun signInUser(onLoginSuccessListener: (User) -> Unit) {
        firestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)
                if (loggedInUser != null)
                    onLoginSuccessListener.invoke(loggedInUser)
            }.addOnFailureListener { e ->
                Log.e("Firestore", "Error getting document", e)
            }
    }

    private fun getCurrentUserId(): String = FirebaseAuth.getInstance().currentUser!!.uid
}
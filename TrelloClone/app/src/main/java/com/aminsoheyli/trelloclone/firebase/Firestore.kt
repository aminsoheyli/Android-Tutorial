package com.aminsoheyli.trelloclone.firebase

import android.util.Log
import android.widget.Toast
import com.aminsoheyli.trelloclone.activities.*
import com.aminsoheyli.trelloclone.models.Board
import com.aminsoheyli.trelloclone.models.User
import com.aminsoheyli.trelloclone.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Firestore {
    private val firestore = Firebase.firestore

    companion object {
        fun getCurrentUserId(): String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    fun registerUser(userInfo: User, activity: SignUpActivity) {
        firestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.onUserRegistrationSuccess()
            }.addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e("Firestore", "Error writing document", e)
            }
    }

    fun loadUserData(activity: BaseActivity) {
        firestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)!!
                when (activity) {
                    is SignInActivity -> activity.onUserSignInSuccess(loggedInUser)
                    is MainActivity -> activity.updateNavigationUserDetails(loggedInUser)
                    is MyProfileActivity -> activity.setUserDataInUi(loggedInUser)
                }
            }.addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while getting loggedIn user details", e)
            }
    }

    fun createBoard(board: Board, activity: CreateBoardActivity) {
        firestore.collection(Constants.BOARDS)
            .document()
            .set(board, SetOptions.merge())
            .addOnSuccessListener {
                activity.onBoardCreatedSuccessfully()
            }.addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating a board.", e)
            }
    }

    fun updateUserProfileData(activity: MyProfileActivity, userHashMap: HashMap<String, Any>) {
        firestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "Profile Data updated successfully!")
                Toast.makeText(activity, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                activity.onProfileUpdateSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating a board.", e)
            }
    }
}
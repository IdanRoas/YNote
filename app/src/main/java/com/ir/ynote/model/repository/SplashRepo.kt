package com.ir.ynote.model.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.ir.ynote.dto.User

class SplashRepo {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollectionRef = firestore.collection("users")
    private val user: User?= User("","")
    var isUserAuthenticateInFirebaseMutableLiveData = MutableLiveData<User>()
    var isAuth =MutableLiveData<Boolean>()
    var authUser=MutableLiveData<User>()

    fun checkIfUserIsAuthenticatedInFirebase(): MutableLiveData<User> {

        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            user?.isAuthenticated = false
            user?.isAuthenticated=false
        } else {
            user?.uid= firebaseUser.uid
            user?.name= firebaseUser.displayName!!
            user?.isAuthenticated = true
            isUserAuthenticateInFirebaseMutableLiveData.value=user
        }
        authUser.value=user
        return isUserAuthenticateInFirebaseMutableLiveData
    }

    fun addUserToLiveData(userID: String): MutableLiveData<User> {
        val userMutableLiveData: MutableLiveData<User> = MutableLiveData()
        usersCollectionRef.document(userID).get()
            .addOnCompleteListener { userTask: Task<DocumentSnapshot?> ->
                if (userTask.isSuccessful) {
                    val document = userTask.result
                    if (document!!.exists()) {
                        val user: User? = document.toObject<User>(User::class.java)
                        userMutableLiveData.value = user
                    }
                } else {
                    Log.d("splash", userTask.exception!!.message)
                }
            }
        return userMutableLiveData
    }

}
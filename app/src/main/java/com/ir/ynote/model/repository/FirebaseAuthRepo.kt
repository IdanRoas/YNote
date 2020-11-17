package com.ir.ynote.model.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.ir.ynote.dto.User


class FirebaseAuthRepo {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val rootRef = FirebaseFirestore.getInstance()
    private val usersRef = rootRef.collection(USERS)
    var authenticatedUserMutableLiveData: MutableLiveData<User> = MutableLiveData()
    companion object{
        const val USERS="users"
    }


    fun firebaseSignInWithGoogle(googleAuthCredential: AuthCredential): MutableLiveData<User> {

         firebaseAuth.signInWithCredential(googleAuthCredential).addOnCompleteListener {
            if (it.isSuccessful) {
                var firebaseUser = firebaseAuth.currentUser;
                if (firebaseUser != null) {
                    var user = User(firebaseUser.uid, firebaseUser.displayName);
                    user.isNew = it.result?.additionalUserInfo?.isNewUser
                    authenticatedUserMutableLiveData.value=user
                }
            } else {
                Log.d("auth_error", it.exception?.message)
            }
        }
        return authenticatedUserMutableLiveData;
    }


    fun createUserInFirestoreIfNotExists(authenticatedUser: User): MutableLiveData<User>? {
        val newUserMutableLiveData = MutableLiveData<User>()
        val uidRef = usersRef.document(authenticatedUser.uid!!)
        uidRef.get().addOnCompleteListener {
                uidTask: Task<DocumentSnapshot?> ->
            if (uidTask.isSuccessful) {
                val document = uidTask.result
                if (!document!!.exists()) {
                    uidRef.set(authenticatedUser)
                        .addOnCompleteListener { userCreationTask: Task<Void?> ->
                            if (userCreationTask.isSuccessful) {
                                authenticatedUser.isCreated = true
                                newUserMutableLiveData.value= authenticatedUser
                            } else {
                                Log.i("createUserInFirestore", userCreationTask.exception?.message)
                            }
                        }
                } else {
                    newUserMutableLiveData.value= authenticatedUser
                }
            } else {
                Log.i("createUser_error",uidTask.exception?.message)
            }
        }
        return newUserMutableLiveData
    }
}


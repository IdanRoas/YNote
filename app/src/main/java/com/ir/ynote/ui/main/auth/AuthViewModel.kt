package com.ir.ynote.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthCredential
import com.ir.ynote.dto.User
import com.ir.ynote.model.repository.FirebaseAuthRepo

class AuthViewModel : ViewModel() {
    private  var authRepository: FirebaseAuthRepo= FirebaseAuthRepo()
    var authenticatedUserLiveData: LiveData<User>?= null
    var createdUserLiveData: LiveData<User>? = null
    var authenticatedUser = MutableLiveData<User>()


    fun signInWithGoogle(googleAuthCredential: AuthCredential) {
        authenticatedUserLiveData = authRepository.firebaseSignInWithGoogle(googleAuthCredential)
        authenticatedUser= authRepository.authenticatedUserMutableLiveData
    }
    fun createUser(authenticatedUser: User) {
        createdUserLiveData = authRepository.createUserInFirestoreIfNotExists(authenticatedUser)
    }

}
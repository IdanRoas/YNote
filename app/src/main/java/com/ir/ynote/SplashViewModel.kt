package com.ir.ynote

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ir.ynote.dto.User
import com.ir.ynote.model.repository.SplashRepo

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private val splashRepository: SplashRepo = SplashRepo()
    lateinit var isUserAuthenticatedLiveData: LiveData<User>
    private lateinit var userLiveData: LiveData<User>
    lateinit var isAuthenticated: MutableLiveData<Boolean>
    lateinit var authUser:MutableLiveData<User>


    fun checkIfUserIsAuthenticated() {
        isUserAuthenticatedLiveData =  splashRepository.checkIfUserIsAuthenticatedInFirebase()
        isAuthenticated=splashRepository.isAuth
        authUser=splashRepository.authUser



    }

    fun setUid(userID: String) {
        userLiveData = splashRepository.addUserToLiveData(userID)
    }

}

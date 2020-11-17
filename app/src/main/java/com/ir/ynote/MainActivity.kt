package com.ir.ynote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ir.ynote.dto.User
import com.ir.ynote.ui.main.AuthFragment
import com.ir.ynote.ui.main.AuthViewModel
import com.ir.ynote.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    var splashViewModel: SplashViewModel? = null
    lateinit var authViewModel:AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashViewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        setContentView(R.layout.main_activity)
        checkIfUserIsAuthenticated()


    }




    private fun checkIfUserIsAuthenticated() {
        splashViewModel?.checkIfUserIsAuthenticated()
        splashViewModel!!.authUser.observe(this, Observer { user->
            if(user.isAuthenticated)
                goToMainScreen(user)
            else
                goToLogin()
        })
    }
    private fun goToLogin() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, AuthFragment.newInstance()).commitNow()
    }

    private fun goToMainScreen(user:User) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, MainFragment.newInstance(user)).commitNow()

    }

}
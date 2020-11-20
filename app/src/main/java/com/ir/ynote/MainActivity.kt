package com.ir.ynote

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ir.ynote.dto.LocationDTO
import com.ir.ynote.dto.User
import com.ir.ynote.location.LocationViewModel
import com.ir.ynote.ui.main.AuthFragment
import com.ir.ynote.ui.main.AuthViewModel
import com.ir.ynote.ui.main.MainFragment
import java.util.jar.Manifest
import android.annotation.SuppressLint as SuppressLint1

class MainActivity : AppCompatActivity() {

    companion object {
        const val LOCATION_REQUEST = 100
    }

    var splashViewModel: SplashViewModel? = null
    lateinit var authViewModel: AuthViewModel
    lateinit var locationViewModel: LocationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        splashViewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)
        checkIfUserIsAuthenticated()
        if(!isPermissionsGranted()){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    ACCESS_FINE_LOCATION,
                    ACCESS_COARSE_LOCATION
                ),
                LOCATION_REQUEST
            )
        }
    }


    private fun checkIfUserIsAuthenticated() {
        splashViewModel?.checkIfUserIsAuthenticated()
        splashViewModel!!.authUser.observe(this, Observer { user ->
            if (user.isAuthenticated)
                goToMainScreen(user)
            else
                goToLogin()
        })
    }

    private fun goToLogin() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, AuthFragment.newInstance()).commitNow()
    }

    private fun goToMainScreen(user: User) {
        supportFragmentManager.beginTransaction().replace(R.id.container, MainFragment.newInstance(user)).commitNow()
    }

    private fun invokeLocationAction() {
        when {
            isPermissionsGranted() -> startLocationUpdate()
            else -> ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    ACCESS_FINE_LOCATION,
                    ACCESS_COARSE_LOCATION
                ),
                LOCATION_REQUEST
            )
        }
    }

    private fun startLocationUpdate() {
        locationViewModel.getLocationData().observe(this, Observer {
         //   Log.i("GPS_LOCATION", it.longitude.toString() + ":" + it.latitude)
        })
    }

    private fun isPermissionsGranted() =
        ActivityCompat.checkSelfPermission(
            this, ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this, ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                invokeLocationAction()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        invokeLocationAction()
    }
}
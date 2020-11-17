package com.ir.ynote.live_data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.ir.ynote.dto.LocationDTO

class LocationLiveData(context: Context) : LiveData<LocationDTO>() {

    companion object {
        private const val INTERVAL: Long= 8000

        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = INTERVAL
            fastestInterval = INTERVAL/2
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)


    private fun setLocationData(location: Location) {
        value = LocationDTO(
            longitude = location.longitude,
            latitude = location.latitude
        )
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {
                setLocationData(location)
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.also {
                    setLocationData(it)
                }
            }
        startLocationUpdates()
    }
    }

package com.ir.ynote.location

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ir.ynote.live_data.LocationLiveData


class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationData = LocationLiveData(application)

    fun getLocationData() = locationData
}
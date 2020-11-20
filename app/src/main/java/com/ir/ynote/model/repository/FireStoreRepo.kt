package com.ir.ynote.model.repository

import android.app.PendingIntent.getActivity
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.maps.android.PolyUtil
import com.ir.ynote.dto.HomeNoteData

class FireStoreRepo {
    companion object{
        const val NOTES="Notes"
    }

    private val rootRef = Firebase.firestore
     var relevantNoteList:ArrayList<HomeNoteData> =  ArrayList()


    fun getAllRelevantNoteTitle(maxMeterDisplay: Int, minMeterDisplay: Int, userLocation: LatLng) {
        relevantNoteList.clear()

        rootRef.collection(NOTES).addSnapshotListener { documentSnapshots, e ->
            if (e != null) {
                Log.w("FS_R_Listen_Failed", e)
                return@addSnapshotListener
            }
            if (!documentSnapshots!!.isEmpty) {
                for (doc in documentSnapshots.documentChanges) {
                    if (doc.document["title"] != null && doc.document["biggestRadius"] != null) {
                        val biggestRadius = doc.document["biggestRadius"].toString().toDouble()
                        val polyLineP: ArrayList<Map<String, Int>> = doc.document["polylineP"] as ArrayList<Map<String, Int>>
                        val noteLatLngListPolygon = convertToLatLngList(polyLineP)

                        if (maxMeterDisplay >= biggestRadius && minMeterDisplay <= biggestRadius)
                            if (PolyUtil.containsLocation(userLocation, noteLatLngListPolygon,true )) {
                                relevantNoteList.add(HomeNoteData(doc.document.id , doc.document["title"].toString()))
                            }
                    }
                }
            }
        }
    }


    // convert  ArrayList<map<in> to List<LatLng>
    fun convertToLatLngList(mapArrayList: ArrayList<Map<String, Int>>): ArrayList<LatLng>? {
        val list: ArrayList<LatLng> = ArrayList()
        for (i in mapArrayList.indices) {
            list.add(
                LatLng(
                    mapArrayList[i].getValue("latitude").toDouble(),
                    mapArrayList[i].getValue("longitude").toDouble()
                )
            )
        }
        return list
    }

}


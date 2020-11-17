package com.ir.ynote.dto

class User( uid:String?, name:String?, score: String="0", profileImg :String="", bio: String= "" ) {

     var isNew:Boolean? = false
     var isCreated:Boolean? = false
     var isAuthenticated=false
     var uid =uid
     var name: String? =name
     var score=score
     var profileImg=profileImg
     var bio =bio

     var notifications: ArrayList<Notification> = ArrayList()

}
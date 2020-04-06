package com.example.demochatapp.model

import com.google.gson.annotations.SerializedName

data class MessageModel (
    @SerializedName("chatroom") val chatroom: String,
    @SerializedName("username") val username: String,
    @SerializedName("message") val message: String,
    val gravityStatus: Boolean)
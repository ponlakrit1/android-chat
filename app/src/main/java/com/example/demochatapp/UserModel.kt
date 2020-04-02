package com.example.demochatapp

data class UserModel (
    var USER_NO: Int? = 0,
    var USERNAME: String? = "",
    var USER_ROLE: String? = ""
)

val userModel = UserModel(0, "username", "userrole");
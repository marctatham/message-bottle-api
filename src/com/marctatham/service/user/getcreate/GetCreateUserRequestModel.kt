package com.marctatham.service.user.getcreate

import com.google.gson.annotations.SerializedName

data class GetCreateUserRequestModel(
    @SerializedName("idToken") val idToken: String
)
package com.marctatham.service.user.getcreate

import com.google.gson.annotations.SerializedName

data class GetCreateUserResponseModel(
    @SerializedName("jwtToken") val jwtToken: String
)
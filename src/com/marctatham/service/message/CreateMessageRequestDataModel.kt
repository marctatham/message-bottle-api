package com.marctatham.service.message

import com.google.gson.annotations.SerializedName

data class CreateMessageRequestDataModel(
    @SerializedName("message") val message: String
)
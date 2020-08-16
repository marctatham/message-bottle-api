package com.marctatham.service.message

import com.google.gson.annotations.SerializedName

data class MessageDataModel(
    @SerializedName("message") val message: String
)
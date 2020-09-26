package com.marctatham.service.message

import com.google.gson.Gson

class CreateMessageRequestMapper(
    private val gson: Gson = Gson()
) {

    fun map(requestBody: String): MessageEntity {
        val request: CreateMessageRequestDataModel = gson.fromJson(
            requestBody,
            CreateMessageRequestDataModel::class.java
        )

        return MessageEntity(request.message)
    }
}

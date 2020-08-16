package com.marctatham.service.message

import com.google.gson.Gson

class MessagesResponseMapper(
    private val gson: Gson = Gson()
) {

    fun map(messageEntity: MessageEntity): String {
        val response = MessageDataModel(messageEntity.message)
        return gson.toJson(response)
    }

    fun map(messageEntities: List<MessageEntity>): String {
        val response = messageEntities.map { MessageDataModel(it.message) }
        return gson.toJson(response)
    }

}

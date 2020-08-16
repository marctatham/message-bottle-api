package com.marctatham.service.message.data

import com.marctatham.service.message.MessageEntity

class MessageRepository(
    private val messages: MutableList<MessageEntity> = mutableListOf()
) {

    fun postMessage(messageEntity: MessageEntity) = messages.add(messageEntity)

    fun getMessages(): List<MessageEntity> = messages.toList()

}
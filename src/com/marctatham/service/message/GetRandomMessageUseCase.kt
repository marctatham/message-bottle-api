package com.marctatham.service.message

import com.marctatham.service.message.data.MessageRepository

class GetRandomMessageUseCase(
    private val messageRepository: MessageRepository
) {

    suspend fun execute(): MessageEntity {
        val messages = messageRepository.getMessages()
        return messages.random()
    }
}
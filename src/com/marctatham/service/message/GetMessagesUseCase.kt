package com.marctatham.service.message

import com.marctatham.service.message.data.MessageRepository

class GetMessagesUseCase(
    private val messageRepository: MessageRepository
) {

    suspend fun execute(): List<MessageEntity> {
        return messageRepository.getMessages()
    }
}
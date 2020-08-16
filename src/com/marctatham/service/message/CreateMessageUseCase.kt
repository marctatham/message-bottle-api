package com.marctatham.service.message

import com.marctatham.service.message.data.MessageRepository

class CreateMessageUseCase(
    val messageRepository: MessageRepository
) {

    suspend fun execute(messageEntity: MessageEntity) {
        messageRepository.postMessage(messageEntity)
    }
}
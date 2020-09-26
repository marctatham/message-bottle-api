package com.marctatham.service.message

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveText
import io.ktor.response.respond

class MessageService(
    private val requestMapper: CreateMessageRequestMapper,
    private val responseMapper: MessagesResponseMapper,
    private val createMessageUseCase: CreateMessageUseCase,
    private val getRandomMessageUseCase: GetRandomMessageUseCase,
    private val getMessagesUseCase: GetMessagesUseCase
) {

    suspend fun createMessage(applicationCall: ApplicationCall) {
        val messageEntity = requestMapper.map(applicationCall.receiveText())
        createMessageUseCase.execute(messageEntity)
        applicationCall.respond(HttpStatusCode.OK)
    }

    suspend fun getMessage(applicationCall: ApplicationCall) {
        val message = getRandomMessageUseCase.execute()
        val response = responseMapper.map(message)

        applicationCall.respond(HttpStatusCode.OK, response)
    }

    suspend fun getMessages(applicationCall: ApplicationCall) {
        val messages = getMessagesUseCase.execute()
        val response = responseMapper.map(messages)

        applicationCall.respond(HttpStatusCode.OK, response)
    }
}
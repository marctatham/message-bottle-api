package com.marctatham

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.marctatham.service.message.*
import com.marctatham.service.message.data.MessageRepository
import com.marctatham.service.user.UserService
import com.marctatham.service.user.getcreate.GetCreateUserRequestMapper
import com.marctatham.service.user.getcreate.GetCreateUserResponseMapper
import com.marctatham.service.user.getcreate.GetCreateUserUseCase
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.jwt
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import java.io.FileInputStream

val configProvider = ConfigProvider()
val simpleJwt: SimpleJWT =
    SimpleJWT(
        configProvider.getOrDefault(
            "JWT_SECRET",
            "DEFAULT"
        )
    )
val gson: Gson = GsonBuilder()
    .setPrettyPrinting()
    .create()

fun main(args: Array<String>) {
    // initialise Firebase
    val serviceAccount = FileInputStream("./resources/service-accounts/bottling-messages-firebase-adminsdk.json")
    val firebaseOptions = FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()
    FirebaseApp.initializeApp(firebaseOptions)

    // fireup
    io.ktor.server.jetty.EngineMain.main(args)
}


@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    val userService = UserService(
        GetCreateUserRequestMapper(gson),
        GetCreateUserResponseMapper(gson),
        GetCreateUserUseCase(FirebaseAuth.getInstance(), simpleJwt)
    )

    val messagesRepository: MessageRepository = MessageRepository()
    val messageService = MessageService(
        CreateMessageRequestMapper(gson),
        MessagesResponseMapper(gson),
        CreateMessageUseCase(messagesRepository),
        GetRandomMessageUseCase(messagesRepository),
        GetMessagesUseCase(messagesRepository)
    )

    install(Authentication) {
        jwt {
            verifier(simpleJwt.verifier)
            validate {
                UserIdPrincipal(it.payload.getClaim(CLAIM_USER_ID).asString())
            }
        }
    }

    routing {

        // just for testing that everything is up
        get("/") {
            call.respondText("hello world", contentType = ContentType.Text.Plain)
        }

        post("user") {
            userService.getCreateUser(call)
        }

        authenticate {
            post("/message") {
                messageService.createMessage(call)
            }

            get("/message") {
                messageService.getMessage(call)
            }

            get("/messages") {
                messageService.getMessages(call)
            }
        }
    }
}


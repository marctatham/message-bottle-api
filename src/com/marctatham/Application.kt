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
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream

private const val SERVICE_ACCOUNT = "bottling-messages-firebase-adminsdk.json"

val logger: Logger = LoggerFactory.getLogger(Application::class.java)

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
    logger.info("Starting up...")

    // find the service account
    val file = File(".").walk().filter { it.name == SERVICE_ACCOUNT }.first()
    logger.debug("service account found: ${file.absolutePath}")

    // initialise the Firebase SDK
    val serviceAccount = FileInputStream(file.absolutePath)
    val firebaseOptions = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()
    FirebaseApp.initializeApp(firebaseOptions)

    // fire up the ol' server
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
        get("/health-check") {
            // todo: let's get this outputting the service version
            call.respondText("healthy", contentType = ContentType.Text.Plain)
        }

        // TODO: tidy up
        post("user") {
            try {
                userService.getCreateUser(call)
            } catch (throwable: Throwable) {
                logger.error("error processing new user", throwable)
            }
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




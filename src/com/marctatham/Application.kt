package com.marctatham

import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.marctatham.service.message.CreateMessageRequestMapper
import com.marctatham.service.message.CreateMessageUseCase
import com.marctatham.service.message.GetMessagesUseCase
import com.marctatham.service.message.GetRandomMessageUseCase
import com.marctatham.service.message.MessageService
import com.marctatham.service.message.MessagesResponseMapper
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
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val logger: Logger = LoggerFactory.getLogger(Application::class.java)

// TODO: let's break these dependencies out when we introduce some dependency injection
val firebaseHandler = FirebaseHandler()
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


/**
 * Entry point of the application, simply fires up the web server
 */
fun main(args: Array<String>) {

    logger.info("Starting up web server...")
    io.ktor.server.jetty.EngineMain.main(args)
}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    logger.debug("initializing Application module")

    logger.debug("initializing configuring ")
    firebaseHandler.initialise()

    val userService = UserService(
        GetCreateUserRequestMapper(gson),
        GetCreateUserResponseMapper(gson),
        GetCreateUserUseCase(FirebaseAuth.getInstance(), simpleJwt)
    )

    // todo: inject
    val messagesRepository: MessageRepository = MessageRepository()
    val messageService = MessageService(
        CreateMessageRequestMapper(gson),
        MessagesResponseMapper(gson),
        CreateMessageUseCase(messagesRepository),
        GetRandomMessageUseCase(messagesRepository),
        GetMessagesUseCase(messagesRepository)
    )

    logger.debug("initializing authentication")
    install(Authentication) {
        jwt {
            verifier(simpleJwt.verifier)
            validate {
                UserIdPrincipal(it.payload.getClaim(CLAIM_USER_ID).asString())
            }
        }
    }

    logger.debug("initializing routing")
    routing {

        // just for testing that everything is up
        get("/health-check") {
            // todo: let's get this outputting the service version
            logger.info("health-check")
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

        get("/messages") {
            messageService.getMessages(call)
        }

        authenticate {
            post("/message") {
                messageService.createMessage(call)
            }

            get("/message") {
                messageService.getMessage(call)
            }
        }
    }


}




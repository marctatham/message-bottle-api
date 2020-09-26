package com.marctatham

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.application.Application
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream

private const val SERVICE_ACCOUNT = "bottling-messages-firebase-adminsdk.json"

class FirebaseHandler {

    fun initialise() {
        // find the service account
        val current = File("..")
        logger.info("Initialising firebase")

        // start looking in the current directory, keep looking til we find the service account
        val file = File(".").walk().filter { it.name == SERVICE_ACCOUNT }.first()
        logger.debug("service account found: ${file.absolutePath}")
        println("service account found: ${file.absolutePath}")

        // initialise the Firebase SDK
        val serviceAccount = FileInputStream(file.absolutePath)
        val firebaseOptions = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()
        FirebaseApp.initializeApp(firebaseOptions)
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(Application::class.java)
    }
}
package com.marctatham.service.user.getcreate

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import com.marctatham.SimpleJWT
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class GetCreateUserUseCase(
    // we should probably go down to the data layer here
    // both to verify the token, and to get the user data
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val jwtIssuer: SimpleJWT
) {

    suspend fun execute(getCreateUserSession: GetCreateUserSessionEntity): UserSessionEntity {
        // verify the token and get the
        val decodedToken: FirebaseToken = firebaseAuth.verifyIdToken(getCreateUserSession.idToken)
        logger.debug("Signing in user... \n${decodedToken.debugContents()}")

        // let's try & log out the sign-in provider
        val firebase = decodedToken.claims["firebase"] as Map<*, *>?
        val provider = firebase?.get("sign_in_provider")
        logger.debug("Signing in via $provider")

        // issue jwt token for this user valid for interaction with our backend
        val userId = decodedToken.uid
        val jwtToken = jwtIssuer.sign(userId)
        return UserSessionEntity(jwtToken, AuthenticationProvider.Google)
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(GetCreateUserUseCase::class.java)
    }
}

private fun FirebaseToken.debugContents(): String {
    val debugFriendlyOutput = StringBuilder()
    claims.forEach { (key, value) ->
        debugFriendlyOutput.appendLine("[$key] - [$value]")
    }

    return debugFriendlyOutput.toString()
}
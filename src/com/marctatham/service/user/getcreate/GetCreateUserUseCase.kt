package com.marctatham.service.user.getcreate

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserRecord
import com.marctatham.SimpleJWT
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class GetCreateUserUseCase(
    // TODO: Retrieve user info from firebase
    // we should probably go down to the data layer here
    // both to verify the token, and to get the user data
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val jwtIssuer: SimpleJWT
) {

    suspend fun execute(getCreateUserSession: GetCreateUserSessionEntity): UserSessionEntity {
        // verify the token and get the
        val decodedToken = firebaseAuth.verifyIdToken(getCreateUserSession.idToken)

        // fetch user info from firebase
        val userId = decodedToken.uid
        val userRecord: UserRecord = firebaseAuth.getUser(userId)
        logger.debug("user: [${userRecord.displayName}] - email: [${userRecord.email}]")

        // issue jwt token for this user
        val jwtToken = jwtIssuer.sign(userId)
        return UserSessionEntity(jwtToken)
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(GetCreateUserUseCase::class.java)
    }
}
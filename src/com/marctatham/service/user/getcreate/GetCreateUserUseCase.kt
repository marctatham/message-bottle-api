package com.marctatham.service.user.getcreate

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserRecord
import com.marctatham.SimpleJWT

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
        println("user: [${userRecord.displayName}] - email: [${userRecord.email}]")

        // issue jwt token
        val jwtToken = jwtIssuer.sign(userId)
        return UserSessionEntity(jwtToken)
    }
}
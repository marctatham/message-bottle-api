package com.marctatham

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm

const val CLAIM_USER_ID = "userId"

// simple implementation of JWT issuance
class SimpleJWT(secret: String) {

    private val algorithm = Algorithm.HMAC256(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .build()

    fun sign(userId: String): String = JWT.create()
        .withClaim(CLAIM_USER_ID, userId)
        .sign(algorithm)
}
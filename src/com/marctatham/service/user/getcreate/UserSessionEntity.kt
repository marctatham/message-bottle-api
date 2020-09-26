package com.marctatham.service.user.getcreate

/**
 * Represents the request object to Get/Create a user
 * This entity is owned by the application
 */
data class UserSessionEntity(
    val jwtToken: String,
    val authProvider: AuthenticationProvider
)

// details all currently supported providers
// along with our very own defined provider ID
sealed class AuthenticationProvider(val providerIdentifier: Int) {

    object Google : AuthenticationProvider(1)
}
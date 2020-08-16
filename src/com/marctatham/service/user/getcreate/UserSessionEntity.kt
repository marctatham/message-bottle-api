package com.marctatham.service.user.getcreate

/**
 * Represents the request object to Get/Create a user
 * This entity is owned by the application
 */
data class UserSessionEntity(
    val jwtToken: String
)
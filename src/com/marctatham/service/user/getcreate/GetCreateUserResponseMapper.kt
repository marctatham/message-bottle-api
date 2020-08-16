package com.marctatham.service.user.getcreate

import com.google.gson.Gson

class GetCreateUserResponseMapper(
    private val gson: Gson = Gson()
) {

    fun map(userSessionEntity: UserSessionEntity): String {
        val response = GetCreateUserResponseModel(userSessionEntity.jwtToken)
        return gson.toJson(response)
    }

}

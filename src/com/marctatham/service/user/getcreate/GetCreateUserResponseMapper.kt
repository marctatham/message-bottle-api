package com.marctatham.service.user.getcreate

import com.google.gson.Gson

class GetCreateUserResponseMapper(
    private val gson: Gson = Gson()
) {

    fun map(userSessionEntity: UserSessionEntity): String {
        // TODO: there's probably a better way of negotiating the content of the API 🤔
        val response = GetCreateUserResponseModel(userSessionEntity.jwtToken)
        return gson.toJson(response)
    }

}

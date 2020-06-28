package com.marctatham.service.user.getcreate

import com.google.gson.Gson

class GetCreateUserRequestMapper(
    private val gson: Gson = Gson()
) {

    fun map(requestBody: String): GetCreateUserSessionEntity {
        // map the string to json
        // TODO: there's probably a better way of negotiating the content of the API 🤔
        val request: GetCreateUserRequestModel = gson.fromJson(requestBody, GetCreateUserRequestModel::class.java)
        return GetCreateUserSessionEntity(request.idToken)
    }
}

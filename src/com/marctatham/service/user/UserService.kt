package com.marctatham.service.user

import com.marctatham.service.user.getcreate.GetCreateUserRequestMapper
import com.marctatham.service.user.getcreate.GetCreateUserResponseMapper
import com.marctatham.service.user.getcreate.GetCreateUserUseCase
import io.ktor.application.ApplicationCall
import io.ktor.request.receiveText
import io.ktor.response.respond

class UserService(
    private val requestMapper: GetCreateUserRequestMapper,
    private val responseMapper: GetCreateUserResponseMapper,
    private val getCreateUserUseCase: GetCreateUserUseCase
) {

    suspend fun getCreateUser(applicationCall: ApplicationCall) {

        // interpret the request
        val requestBody = applicationCall.receiveText()
        val getCreateUserEntity = requestMapper.map(requestBody)

        // execute the use case / interactor
        val userSessionEntity = getCreateUserUseCase.execute(getCreateUserEntity)
        val responseModel = responseMapper.map(userSessionEntity)

        //HttpStatusCode.OK,
        applicationCall.respond(responseModel)

//        val user = userRepository.create(createUserRequest.email)
//        applicationCall.respond(
//            Gson().toJson(user)
//        )


        // get user info from firebase
        // return response object
        /**
         * we'll move toward something like
         * error code :
         *  100 = invalid token
         *  101 = token expired
         *  etc.
         */
    }


}
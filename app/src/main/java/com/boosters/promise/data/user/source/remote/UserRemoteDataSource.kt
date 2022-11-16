package com.boosters.promise.data.user.source.remote

import kotlinx.coroutines.flow.Flow

interface UserRemoteDataSource {

    suspend fun requestSignUp(userName: String): Result<String>

    fun getUserBody(userCode: String): Flow<UserBody>

}
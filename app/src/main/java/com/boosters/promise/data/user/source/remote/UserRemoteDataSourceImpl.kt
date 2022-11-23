package com.boosters.promise.data.user.source.remote

import com.boosters.promise.data.network.NetworkConnectionUtil
import com.boosters.promise.data.user.di.UserModule
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteDataSourceImpl @Inject constructor(
    @UserModule.UserCollectionReference private val userCollectionReference: CollectionReference,
    private val networkConnectionUtil: NetworkConnectionUtil,
) : UserRemoteDataSource {

    override suspend fun requestSignUp(userName: String): Result<UserBody> = runCatching {
        networkConnectionUtil.checkNetworkOnline()

        val userCode = userCollectionReference.document().id.take(USER_CODE_LENGTH)
        val userBody = UserBody(
            userCode = userCode,
            userName = userName,
            location = null
        )
        userCollectionReference.document(userCode).set(
            userBody
        ).await()

        userBody
    }

    override fun getAllUsers(): Flow<List<UserBody>> =
        userCollectionReference.snapshots().map {
            it.toObjects(UserBody::class.java)
        }

    override fun getUser(userCode: String): Flow<UserBody> =
        userCollectionReference.document(userCode).snapshots().mapNotNull {
            it.toObject(UserBody::class.java)
        }

    companion object {
        private const val USER_CODE_LENGTH = 6
    }

}
package com.boosters.promise.data.user.source.remote

import com.boosters.promise.data.network.NetworkConnectionUtil
import com.boosters.promise.data.user.User
import com.boosters.promise.data.user.di.UserModule
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteDataSourceImpl @Inject constructor(
    @UserModule.UserCollectionReference private val userCollectionReference: CollectionReference,
    private val networkConnectionUtil: NetworkConnectionUtil
) : UserRemoteDataSource {

    override suspend fun requestSignUp(userName: String): Result<User> = runCatching {
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

        userBody.toUser()
    }

    override fun getUser(userCode: String): Flow<User> =
        userCollectionReference.document(userCode).snapshots().mapNotNull {
            try {
                it.toObject(UserBody::class.java)?.toUser()
            } catch (e: NullPointerException) {
                null
            }
        }

    companion object {
        private const val USER_CODE_LENGTH = 6
    }

}
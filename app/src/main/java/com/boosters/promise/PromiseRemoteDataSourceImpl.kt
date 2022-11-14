package com.boosters.promise

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class PromiseRemoteDataSourceImpl @Inject constructor(
    private val database: FirebaseDatabase
) : PromiseRemoteDataSource {

    private val promiseRef = database.getReference("promise")

    init {
        promiseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = (dataSnapshot.value ?: return) as HashMap<String, Promise>
                value.keys.forEach { key ->
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override suspend fun addPromise(promise: Promise) {
        promiseRef.push().setValue(promise)
    }

    override suspend fun removePromise(promise: Promise) {
        TODO()
    }

    override suspend fun updatePromise(promise: Promise) {
        TODO()
    }

}
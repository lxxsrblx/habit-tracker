package com.app.habittracker.repository

import com.app.habittracker.data.User
import com.app.habittracker.data.UserDao
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userDao: UserDao
) {

    suspend fun saveUser(user: User) {
        userDao.insert(user)
    }

    suspend fun getUser(): User? {
        return userDao.getUser()
    }

    fun getUserFlow(): Flow<User?> {
        return userDao.getUserFlow()
    }
}
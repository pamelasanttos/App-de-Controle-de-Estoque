package com.usedocetangerinaestoque.usecases.user

import com.usedocetangerinaestoque.data.dao.UserDao
import com.usedocetangerinaestoque.data.entities.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetUserByIdUseCase @Inject constructor(
    private val userDao: UserDao
) {
    suspend operator fun invoke(id: Long): User? {
        return userDao.getById(id)
    }
}
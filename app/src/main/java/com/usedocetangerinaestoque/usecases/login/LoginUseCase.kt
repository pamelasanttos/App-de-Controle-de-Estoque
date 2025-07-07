package com.usedocetangerinaestoque.usecases.login

import com.usedocetangerinaestoque.data.dao.UserDao
import com.usedocetangerinaestoque.data.entities.User
import com.usedocetangerinaestoque.exceptions.InvalidLoginExeception
import com.usedocetangerinaestoque.services.PasswordEncripter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUseCase @Inject constructor(
    private val userDao: UserDao,
    private val passwordEncripter: PasswordEncripter
) {
    suspend operator fun invoke(user: User): User {
        val encriptedPassword = passwordEncripter.encrypt(user.password)

        val existingUser = userDao.getByEmailAndPassword(user.email, encriptedPassword)
            ?: throw InvalidLoginExeception()

        return existingUser
    }
}
package com.usedocetangerinaestoque.usecases.user

import com.usedocetangerinaestoque.data.dao.UserDao
import com.usedocetangerinaestoque.data.entities.User
import com.usedocetangerinaestoque.services.PasswordEncripter
import com.usedocetangerinaestoque.util.title
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateUserUseCase @Inject constructor(
    private val userDao: UserDao,
    private val passwordEncripter: PasswordEncripter
) {

    suspend operator fun invoke(user: User): User {
        validate(user)

        user.name = user.name.trim().title()
        user.password = passwordEncripter.encrypt(user.password)
        user.email = user.email.lowercase()

        userDao.update(user)

        return user
    }

    private suspend fun validate(user: User) {
        RegisterUserValidator(user).validate()

        val emailExists = userDao.existOtherEmail(user.email, user.id)

        if (emailExists) {
            throw RuntimeException("E-mail já cadastrado")
        }

    }
}
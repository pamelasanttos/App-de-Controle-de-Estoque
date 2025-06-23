package com.usedocetangerinaestoque.usecases.user

import com.usedocetangerinaestoque.data.entities.User
import org.valiktor.*
import org.valiktor.functions.*


class RegisterUserValidator(private val user: User) {
    fun validate() {
        validate(user) {
            validate(User::name).isNotBlank()
            validate(User::email).isNotBlank().isEmail()
            validate(User::password).hasSize(min = 6)
        }
    }

    companion object {
        fun errorsTranslateToPtBr(ex: ConstraintViolationException): List<String> {
            return ex.constraintViolations.map { violation ->
                val field = violation.property
                val type = violation.constraint.name
                when (field to type) {
                    "name" to "NotBlank" -> "O nome não pode estar em branco"
                    "email" to "NotBlank" -> "O e-mail não pode estar em branco"
                    "email" to "Email" -> "O e-mail informado é inválido"
                    "password" to "Size" -> "A senha deve ter no mínimo 6 caracteres"
                    else -> "$field: Erro desconhecido ($type)"
                }
            }
        }
    }
}
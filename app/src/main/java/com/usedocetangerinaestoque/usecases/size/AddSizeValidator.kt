package com.usedocetangerinaestoque.usecases.size

import com.usedocetangerinaestoque.data.entities.Size
import org.valiktor.*
import org.valiktor.functions.*

class AddSizeValidator(private val size: Size) {
    fun validate() {
        validate(size) {
            validate(Size::name).isNotBlank()
        }
    }

    companion object {
        fun errosTranslateToPtBr(ex: ConstraintViolationException): List<String> {
            return ex.constraintViolations.map { violation ->
                val field = violation.property
                val type = violation.constraint.name
                when (field to type) {
                    "name" to "NotBlank" -> "O nome não pode estar em branco"
                    else -> "$field: Erro desconhecido ($type)"
                }
            }
        }
    }

}

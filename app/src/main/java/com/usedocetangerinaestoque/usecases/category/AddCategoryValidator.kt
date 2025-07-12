package com.usedocetangerinaestoque.usecases.category

import com.usedocetangerinaestoque.data.entities.Category
import org.valiktor.*
import org.valiktor.functions.*

class AddCategoryValidator(private val category: Category) {
    fun validate() {
        validate(category) {
            validate(Category::name).isNotBlank()
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

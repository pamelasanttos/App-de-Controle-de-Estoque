package com.usedocetangerinaestoque.usecases.item

import com.usedocetangerinaestoque.data.entities.Item
import org.valiktor.*
import org.valiktor.functions.*

class AddNewItemValidator(private val item: Item) {
    fun validate() {
        validate(item) {
            validate(Item::name).isNotBlank()
            validate(Item::value).isGreaterThan(0.0)
            validate(Item::quantity).isGreaterThanOrEqualTo(0)
        }
    }

    companion object {
        fun errosTranslateToPtBr(ex: ConstraintViolationException): List<String> {
            return ex.constraintViolations.map { violation ->
                val field = violation.property
                val type = violation.constraint.name
                when (field to type) {
                    "name" to "NotBlank" -> "O nome não pode estar em branco"
                    "value" to "GreaterThan" -> "O valor precisa ser mais que 0"
                    "quantity" to "GreaterThanOrEqualTo" -> "A quantidade não pode ser menor que 0"
                    else -> "$field: Erro desconhecido ($type)"
                }
            }
        }
    }
}
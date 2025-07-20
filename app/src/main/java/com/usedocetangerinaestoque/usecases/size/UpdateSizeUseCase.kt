package com.usedocetangerinaestoque.usecases.size

import com.usedocetangerinaestoque.data.dao.CategoryDao
import com.usedocetangerinaestoque.data.dao.SizeDao
import com.usedocetangerinaestoque.data.entities.Category
import com.usedocetangerinaestoque.data.entities.Size
import com.usedocetangerinaestoque.usecases.category.AddCategoryValidator
import com.usedocetangerinaestoque.util.capitalizeLocale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateSizeUseCase @Inject constructor(
    private val sizeDao: SizeDao
){
    suspend operator fun invoke(size: Size): Size {
        size.name = size.name.capitalizeLocale()

        validate(size)

        sizeDao.update(size)

        return size
    }

    private suspend fun validate(size: Size) {
        AddSizeValidator(size).validate()

        val itemExists = sizeDao.existName(size.name)
        if (itemExists) {
            throw RuntimeException("Já existe um tamanho com este nome.")
        }
    }
}
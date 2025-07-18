package com.usedocetangerinaestoque.usecases.category

import com.usedocetangerinaestoque.data.dao.CategoryDao
import com.usedocetangerinaestoque.data.entities.Category
import com.usedocetangerinaestoque.util.capitalizeLocale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateCategoryUseCase @Inject constructor(
    private val categoryDao: CategoryDao
){
    suspend operator fun invoke(category: Category): Category {
        category.name = category.name.capitalizeLocale()

        validate(category)

        categoryDao.update(category)

        return category
    }

    private suspend fun validate(category: Category) {
        AddCategoryValidator(category).validate()

        val itemExists = categoryDao.existName(category.name)
        if (itemExists) {
            throw RuntimeException("Já existe uma categoria com este nome.")
        }
    }
}
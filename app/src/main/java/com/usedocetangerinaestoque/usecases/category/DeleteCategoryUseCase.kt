package com.usedocetangerinaestoque.usecases.category

import com.usedocetangerinaestoque.data.dao.CategoryDao
import com.usedocetangerinaestoque.data.entities.Category
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteCategoryUseCase @Inject constructor(
    private val categoryDao: CategoryDao
) {
    suspend operator fun invoke(category: Category) {
        categoryDao.deleteById(category)
    }
}
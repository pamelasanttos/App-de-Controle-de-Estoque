package com.usedocetangerinaestoque.usecases.category

import com.usedocetangerinaestoque.data.dao.CategoryDao
import com.usedocetangerinaestoque.data.entities.Category
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllCategoriesUseCase @Inject constructor(
    private val categoryDao: CategoryDao
) {
    operator fun invoke(): Flow<List<Category>> =
        categoryDao.observerAll()
}
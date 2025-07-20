package com.usedocetangerinaestoque.usecases.size

import com.usedocetangerinaestoque.data.dao.SizeDao
import com.usedocetangerinaestoque.data.entities.Size
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteSizeUseCase @Inject constructor(
    private val sizeDao: SizeDao
) {
    suspend operator fun invoke(size: Size) {
        sizeDao.deleteById(size)
    }
}
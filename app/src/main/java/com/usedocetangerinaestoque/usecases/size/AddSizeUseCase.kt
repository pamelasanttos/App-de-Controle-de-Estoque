package com.usedocetangerinaestoque.usecases.size

import com.usedocetangerinaestoque.data.dao.SizeDao
import com.usedocetangerinaestoque.data.entities.Size
import com.usedocetangerinaestoque.util.title
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddSizeUseCase @Inject constructor(
    private val sizeDao: SizeDao
){
    suspend operator fun invoke(size: Size): Size {
        size.name = size.name.title()

        validate(size)

        sizeDao.add(size)

        return size
    }

    private suspend fun validate(size: Size) {
        AddSizeValidator(size).validate()

        val itemExists = sizeDao.existName(size.name)
        if (itemExists) {
            throw RuntimeException("Já existe uma categoria com este nome.")
        }
    }

}
package com.usedocetangerinaestoque.usecases.item

import com.usedocetangerinaestoque.data.dao.ItemDao
import com.usedocetangerinaestoque.data.relations.ItemFull
import com.usedocetangerinaestoque.exceptions.UseDoceTangerinaExceptions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetItemFullByIdUseCase @Inject constructor (
    private val itemDao: ItemDao
) {
    suspend operator fun invoke(itemId: Long): ItemFull {
        return itemDao.getById(itemId) ?:
            throw UseDoceTangerinaExceptions("Item não encontrado.")
    }
}
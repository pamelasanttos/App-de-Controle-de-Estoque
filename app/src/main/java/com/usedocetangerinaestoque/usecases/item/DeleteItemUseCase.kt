package com.usedocetangerinaestoque.usecases.item

import com.usedocetangerinaestoque.data.dao.ItemDao
import com.usedocetangerinaestoque.data.entities.Item
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteItemUseCase @Inject constructor(
    private val itemDao: ItemDao
) {
    suspend operator fun invoke(item: Item) {
        itemDao.deleteById(item)
    }
}
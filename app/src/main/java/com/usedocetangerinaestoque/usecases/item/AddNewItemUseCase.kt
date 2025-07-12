package com.usedocetangerinaestoque.usecases.item

import com.usedocetangerinaestoque.data.dao.ImageDao
import com.usedocetangerinaestoque.data.dao.ItemDao
import com.usedocetangerinaestoque.data.entities.Item
import com.usedocetangerinaestoque.data.relations.ItemFull
import com.usedocetangerinaestoque.exceptions.ItemAlreadyExistsException
import com.usedocetangerinaestoque.util.capitalizeLocale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddNewItemUseCase @Inject constructor(
    private val itemDao: ItemDao,
    private val imageDao: ImageDao
){
    suspend operator fun invoke(itemFull: ItemFull): ItemFull {
        val item = itemFull.item.apply {
            name = name.trim().capitalizeLocale()
            description = description.trim().capitalizeLocale()
        }

        validate(item)

        val generatedId  = itemDao.add(item)
        val images = itemFull.images.onEach { image -> image.itemId = generatedId  }

        if (images.isNotEmpty())
            imageDao.addMultiple(images)

        item.apply { id = generatedId }
        return ItemFull(item, images)
    }

    private suspend fun validate(item: Item) {
        AddNewItemValidator(item).validate()

        val itemExists = itemDao.existName(item.name)
        if (itemExists) {
            throw ItemAlreadyExistsException()
        }
    }
}
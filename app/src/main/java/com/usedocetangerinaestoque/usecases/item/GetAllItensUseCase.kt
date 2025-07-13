package com.usedocetangerinaestoque.usecases.item

import com.usedocetangerinaestoque.data.dao.ItemDao
import com.usedocetangerinaestoque.data.relations.ItemFull
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllItensUseCase @Inject constructor(
    private val itemDao: ItemDao
) {
    operator fun invoke(): Flow<List<ItemFull>> =
        itemDao.observeAll()
}
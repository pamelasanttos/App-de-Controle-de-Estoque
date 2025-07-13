package com.usedocetangerinaestoque.usecases.size

import android.util.Log
import com.usedocetangerinaestoque.data.dao.SizeDao
import com.usedocetangerinaestoque.data.entities.Size
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllSizesUseCase @Inject constructor(
    private val sizeDao: SizeDao
) {
    operator fun invoke(): Flow<List<Size>> {
        return sizeDao.observeAll()
    }
}
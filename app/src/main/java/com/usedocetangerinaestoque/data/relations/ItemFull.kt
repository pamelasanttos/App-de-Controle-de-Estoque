package com.usedocetangerinaestoque.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.usedocetangerinaestoque.data.entities.Category
import com.usedocetangerinaestoque.data.entities.Image
import com.usedocetangerinaestoque.data.entities.Item
import com.usedocetangerinaestoque.data.entities.Size

class ItemFull (
    @Embedded
    var item: Item,
    @Relation(
        parentColumn = "id",
        entityColumn = "itemId"
    )
    val images: List<Image> = emptyList(),

    @Relation(
        parentColumn = "sizeId",
        entityColumn = "id"
    )
    val size: Size? = null,

    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: Category? = null
)
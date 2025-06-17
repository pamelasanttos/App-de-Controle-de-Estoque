package com.usedocetangerinaestoque.data.relations

import com.usedocetangerinaestoque.data.entities.Category

data class CategoryWithFirstItem(
    val category: Category,
    val firstItemImagePath: String?
)

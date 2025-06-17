package com.usedocetangerinaestoque.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.Date

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Size::class,
            parentColumns = ["id"],
            childColumns = ["sizeId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        ),
    ],
    indices = [
        Index(value = ["sizeId"]),
        Index(value = ["categoryId"])
    ]
)
data class Item(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var name: String = "",
    var description: String = "",
    var value: Double = 0.00,
    var quantity: Int = 0,
    var sizeId: Long? = null,
    var categoryId: Long? = null,
)

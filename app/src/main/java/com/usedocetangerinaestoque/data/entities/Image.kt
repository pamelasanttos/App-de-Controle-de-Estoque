package com.usedocetangerinaestoque.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    foreignKeys = [
        ForeignKey(
            Item::class,
            ["id"],
            ["itemId"],
            ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("itemId")
    ]
)
data class Image(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var path: String = "",
    var itemId: Long = 0L,
)

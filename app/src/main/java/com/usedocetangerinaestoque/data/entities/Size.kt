package com.usedocetangerinaestoque.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Size(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var name: String = ""
) {
    override fun toString() = name

}

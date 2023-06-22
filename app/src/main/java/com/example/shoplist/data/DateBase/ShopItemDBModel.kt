package com.example.shoplist.data.DateBase

import androidx.room.Entity
import androidx.room.PrimaryKey
/*
 не можем использовать класс из Domain слоя
 так как если будем так делать, то Dpmain слой начинает
 зависеть от конкретной реализации, от конкретной библиотеки Room
 */
@Entity(tableName = "shop_items")
data class ShopItemDBModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val count: Int,
    val enabled: Boolean
)
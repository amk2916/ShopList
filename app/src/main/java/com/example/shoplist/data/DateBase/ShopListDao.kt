package com.example.shoplist.data.DateBase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShopListDao {

    @Query("SELECT * FROM shop_items")
    fun getShopList():LiveData<List<ShopItemDBModel>>

    /*
        Так как есть возможность редактировать записи,
        то возникают ситуации, когда вызывается этот метод
        с существующим id,  в таком случае, мы просто заменяем существующую
        запись
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addShopItem(shopItemDBModel: ShopItemDBModel)

    @Delete
    fun deleteShopItem(shopItemId: Int)

    @Query("select * from shop_items where id = :shopItemId limit 1")
    fun getShopItem(shopItemId: Int): ShopItemDBModel

}
package com.example.shoplist.domain

import androidx.lifecycle.LiveData

//черный ящик для домаин слоя который умеет работать с данными
//но реализация не важна
interface ShopListRepository {

    suspend fun addShopItem(shopItem: ShopItem)

    suspend fun deleteShopItem(shopItem: ShopItem)

    suspend fun editShopItem(shopItem: ShopItem)

    suspend fun getShopItem(shopItemId: Int) : ShopItem

    //возвращаем LiveData чтобы один раз подписаться на обновления
    fun getShopList() : LiveData<List<ShopItem>>
}
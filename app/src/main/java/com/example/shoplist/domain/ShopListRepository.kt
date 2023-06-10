package com.example.shoplist.domain

import androidx.lifecycle.LiveData

//черный ящик для домаин слоя который умеет работать с данными
//но реализация не важна
interface ShopListRepository {

    fun addShopItem(shopItem: ShopItem)

    fun deleteShopItem(shopItem: ShopItem)

    fun editShopItem(shopItem: ShopItem)

    fun getShopItem(shopItemId: Int) : ShopItem

    //возвращаем LiveData чтобы один раз подписаться на обновления
    fun getShopList() : LiveData<List<ShopItem>>
}
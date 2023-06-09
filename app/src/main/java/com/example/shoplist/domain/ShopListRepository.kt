package com.example.shoplist.domain

//черный ящик для домаин слоя который умеет работать с данными
//но реализация не важна
interface ShopListRepository {

    fun addShopItem(shopItem: ShopItem)

    fun deleteShopItem(shopItem: ShopItem)

    fun editShopItem(shopItem: ShopItem)

    fun getShopItem(shopItemId: Int) : ShopItem

    fun getShopList() : List<ShopItem>
}
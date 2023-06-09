package com.example.shoplist.domain

class GetShopItemUseCase(private val shopListRepository:ShopListRepository) {
    fun getShopItemById(shopItemId:Int):ShopItem{
        return shopListRepository.getShopItem(shopItemId)
    }
}
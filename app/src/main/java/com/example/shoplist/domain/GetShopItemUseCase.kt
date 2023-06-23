package com.example.shoplist.domain

class GetShopItemUseCase(private val shopListRepository:ShopListRepository) {
    suspend fun getShopItemById(shopItemId:Int):ShopItem{
        return shopListRepository.getShopItem(shopItemId)
    }
}
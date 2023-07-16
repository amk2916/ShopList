package com.example.shoplist.domain

import javax.inject.Inject

class GetShopItemUseCase @Inject constructor(
    private val shopListRepository:ShopListRepository
    ) {
    suspend fun getShopItemById(shopItemId:Int):ShopItem{
        return shopListRepository.getShopItem(shopItemId)
    }
}
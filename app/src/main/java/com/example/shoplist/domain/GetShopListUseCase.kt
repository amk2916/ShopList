package com.example.shoplist.domain

class GetShopListUseCase(private val shopListRepository: ShopListRepository){
    fun getShopListUseCase(): List<ShopItem>{
        return shopListRepository.getShopList()
    }
}
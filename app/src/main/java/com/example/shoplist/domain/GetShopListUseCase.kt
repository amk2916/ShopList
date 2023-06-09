package com.example.shoplist.domain

class GetShopListUseCase(private val shopListRepository: ShopListRepository){
    fun getShopList(): List<ShopItem>{
        return shopListRepository.getShopList()
    }
}
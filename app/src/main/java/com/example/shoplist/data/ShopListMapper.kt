package com.example.shoplist.data

import com.example.shoplist.data.DateBase.ShopItemDBModel
import com.example.shoplist.domain.ShopItem
import javax.inject.Inject

class ShopListMapper @Inject constructor(){
    fun mapEntityToDbModel(shopItem: ShopItem): ShopItemDBModel{
        return ShopItemDBModel(
            id = shopItem.id,
            name = shopItem.name,
            count = shopItem.count,
            enabled = shopItem.enabled
        )
    }

    fun mapDbModelToEntity(shopItemDBModel: ShopItemDBModel): ShopItem{
        return ShopItem(
            id = shopItemDBModel.id,
            name = shopItemDBModel.name,
            count = shopItemDBModel.count,
            enabled = shopItemDBModel.enabled
        )
    }

    fun mapListDbModelToListEntity(list: List<ShopItemDBModel>) = list.map {
        mapDbModelToEntity(it)
    }
}
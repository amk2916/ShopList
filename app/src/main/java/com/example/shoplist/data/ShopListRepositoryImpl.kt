package com.example.shoplist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.shoplist.data.DateBase.AppDataBase
import com.example.shoplist.domain.ShopItem
import com.example.shoplist.domain.ShopListRepository
import java.lang.RuntimeException
import java.util.Random

//Логика репозитория
//для ускорения , делаем пока без БД
class ShopListRepositoryImpl(
    val application: Application
) : ShopListRepository {

    private val shopListDao = AppDataBase.getInstance(application).shopListDao()
    private val mapper = ShopListMapper()


    override fun addShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopListDao.deleteShopItem(shopItem.id)
    }

    override fun editShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return mapper.mapDbModelToEntity(shopListDao.getShopItem(shopItemId))
    }

    override fun getShopList(): LiveData<List<ShopItem>> = MediatorLiveData<List<ShopItem>>().apply {
        addSource(shopListDao.getShopList()){
            mapper.mapListDbModelToListEntity(it)
        }
    }
//     решение, под капотом которого используется МедиаторЛайвДата
//    fun alternativeSolutionGetShopList(): LiveData<List<ShopItem>> = shopListDao.getShopList().map {
//        mapper.mapListDbModelToListEntity(it)
//    }
}
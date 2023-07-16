package com.example.shoplist.di

import android.app.Application
import com.example.shoplist.data.DateBase.AppDataBase
import com.example.shoplist.data.DateBase.ShopListDao
import com.example.shoplist.data.ShopListRepositoryImpl
import com.example.shoplist.domain.ShopListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindRepository(
        shopListRepositoryImpl: ShopListRepositoryImpl
    ) : ShopListRepository

    companion object{
        @ApplicationScope
        @Provides
        fun provideShopListDao(application: Application) : ShopListDao{
            return AppDataBase.getInstance(application).shopListDao()
        }
    }
}
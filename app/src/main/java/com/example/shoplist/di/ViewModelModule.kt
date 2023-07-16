package com.example.shoplist.di

import androidx.lifecycle.ViewModel
import com.example.shoplist.presentation.MainViewModel
import com.example.shoplist.presentation.ShopItemViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(MainViewModel::class)
    @Binds
    fun bideExampleViewModel(impl: MainViewModel): ViewModel

    @IntoMap
    @ViewModelKey(ShopItemViewModel::class)
    @Binds
    fun bideShopItemViewModel(impl: ShopItemViewModel): ViewModel

}
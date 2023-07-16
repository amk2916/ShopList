package com.example.shoplist

import android.app.Application
import com.example.shoplist.di.DaggerApplicationComponent

class App : Application() {
    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}
package com.example.shoplist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.shoplist.R
import com.example.shoplist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout

class ShopItemActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishListener {

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseInten()
        //нам нужно вызывать метод только при первом запуске
        //данная проверка показывает, если налл то первый раз
        //иначе у фрагмента onCreate будет вызываться несколько раз
        /*
        раньше, независимо не от чего мы запускали новый фрагмент
        при этом, при перевороте экрана, если фрагмент уже был добавлен
        то система его пересоздаст и будет вызван onCreate у фрагмента
        при этом у активити также будет вызван onCreate который вызовет
        launchRightMode который вызовет onCreate у фрагмента
        получается при повороте экрана всегда создавалось 2 фрагмента, один руками другой
        система
         */
        if(savedInstanceState == null) {
            launchRightMode()
        }
    }
    private fun launchRightMode() {
        val fragment = when (screenMode) {
            MODE_EDIT -> ShopItemFragment.newInstanceEditItem(shopItemId)
            MODE_ADD -> ShopItemFragment.newInstanceAddItem()
            else -> throw RuntimeException("Unknown screen mode: $screenMode")
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.shop_item_container, fragment)// возьмет старый фрагмент который там был и заменит на  новый
            //.add(R.id.shop_item_container, fragment)//метод позволяет создать новый фрагмент, но с предыдущим ничего не делает , он просто добавляет фрагмент
            .commit()

    }

    private fun parseInten() {
        //Если интент не содержит параметра скрин мод, то бросаем исключение
        if (!intent.hasExtra(EXTRA_SCREEN_MODE))
            throw RuntimeException("Param screen mode is absent")

        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)

        if (mode != MODE_ADD && mode != MODE_EDIT)
            throw RuntimeException("Unknown screen mode $mode")

        screenMode = mode

        if (mode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Param screen mode is absent")
            }
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    /*
        Статичные методы , чтобы не допускать ошибок при создании интентов
        здесь статичноуказаны теги для путэкстра
        и есть два метода создания интента: режим вставки и режим редактирования
        В режиме редактирования нам обязательно нужен айди для юзкейса
        поэтому жестко его задаем принимаемым параметром
     */
    companion object {

        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"

        fun newIntentItemAdd(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentItemEdit(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }

    }

    override fun onEditingFinish() {
        finish()
    }
}
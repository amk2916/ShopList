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

class ShopItemActivity : AppCompatActivity() {

    private lateinit var viewModel: ShopItemViewModel

    private val tilName by lazy { findViewById<TextInputLayout>(R.id.til_name) }
    private val tilCount by lazy { findViewById<TextInputLayout>(R.id.til_count) }
    private val etName by lazy { findViewById<EditText>(R.id.et_name) }
    private val etCount by lazy { findViewById<EditText>(R.id.et_count) }
    private val buttonSave by lazy { findViewById<Button>(R.id.save_button) }

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseInten()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        launchRightMode()
        installTextChangedListener()
        observeViewModel()


    }

    private fun observeViewModel() {
        with(viewModel) {
            errorInputName.observe(this@ShopItemActivity) {
                val message = if (it) {
                    "Error Name"
                } else {
                    null
                }
                tilName.error = message
            }
            errorInputCount.observe(this@ShopItemActivity) {
                val message = if (it) {
                    "Error Count"
                } else {
                    null
                }
                tilCount.error = message
            }
            shouldCloseScreen.observe(this@ShopItemActivity) {
                finish()
            }
        }
    }

    private fun installTextChangedListener() {
        etName.addTextChangedListener(object : TextWatcher {
            //вероятно можно избавится от этого пустого кода
            // пока непонятно как,todo: в процессе поискать
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun launchEditMode() {

        with(viewModel) {
            getShopItem(shopItemId)
            shopItem.observe(this@ShopItemActivity) {
                etName.setText(it.name)
                etCount.setText(it.count.toString())
            }

            buttonSave.setOnClickListener {
                editShopItem(etName.text?.toString(), etCount.text?.toString())
            }
        }


    }

    private fun launchAddMode() {
        buttonSave.setOnClickListener {
            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
        }
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
}
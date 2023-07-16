package com.example.shoplist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.shoplist.data.ShopListRepositoryImpl
import com.example.shoplist.domain.AddShopItemUseCase
import com.example.shoplist.domain.EditShopItemUseCase
import com.example.shoplist.domain.GetShopItemUseCase
import com.example.shoplist.domain.ShopItem
import kotlinx.coroutines.launch

class ShopItemViewModel(application: Application) : AndroidViewModel(application) {

    /*
        способ для валидации ввведенных данных
        мы не можем вызвать show() из viewModel
        но создаем ЛайвДату для того, чтобы в активити
        подписаться на ее изменения  и при изменении
        выдавать ошибку ввода
     */
    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    /*
     Тоже самое для объектов ShopItem
     */
    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    /*
        переменная лайв дата показывающая , можно ли закрыть экран
        Unit, так как нам просто нужно оповестить активити, что можно закрыть экран
        а какой тип вернется не важно
     */
    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    private val repository = ShopListRepositoryImpl(application)

    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)



    /*
         не можем вернуть shopItem, так как во вью работать с распарсом объекта
         не комильфо, поэтому нужен объект лайв дата
         чтобы логика оставалась внутри вьюМодели
     */
    fun getShopItem(shopItemId: Int) {
        viewModelScope.launch {
            val item = getShopItemUseCase.getShopItemById(shopItemId)
            _shopItem.value = item
        }
    }

    fun addShopItem(inputName: String?, inputCount: String?) {

        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldValid = validateInput(name, count)
        if (fieldValid) {
            viewModelScope.launch {
                val shopItem = ShopItem(name, count, true)

                addShopItemUseCase.addShopItem(shopItem)

                /*
                метод оповещающий, что экран готов к закрытию
                можно было бы просто вызвать финиш в активити
                методы могут работать в другом потококе
                и появляется вероятность закрыть экран
                до того как метод завершит свою работу
             */
                finishWork()
            }
        }

    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldValid = validateInput(name, count)
        if (fieldValid) {
             _shopItem.value?.let {
                 viewModelScope.launch {
                     val item = it.copy(name = name, count = count)
                     editShopItemUseCase.editShopItem(item)
                     finishWork()
                 }
             }
        }
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int {
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    fun resetErrorInputName(){
        _errorInputName.value = false
    }

    fun resetErrorInputCount(){
        _errorInputCount.value = false
    }
//     для отмены запросов, я так понимаю он вызывается в конце существования вью модели
//    override fun onCleared() {
//        super.onCleared()
//        scope.cancel()
//    }
}
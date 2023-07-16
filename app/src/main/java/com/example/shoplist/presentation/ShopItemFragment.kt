package com.example.shoplist.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoplist.R
import com.example.shoplist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment() : Fragment() {

    private lateinit var viewModel: ShopItemViewModel

    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var buttonSave: Button

    private lateinit var onEditingFinishListener: OnEditingFinishListener


    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e("onAttach", "Вызван")
        if(context is OnEditingFinishListener){
            onEditingFinishListener = context
        }else{
            throw RuntimeException("Activity must implementation OnEditingFinishListener")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("onCreate", "Вызван")
        parseParam()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("onCreateView", "Вызван")
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    /*
      качестве параметра прилетает View  которое мы создаем в методе onCreateView
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("onViewCreated", "Вызван")
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initView(view)
        launchRightMode()
        installTextChangedListener()
        observeViewModel()
    }

    override fun onStart() {
        super.onStart()
        Log.e("onStart", "Вызван")
    }

    override fun onResume() {
        super.onResume()
        Log.e("onResume", "Вызван")
    }

    override fun onPause() {
        super.onPause()
        Log.e("onPause", "Вызван")
    }

    override fun onStop() {
        super.onStop()
        Log.e("onStop", "Вызван")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("onDestroyView", "Вызван")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("onDestroy", "Вызван")
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("onDetach", "Вызван")
    }

    /*
     в активити был метод parseIntent, но если мы во фрагменте будем получать
     из активити интент, через метод requireActivity, то получается , фрагмент
     установит требования, что активити нужно создавать с какими то обязательными
     параметрами, это не правильно
     */
    private fun parseParam() {
        val args = requireArguments()
        //Если интент не содержит параметра скрин мод, то бросаем исключение
        if (!args.containsKey(EXTRA_SCREEN_MODE))
            throw RuntimeException("Param screen mode is absent")

        val mode = args.getString(EXTRA_SCREEN_MODE)

        if (mode != MODE_ADD && mode != MODE_EDIT)
            throw RuntimeException("Unknown screen mode $mode")

        screenMode = mode

        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Param screen mode is absent")
            }
            shopItemId = args.getInt(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
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

    private fun observeViewModel() {
        with(viewModel) {
            errorInputName.observe(viewLifecycleOwner) {
                val message = if (it) {
                    "Error Name"
                } else {
                    null
                }
                tilName.error = message
            }
            errorInputCount.observe(viewLifecycleOwner) {
                val message = if (it) {
                    "Error Count"
                } else {
                    null
                }
                tilCount.error = message
            }
            shouldCloseScreen.observe(viewLifecycleOwner) {
                onEditingFinishListener.onEditingFinish()
            }
        }
    }

    private fun launchEditMode() {

        with(viewModel) {
            getShopItem(shopItemId)
            shopItem.observe(viewLifecycleOwner) {
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
    /*
     во Фрагмете нет метода findViewById поэтому в конструкторе надо
     передавать созданное вью, и через него вызывать этот метод
     */
    private fun initView(view: View) {
        tilName = view.findViewById(R.id.til_name)
        tilCount = view.findViewById(R.id.til_count)
        etName = view.findViewById(R.id.et_name)
        etCount = view.findViewById(R.id.et_count)
        buttonSave = view.findViewById(R.id.save_button)
    }

    interface OnEditingFinishListener{
        fun onEditingFinish()
    }

    companion object {

        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem(shopItemId: Int):ShopItemFragment{
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_SCREEN_MODE, MODE_EDIT)
                    putInt(EXTRA_SHOP_ITEM_ID, shopItemId)
                }
            }
        }

    }
}
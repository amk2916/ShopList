package com.example.shoplist.presentation

import android.app.Application
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplist.App
import com.example.shoplist.R
import com.example.shoplist.presentation.ShopItemActivity.Companion.newIntentItemAdd
import com.example.shoplist.presentation.ShopItemActivity.Companion.newIntentItemEdit
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishListener {

    private lateinit var viewModel: MainViewModel

    private lateinit var shopListAdapter: ShopListAdapter

    private var shopItemContainer: FragmentContainerView? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (application as App).component
    }

    private val floatingActionButton by lazy {
        findViewById<FloatingActionButton>(R.id.button_add_shop_item)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*
        при клике наэлемент списка, в книжной ореинтации надо отправлять пользователя
        на ShopItemActivity, а при альбомной создавать фрагмент. Определением в каком
        положении находится экран занимается , вроде как, система, типа shopItemContainer
        уществует только в альбомном экране , в ином случае у нее будет null
         */
        shopItemContainer = findViewById(R.id.shop_item_containerMA)

        setupRecyclerView()

        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            //метод ListAdapter вместо установки значения поля, записываем лист так, поле убрали
            //вычисления идут в другом потоке
            shopListAdapter.submitList(it)
        }


        floatingActionButton.setOnClickListener {
            if(isOnePaneMode()){
                val intent = newIntentItemAdd(this)
                startActivity(intent)
            }else{
                launchFragment(ShopItemFragment.newInstanceAddItem())
            }
        }
    }

    override fun onEditingFinish(){
        Toast.makeText(this,"save success",Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }

    //Проверили, в каком состаянии находится экран, этот параметр не налл
    //только в альбомной ореинтации
    private fun isOnePaneMode() : Boolean {
        return shopItemContainer == null
    }

    //выполняем установку фрагмента
    private fun launchFragment(fragment: Fragment){
        //Метод удалит из BackStack рагмент, а если его там не было , то ничего не сделает
        //т е перед добавлением нового фрагмента в контейнер, старый будет удален
        //если этого не сделать фрагменты накладываюься друг на друга в бэкстэк
        supportFragmentManager.popBackStack()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.shop_item_containerMA, fragment)
                //в параметре можно передать ИМЯ или null
                //в методе popBackStack (передаем это ИМЯ, флаг), в зависимости от
                // флага переходим либо на фрагмент ИМЯ удаляя все из бэкстека (флаг 0) либо зависит  от флага
            .addToBackStack(null) //добавляет фрагмент в бэкстэк
            .commit()
    }

    private fun setupRecyclerView() {
        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)
        shopListAdapter = ShopListAdapter()
        with(rvShopList) {
            adapter = shopListAdapter
            //устанавливаем для каждого шаблона максимум в пуле вьюхолдеров
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }
        //Метод выделения элемента по долгому нажатию
        setupLongClickListener()
        setupClickListener()
        //метод удаления элемента по свайпу в любую сторону
        setupSwipeListener(rvShopList)
    }

    private fun setupSwipeListener(rvShopList: RecyclerView?) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //обращаемся к списку через метод ListAdapter
                val shopItem = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(shopItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun setupClickListener() {
        shopListAdapter.onShopItemClickListener = {
            if(isOnePaneMode()) {
                val intent = newIntentItemEdit(this, it.id)
                startActivity(intent)
            }else{
                launchFragment(ShopItemFragment.newInstanceEditItem(it.id))
            }
        }


    }

    private fun setupLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }

}



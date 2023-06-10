package com.example.shoplist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplist.R
import com.example.shoplist.domain.ShopItem

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {
    var shopList = listOf<ShopItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ShopItemViewHolder(val parentView: View) : RecyclerView.ViewHolder(parentView) {
        val tvName = parentView.findViewById<TextView>(R.id.tv_name)
        val tvCount = parentView.findViewById<TextView>(R.id.tv_count)
    }
    var count = 0
    //Как создать View
    //ViewType нужно, если у нас описывается несколькими шаблонами
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        //создаем view из xml при помощи LayoutInflater метода Layout, передаем шаблон , группу -родителя куда будем крепить
        // и надо ли закрепить или нет
        Log.e("onCreateViewHolder","Вызван =${++count}")
        val layout = when(viewType){
            VIEW_TYPE_ENABLED -> R.layout.item_shop_enabled
            VIEW_TYPE_DISABLED -> R.layout.item_shop_disabled
            else -> throw RuntimeException("Unknow view type: $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ShopItemViewHolder(view)
    }


    override fun getItemCount(): Int {
        return shopList.size
    }

    //Как вставлять значения внутри View
    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = shopList[position]
        holder.tvName.text = shopItem.name
        holder.tvCount.text = shopItem.count.toString()
        holder.parentView.setOnLongClickListener {
            true
        }
    }

    //используется в тот момент когда вью холдер хотят переиспользовать (можно установить
    // значение view  по умолчанию)
    override fun onViewRecycled(holder: ShopItemViewHolder) {
        super.onViewRecycled(holder)
    }
    //определяем viewType в случае если у нас несколько шаблонов
    override fun getItemViewType(position: Int): Int {
        val item = shopList[position]
        return if (item.enabled) {
            VIEW_TYPE_ENABLED
        } else {
            VIEW_TYPE_DISABLED
        }
    }

    companion object{
        // Максимальный размер пула ViewHolder
        const val MAX_POOL_SIZE = 20

        //Типы шаблона
        const val VIEW_TYPE_ENABLED = 100
        const val VIEW_TYPE_DISABLED = 101
    }
}
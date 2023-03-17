package com.example.blecomunication.adapter



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.blecomunication.R
import mva2.adapter.ItemBinder
import mva2.adapter.ItemViewHolder


private class BeerBinder : ItemBinder<CarModel, BeerBinder.CarViewHolder?>() {
    override fun bindViewHolder(viewHolder: CarViewHolder?, p1: CarModel?) {
        
    }

    override fun createViewHolder(parent: ViewGroup?): CarViewHolder {
        val view = LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_beer, parent, false)
        return CarViewHolder(view)
    }

    override fun canBindData(item: Any?): Boolean {
        return item is CarModel
    }

   
    class CarViewHolder(itemView: View?) : ItemViewHolder<CarModel>(itemView){

    }


}
data class CarModel(val id : Int = 0, val name: String)

package com.example.blecomunication.adapter


import android.R
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import mva2.adapter.ItemBinder
import mva2.adapter.ItemViewHolder


private class CarBinder : ItemBinder<CarModel, CarBinder.CarViewHolder?>() {
    override fun bindViewHolder(p0: CarViewHolder?, p1: CarModel?) {
    }

    override fun createViewHolder(parent: ViewGroup?): CarViewHolder {
        return CarViewHolder(inflate(R.layout.item_car, parent))
    }

    override fun canBindData(item: Any?): Boolean {
        return item is CarModel
    }

    fun bindViewHolder(holder: CarViewHolder, item: CarModel) {
        holder.tvCarName.setText(item.getName())
    }
    class CarViewHolder(itemView: View?) : ItemViewHolder<CarModel>(itemView){

    }


}
data class CarModel(val id : Int = 0, val name: String)

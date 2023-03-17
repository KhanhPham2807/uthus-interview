package com.example.blecomunication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.blecomunication.R
import com.example.blecomunication.databinding.ItemBeerBinding
import com.example.blecomunication.model.Beer
import mva2.adapter.ItemBinder
import mva2.adapter.ItemViewHolder

class ItemBeerBinder : ItemBinder<Beer, ItemBeerBinder.BeerViewHolder?>() {
    override fun bindViewHolder(viewHolder: BeerViewHolder?, beer: Beer?) {
        beer?.let {
            viewHolder?.bindView(it)
        }

    }

    override fun createViewHolder(parent: ViewGroup?): BeerViewHolder {

        return from(parent)
    }

    override fun canBindData(item: Any?): Boolean {
        return item is Beer
    }


    class BeerViewHolder  constructor(val  binding: ItemBeerBinding?) : ItemViewHolder<Beer>(binding?.root) {
        fun bindView(beerItem : Beer){
            binding?.tvBeerName?.text = beerItem.name
            binding?.tvPrice?.text = beerItem.price
        }
    }

    companion object{
        fun from(parent: ViewGroup?) : BeerViewHolder{
            val layoutInflater = LayoutInflater.from(parent?.context)
            val binding = ItemBeerBinding.inflate(layoutInflater, parent, false)
            return BeerViewHolder(binding)
        }
    }


}

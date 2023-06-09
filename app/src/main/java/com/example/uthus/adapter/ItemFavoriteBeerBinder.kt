package com.example.uthus.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.uthus.common.extention.setSafeOnClickListener
import com.example.uthus.databinding.ItemFavoriteBeerBinding
import com.example.uthus.model.BeerLocal

import mva2.adapter.ItemBinder
import mva2.adapter.ItemViewHolder

class ItemFavoriteBeerBinder(val onBtnSaveClick: (BeerLocal, String, Int) -> Unit , val onBtnDeleteClick : (BeerLocal, Int) -> Unit   ) :
    ItemBinder<BeerLocal, ItemFavoriteBeerBinder.FavoriteBeerViewHolder?>() {

    override fun bindViewHolder(viewHolder: FavoriteBeerViewHolder?, BeerLocal: BeerLocal?) {

        BeerLocal?.let {
            viewHolder?.bindView(it,onBtnSaveClick,onBtnDeleteClick)
        }

    }

    override fun createViewHolder(parent: ViewGroup?): FavoriteBeerViewHolder {

        return from(parent )
    }

    override fun canBindData(item: Any?): Boolean {
        return item is BeerLocal
    }


    class FavoriteBeerViewHolder constructor(val binding: ItemFavoriteBeerBinding?) :
        ItemViewHolder<BeerLocal>(binding?.root) {

        fun bindView(
            BeerLocalItem: BeerLocal,
            onBtnSaveClick: (BeerLocal, String, Int) -> Unit,
            onBtnDeleteClick: (BeerLocal, Int) -> Unit
        ) {
            binding?.item = BeerLocalItem

            binding?.btnEdit?.setSafeOnClickListener {
                onBtnSaveClick.invoke(item, binding.editNote.text.toString(),   this@FavoriteBeerViewHolder.bindingAdapterPosition)
            }
            binding?.btnDelete?.setSafeOnClickListener {
                onBtnDeleteClick.invoke(item,this@FavoriteBeerViewHolder.bindingAdapterPosition)
            }


        }
    }

    companion object {
        fun from(parent: ViewGroup?): FavoriteBeerViewHolder {
            val layoutInflater = LayoutInflater.from(parent?.context)
            val binding = ItemFavoriteBeerBinding.inflate(layoutInflater, parent, false)

            return FavoriteBeerViewHolder(binding)
        }
    }


}

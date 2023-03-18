package com.example.uthus.adapter

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.uthus.R
import com.example.uthus.common.extention.setSafeOnClickListener
import com.example.uthus.databinding.ItemBeerBinding
import com.example.uthus.model.BeerResponse
import com.example.uthus.model.BeerResponse.Companion.SaveStatus.SAVED
import com.example.uthus.model.BeerResponse.Companion.SaveStatus.SAVING
import com.example.uthus.model.BeerResponse.Companion.SaveStatus.UNSAVED
import mva2.adapter.ItemBinder
import mva2.adapter.ItemViewHolder

class ItemBeerBinder(val onBtnSaveClick: (BeerResponse,String, Int) -> Unit) :
    ItemBinder<BeerResponse, ItemBeerBinder.BeerViewHolder?>() {
    override fun bindViewHolder(viewHolder: BeerViewHolder?, beerResponse: BeerResponse?) {

        beerResponse?.let {
            viewHolder?.bindView(it,onBtnSaveClick)
        }

    }

    override fun createViewHolder(parent: ViewGroup?): BeerViewHolder {

        return from(parent )
    }

    override fun canBindData(item: Any?): Boolean {
        return item is BeerResponse
    }


    class BeerViewHolder constructor(val binding: ItemBeerBinding?) :
        ItemViewHolder<BeerResponse>(binding?.root) {

        fun bindView(beerResponseItem: BeerResponse, onBtnSaveClick: (BeerResponse,String,Int ) -> Unit) {
            binding?.item = beerResponseItem

            binding?.btnSave?.setSafeOnClickListener {
                onBtnSaveClick.invoke(item, binding.editNote.text.toString(),   this@BeerViewHolder.bindingAdapterPosition)
            }
            when(beerResponseItem.saveStatus){
                SAVING ->{
                    binding?.btnSave?.visibility = View.VISIBLE
                    binding?.editNote?.visibility = View.VISIBLE
                    binding?.btnSave?.text = "SAVING"
                    binding?.btnSave?.isEnabled = false

                }
                SAVED ->{
                    binding?.btnSave?.visibility = View.GONE
                    binding?.btnSave?.isEnabled = false
                    binding?.editNote?.isEnabled  = false
                    binding?.editNote?.visibility = View.VISIBLE

                }
                UNSAVED ->{
                    binding?.btnSave?.visibility = View.VISIBLE
                    binding?.editNote?.visibility = View.VISIBLE
                    binding?.btnSave?.text = "SAVE"
                    binding?.btnSave?.isEnabled = true
                    binding?.editNote?.isEnabled  = true
                }
            }

        }
    }

    companion object {
        fun from(parent: ViewGroup?): BeerViewHolder {
            val layoutInflater = LayoutInflater.from(parent?.context)
            val binding = ItemBeerBinding.inflate(layoutInflater, parent, false)

            return BeerViewHolder(binding)
        }
    }


}

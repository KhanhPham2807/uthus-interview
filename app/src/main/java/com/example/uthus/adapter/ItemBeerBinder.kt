package com.example.uthus.adapter

import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.uthus.common.extention.setSafeOnClickListener
import com.example.uthus.common.millisToDHMS
import com.example.uthus.databinding.ItemBeerBinding
import com.example.uthus.model.BeerResponse
import com.example.uthus.model.BeerResponse.Companion.SaveStatus.SAVED
import com.example.uthus.model.BeerResponse.Companion.SaveStatus.SAVING
import com.example.uthus.model.BeerResponse.Companion.SaveStatus.UNSAVED
import kotlinx.coroutines.*
import mva2.adapter.ItemBinder
import mva2.adapter.ItemViewHolder
import java.util.*
import java.util.concurrent.TimeUnit


class ItemBeerBinder(val onBtnSaveClick: (BeerResponse,String, Int) -> Unit) :
    ItemBinder<BeerResponse, ItemBeerBinder.BeerViewHolder?>() {
    private val countDownMap = HashMap<Int, CountDownTimer>()
    override fun bindViewHolder(viewHolder: BeerViewHolder?, beerResponse: BeerResponse?) {

        beerResponse?.let {
            viewHolder?.bindView(it, onBtnSaveClick)

        }





    }

    private fun updateCountdownTimer(viewHolder: BeerViewHolder?, targetTime: Long?) {

    }


    override fun createViewHolder(parent: ViewGroup?): BeerViewHolder {

        return from(parent )
    }

    override fun canBindData(item: Any?): Boolean {
        return item is BeerResponse
    }


    class BeerViewHolder constructor(val binding: ItemBeerBinding?) :
        ItemViewHolder<BeerResponse>(binding?.root) {

        private var job: Job? = null
        fun bindView(
            beerResponseItem: BeerResponse,
            onBtnSaveClick: (BeerResponse, String, Int) -> Unit,
        ) {

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
            recycle()
            var remainingTime = beerResponseItem.saleOffTime?.minus(System.currentTimeMillis())
            if(remainingTime!! >0){
                job = CoroutineScope(Dispatchers.Main).launch {
                    if (remainingTime != null) {
                        while (isActive && remainingTime!! > 0) {
                            // Update your countdown timer view element here

                            // Delay for 1 second before updating again
                            delay(1000)

                            // Recalculate the remaining time in milliseconds
                            val currentTime = System.currentTimeMillis()
                            remainingTime = item.saleOffTime?.minus(currentTime)

                            updateSaleOfTime(millisToDHMS(remainingTime!!))


                        }
                    }
                }
            }
            else{
                updateSaleOfTime("Saled off")
            }



        }

        private fun recycle() {
            // Cancel the coroutine job if it is still active
            job?.cancel()
        }
        fun millisToDHMS(duration: Long): String {
            val days = TimeUnit.MILLISECONDS.toDays(duration)
            val hours = TimeUnit.MILLISECONDS.toHours(duration) % 24
            val minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60
            val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60
            val months = days / 30
            val remainingDays = days % 30
            return " $hours:$minutes:$seconds  $remainingDays-$months,"
        }

        fun updateSaleOfTime(s: String) {
            binding?.tvSaleOffTime?.text = s
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

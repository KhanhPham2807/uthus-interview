package com.example.uthus.adapter

import android.icu.text.SimpleDateFormat
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.uthus.common.extention.setSafeOnClickListener
import com.example.uthus.databinding.ItemBeerBinding
import com.example.uthus.model.BeerResponse
import com.example.uthus.model.BeerResponse.Companion.SaveStatus.SAVED
import com.example.uthus.model.BeerResponse.Companion.SaveStatus.SAVING
import com.example.uthus.model.BeerResponse.Companion.SaveStatus.UNSAVED
import mva2.adapter.ItemBinder
import mva2.adapter.ItemViewHolder
import java.util.*
import java.util.concurrent.TimeUnit

class ItemBeerBinder(val onBtnSaveClick: (BeerResponse,String, Int) -> Unit) :
    ItemBinder<BeerResponse, ItemBeerBinder.BeerViewHolder?>() {
    private val countDownMap = HashMap<Int, CountDownTimer>()
    override fun bindViewHolder(viewHolder: BeerViewHolder?, beerResponse: BeerResponse?) {

        beerResponse?.let {
            viewHolder?.bindView(it,onBtnSaveClick,countDownMap)
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

        fun bindView(
            beerResponseItem: BeerResponse,
            onBtnSaveClick: (BeerResponse, String, Int) -> Unit,
            countDownMap: HashMap<Int, CountDownTimer>
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
            val currentTime = System.currentTimeMillis()
            val diff = item.saleOffTime?.minus(currentTime)

            if (countDownMap.containsKey(item.id)) {
                countDownMap[item.id]?.cancel()
            }
            val countDownTimer = object : CountDownTimer(diff!!, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                    binding?.tvSaleOffTime?.text = "Sale of in ${millisToDHMS(millisUntilFinished)} "
                }

                override fun onFinish() {
                    // Do something when the countdown is finished
                    binding?.tvSaleOffTime?.text = "Sale Off"
                }
            }

            countDownMap[item.id] = countDownTimer
            countDownTimer.start()

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
    }


    companion object {
        fun from(parent: ViewGroup?): BeerViewHolder {
            val layoutInflater = LayoutInflater.from(parent?.context)
            val binding = ItemBeerBinding.inflate(layoutInflater, parent, false)

            return BeerViewHolder(binding)
        }
    }


}

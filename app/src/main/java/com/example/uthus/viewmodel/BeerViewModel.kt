package com.example.uthus.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uthus.model.Beer
import com.example.uthus.network.NetworkResult
import com.example.uthus.repository.BeerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeerViewModel @Inject constructor(val beerRepository: BeerRepository)  : ViewModel() {
    private var _beerResponseChannel = Channel<List<Beer>>()
    val beerResponseChannel = _beerResponseChannel.receiveAsFlow()
    fun getListBeers(){
        viewModelScope.launch {

            beerRepository.getListBeer().collect{
                when(it){
                    is NetworkResult.LoadingDialog ->{
                        Log.d(TAG, "LoadingDialog: ")
                    }
                    is NetworkResult.Success ->{
                        it.dataResponse?.data?.let { it1 -> _beerResponseChannel.send(it1) }
                    }
                    is NetworkResult.Error ->{
                        Log.d(TAG, "Error: ")
                    }
                    else -> {

                    }

                }
            }

        }
    }
    companion object{
        const val TAG ="Henry"
    }
}
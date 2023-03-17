package com.example.uthus.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uthus.network.NetworkResult
import com.example.uthus.repository.BeerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeerViewModel @Inject constructor(val beerRepository: BeerRepository)  : ViewModel() {
    fun getBeer(){
        viewModelScope.launch {

            beerRepository.getListBeer().collect{
                when(it){
                    is NetworkResult.LoadingDialog ->{
                        Log.d(TAG, "LoadingDialog: ")
                    }
                    is NetworkResult.Success ->{
                        Log.d(TAG, "Success: ${it.dataResponse?.data} ")
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
package com.example.uthus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uthus.common.CoroutineDispatcherProvider
import com.example.uthus.db_manager.UthusDBController
import com.example.uthus.model.BeerLocal
import com.example.uthus.model.BeerResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val uthusDBController: UthusDBController,
    private val dispatcher: CoroutineDispatcherProvider
) : ViewModel() {

    private var _favoriteBeersFlow = MutableStateFlow<GetBeerLocalState?>(null)
    val favoriteBeersFlow: StateFlow<GetBeerLocalState?> = _favoriteBeersFlow

    fun getListFavoriteBeer(){
        viewModelScope.launch {
            _favoriteBeersFlow.emit(GetBeerLocalState.Loading)
           val listFavoriteBeer = uthusDBController.beerDao.getAllBeer()
            _favoriteBeersFlow.emit(GetBeerLocalState.Success(listFavoriteBeer))
        }
    }

    sealed class GetBeerLocalState {
        object Loading : GetBeerLocalState()
        class Success(val listBeerResponse: List<BeerLocal>) : GetBeerLocalState()

        class Error(val errorMessage: String?) : GetBeerLocalState()
    }

}
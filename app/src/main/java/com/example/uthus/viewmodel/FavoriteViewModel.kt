package com.example.uthus.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uthus.common.Constant.DEFAULT_LIMIT_PAGINATION
import com.example.uthus.common.CoroutineDispatcherProvider
import com.example.uthus.common.wrapper.DataListWrapper
import com.example.uthus.db_manager.UthusDBController
import com.example.uthus.model.BeerLocal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val uthusDBController: UthusDBController,
    private val dispatcher: CoroutineDispatcherProvider
) : ViewModel() {

    private var _getFavoriteBeersFlow = MutableStateFlow<GetBeerLocalState?>(null)
    val getFavoriteBeersFlow: StateFlow<GetBeerLocalState?> = _getFavoriteBeersFlow

    private var _deleteBeersFlow = MutableStateFlow<DeleteBeerState?>(null)
    val deleteBeersFlow: StateFlow<DeleteBeerState?> = _deleteBeersFlow

    private var _updateBeerFlow = MutableStateFlow<UpdateBeerState?>(null)
    val updateBeerFlow: StateFlow<UpdateBeerState?> = _updateBeerFlow

    private var dataListWrapper = DataListWrapper<BeerLocal>()

    private var _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private fun getListFavoriteBeer(shouldShowLoading: Boolean) {
        viewModelScope.launch {
            if (shouldShowLoading) {
                _getFavoriteBeersFlow.emit(GetBeerLocalState.Loading)
            }

            val listFavoriteBeer = uthusDBController.beerDao.getBeer(
                offset = dataListWrapper.offset
            )
            dataListWrapper.dataList.addAll(listFavoriteBeer)
            dataListWrapper.allowLoadMore = listFavoriteBeer.size >= DEFAULT_LIMIT_PAGINATION
            _getFavoriteBeersFlow.emit(GetBeerLocalState.Success(dataListWrapper.dataList))
        }
    }

    fun startGetListFavoriteBeer(shouldShowLoading: Boolean) {

        viewModelScope.launch {
            _uiEvent.send(UIEvent.ClearSection)
            dataListWrapper.reset()
            getListFavoriteBeer(shouldShowLoading)
        }


    }

    fun loadMoreFavoriteBeer() {

        if (dataListWrapper.allowLoadMore) {
            dataListWrapper.offset += DEFAULT_LIMIT_PAGINATION
            getListFavoriteBeer(false)
        }
    }

    fun deleteBeerLocal(beerLocal: BeerLocal, itemPosition: Int) {
        viewModelScope.launch(dispatcher.io) {
            _deleteBeersFlow.emit(DeleteBeerState.Loading)

            beerLocal.id?.let {
                uthusDBController.beerDao.deleteBeer(
                    it
                )
            }
            dataListWrapper.dataList.find {
                it.id == beerLocal.id
            }?.let {
                dataListWrapper.dataList.remove(it)
            }
            _deleteBeersFlow.emit(DeleteBeerState.Success(itemPosition))
        }
    }

    fun updateBeerLocal(
        beerLocal: BeerLocal,
        note: String,
        itemPosition: Int
    ) {
        viewModelScope.launch {
            viewModelScope.launch(dispatcher.io) {
                _updateBeerFlow.emit(UpdateBeerState.Loading)
                dataListWrapper.dataList.find {
                    it.id == beerLocal.id
                }?.let {
                    it.note = note
                }
                uthusDBController.beerDao.updateUser(
                    beerLocal
                )


                _updateBeerFlow.emit(UpdateBeerState.Success(itemPosition))
            }
        }
    }


    sealed class GetBeerLocalState {
        object Loading : GetBeerLocalState()
        class Success(val listBeerResponse: List<BeerLocal>) : GetBeerLocalState()
        class Error(val errorMessage: String?) : GetBeerLocalState()
    }

    sealed class DeleteBeerState {
        object Loading : DeleteBeerState()
        class Success(val position: Int) : DeleteBeerState()
        class Error(val errorMessage: String?) : DeleteBeerState()
    }

    sealed class UpdateBeerState {
        object Loading : UpdateBeerState()
        class Success(val position: Int) : UpdateBeerState()
        class Error(val errorMessage: String?) : UpdateBeerState()
    }

    sealed class UIEvent {
        object ClearSection : UIEvent()

    }

}
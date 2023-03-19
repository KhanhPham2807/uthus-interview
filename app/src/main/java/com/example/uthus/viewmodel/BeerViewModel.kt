package com.example.uthus.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uthus.common.CoroutineDispatcherProvider
import com.example.uthus.common.helper.FileHelper
import com.example.uthus.common.wrapper.DataListWrapper
import com.example.uthus.db_manager.UthusDBController
import com.example.uthus.model.BeerLocal
import com.example.uthus.model.BeerResponse
import com.example.uthus.model.BeerResponse.Companion.SaveStatus.SAVED
import com.example.uthus.model.BeerResponse.Companion.SaveStatus.SAVING
import com.example.uthus.network.NetworkResult
import com.example.uthus.repository.BeerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeerViewModel @Inject constructor(
    private val beerRepository: BeerRepository,
    private val uthusDBController: UthusDBController,
    private val dispatcher: CoroutineDispatcherProvider
) : ViewModel() {
    private val dataListWrapper = DataListWrapper<BeerResponse>()

//
//    private var _beerResponseChannel = Channel<List<Beer>>()
//    val beerResponseChannel = _beerResponseChannel.receiveAsFlow()

    private var _beerResponseFlow = MutableStateFlow<FetchBeerState?>(null)
    val beerResponseFlow: StateFlow<FetchBeerState?> = _beerResponseFlow


    private var _saveBeerFlow = MutableStateFlow<SaveBeerState?>(null)
    val saveBeerFlow: StateFlow<SaveBeerState?> = _saveBeerFlow

    private var _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()





    private fun getListBeers(shouldShowLoading: Boolean, isLoadMore: Boolean) {
        viewModelScope.launch {

            beerRepository.getListBeerCheckExist(dataListWrapper.currentPage, shouldShowLoading)
                .collect { networkResult ->
                    when (networkResult) {
                        is NetworkResult.LoadingDialog -> {
                            _beerResponseFlow.emit(FetchBeerState.Loading)
                        }
                        is NetworkResult.Success -> {
                            dataListWrapper
                            networkResult.dataResponse?.let {
                                if (it.data != null) {
                                    dataListWrapper.dataList.addAll(it.data!!)
                                    dataListWrapper.allowLoadMore = it.allowLoadMore
                                }
                            }
                            _beerResponseFlow.emit(FetchBeerState.Success(dataListWrapper.dataList, isLoadMore))
                        }
                        is NetworkResult.Error -> {
                            _beerResponseFlow.emit(FetchBeerState.Error(networkResult?.baseErrorApiResponse?.message))
                        }
                        else -> {

                        }

                    }
                }

        }
    }

    fun startGetListBeer(shouldShowLoading: Boolean) {
        viewModelScope.launch {
            _uiEvent.send(UIEvent.ClearSection)
            dataListWrapper.reset()
            getListBeers(shouldShowLoading, false)
        }

    }

    fun loadMoreBeer() {
        if (dataListWrapper.allowLoadMore) {
            dataListWrapper.currentPage++
            getListBeers(false, true)
        }
    }

    fun saveBeerToFavorite(context :Context,beerResponse: BeerResponse, note: String, beerPosition :Int) {
        viewModelScope.launch(dispatcher.io) {
            val selectedBeer =   dataListWrapper.dataList.find {
                it.id  ==  beerResponse.id
            }
            selectedBeer?.saveStatus = SAVING
            selectedBeer?.note = note
            _saveBeerFlow.emit(SaveBeerState.Loading(beerPosition))
            val imageFilePatch = FileHelper.downloadFileFromURL(context, beerResponse.image)
            uthusDBController.beerDao.insertBeer(
                BeerLocal.mapData(
                    beerResponse = beerResponse,
                    note = note,
                    imageURL = imageFilePatch
                )
            )
            selectedBeer?.saveStatus = SAVED
            _saveBeerFlow.emit(SaveBeerState.Success(beerPosition))
        }
    }

    companion object {
        const val TAG = "Henry"
    }

    sealed class FetchBeerState {
        object Loading : FetchBeerState()
        class Success(val listBeerResponse: List<BeerResponse>,val isLoadMore: Boolean) : FetchBeerState()

        class Error(val errorMessage: String?) : FetchBeerState()
    }

    sealed class SaveBeerState {
        class Loading(val beerPosition: Int) : SaveBeerState()
        class Success(val beerPosition: Int) : SaveBeerState()
        class Error(val errorMessage: String?) : SaveBeerState()
    }
    sealed class UIEvent {
        object ClearSection :UIEvent()

    }

}
package com.example.uthus.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uthus.R

import com.example.uthus.adapter.ItemFavoriteBeerBinder
import com.example.uthus.common.extention.FragmentExt.collectFlowWhenStarted
import com.example.uthus.common.extention.onReachBottom
import com.example.uthus.databinding.FragmentFavoriteBinding
import com.example.uthus.model.BeerLocal
import com.example.uthus.model.BeerResponse
import com.example.uthus.viewmodel.BeerViewModel
import com.example.uthus.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mva2.adapter.ListSection
import mva2.adapter.MultiViewAdapter

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private lateinit var dataBinding: FragmentFavoriteBinding
    val adapter by lazy {
        MultiViewAdapter()
    }
    var listSection: ListSection<BeerLocal> = ListSection<BeerLocal>()
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false)
        this.dataBinding.lifecycleOwner = this.viewLifecycleOwner
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        collectDataFromViewModel()
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    private fun collectDataFromViewModel() {
        with(favoriteViewModel) {

            collectFlowWhenStarted(getFavoriteBeersFlow) { getBeerState ->

                when (getBeerState) {
                    is FavoriteViewModel.GetBeerLocalState.Loading -> {
                        dataBinding.progressCircular.visibility = android.view.View.VISIBLE
                    }
                    is FavoriteViewModel.GetBeerLocalState.Success -> {
                        dataBinding.refreshLayout.isRefreshing = false
                        dataBinding.progressCircular.visibility = android.view.View.GONE
                        addListBeerToView(getBeerState.listBeerResponse)
                    }
                    is FavoriteViewModel.GetBeerLocalState.Error -> {
                        dataBinding.refreshLayout.isRefreshing = false
                        dataBinding.progressCircular.visibility = android.view.View.GONE
                        android.widget.Toast.makeText(
                            requireContext(),
                            getBeerState.errorMessage,
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {}
                }

            }
            collectFlowWhenStarted(deleteBeersFlow) { deleteBeerState ->

                when (deleteBeerState) {
                    is FavoriteViewModel.DeleteBeerState.Loading -> {
                        dataBinding.progressCircular.visibility = android.view.View.VISIBLE
                    }
                    is FavoriteViewModel.DeleteBeerState.Success -> {

                            dataBinding.progressCircular.visibility = android.view.View.GONE
                            listSection.remove(deleteBeerState.position)
                            adapter.notifyDataSetChanged()





                      //  adapter.notifyItemRangeRemoved(0, 1)
                    }
                    is FavoriteViewModel.DeleteBeerState.Error -> {
                        dataBinding.refreshLayout.isRefreshing = false
                        dataBinding.progressCircular.visibility = android.view.View.GONE
                        android.widget.Toast.makeText(
                            requireContext(),
                            deleteBeerState.errorMessage,
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }


                }

            }
            collectFlowWhenStarted(uiEvent){event ->
                when(event){
                    is FavoriteViewModel.UIEvent.ClearSection ->{
                        adapter.removeAllSections()
                        listSection= ListSection<BeerLocal>()
                    }
                }
            }
            collectFlowWhenStarted(updateBeerFlow) { updateBeerFlow ->

                when (updateBeerFlow) {
                    is FavoriteViewModel.UpdateBeerState.Loading -> {
                        dataBinding.progressCircular.visibility = android.view.View.VISIBLE
                    }
                    is FavoriteViewModel.UpdateBeerState.Success -> {
                            dataBinding.progressCircular.visibility = android.view.View.GONE
                            adapter.notifyItemChanged(updateBeerFlow.position)
                        //  adapter.notifyItemRangeRemoved(0, 1)
                    }
                    is FavoriteViewModel.UpdateBeerState.Error -> {
                        dataBinding.refreshLayout.isRefreshing = false
                        dataBinding.progressCircular.visibility = android.view.View.GONE
                        android.widget.Toast.makeText(
                            requireContext(),
                            updateBeerFlow.errorMessage,
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }


                }

            }
        }
    }

    private fun addListBeerToView(listBeerResponse: List<BeerLocal>) {
        listSection= ListSection<BeerLocal>()
        listSection.addAll(listBeerResponse)

        adapter.addSection(listSection)


    }

    private fun fetchData() {

        favoriteViewModel.startGetListFavoriteBeer(true)
    }

    private fun initView() {

        dataBinding.recyclerFavoriteBeer.layoutManager = LinearLayoutManager(context)
        dataBinding.recyclerFavoriteBeer.setAdapter(adapter)
        adapter.registerItemBinders(
            ItemFavoriteBeerBinder(
                onBtnSaveClick = { beerLocal, note, itemPosition ->
                   favoriteViewModel.updateBeerLocal(beerLocal,note,itemPosition)
                },
                onBtnDeleteClick = { beerLocal: BeerLocal, itemPosition: Int ->
                    favoriteViewModel.deleteBeerLocal(beerLocal,itemPosition)
                })
        )
        dataBinding.recyclerFavoriteBeer.onReachBottom {
            favoriteViewModel.loadMoreFavoriteBeer()
        }
        dataBinding.refreshLayout.setOnRefreshListener {

            favoriteViewModel.startGetListFavoriteBeer(
                shouldShowLoading = false
            )

        }
    }


}
package com.example.uthus.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uthus.R

import com.example.uthus.adapter.ItemFavoriteBeerBinder
import com.example.uthus.common.extention.FragmentExt.collectFlowWhenStarted
import com.example.uthus.common.extention.onReachBottom
import com.example.uthus.databinding.FragmentBeerBinding
import com.example.uthus.databinding.FragmentFavoriteBinding
import com.example.uthus.databinding.ItemFavoriteBeerBinding
import com.example.uthus.model.BeerLocal
import com.example.uthus.model.BeerResponse
import com.example.uthus.viewmodel.BeerViewModel
import com.example.uthus.viewmodel.FavoriteViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import mva2.adapter.ListSection
import mva2.adapter.MultiViewAdapter

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private lateinit var dataBinding: FragmentFavoriteBinding
    val adapter by lazy {
        MultiViewAdapter()
    }
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
        fetchData()
        collectDataFromViewModel()
    }

    private fun collectDataFromViewModel() {
        with(favoriteViewModel) {

            collectFlowWhenStarted(favoriteBeersFlow) { getBeerState ->

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

                }

            }
        }
    }
    private fun addListBeerToView(listBeerResponse: List<BeerLocal>) {
        val listSection: ListSection<BeerLocal> = ListSection<BeerLocal>()
        listSection.addAll(listBeerResponse)
        adapter.addSection(listSection)
    }

    private fun fetchData() {
        favoriteViewModel.getListFavoriteBeer()
    }

    private fun initView() {

        dataBinding.recyclerFavoriteBeer.layoutManager = LinearLayoutManager(context)
        dataBinding.recyclerFavoriteBeer.setAdapter(adapter)
        adapter.registerItemBinders(ItemFavoriteBeerBinder(onBtnSaveClick = { beerResponse, note, itemPosition ->
            //    favoriteViewModel.saveBeerToFavorite(requireContext(),beerResponse,note,itemPosition)
        }))
        dataBinding.recyclerFavoriteBeer.onReachBottom {
            // beerViewModel.loadMoreBeer()
        }
        dataBinding.refreshLayout.setOnRefreshListener {
//            beerViewModel.startGetListBeer(
//                shouldShowLoading = false
//            )

        }
    }


}
package com.example.uthus.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uthus.R
import com.example.uthus.adapter.ItemBeerBinder
import com.example.uthus.common.extention.FragmentExt.collectFlowWhenStarted
import com.example.uthus.common.extention.onReachBottom
import com.example.uthus.databinding.FragmentBeerBinding
import com.example.uthus.model.BeerResponse
import com.example.uthus.viewmodel.BeerViewModel
import dagger.hilt.android.AndroidEntryPoint
import mva2.adapter.ListSection
import mva2.adapter.MultiViewAdapter
import mva2.adapter.util.InfiniteLoadingHelper


/**
 * A simple [Fragment] subclass.
 * Use the [BeerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class BeerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var dataBinding: FragmentBeerBinding
    val adapter by lazy {
        MultiViewAdapter()
    }
    private var listSection: ListSection<BeerResponse> = ListSection<BeerResponse>()
    private val beerViewModel: BeerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_beer, container, false)
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
        with(beerViewModel) {

            collectFlowWhenStarted(beerResponseFlow) { getBeerState ->

                when (getBeerState) {
                    is BeerViewModel.FetchBeerState.Loading -> {
                        dataBinding.progressCircular.visibility = View.VISIBLE
                    }
                    is BeerViewModel.FetchBeerState.Success -> {
                        dataBinding.refreshLayout.isRefreshing = false
                        dataBinding.progressCircular.visibility = View.GONE
                        addListBeerToView(getBeerState.listBeerResponse, getBeerState.isLoadMore)
                    }
                    is BeerViewModel.FetchBeerState.Error -> {
                        dataBinding.refreshLayout.isRefreshing = false
                        dataBinding.progressCircular.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            getBeerState.errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

            }
            collectFlowWhenStarted(saveBeerFlow) { saveBeerState ->

                when (saveBeerState) {
                    is BeerViewModel.SaveBeerState.Loading -> {
                        adapter.notifyItemChanged(saveBeerState.beerPosition)

                    }
                    is BeerViewModel.SaveBeerState.Success -> {
                        adapter.notifyItemChanged(saveBeerState.beerPosition)
                    }
                    else -> {

                    }


                }

            }
            collectFlowWhenStarted(uiEvent) { event ->
                when (event) {
                    is BeerViewModel.UIEvent.ClearSection -> {
                        adapter.removeAllSections()
                        listSection = ListSection<BeerResponse>()

                    }
                }
            }
        }
    }

    private fun addListBeerToView(listBeerResponse: List<BeerResponse>, isLoadMore: Boolean) {
        if (!isLoadMore) {
            listSection.addAll(listBeerResponse)
            adapter.addSection(listSection)
        } else {
            listSection.addAll(listBeerResponse)

        }


    }

    private fun fetchData() {
        beerViewModel.startGetListBeer(true)
    }

    private fun initView() {
        dataBinding.recyclerBeer.layoutManager = LinearLayoutManager(context)
        dataBinding.recyclerBeer.setAdapter(adapter)
        adapter.registerItemBinders(ItemBeerBinder(onBtnSaveClick = { beerResponse, note, itemPosition ->
            beerViewModel.saveBeerToFavorite(requireContext(), beerResponse, note, itemPosition)
        }))
        dataBinding.recyclerBeer.onReachBottom {
            beerViewModel.loadMoreBeer()
        }


        dataBinding.refreshLayout.setOnRefreshListener {
            beerViewModel.startGetListBeer(
                shouldShowLoading = false
            )

        }
    }

    companion object {

    }
}
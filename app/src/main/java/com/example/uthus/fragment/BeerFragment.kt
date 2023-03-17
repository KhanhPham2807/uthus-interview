package com.example.uthus.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.uthus.R
import com.example.uthus.adapter.ItemBeerBinder
import com.example.uthus.databinding.FragmentBeerBinding
import com.example.uthus.model.Beer
import com.example.uthus.viewmodel.BeerViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import mva2.adapter.ListSection

import mva2.adapter.MultiViewAdapter




/**
 * A simple [Fragment] subclass.
 * Use the [BeerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class BeerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var dataBinding:  FragmentBeerBinding
    private val beerViewModel : BeerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_beer, container, false)
        this.dataBinding.lifecycleOwner = this.viewLifecycleOwner
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = MultiViewAdapter()
        dataBinding.recyclerBeer.layoutManager =  LinearLayoutManager(context)

        dataBinding.recyclerBeer.setAdapter(adapter)

        // Register Binder

        // Register Binder
        adapter.registerItemBinders(ItemBeerBinder())

        // Create Section and add items

        // Create Section and add items
        val listSection: ListSection<Beer> = ListSection<Beer>()
        listSection.addAll(listOf<Beer>(
            Beer(name = "Saigon", price = "2000"),
            Beer(name = "Haniken", price = "40000")
        ))

        // Add Section to the adapter

        // Add Section to the adapter
        adapter.addSection(listSection)
        beerViewModel.getBeer()
    }

    companion object {

    }
}
package com.example.blecomunication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

import com.example.blecomunication.R
import com.example.blecomunication.adapter.ItemBeerBinder
import com.example.blecomunication.databinding.FragmentBeerBinding
import com.example.blecomunication.model.Beer
import mva2.adapter.ListSection

import mva2.adapter.MultiViewAdapter




/**
 * A simple [Fragment] subclass.
 * Use the [BeerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BeerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var dataBinding:  FragmentBeerBinding

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
    }

    companion object {

    }
}
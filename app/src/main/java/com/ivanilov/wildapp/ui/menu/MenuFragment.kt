package com.ivanilov.wildapp.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivanilov.wildapp.R
import kotlinx.android.synthetic.main.fragment_menu.view.*



class MenuFragment : Fragment() {

    private lateinit var menuViewModel: MenuViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        menuViewModel = ViewModelProviders.of(this).get(MenuViewModel::class.java)
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.group_recycler_view.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        val groupAdapter = GroupMenuAdapter(menuViewModel.getGroupArray()!!)
        view.group_recycler_view?.adapter = groupAdapter


        view.products_recycler_view.layoutManager = LinearLayoutManager(context)
        val productAdapter = ProductMenuAdapter(menuViewModel.getProductArray()!!)
        view.products_recycler_view?.adapter = productAdapter

    }

}

package com.valance.fiteat.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.valance.fiteat.ui.adapter.RecyclerViewComponents
import com.valance.fiteat.ui.adapter.ComponentsData
import com.valance.fiteat.databinding.MenuFragmentBinding

class MenuFragment: Fragment() {
    private lateinit var binding: MenuFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MenuFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = binding.recyclerView
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        val data = listOf(
            ComponentsData("Белки","a"),
            ComponentsData("Жиры","a"),
            ComponentsData("Углеводы","a"),
            ComponentsData("Клетчатка","a"),
            ComponentsData("Сахар","a"),
        )
        val recyclerAdapter = RecyclerViewComponents(data)
        recyclerView.adapter = recyclerAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = MenuFragment()
    }
}
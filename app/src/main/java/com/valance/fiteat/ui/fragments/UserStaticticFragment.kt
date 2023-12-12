package com.valance.fiteat.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.valance.fiteat.databinding.UserStatisticFragmentBinding
import com.valance.fiteat.ui.adapter.FoodListAdapter
import com.valance.fiteat.ui.viewmodels.UserStatisticViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserStaticticFragment : Fragment(){

    private lateinit var binding: UserStatisticFragmentBinding
    private lateinit var userStatisticViewModel: UserStatisticViewModel
    private lateinit var foodListAdapter: FoodListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UserStatisticFragmentBinding.inflate(inflater, container, false)
        userStatisticViewModel = ViewModelProvider(this)[UserStatisticViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mealsRecyclerView: RecyclerView = binding.countryRv
        mealsRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        foodListAdapter = FoodListAdapter { id ->

        }

        userStatisticViewModel.mealsLiveData.observe(viewLifecycleOwner) { meals ->
            meals?.let {
                foodListAdapter.updateData(meals)
            }
        }


        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                foodListAdapter.getFilter().filter(s)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        userStatisticViewModel.loadAllMeals()

        mealsRecyclerView.adapter = foodListAdapter
    }
}


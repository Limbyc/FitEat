package com.valance.fiteat.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.valance.fiteat.R
import com.valance.fiteat.databinding.UserStatisticFragmentBinding
import com.valance.fiteat.ui.adapter.FoodListAdapter
import com.valance.fiteat.ui.viewmodels.SharedViewModel
import com.valance.fiteat.ui.viewmodels.UserStatisticViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserStaticticFragment : Fragment(){

    private lateinit var binding: UserStatisticFragmentBinding
    private val userStatisticViewModel: UserStatisticViewModel by viewModels()
    private lateinit var foodListAdapter: FoodListAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UserStatisticFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mealsRecyclerView: RecyclerView = binding.countryRv
        mealsRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        foodListAdapter = FoodListAdapter { id ->
            sharedViewModel.setMealId(id)
            requireActivity().supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                .replace(R.id.Fragment_container, MenuFragment())
                .addToBackStack(null)
                .commit()

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





package com.valance.fiteat.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.valance.fiteat.R
import com.valance.fiteat.databinding.UserStatisticFragmentBinding
import com.valance.fiteat.db.MainDB
import com.valance.fiteat.ui.adapter.MealAdapter
import kotlinx.coroutines.launch

class UserStaticticFragment : Fragment(){

    private lateinit var binding: UserStatisticFragmentBinding
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

        val mealDao = context?.let { MainDB.getDB(it).mealDao() }

        lifecycleScope.launch {
            val meals = mealDao?.getAllMeals()

            if (!meals.isNullOrEmpty()) {
                val adapter = MealAdapter(meals)
                binding.countryRv.adapter = adapter
                binding.countryRv.layoutManager = LinearLayoutManager(requireContext())
            } else {
                Log.d("d", "ewewewewewwwwww")
            }
        }

    }





    companion object {
        @JvmStatic
        fun newInstance() = UserStaticticFragment()
    }
}

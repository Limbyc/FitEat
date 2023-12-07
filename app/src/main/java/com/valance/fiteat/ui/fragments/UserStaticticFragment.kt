package com.valance.fiteat.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.valance.fiteat.databinding.UserStatisticFragmentBinding
import com.valance.fiteat.db.AppDatabase
import com.valance.fiteat.db.dao.MealDao
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

    }

}

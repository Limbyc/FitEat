package com.valance.fiteat.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.valance.fiteat.R
import com.valance.fiteat.ui.adapter.FoodComponentsAdapter
import com.valance.fiteat.ui.adapter.FoodComponentsData
import com.valance.fiteat.databinding.MenuFragmentBinding
import com.valance.fiteat.db.dao.UserDao
import com.valance.fiteat.db.entity.Meal
import com.valance.fiteat.db.entity.User
import com.valance.fiteat.ui.adapter.UserComponentsData
import com.valance.fiteat.ui.adapter.UserComponentsAdapter
import com.valance.fiteat.ui.viewmodels.MenuViewModel
import com.valance.fiteat.ui.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class MenuFragment : Fragment() {
    private lateinit var binding: MenuFragmentBinding
    private val menuViewModel: MenuViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var foodComponentsAdapter: FoodComponentsAdapter
    private lateinit var userComponentsAdapter: UserComponentsAdapter
    private var isWeightValid = false
    private var isTrackingTimeWithoutFood = false
    private var timeWithoutFood: Long = 0
    private lateinit var handler: Handler
    private lateinit var hungryTextView: TextView
    private val TIME_INTERVAL: Long = 1000
    private var currentUserId: Int = 0

    private var user : User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MenuFragmentBinding.inflate(inflater, container, false)

        hungryTextView = binding.Hungry
        handler = Handler()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFoodRecyclerView()
        setupUserRecyclerView()

        observeSharedViewModel()
        setupEmotionClickListener()
        binding.Water.setOnClickListener { showWaterRecallDialog() }
        setupPlusFoodClickListeners()
        updateUserData()
        startTrackingTimeWithoutFood()
    }

    private fun setupFoodRecyclerView() {
        val recyclerView: RecyclerView = binding.recyclerView
        foodComponentsAdapter = FoodComponentsAdapter(emptyList())
        recyclerView.adapter = foodComponentsAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupUserRecyclerView() {
        val recyclerView1: RecyclerView = binding.recyclerView1
        userComponentsAdapter = UserComponentsAdapter(emptyList())
        recyclerView1.adapter = userComponentsAdapter
        recyclerView1.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeSharedViewModel() {
        lifecycleScope.launch {
            sharedViewModel.mealId.collect { id ->
                Log.d("MenuFragment", "Received id: $id")
                val meal: Meal = menuViewModel.getMealById(id)
                updateFoodComponents(meal)
                lifecycleScope.launch {
                sharedViewModel.userId.collect { id ->
                    menuViewModel.getUserById(id)
                    updateUserData()
                    }
                }
                lifecycleScope.launch{
                    menuViewModel.user.collect {
                        user = it
                    }
                }
            }
        }
    }

    private fun updateUserData() {
        lifecycleScope.launch {
            if (user != null) {
                currentUserId = user!!.id

                val mealId = sharedViewModel.mealId.value
                val userWeight = menuViewModel.getWeight(currentUserId)
                val userHeight = menuViewModel.getHeight(currentUserId)

                mealId.let { id ->
                    val meal: Meal = menuViewModel.getMealById(id)

                    userWeight?.let { weightMetrics ->
                        val weight = weightMetrics.weight

                        userHeight?.let { heightMetrics ->
                            val height = heightMetrics.height / 100.0
                            val bmi = weight / (height * height)
                            val formattedBMI = String.format("%.2f", bmi)
                            val weightWithUnit = "$weight кг"
                            val mealCalories = "${meal?.calories ?: 0} ккал"

                            val data1 = listOf(
                                UserComponentsData("Вода", ""),
                                UserComponentsData("Прием", mealCalories),
                                UserComponentsData("Вес", weightWithUnit),
                                UserComponentsData("Индекс. масса", formattedBMI)
                            )

                            userComponentsAdapter.setData1(data1)
                        }
                    }
                }
            }
        }

    }

    private fun updateFoodComponents(meal: Meal?) {
        val defaultProteinGrams = "${meal?.squirrels ?: 0} г"
        val defaultFatsGrams = "${meal?.fats ?: 0} г"
        val defaultCarbsGrams = "${meal?.carbohydrates ?: 0} г"
        val defaultFiberGrams = "${meal?.fibre ?: 0} г"
        val defaultSugarGrams = "${meal?.sugar ?: 0} г"

        val data = listOf(
            FoodComponentsData("Белки", defaultProteinGrams),
            FoodComponentsData("Жиры", defaultFatsGrams),
            FoodComponentsData("Углеводы", defaultCarbsGrams),
            FoodComponentsData("Клетчатка", defaultFiberGrams),
            FoodComponentsData("Сахар", defaultSugarGrams)
        )

        foodComponentsAdapter.setData(data)
    }

    private fun setupEmotionClickListener() {
        binding.Emotion.setOnClickListener {
            val layoutInflater = LayoutInflater.from(requireContext())
            val dialogView = layoutInflater.inflate(R.layout.weight_change_dialog, null)

            val builder = AlertDialog.Builder(requireContext(), R.style.CustomDialog)
            builder.setView(dialogView)
            val dialog = builder.create()

            val confirmButton: TextView = dialogView.findViewById(R.id.confirm)
            val cancelButton: TextView = dialogView.findViewById(R.id.cancel)
            val newWeightEditText: EditText = dialogView.findViewById(R.id.EtNewWeight)

            newWeightEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val input = s.toString().replace(" ", "")
                    if (input.isNotEmpty()) {
                        val value = input.toIntOrNull()
                        isWeightValid = value in 30..200
                        confirmButton.isEnabled = isWeightValid
                    } else {
                        isWeightValid = false
                        confirmButton.isEnabled = false
                    }
                }
            })

            confirmButton.setOnClickListener {

                val newWeight = newWeightEditText.text.toString()
                if (isWeightValid) {
                    lifecycleScope.launch {
                        menuViewModel.setWeight(id, newWeight)
                        updateUserData()
                    }
                    dialog.dismiss()
                    binding.SadEmotion.visibility = View.GONE
                    binding.SmileWeight.visibility = View.VISIBLE
                    binding.TvWeightConfirm.visibility = View.VISIBLE
                    binding.Emotion.text = "Желаете изменить?"
                }
            }

            cancelButton.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }


    private fun showWaterRecallDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.WaterRecall)
        val dialogView = layoutInflater.inflate(R.layout.water_recall_dialog, null)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    private fun setupPlusFoodClickListeners() {
        val openUserStatisticFragment: () -> Unit = {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.Fragment_container, UserStaticticFragment())
                .commit()
        }

        binding.plusFood.setOnClickListener { openUserStatisticFragment() }
        binding.plusFood1.setOnClickListener { openUserStatisticFragment() }
    }

    private fun startTrackingTimeWithoutFood() {
        isTrackingTimeWithoutFood = true
        handler.postDelayed(timerRunnable, TIME_INTERVAL)
    }

    private val timerRunnable = object : Runnable {
        override fun run() {
            if (isTrackingTimeWithoutFood) {
                timeWithoutFood += TIME_INTERVAL
                val timeWithoutFoodInSeconds = timeWithoutFood / 1000
                val hours = timeWithoutFoodInSeconds / 3600
                val minutes = (timeWithoutFoodInSeconds % 3600) / 60
                val seconds = timeWithoutFoodInSeconds % 60

                val timeText = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                hungryTextView.text = timeText

                handler.postDelayed(this, TIME_INTERVAL)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isTrackingTimeWithoutFood = false
        handler.removeCallbacks(timerRunnable)
    }
}

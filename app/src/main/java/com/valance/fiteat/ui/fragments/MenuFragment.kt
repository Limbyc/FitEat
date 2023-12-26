package com.valance.fiteat.ui.fragments

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.valance.fiteat.R
import com.valance.fiteat.ui.adapter.FoodComponentsAdapter
import com.valance.fiteat.ui.adapter.FoodComponentsData
import com.valance.fiteat.databinding.MenuFragmentBinding
import com.valance.fiteat.db.sharedPreferences.UserSharedPreferences
import com.valance.fiteat.db.entity.Meal
import com.valance.fiteat.db.sharedPreferences.PermissionManager
import com.valance.fiteat.ui.WaterReminderWorker
import com.valance.fiteat.ui.adapter.UserComponentsData
import com.valance.fiteat.ui.adapter.UserComponentsAdapter
import com.valance.fiteat.ui.viewmodels.MenuViewModel
import com.valance.fiteat.ui.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MenuFragment : Fragment() {
    private lateinit var binding: MenuFragmentBinding
    private val menuViewModel: MenuViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var foodComponentsAdapter: FoodComponentsAdapter
    private lateinit var userComponentsAdapter: UserComponentsAdapter
    private var isWeightValid = false
    private var isTrackingTimeWithoutFood = false
    private var isTrackingTimeWithoutWater = false
    private var timeWithoutFood: Long = 0
    private var timeWithoutWater: Long = 0
    private lateinit var handler: Handler
    private lateinit var hungryTextView: TextView
    private lateinit var thirst: TextView
    private val TIME_INTERVAL: Long = 1000
    private var cumulativeProteinGrams = 0
    private var timeInMillis: Long = 0
    private var cumulativeFatsGrams = 0
    private var cumulativeCarbsGrams = 0
    private var cumulativeFiberGrams = 0
    private var cumulativeSugarGrams = 0
    private var cumulativeMealCalories = 0
    private var cumulativeTimeWater = 0
    private val TIME_WITHOUT_FOOD_KEY = "timeWithoutFood"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MenuFragmentBinding.inflate(inflater, container, false)
        val context = requireContext()
        hungryTextView = binding.Hungry
        thirst = binding.Thirst

        handler = Handler()
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFoodRecyclerView()
        setupUserRecyclerView()

        loadFoodComponentsFromSharedPreferences()
        loadCaloriesFromSharedPreferences()
        loadTimeOfWaterToSharedPreferences()



        observeSharedViewModel()
        setupEmotionClickListener()
        binding.Water.setOnClickListener { showWaterRecallDialog() }
        setupPlusFoodClickListeners()
        context?.let { startTrackingTimeWithoutFood(it) }
        context?.let { startTrackingTimeWithoutWater(it) }
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
                        updateUserData()
                    }
                }
            }
        }
    }

    private fun updateUserData() {
        lifecycleScope.launch {
            val userSharedPreferences = UserSharedPreferences(requireContext())
            val user = userSharedPreferences.getUser()

            user?.let { userData ->

                val mealId = sharedViewModel.mealId.value
                val userWeight = userSharedPreferences.getWeight()
                val userHeight = userSharedPreferences.getHeight()

                mealId?.let { id ->
                    if (userWeight != -1 && userHeight != -1) {
                        val weight = userWeight.toDouble()
                        val height = userHeight.toDouble() / 100.0
                        val bmi = weight / (height * height)
                        val formattedBMI = String.format("%.2f", bmi)
                        val weightWithUnit = "$userWeight кг"
                        val meal: Meal = menuViewModel.getMealById(id)
                        cumulativeMealCalories += meal?.calories ?: 0

                        saveCaloriesToSharedPreferences()

                        val data1 = listOf(
                            UserComponentsData("Вода", "${formatTime(cumulativeTimeWater)} время"),
                            UserComponentsData("Прием", "$cumulativeMealCalories ккал"),
                            UserComponentsData("Вес", weightWithUnit),
                            UserComponentsData("Индекс. масса", formattedBMI)
                        )

                        userComponentsAdapter.setData1(data1)
                    }
                }
            }
        }
    }

    private fun formatTime(minutes: Int): String {
        val hours = minutes / 60
        val mins = minutes % 60
        return String.format("%02d:%02d", hours, mins)
    }

    private fun updateFoodComponents(meal: Meal?) {
        meal?.let {
            cumulativeProteinGrams += it.squirrels ?: 0
            cumulativeFatsGrams += it.fats ?: 0
            cumulativeCarbsGrams += it.carbohydrates ?: 0
            cumulativeFiberGrams += it.fibre ?: 0
            cumulativeSugarGrams += it.sugar ?: 0
        }

        saveFoodComponentsToSharedPreferences()

        val data = listOf(
            FoodComponentsData("Белки", "$cumulativeProteinGrams г"),
            FoodComponentsData("Жиры", "$cumulativeFatsGrams г"),
            FoodComponentsData("Углеводы", "$cumulativeCarbsGrams г"),
            FoodComponentsData("Клетчатка", "$cumulativeFiberGrams г"),
            FoodComponentsData("Сахар", "$cumulativeSugarGrams г")
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

                val newWeightText = newWeightEditText.text.toString()
                if (isWeightValid) {
                    lifecycleScope.launch {
                        val newWeight = newWeightText.toIntOrNull()
                        val userSharedPreferences = UserSharedPreferences(requireContext())
                        if (newWeight != null) {
                            userSharedPreferences.saveWeight(newWeight)
                        }
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
        val timeWaterEditText = dialogView.findViewById<EditText>(R.id.TimeWater)
        val buttonRemember = dialogView.findViewById<TextView>(R.id.buttonRemember)
        val permission = dialogView.findViewById<TextView>(R.id.permission)
        val timeWaterRecallEditText = dialogView.findViewById<EditText>(R.id.TimeWaterRecall)
        timeWaterEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val inputText = s.toString()
                if (!isValidTimeFormat(inputText)) {
                    timeWaterEditText.error = "Введите время в формате чч:мм"
                    buttonRemember.setBackgroundResource(R.drawable.item_decoration)
                } else {
                    timeWaterEditText.error = null
                    buttonRemember.setBackgroundResource(R.drawable.item_decoration_button)
                }
            }
            fun isValidTimeFormat(time: String): Boolean {
                val timeRegex = Regex("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")
                return time.matches(timeRegex)
            }
        })
        timeWaterRecallEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val inputText = s.toString()
                if (!isValidTimeFormat(inputText)) {
                    timeWaterRecallEditText.error = "Введите время в формате чч:мм"
                } else {
                    timeWaterRecallEditText.error = null
                }
            }

            private fun isValidTimeFormat(time: String): Boolean {
                val timeRegex = Regex("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")
                return time.matches(timeRegex)
            }
        })

        buttonRemember.setOnClickListener {
            fun isValidTimeFormat(time: String): Boolean {
                val timeRegex = Regex("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")
                return time.matches(timeRegex)
            }

            val time = timeWaterEditText.text.toString()
            val isValidTimeFormat = isValidTimeFormat(time)
            val timeWaterRecallText = timeWaterRecallEditText.text.toString()

            if (isValidTimeFormat(timeWaterRecallText)) {
                saveTimeWaterRecallToSharedPreferences(timeWaterRecallText)
                scheduleNotificationWithWorkManager(timeWaterRecallText)
            }

            if (isValidTimeFormat) {
                cumulativeTimeWater = convertTimeToMinutes(time)
                saveTimeOfWaterToSharedPreferences()
                updateUserData()
                alertDialog.dismiss()

                handler.removeCallbacks(waterTimerRunnable)

                startTrackingTimeWithoutWater(requireContext())
            } else {
                buttonRemember.isEnabled = true
            }
        }

        val isPermissionGranted = PermissionManager.isPermissionGranted(requireContext())
        if (isPermissionGranted) {
            permission.visibility = View.GONE
        } else {
            permission.visibility = View.VISIBLE
        }
    }

    private fun convertTimeToMinutes(time: String): Int {
        val parts = time.split(":")
        if (parts.size == 2) {
            val hours = parts[0].toIntOrNull() ?: 0
            val minutes = parts[1].toIntOrNull() ?: 0
            return hours * 60 + minutes
        }
        return 0
    }
    private fun setupPlusFoodClickListeners() {
        val openUserStatisticFragment: () -> Unit = {
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
            .replace(R.id.Fragment_container, UserStatisticFragment())
            .addToBackStack(null)
            .commit()
        }

        binding.plusFood.setOnClickListener { openUserStatisticFragment() }
        binding.plusFood1.setOnClickListener { openUserStatisticFragment() }
    }


    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("prefName", Context.MODE_PRIVATE)
    }

    private fun startTrackingTimeWithoutFood(context: Context) {
        isTrackingTimeWithoutFood = true

        val sharedPreferencesTimeWithoutFood = context.getSharedPreferences("TimeWithoutFood", Context.MODE_PRIVATE)
        val savedTimeInMillis = sharedPreferencesTimeWithoutFood.getLong("selectedTime", 0)

        if (savedTimeInMillis != 0L) {
            timeWithoutFood = System.currentTimeMillis() - savedTimeInMillis
            handler.postDelayed(timerRunnable, TIME_INTERVAL)
        } else {
            val sharedPreferences = getSharedPreferences(context)
            timeWithoutFood = sharedPreferences.getLong(TIME_WITHOUT_FOOD_KEY, 0)

            if (timeWithoutFood == 0L) {
                val savedTimeInMinutes = sharedPreferences.getInt("selectedTime", 0)
                timeWithoutFood = (savedTimeInMinutes * 60 * 1000).toLong()
            }

            handler.postDelayed(timerRunnable, TIME_INTERVAL)
        }
    }

    private fun startTrackingTimeWithoutWater(context: Context) {
        isTrackingTimeWithoutWater = true

        val sharedPreferences = context.getSharedPreferences("WaterTimeData", Context.MODE_PRIVATE)
        val savedTimeInMinutes = sharedPreferences.getInt("cumulativeTimeWater", 0)

        val currentTimeInMoscowMinutes = getCurrentTimeInMoscowMinutes()
        val timeDifferenceInMinutes = currentTimeInMoscowMinutes - savedTimeInMinutes

        timeWithoutWater = if (timeDifferenceInMinutes > 0) {
            (timeDifferenceInMinutes * 60 * 1000).toLong()
        } else {
            0L
        }

        handler.postDelayed(waterTimerRunnable, TIME_INTERVAL)
    }
    private fun getCurrentTimeInMoscowMinutes(): Int {
        val moscowTimeZone = TimeZone.getTimeZone("Europe/Moscow")
        val calendar = Calendar.getInstance(moscowTimeZone)
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        return currentHour * 60 + currentMinute
    }


    private val timerRunnable = object : Runnable {
        override fun run() {
            if (isTrackingTimeWithoutFood) {
                timeWithoutFood += TIME_INTERVAL

                saveTimeWithoutFoodToSharedPreferences(timeWithoutFood)

                val timeWithoutFoodInSeconds = timeWithoutFood / 1000
                val hours = timeWithoutFoodInSeconds / 3600
                val minutes = (timeWithoutFoodInSeconds % 3600) / 60

                val timeText = String.format("%02dч:%02dмин", hours, minutes)
                hungryTextView.text = timeText

                handler.postDelayed(this, TIME_INTERVAL)
            }
        }
    }
    private val waterTimerRunnable = object : Runnable {
        override fun run() {
            if (isTrackingTimeWithoutWater) {
                timeWithoutWater += TIME_INTERVAL
                val timeWithoutWaterInSeconds = timeWithoutWater / 1000
                val hours = timeWithoutWaterInSeconds / 3600
                val minutes = (timeWithoutWaterInSeconds % 3600) / 60

                val timeText = String.format("%02dч:%02dмин", hours, minutes)
                thirst.text = timeText

                handler.postDelayed(this, TIME_INTERVAL)
            }
        }
    }

    private fun saveTimeWithoutFoodToSharedPreferences(timeWithoutFood: Long) {
        val sharedPreferences = getSharedPreferences(requireContext())
        val editor = sharedPreferences.edit()
        editor.putLong(TIME_WITHOUT_FOOD_KEY, timeWithoutFood)
        editor.apply()
    }
    private fun saveFoodComponentsToSharedPreferences() {
        val sharedPreferences = requireContext().getSharedPreferences("FoodData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("cumulativeProtein", cumulativeProteinGrams)
        editor.putInt("cumulativeFats", cumulativeFatsGrams)
        editor.putInt("cumulativeCarbs", cumulativeCarbsGrams)
        editor.putInt("cumulativeFiber", cumulativeFiberGrams)
        editor.putInt("cumulativeSugar", cumulativeSugarGrams)
        editor.apply()
    }
    private fun saveTimeOfWaterToSharedPreferences(){
        val sharedPreferences = requireContext().getSharedPreferences("WaterTimeData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("cumulativeTimeWater", cumulativeTimeWater)
        editor.apply()
    }
    private fun saveCaloriesToSharedPreferences(){
        val sharedPreferences = requireContext().getSharedPreferences("CaloriesData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("cumulativeCalories", cumulativeMealCalories)
        editor.apply()
    }
    private fun loadFoodComponentsFromSharedPreferences() {
        val sharedPreferences = requireContext().getSharedPreferences("FoodData", Context.MODE_PRIVATE)

        cumulativeProteinGrams = sharedPreferences.getInt("cumulativeProtein", 0)
        cumulativeFatsGrams = sharedPreferences.getInt("cumulativeFats", 0)
        cumulativeCarbsGrams = sharedPreferences.getInt("cumulativeCarbs", 0)
        cumulativeFiberGrams = sharedPreferences.getInt("cumulativeFiber", 0)
        cumulativeSugarGrams = sharedPreferences.getInt("cumulativeSugar", 0)
    }
    private  fun loadCaloriesFromSharedPreferences(){
        val sharedPreferences = requireContext().getSharedPreferences("CaloriesData", Context.MODE_PRIVATE)
        cumulativeMealCalories = sharedPreferences.getInt("cumulativeCalories", 0)
    }
    private  fun loadTimeOfWaterToSharedPreferences(){
        val sharedPreferences = requireContext().getSharedPreferences("WaterTimeData", Context.MODE_PRIVATE)
        cumulativeTimeWater = sharedPreferences.getInt("cumulativeTimeWater", 0)
    }
    private fun saveTimeWaterRecallToSharedPreferences(timeWaterRecall: String) {
        val sharedPreferences = requireContext().getSharedPreferences("YourPrefsName", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("timeWaterRecall", timeWaterRecall)
        editor.apply()
    }
    private fun convertTimeToSeconds(timeWaterRecall: String): Long {
        val parts = timeWaterRecall.split(":")
        val hours = parts[0].toInt()
        val minutes = parts[1].toInt()

        return (hours * 60 + minutes) * 60L
    }
    private fun calculateTimeInSecondsFromNow(timeWaterRecall: String): Long {
        val currentTimeInSeconds = System.currentTimeMillis() / 1000
        val recallTimeInSeconds = convertTimeToSeconds(timeWaterRecall)

        val timeDifferenceInSeconds = if (recallTimeInSeconds <= currentTimeInSeconds) {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"))
            val parts = timeWaterRecall.split(":")
            val hours = parts[0].toInt()
            val minutes = parts[1].toInt()

            calendar.set(Calendar.HOUR_OF_DAY, hours)
            calendar.set(Calendar.MINUTE, minutes)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            calendar.timeInMillis / 1000 - currentTimeInSeconds
        } else {
            recallTimeInSeconds - currentTimeInSeconds
        }
        Log.d("TimeDifference", "Time difference in seconds: $timeDifferenceInSeconds")
        return timeDifferenceInSeconds
    }

    private fun scheduleNotificationWithWorkManager(timeWaterRecall: String) {
        val inputData = Data.Builder()
            .putString("timeWaterRecall", timeWaterRecall)
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<WaterReminderWorker>()
            .setInputData(inputData)
            .setConstraints(constraints)
            .setInitialDelay(calculateTimeInSecondsFromNow(timeWaterRecall), TimeUnit.SECONDS)
            .build()

        context?.let { WorkManager.getInstance(it).enqueue(workRequest) }
    }

}
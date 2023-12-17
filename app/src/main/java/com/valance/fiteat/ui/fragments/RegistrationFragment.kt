package com.valance.fiteat.ui.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.valance.fiteat.R
import com.valance.fiteat.databinding.RegistrationFragmentBinding
import com.valance.fiteat.db.sharedPreferences.User
import com.valance.fiteat.db.sharedPreferences.UserSharedPreferences
import com.valance.fiteat.ui.adapter.TimeMealAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistrationFragment : Fragment() {
    private var isListExpanded = false
    private var height: Int? = null
    private var weight: Int? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TimeMealAdapter
    private lateinit var binding: RegistrationFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegistrationFragmentBinding.inflate(inflater, container, false)
        setupEditTextValidation()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //RecyclerView
        val yourDataList = listOf(
            "Только что",
            "Пол часа назад",
            "Час назад",
            "2 часа назад",
            "Более чем 3 часа назад"
        )

        binding.TVMealTime.setOnClickListener{
            showOrHideList()
            }
        recyclerView = binding.recyclerViewMealTime
        adapter = TimeMealAdapter(yourDataList) { selectedItem ->
            binding.TVMealTime.text = selectedItem
            showOrHideList()
            updateTextViewBackground()
        }

        //Height
        val heightWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateTextViewBackground()
            }
        }

        val weightWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateTextViewBackground()
            }
        }
        binding.EtHeight.addTextChangedListener(heightWatcher)
        binding.EtWeight.addTextChangedListener(weightWatcher)
    }

    private fun setupEditTextValidation() {
        binding.EtHeight.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                true
            } else {
                false
            }
        }
        binding.EtHeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString().replace(" ", "")
                height = input.toIntOrNull()

                val enteredHeight = height ?: return
                val isHeightValid = enteredHeight in 120..240

                if (!isHeightValid) {
                    binding.EtHeight.setBackgroundResource(R.drawable.item_decoration_rectangle)
                } else {
                    binding.EtHeight.setBackgroundResource(R.drawable.item_decoration)
                }

                val enteredWeight = weight ?: return
                val isWeightValid = enteredWeight in 30..200
                binding.ButtonRegistration.isEnabled = isHeightValid && isWeightValid
            }
        })
        binding.EtWeight.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                true
            } else {
                false
            }
        }
    binding.EtWeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString().replace(" ", "")
                weight = input.toIntOrNull()

                val enteredWeight = weight ?: return
                val isWeightValid = enteredWeight in 30..200

                if (!isWeightValid) {
                    binding.EtWeight.setBackgroundResource(R.drawable.item_decoration_rectangle)
                } else {
                    binding.EtWeight.setBackgroundResource(R.drawable.item_decoration)
                }

                val enteredHeight = height ?: return
                val isHeightValid = enteredHeight in 120..240
                binding.ButtonRegistration.isEnabled = isHeightValid && isWeightValid
            }
        })
    }

    //RecyclerView
    private fun showOrHideList() {
        if (!::recyclerView.isInitialized) return
        if (isListExpanded) {
            collapseList()
            isListExpanded = false
        } else {
            expandList()
            isListExpanded = true
        }
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = adapter
    }

    private fun expandList() {
        val targetExpandedHeight = convertDpToPx(200)
        val initialHeight = binding.recyclerViewMealTime.height

        val animator = ValueAnimator.ofInt(initialHeight, targetExpandedHeight).apply {
            interpolator = AccelerateInterpolator()
            duration = 500
            addUpdateListener {
                val animatedValue = it.animatedValue as Int
                val layoutParams = binding.recyclerViewMealTime.layoutParams
                layoutParams.height = animatedValue
                binding.recyclerViewMealTime.layoutParams = layoutParams
                binding.recyclerViewMealTime.requestLayout()
            }
        }
        animator.start()
        recyclerView.visibility = View.VISIBLE
        binding.IvUpArrow.visibility = View.VISIBLE
        binding.IvDownArrow.visibility = View.GONE
    }
    private fun collapseList() {
        val targetCollapsedHeight = convertDpToPx(0)
        val initialHeight = binding.recyclerViewMealTime.height

        val animator = ValueAnimator.ofInt(initialHeight, targetCollapsedHeight).apply {
            interpolator = DecelerateInterpolator()
            duration = 500
            addUpdateListener { valueAnimator ->
                val animatedValue = valueAnimator.animatedValue as Int
                val layoutParams = binding.recyclerViewMealTime.layoutParams
                layoutParams.height = animatedValue
                binding.recyclerViewMealTime.layoutParams = layoutParams
                binding.recyclerViewMealTime.requestLayout()
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    val layoutParams = binding.recyclerViewMealTime.layoutParams
                    layoutParams.height = targetCollapsedHeight
                    binding.recyclerViewMealTime.layoutParams = layoutParams

                    recyclerView.visibility = View.GONE
                    binding.IvUpArrow.visibility = View.GONE
                    binding.IvDownArrow.visibility = View.VISIBLE
                }
            })
        }
        animator.start()
    }
    private fun convertDpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
    private fun updateTextViewBackground() {
        val enteredHeight = height ?: return
        val enteredWeight = weight ?: return
        val isMealTimeEntered = !binding.TVMealTime.text.isNullOrBlank()
        val isHeightValid = enteredHeight in 120..240
        val isWeightValid = enteredWeight in 30..200
        val isNameEntered = !binding.EtName.text.isNullOrBlank()


        if (isHeightValid && isWeightValid && isNameEntered && isMealTimeEntered) {
            binding.ButtonRegistration.setBackgroundResource(R.drawable.item_decoration_button)
            binding.ButtonRegistration.setOnClickListener {
                val userName = binding.EtName.text.toString()
                val userHeight = binding.EtHeight.text.toString().toIntOrNull() ?: 0
                val userWeight = binding.EtWeight.text.toString().toIntOrNull() ?: 0
                val userMealTime = binding.TVMealTime.text.toString()



                lifecycleScope.launch {
                    try {
                        fun timeToMinutes(timeString: String): Int {
                            return when (timeString) {
                                "Только что" -> 0
                                "Пол часа назад" -> 30
                                "Час назад" -> 60
                                "2 часа назад" -> 120
                                "Более чем 3 часа назад" -> 180
                                else -> 0
                            }
                        }

                        fun saveSelectedTime(context: Context, selectedTime: String) {
                            val sharedPreferences: SharedPreferences = context.getSharedPreferences("prefName", Context.MODE_PRIVATE)
                            val editor: SharedPreferences.Editor = sharedPreferences.edit()
                            val minutes = timeToMinutes(selectedTime)
                            editor.putInt("selectedTime", minutes)
                            editor.apply()
                        }

                        val user = User(name = userName,height = userHeight,weight = userWeight,time = userMealTime)
                        val userSharedPreferences = context?.let { it1 -> UserSharedPreferences(it1) }
                        userSharedPreferences?.saveUser(user)
                        val selectedTime = userMealTime
                        context?.let { it1 -> saveSelectedTime(it1, selectedTime) }
                        Log.e("e", user.toString())
                        val sharedPreferences = requireActivity().getSharedPreferences("registration", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putBoolean("isRegistered", true)
                        editor.apply()
                        requireActivity().supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.Fragment_container, MenuFragment())
                            .commit()
                    } catch (exception: Exception) {
                        Log.e("e", exception.message.toString())
                    }
                }

            }
        } else {
            binding.ButtonRegistration.setBackgroundResource(R.drawable.item_decoration)
        }
    }
    private fun hideKeyboard() {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }
}

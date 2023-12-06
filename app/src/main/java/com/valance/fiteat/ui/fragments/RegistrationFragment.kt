package com.valance.fiteat.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.valance.fiteat.FitEatApp
import com.valance.fiteat.R
import com.valance.fiteat.databinding.RegistrationFragmentBinding
import com.valance.fiteat.db.dao.UserDao
import com.valance.fiteat.db.entity.User
import com.valance.fiteat.ui.adapter.TimeMealAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RegistrationFragment : Fragment() {
    private var height: Int? = null
    private var weight: Int? = null
    private lateinit var userDao: UserDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TimeMealAdapter
    private lateinit var binding: RegistrationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegistrationFragmentBinding.inflate(inflater, container, false)

      //  val mainDB = (requireActivity().application as FitEatApp).database
      //  userDao = mainDB.userDao()

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



        activity?.window?.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
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
        if (::recyclerView.isInitialized) {
            if (recyclerView.visibility == View.VISIBLE) {
                recyclerView.visibility = View.GONE
                binding.IvUpArrow.visibility = View.GONE
                binding.IvDownArrow.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                binding.IvUpArrow.visibility = View.VISIBLE
                binding.IvDownArrow.visibility = View.GONE
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = adapter
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


                val user = User(id = null , name = userName, height = userHeight, weight = userWeight, time = userMealTime)

                lifecycleScope.launch {
                    try {
                        val insertedUserId = withContext(Dispatchers.IO) {
                            userDao.insertUser(user)
                        }
//                          Проверка вставки в базу данных
//                        Log.d("Добавлен новый пользователь:", "${user.name} , ${user.height}, ${user.weight} ")
//
//                        val insertedUser = withContext(Dispatchers.IO) {
//                            userDao.getUserByName(user.name)
//                        }
//
//                        if (insertedUser != null) {
//                            Log.d("UserInsertion", "User successfully inserted: ${insertedUser.name}, ${insertedUser.height}, ${insertedUser.weight}")
//                        } else {
//                            Log.e("UserInsertion", "Failed to find inserted user")
//                        }

                        requireActivity().supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.Fragment_container, MenuFragment.newInstance())
                            .commit()
                        Log.d("UserInsertion","Complete to insert user:" )
                    } catch (e: Exception) {
                        Log.e("UserInsertion", "Failed to insert user: ${e.message}")
                    }
                }

            }
        } else {
            binding.ButtonRegistration.setBackgroundResource(R.drawable.item_decoration)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = RegistrationFragment()
    }
}
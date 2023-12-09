package com.valance.fiteat.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.valance.fiteat.R
import com.valance.fiteat.ui.adapter.FoodComponentsAdapter
import com.valance.fiteat.ui.adapter.FoodComponentsData
import com.valance.fiteat.databinding.MenuFragmentBinding
import com.valance.fiteat.ui.adapter.UserComponentsData
import com.valance.fiteat.ui.adapter.UserComponentsAdapter

class MenuFragment: Fragment() {
    private var numberOfOpenDialogs = 0
    private lateinit var binding: MenuFragmentBinding
    private lateinit var dialog: Dialog
    private lateinit var etNewWeight: EditText
    private lateinit var confirmButton: TextView
    private lateinit var cancelButton: TextView
    var isWeightValid = false

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
            FoodComponentsData("Белки","a"),
            FoodComponentsData("Жиры","a"),
            FoodComponentsData("Углеводы","a"),
            FoodComponentsData("Клетчатка","a"),
            FoodComponentsData("Сахар","a"),
        )
        val recyclerAdapter = FoodComponentsAdapter(data)
        recyclerView.adapter = recyclerAdapter

        val recyclerView1: RecyclerView = binding.recyclerView1
        val layoutManager1 = LinearLayoutManager(requireContext())
        recyclerView1.layoutManager = layoutManager1

        val data1 = listOf(
            UserComponentsData("Вода", "a"),
            UserComponentsData("Прием", "a"),
            UserComponentsData("Вес", "a"),
            UserComponentsData("Индекс. масса", "a")
        )
        val recyclerAdapter1 = UserComponentsAdapter(data1)
        recyclerView1.adapter = recyclerAdapter1

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


        binding.Water.setOnClickListener {
            showWaterRecallDialog()
        }

        fun openUserStatisticFragment() {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.Fragment_container, UserStaticticFragment())
                .commit()
        }

        binding.plusFood.setOnClickListener{
            openUserStatisticFragment()
        }
        binding.plusFood1.setOnClickListener{
            openUserStatisticFragment()
        }
    }
    private fun showWaterRecallDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.WaterRecall)
        val dialogView = layoutInflater.inflate(R.layout.water_recall_dialog, null)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

}
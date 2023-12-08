package com.valance.fiteat.ui.fragments

import android.app.Dialog
import android.os.Bundle
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

        binding.Emotion.setOnClickListener{
            showInputDialog()
        }

        binding.plusFood.setOnClickListener{
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.Fragment_container, UserStaticticFragment())
                .commit()
        }
    }


    private fun setupChangeWeightDialog() {
        dialog = Dialog(requireContext(), R.style.CustomDialog)
        dialog.setContentView(R.layout.change_weight_dialog)

        etNewWeight = dialog.findViewById(R.id.EtNewWeight)
        confirmButton = dialog.findViewById(R.id.confirm)
        cancelButton = dialog.findViewById(R.id.cancel)

        numberOfOpenDialogs++
        confirmButton.setOnClickListener {
            val newWeight = etNewWeight.text.toString().trim()

            if (newWeight.isNotEmpty() && !newWeight.contains(" ")) {
                setupWeightRecordedDialog()
            } else {
                Toast.makeText(requireContext(), "Введите корректный вес", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            checkAndPerformUpdate()
            dialog.dismiss()
            numberOfOpenDialogs--
        }

        dialog.show()
    }

    private fun updateInterface(){
        binding.Emotion.text = "Сегодня вы уже внесли свой вес"
        binding.SmileWeight.visibility = View.VISIBLE
        binding.SadEmotion.visibility = View.GONE
        binding.Emotion.isEnabled = false
    }

    private fun setupWeightRecordedDialog() {
        dialog.setContentView(R.layout.weight_is_recorded_dialog)

        val changeWeight: TextView = dialog.findViewById(R.id.change_weight)

        changeWeight.setOnClickListener {
            dialog.dismiss()
            setupChangeWeightDialog()
        }

        dialog.setOnDismissListener {
            numberOfOpenDialogs--
            checkAndPerformUpdate()
        }
    }
    private fun checkAndPerformUpdate() {
        if (numberOfOpenDialogs == 0) {
            updateInterface()
        }
    }
    private fun showInputDialog() {
        setupChangeWeightDialog()
    }
}
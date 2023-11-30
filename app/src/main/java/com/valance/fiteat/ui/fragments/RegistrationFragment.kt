package com.valance.fiteat.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.valance.fiteat.R
import com.valance.fiteat.databinding.FragmentRegistrationBinding


class RegistrationFragment : Fragment() {
    private var height: Int? = null
    private var weight: Int? = null
    private lateinit var binding: FragmentRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        setupEditTextValidation()

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


        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.mealtime,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.SpinnerItem.adapter = adapter
        }
    }

    private fun setupEditTextValidation() {
        binding.EtHeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
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
                val input = s.toString()
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

    private fun updateTextViewBackground() {
        val enteredHeight = height ?: return
        val enteredWeight = weight ?: return
        val isHeightValid = enteredHeight in 120..240
        val isWeightValid = enteredWeight in 30..200

        if (isHeightValid && isWeightValid) {
            binding.ButtonRegistration.setBackgroundResource(R.drawable.item_decoration_button)
            binding.ButtonRegistration.setOnClickListener {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.Fragment_container, MenuFragment.newInstance())
                    .commit()
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
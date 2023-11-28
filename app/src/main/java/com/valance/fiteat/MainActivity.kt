package com.valance.fiteat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.valance.fiteat.databinding.ActivityMainBinding
import com.valance.fiteat.ui.fragments.RegistrationFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager
            .beginTransaction().replace(R.id.Fragment_container, RegistrationFragment.newInstance())
            .commit()
    }

}
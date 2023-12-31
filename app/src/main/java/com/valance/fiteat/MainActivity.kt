package com.valance.fiteat

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.FragmentTransaction
import com.valance.fiteat.databinding.ActivityMainBinding
import com.valance.fiteat.db.sharedPreferences.PermissionManager
import com.valance.fiteat.ui.fragments.MenuFragment
import com.valance.fiteat.ui.fragments.RegistrationFragment
import com.valance.fiteat.ui.fragments.UserStatisticFragment
import dagger.hilt.android.AndroidEntryPoint


private val TIME_INTERVAL: Long = 2000
private var mBackPressedTime: Long = 0
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var pLauncher: ActivityResultLauncher<Array<String>>

 @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUI()
        registerPermissionListener()
        checkPermissions()
        back()
        val sharedPreferences = getSharedPreferences("registration", Context.MODE_PRIVATE)
        val isRegistered = sharedPreferences.getBoolean("isRegistered", false)
        Log.d("MainActivity", "isRegistered: $isRegistered")

        if (!isRegistered) {
            showRegistrationFragment()
        } else {
            showMenuFragment()
        }
    }

    private fun showRegistrationFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.Fragment_container, RegistrationFragment())
            .commit()
    }

    private fun showMenuFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.Fragment_container, MenuFragment())
            .addToBackStack(null)
            .commit()
    }


    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermissions() {
        val permissionsToCheck = listOf(
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.SCHEDULE_EXACT_ALARM,
            Manifest.permission.USE_EXACT_ALARM
        )

        val permissionsToRequest = mutableListOf<String>()

        permissionsToCheck.forEach { permission ->
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission)
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            pLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }


    private fun registerPermissionListener() {
        pLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach { entry ->
                entry.key
                val isGranted = entry.value
                if (isGranted) {
                    PermissionManager.savePermissionGranted(this, true)
                } else {
                    PermissionManager.savePermissionGranted(this, false)
                }
            }
        }
    }

    //Navigation
    private fun back() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentFragment = supportFragmentManager.findFragmentById(R.id.Fragment_container)

                if (currentFragment is UserStatisticFragment) {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_left)
                    currentFragment.view?.startAnimation(
                        AnimationUtils.loadAnimation(
                            this@MainActivity,
                            R.anim.slide_in_left
                        )
                    )
                    supportFragmentManager.popBackStack()
                    transaction.commit()
                } else {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - mBackPressedTime > TIME_INTERVAL) {
                        Toast.makeText(this@MainActivity, "Нажмите еще раз чтобы выйти из приложения", Toast.LENGTH_SHORT).show()
                        mBackPressedTime = currentTime
                    } else {
                        finish()
                    }
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }
}


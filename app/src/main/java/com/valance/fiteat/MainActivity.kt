package com.valance.fiteat

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.valance.fiteat.databinding.ActivityMainBinding
import com.valance.fiteat.db.sharedPreferences.PermissionManager
import com.valance.fiteat.ui.fragments.MenuFragment
import com.valance.fiteat.ui.fragments.RegistrationFragment
import dagger.hilt.android.AndroidEntryPoint


const val CHANNEL_ID = "channelId"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var pLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()
        hideSystemUI()
        registerPermissionListener()
        checkPostNatification()
        val sharedPreferences = getSharedPreferences("registration", Context.MODE_PRIVATE)
        val isRegistered = sharedPreferences.getBoolean("isRegistered", false)
        Log.d("MainActivity", "isRegistered: $isRegistered")

        if (!isRegistered) {
            showRegistrationFragment()
        } else {
            showMenuFragment()
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "First channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Test"

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
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
    private fun checkPostNatification(){
        when{
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED ->{
            }
            else ->{
                pLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    private fun registerPermissionListener() {
        pLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                PermissionManager.savePermissionGranted(this, true)
            } else {
                PermissionManager.savePermissionGranted(this, false)
            }
        }
    }
}
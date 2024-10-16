package com.example.hdwallpapers

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hdwallpapers.broadcastreceiver.BatteryReceiver
import com.example.hdwallpapers.databinding.ActivityBatteryStatusBinding
import com.example.hdwallpapers.databinding.ActivityLoginBinding
import com.example.hdwallpapers.databinding.ActivitySignUpBinding

class BatteryStatus : AppCompatActivity() {

    private lateinit var binding: ActivityBatteryStatusBinding
    private lateinit var batteryReceiver: BatteryReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBatteryStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.BackButtonOfBattery.setOnClickListener {
            finish()
        }

        batteryReceiver = BatteryReceiver { percentage, isCharging ->
            binding.percentage.text = "$percentage%"
            if (isCharging) {
                if (percentage == 100) {
                    binding.chargingStatus.text = "Fully Charged"
                } else {
                    binding.chargingStatus.text = "Charging..."
                }
            } else {
                binding.chargingStatus.text = "Disconneccted"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(batteryReceiver, filter)
    }


    override fun onPause() {
        super.onPause()
        unregisterReceiver(batteryReceiver)
    }
}
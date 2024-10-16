package com.example.hdwallpapers

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import com.example.hdwallpapers.BatteryStatus
import com.example.hdwallpapers.DownloadImages
import com.example.hdwallpapers.Login
import com.example.hdwallpapers.R
import com.example.hdwallpapers.adaptor.ViewPagerAdapter
import com.example.hdwallpapers.databinding.ActivityHomePageBinding
import com.example.hdwallpapers.models.ImageViewModel
import com.example.hdwallpapers.models.NetworkUtils
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.tashila.pleasewait.PleaseWaitDialog

class HomePage : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityHomePageBinding
    private val viewModel: ImageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.NavigationButton.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.END)
        }

        // Set up the ViewPager2 with the ViewPagerAdapter
        binding.viewpagerhome.adapter = ViewPagerAdapter(this)

        // Set up the TabLayout with ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewpagerhome) { tab, position ->
            tab.text = if (position == 0) "Images" else "Videos"
        }.attach()

        binding.navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> { }
            R.id.nav_download -> {
                startActivity(Intent(this, DownloadImages::class.java))
            }
            R.id.nav_battery -> {
                startActivity(Intent(this, BatteryStatus::class.java))
            }
            R.id.nav_logout -> {
                val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                builder.setTitle("Logout")
                builder.setMessage("Are you sure you want to logout?")
                builder.setPositiveButton("Yes") { dialog, _ ->
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, Login::class.java))
                    finish()
                    dialog.dismiss()
                }
                builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                val alertDialog = builder.create()
                alertDialog.show()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.END)
        return true
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
        } else {
            super.onBackPressed()
        }
    }
}

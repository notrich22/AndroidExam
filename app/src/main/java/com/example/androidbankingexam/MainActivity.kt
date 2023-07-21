package com.example.androidbankingexam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView



class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.add_card -> {
                    navController.navigate(R.id.cardFragment)
                    true
                }
                R.id.add_charge -> {
                    navController.navigate(R.id.chargeFragment)
                    true
                }
                R.id.history -> {
                    navController.navigate(R.id.historyFragment)
                    true
                }
                //R.id.settings ->{
                //    navController.navigate(R.id.settingsFragment)
                 //   true
                //}
                else -> false
            }
        }
    }
}

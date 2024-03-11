package com.epicor.canvas

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.epicor.canvas.ui.HomePage
import com.epicor.canvas.ui.PackagePage
import com.gyf.immersionbar.ImmersionBar

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ImmersionBar.with(this).transparentNavigationBar().init()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF9F9F9)
                ) {
                    AppNavigation()
                }
            }
        }
    }

    @Composable
    private fun AppNavigation(){
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "HomePage" ){
            composable("HomePage"){
                HomePage(navController)
            }
            composable("PackagePage"){
                PackagePage(navController)
            }
        }
    }


}
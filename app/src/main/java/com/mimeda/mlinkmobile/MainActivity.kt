package com.mimeda.mlinkmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.mimeda.mlinkmobile.ui.navigation.ProductAppNavigation
import com.mimeda.mlinkmobile.ui.theme.MlinkMobileSDKAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MlinkMobileSDKAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    MlinkManager.init(this, true)

                    val navHostController = rememberNavController()
                    ProductAppNavigation(navHostController)
                }
            }
        }
    }
}
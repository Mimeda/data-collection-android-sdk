package com.mimeda.sdk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.mimeda.sdk.ui.navigation.ProductAppNavigation
import com.mimeda.sdk.ui.theme.MlinkMobileSDKAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MlinkMobileSDKAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navHostController = rememberNavController()
                    ProductAppNavigation(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        navHostController = navHostController
                    )
                }
            }
        }
    }
}
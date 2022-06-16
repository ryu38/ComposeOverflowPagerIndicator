package com.example.composeoverflowpagerindicator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.composeoverflowpagerindicator.ui.MainScreen
import com.example.composeoverflowpagerindicator.ui.theme.ComposeOverflowPagerIndicatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeOverflowPagerIndicatorTheme {
                MainScreen()
            }
        }
    }
}
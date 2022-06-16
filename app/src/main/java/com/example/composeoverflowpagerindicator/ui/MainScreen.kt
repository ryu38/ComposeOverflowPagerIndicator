package com.example.composeoverflowpagerindicator.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeoverflowpagerindicator.ui.old.OverflowPagerIndicator2

private const val MAX_PAGES = 15

@Composable
fun MainScreen() {
    var page by rememberSaveable { mutableStateOf(0) }
    Scaffold(
        topBar = { TopAppBar(title = { Text("Test OverFlowPagerIndicator") }) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("$page", fontSize = 26.sp)
            Spacer(Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                TextButton(
                    enabled = page != 0,
                    onClick = { page -= 1 }
                ) {
                    Text("prev")
                }
                Spacer(Modifier.width(16.dp))
                TextButton(
                    enabled = page != MAX_PAGES - 1,
                    onClick = { page += 1 }
                ) {
                    Text("next")
                }
            }
            Spacer(Modifier.height(16.dp))
            OverflowPagerIndicator(numPages = MAX_PAGES, currentPage = page)
        }
    }
}
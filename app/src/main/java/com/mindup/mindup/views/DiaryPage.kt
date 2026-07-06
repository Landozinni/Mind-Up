package com.mindup.mindup

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class DiaryPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enables edge-to-edge natively
        enableEdgeToEdge()

        // setContent replaces setContentView for Jetpack Compose
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DiaryScreen()
                }
            }
        }
    }
}
@Preview
@Composable
fun DiaryScreen() {
    // State to hold the diary entry text
    var diaryNotes by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .systemBarsPadding(), // Handles system bar overlaps cleanly without XML listeners
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {

        // 1. Top Section: Big text header
        Text(
            text = "What happened today?",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth()
        )

        // 2. Middle Section: Placeholder for your mood selector assets
        // Using weight(1f) ensures this expands to take up all available center space
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "(Mood Selector Assets Go Here)",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }

        // 3. Bottom Section: Description and notes text box
        OutlinedTextField(
            value = diaryNotes,
            onValueChange = { diaryNotes = it },
            label = { Text("Notes & Descriptions") },
            placeholder = { Text("How are you feeling? Write it down...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp) // Fixed height to give plenty of typing room
                .padding(bottom = 24.dp),
            shape = MaterialTheme.shapes.medium
        )
    }
}
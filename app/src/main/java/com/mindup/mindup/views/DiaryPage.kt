package com.mindup.mindup

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindup.mindup.ui.theme.MindUpFont
import com.mindup.mindup.ui.theme.White
import com.mindup.mindup.ui.theme.AzulMindUp
import com.mindup.mindup.ui.theme.MindUpFont
import com.mindup.mindup.ui.theme.RosaMindUp
import com.mindup.mindup.ui.theme.RoxoMindUp
import com.mindup.mindup.ui.theme.White

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
    var diaryNotes by remember { mutableStateOf("o dia de hoje...") }

    Column(
        modifier = Modifier

            .background(
            brush = Brush.linearGradient(
                colors = listOf(
                    RosaMindUp,
                    AzulMindUp
                )
            )
            )



    ) {

        // titulo da pagina
        Text(
                text = "COMO FOI SEU DIA HOJE?",
            color = White,
            fontSize = 42.sp,
            fontFamily = MindUpFont,

            textAlign = TextAlign.Center

            )

        // aqui vai o seletor de sentimentos do dia
        // Using weight(1f) ensures this expands to take up all available center space
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "(seletor de sentimentos aqui)",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }

        // descrição de eventos do dia
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
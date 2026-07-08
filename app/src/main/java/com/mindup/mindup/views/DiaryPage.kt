package com.mindup.mindup

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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

            Image(
                painter = painterResource(R.drawable.baseline_sentiment_satisfied_alt_24),
                contentDescription = null,
                modifier = Modifier.size(240.dp),
                contentScale = ContentScale.Fit
            )
        }

        // descrição de eventos do dia

        OutlinedTextField(
            value = diaryNotes,
            onValueChange = { diaryNotes = it },
            label = { Text("Descrições do dia") },
            placeholder = { Text("Como se sente?") },
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp), // Slightly adjusted height to accommodate buttons nicely
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Action Buttons Row (Save and Go Back)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Secondary action button
            OutlinedButton(
                onClick = { /* Visual only: Handle navigation back later */ },
                modifier = Modifier.weight(1f)
            ) {
                Text("Back")
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Primary action button
            Button(
                onClick = { /* Visual only: Handle saving entry later */ },
                modifier = Modifier.weight(1f)
            ) {
                Text("Save")
            }
        }


    }
}
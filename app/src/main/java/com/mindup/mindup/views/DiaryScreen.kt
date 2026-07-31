package com.mindup.mindup.views

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SentimentNeutral
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material.icons.filled.SentimentVerySatisfied
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindup.mindup.DBHelper
import com.mindup.mindup.ui.theme.AzulMindUp
import com.mindup.mindup.ui.theme.MindUpFont
import com.mindup.mindup.ui.theme.RosaMindUp
import com.mindup.mindup.ui.theme.White

class Diary : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

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

private data class MoodItem(
    val label: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun DiaryScreen(
    onVoltar: () -> Unit = {}
) {
    var diaryNotes by remember { mutableStateOf("") }
    var selectedMood by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val db = remember { DBHelper(context) }

    val moodOptions = listOf(
        MoodItem("muito mal", Icons.Default.SentimentVeryDissatisfied, Color(0xFFFF5252)),
        MoodItem("mal", Icons.Default.SentimentDissatisfied, Color(0xFFFF9800)),
        MoodItem("neutro", Icons.Default.SentimentNeutral, Color(0xFFFFEB3B)),
        MoodItem("bem", Icons.Default.SentimentSatisfied, Color(0xFF8BC34A)),
        MoodItem("muito bem", Icons.Default.SentimentVerySatisfied, Color(0xFF4CAF50))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        RosaMindUp,
                        AzulMindUp
                    )
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        // Titulo da pagina
        Text(
            text = "COMO FOI SEU DIA HOJE?",
            color = White,
            fontSize = 32.sp,
            fontFamily = MindUpFont,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Seletor de sentimentos (Mood Selector)
        Text(
            text = "Selecione o seu humor:",
            color = White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            moodOptions.forEach { mood ->
                val isSelected = selectedMood == mood.label

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedMood = mood.label }
                        .padding(4.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = if (isSelected) mood.color.copy(alpha = 0.35f) else Color.Transparent,
                                shape = CircleShape
                            )
                            .border(
                                width = if (isSelected) 2.dp else 0.dp,
                                color = if (isSelected) mood.color else Color.Transparent,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = mood.icon,
                            contentDescription = mood.label,
                            tint = if (isSelected) mood.color else White.copy(alpha = 0.8f),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = mood.label,
                        color = if (isSelected) White else White.copy(alpha = 0.8f),
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Descricao de eventos do dia
        Text(
            text = "Descrição do dia:",
            color = White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = diaryNotes,
            onValueChange = { diaryNotes = it },
            placeholder = {
                Text(
                    text = "Como se sente hoje? Escreva sobre o seu dia...",
                    color = Color.Gray
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Action Buttons Row (Save and Go Back)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Secondary action button - VOLTAR
            OutlinedButton(
                onClick = { onVoltar() },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = White
                ),
                border = BorderStroke(1.dp, White)
            ) {
                Text(
                    text = "VOLTAR",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Primary action button - SALVAR
            Button(
                onClick = {
                    val mood = selectedMood
                    if (mood == null) {
                        Toast.makeText(
                            context,
                            "Por favor, selecione como você se sente hoje.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (diaryNotes.trim().isEmpty()) {
                        Toast.makeText(
                            context,
                            "Preencha a descrição do seu dia.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val sucesso = db.salvarEntradaDiario(mood, diaryNotes)
                        if (sucesso) {
                            Toast.makeText(
                                context,
                                "Entrada do diário salva com sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()
                            diaryNotes = ""
                            selectedMood = null
                        } else {
                            Toast.makeText(
                                context,
                                "Erro ao salvar no diário.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = AzulMindUp
                )
            ) {
                Text(
                    text = "SALVAR",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun DiaryScreenPreview() {
    DiaryScreen()
}
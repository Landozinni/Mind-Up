package com.mindup.mindup.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindup.mindup.ui.theme.AzulMindUp
import com.mindup.mindup.ui.theme.RosaMindUp
import com.mindup.mindup.ui.theme.RoxoMindUp

data class DassQuestion(
    val id: Int,
    val text: String,
    val category: DassCategory
)

enum class DassCategory(val title: String, val color: Color) {
    ESTRESSE("Estresse", Color(0xFFFF9800)),
    ANSIEDADE("Ansiedade", Color(0xFF2196F3)),
    DEPRESSAO("Depressão", Color(0xFF9C27B0))
}

val dass21Questions = listOf(
    DassQuestion(1, "Achei difícil me acalmar", DassCategory.ESTRESSE),
    DassQuestion(2, "Senti minha boca seca", DassCategory.ANSIEDADE),
    DassQuestion(3, "Não consegui vivenciar nenhum sentimento positivo", DassCategory.DEPRESSAO),
    DassQuestion(4, "Tive dificuldade em respirar em alguns momentos (ex. respiração ofegante, falta de ar, sem ter feito nenhum esforço físico)", DassCategory.ANSIEDADE),
    DassQuestion(5, "Achei difícil ter iniciativa para fazer as coisas", DassCategory.DEPRESSAO),
    DassQuestion(6, "Tive a tendência de reagir de forma exagerada às situações", DassCategory.ESTRESSE),
    DassQuestion(7, "Senti tremores (ex. nas mãos)", DassCategory.ANSIEDADE),
    DassQuestion(8, "Senti que estava sempre nervoso", DassCategory.ESTRESSE),
    DassQuestion(9, "Preocupei-me com situações em que eu pudesse entrar em pânico e parecesse ridículo (a)", DassCategory.ANSIEDADE),
    DassQuestion(10, "Senti que não tinha nada a desejar", DassCategory.DEPRESSAO),
    DassQuestion(11, "Senti-me agitado", DassCategory.ESTRESSE),
    DassQuestion(12, "Achei difícil relaxar", DassCategory.ESTRESSE),
    DassQuestion(13, "Senti-me depressivo (a) e sem ânimo", DassCategory.DEPRESSAO),
    DassQuestion(14, "Fui intolerante com as coisas que me impediam de continuar o que eu estava fazendo", DassCategory.ESTRESSE),
    DassQuestion(15, "Senti que ia entrar em pânico", DassCategory.ANSIEDADE),
    DassQuestion(16, "Não consegui me entusiasmar com nada", DassCategory.DEPRESSAO),
    DassQuestion(17, "Senti que não tinha valor como pessoa", DassCategory.DEPRESSAO),
    DassQuestion(18, "Senti que estava um pouco emotivo/sensível demais", DassCategory.ESTRESSE),
    DassQuestion(19, "Sabia que meu coração estava alterado mesmo não tendo feito nenhum esforço físico (ex. aumento da frequência cardíaca, disritmia cardíaca)", DassCategory.ANSIEDADE),
    DassQuestion(20, "Senti medo sem motivo", DassCategory.ANSIEDADE),
    DassQuestion(21, "Senti que a vida não tinha sentido", DassCategory.DEPRESSAO)
)

val ratingOptions = listOf(
    0 to "0 - Não se aplicou de maneira alguma",
    1 to "1 - Aplicou-se em algum grau (pouco tempo)",
    2 to "2 - Aplicou-se em grau considerável",
    3 to "3 - Aplicou-se muito (maioria do tempo)"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dass21Screen(
    onVoltar: () -> Unit = {},
    onFinalizar: (answers: Map<Int, Int>) -> Unit = {}
) {
    val answers = remember { mutableStateMapOf<Int, Int>() }
    var showInstructionDetails by remember { mutableStateOf(false) }

    val answeredCount = answers.size
    val totalCount = dass21Questions.size
    val progress = answeredCount.toFloat() / totalCount.toFloat()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Questionário DASS-21",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF1F2937)
                        )
                        Text(
                            text = "Avaliação da última semana",
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color(0xFF1F2937)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showInstructionDetails = !showInstructionDetails }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Instruções",
                            tint = RoxoMindUp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Progresso",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "$answeredCount de $totalCount respondidas",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF1F2937)
                        )
                    }

                    Button(
                        onClick = { onFinalizar(answers) },
                        enabled = answeredCount == totalCount,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RoxoMindUp,
                            disabledContainerColor = Color(0xFFE5E7EB)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text(
                            text = "Finalizar",
                            fontWeight = FontWeight.Bold,
                            color = if (answeredCount == totalCount) Color.White else Color.Gray
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            RosaMindUp.copy(alpha = 0.25f),
                            AzulMindUp.copy(alpha = 0.25f)
                        )
                    )
                )
                .padding(paddingValues)
        ) {
            // Linear progress indicator at the top
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = RoxoMindUp,
                trackColor = Color.White.copy(alpha = 0.5f)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Instructions Box
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = RoxoMindUp,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Instruções do Teste",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color(0xFF1F2937)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Por favor, leia cada afirmação e selecione a opção de 0 a 3 que melhor indica o quanto ela se aplicou a você durante a última semana.",
                                fontSize = 14.sp,
                                color = Color(0xFF4B5563),
                                lineHeight = 20.sp
                            )

                            AnimatedVisibility(visible = showInstructionDetails) {
                                Column(
                                    modifier = Modifier.padding(top = 12.dp)
                                ) {
                                    HorizontalDivider(color = Color(0xFFF3F4F6))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    ratingOptions.forEach { (value, desc) ->
                                        Text(
                                            text = desc,
                                            fontSize = 12.sp,
                                            color = Color(0xFF6B7280),
                                            modifier = Modifier.padding(vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Question White Cards
                itemsIndexed(dass21Questions) { index, question ->
                    QuestionCard(
                        questionNumber = index + 1,
                        question = question,
                        selectedOption = answers[question.id],
                        onOptionSelected = { optionValue ->
                            answers[question.id] = optionValue
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun QuestionCard(
    questionNumber: Int,
    question: DassQuestion,
    selectedOption: Int?,
    onOptionSelected: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (selectedOption != null) 3.dp else 1.dp,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header Row: Question number & Category Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(RoxoMindUp.copy(alpha = 0.12f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Questão %02d".format(questionNumber),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = RoxoMindUp
                    )
                }

                if (selectedOption != null) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Respondido",
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Question Text Box
            Text(
                text = question.text,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF111827),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 4 Option Buttons (0 to 3) in a row / grid layout
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                (0..3).forEach { optionValue ->
                    val isSelected = selectedOption == optionValue

                    val backgroundColor by animateColorAsState(
                        targetValue = if (isSelected) RoxoMindUp else Color(0xFFF9FAFB),
                        label = "bgColor"
                    )

                    val textColor by animateColorAsState(
                        targetValue = if (isSelected) Color.White else Color(0xFF374151),
                        label = "textColor"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(backgroundColor)
                            .border(
                                width = if (isSelected) 0.dp else 1.dp,
                                color = Color(0xFFE5E7EB),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { onOptionSelected(optionValue) },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = optionValue.toString(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = textColor
                            )
                            Text(
                                text = when(optionValue) {
                                    0 -> "Nunca"
                                    1 -> "Pouco"
                                    2 -> "Bastante"
                                    else -> "Sempre"
                                },
                                fontSize = 9.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = textColor.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Dass21ScreenPreview() {
    Dass21Screen()
}

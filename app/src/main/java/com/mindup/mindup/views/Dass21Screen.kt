package com.mindup.mindup.views

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mindup.mindup.DBHelper
import com.mindup.mindup.model.Dass21Data
import com.mindup.mindup.model.Dass21Evaluation
import com.mindup.mindup.model.DassCategory
import com.mindup.mindup.model.DassQuestionItem
import com.mindup.mindup.model.SubscaleResult
import com.mindup.mindup.ui.theme.AzulMindUp
import com.mindup.mindup.ui.theme.MindUpFont
import com.mindup.mindup.ui.theme.RosaMindUp
import com.mindup.mindup.ui.theme.RoxoMindUp
import com.mindup.mindup.ui.theme.White
import com.mindup.mindup.viewmodel.Dass21ViewModel

val ratingScaleGuide = listOf(
    0 to "0 - Não se aplicou de maneira alguma",
    1 to "1 - Aplicou-se em algum grau, ou por pouco de tempo",
    2 to "2 - Aplicou-se em um grau considerável, ou por boa parte do tempo",
    3 to "3 - Aplicou-se muito, ou na maioria do tempo"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dass21Screen(
    onVoltar: () -> Unit = {},
    onFinalizar: () -> Unit = {},
    viewModel: Dass21ViewModel = remember { Dass21ViewModel() }
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val db = remember { DBHelper(context) }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

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
                    IconButton(onClick = { viewModel.toggleInstructions() }) {
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
                            text = "${uiState.answeredCount} de ${uiState.totalCount} respondidas",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF1F2937)
                        )
                    }

                    Button(
                        onClick = {
                            viewModel.submitQuestionnaire(dbHelper = db)
                        },
                        enabled = uiState.isComplete,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RoxoMindUp,
                            disabledContainerColor = Color(0xFFE5E7EB)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text(
                            text = "Finalizar Teste",
                            fontWeight = FontWeight.Bold,
                            color = if (uiState.isComplete) Color.White else Color.Gray
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
            // Linear progress indicator
            LinearProgressIndicator(
                progress = { uiState.progress },
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
                                text = "Por favor, leia cada afirmação e selecione o número apropriado (0, 1, 2 ou 3) que indique o quanto ela se aplicou a você durante a última semana.",
                                fontSize = 14.sp,
                                color = Color(0xFF4B5563),
                                lineHeight = 20.sp
                            )

                            AnimatedVisibility(visible = uiState.showInstructionDetails) {
                                Column(
                                    modifier = Modifier.padding(top = 12.dp)
                                ) {
                                    HorizontalDivider(color = Color(0xFFF3F4F6))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    ratingScaleGuide.forEach { (_, desc) ->
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

                // Question Cards
                itemsIndexed(Dass21Data.questions) { index, question ->
                    QuestionCard(
                        questionNumber = index + 1,
                        question = question,
                        selectedOption = uiState.answers[question.id],
                        onOptionSelected = { optionValue ->
                            viewModel.onAnswerSelected(question.id, optionValue)
                        }
                    )
                }
            }
        }
    }

    // Results Dialog
    if (uiState.showResultDialog && uiState.evaluation != null) {
        Dass21ResultDialog(
            evaluation = uiState.evaluation!!,
            onDismiss = { viewModel.dismissResultDialog() },
            onFinalizar = {
                viewModel.dismissResultDialog()
                onFinalizar()
            },
            onRefazer = {
                viewModel.resetQuestionnaire()
            }
        )
    }
}

@Composable
fun QuestionCard(
    questionNumber: Int,
    question: DassQuestionItem,
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
                        .background(question.category.color.copy(alpha = 0.12f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Questão %02d • %s".format(questionNumber, question.category.displayName),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = question.category.color
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

            // 4 Option Buttons (0 to 3) in a row
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
                                text = when (optionValue) {
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

@Composable
fun Dass21ResultDialog(
    evaluation: Dass21Evaluation,
    onDismiss: () -> Unit,
    onFinalizar: () -> Unit,
    onRefazer: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.90f),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFF9FAFB),
            shadowElevation = 12.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Resultado DASS-21",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = MindUpFont,
                            color = Color(0xFF1F2937)
                        )
                        Text(
                            text = "Diagnóstico e Interpretação Clínica",
                            fontSize = 13.sp,
                            color = Color(0xFF6B7280)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                color = RoxoMindUp.copy(alpha = 0.15f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = RoxoMindUp,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // General Summary Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Spa,
                            contentDescription = null,
                            tint = AzulMindUp,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = evaluation.generalSummary,
                            fontSize = 13.sp,
                            color = Color(0xFF374151),
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Scrollable Subscales Results
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SubscaleResultCard(result = evaluation.depression)
                    SubscaleResultCard(result = evaluation.anxiety)
                    SubscaleResultCard(result = evaluation.stress)

                    // Reference and Disclaimer note
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = AzulMindUp,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Baseado na escala DASS-21 validada para o Português Brasileiro (GAIPA/UFC, Lovibond & Lovibond). Este resultado é informativo e de triagem. Para avaliação diagnóstica aprofundada, consulte um psicólogo ou psiquiatra.",
                                fontSize = 11.sp,
                                color = Color(0xFF1E40AF),
                                lineHeight = 15.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom Dialog Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onRefazer,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFD1D5DB))
                    ) {
                        Text(
                            text = "Refazer Teste",
                            color = Color(0xFF4B5563),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }

                    Button(
                        onClick = onFinalizar,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RoxoMindUp)
                    ) {
                        Text(
                            text = "Concluir",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SubscaleResultCard(result: SubscaleResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            // Category Title + Severity Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(result.category.color, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = result.category.displayName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                }

                // Severity Badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = result.severity.backgroundColor
                ) {
                    Text(
                        text = result.severity.label,
                        color = result.severity.color,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Score details and bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pontuação final (DASS-42 equiv.):",
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
                )
                Text(
                    text = "${result.finalScore} / 42",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = result.severity.color
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { (result.finalScore.toFloat() / 42f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = result.severity.color,
                trackColor = Color(0xFFF3F4F6)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Detailed message describing condition or lack thereof
            Text(
                text = result.conditionMessage,
                fontSize = 13.sp,
                color = Color(0xFF374151),
                lineHeight = 18.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Dass21ScreenPreview() {
    Dass21Screen()
}

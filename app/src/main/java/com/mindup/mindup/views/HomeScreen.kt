package com.mindup.mindup.views

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SentimentNeutral
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material.icons.filled.SentimentVerySatisfied
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.mindup.mindup.DBHelper
import com.mindup.mindup.R
import com.mindup.mindup.ui.theme.MindUpBackgroundBrush
import com.mindup.mindup.ui.theme.MindUpTheme
import com.mindup.mindup.ui.theme.RosaMindUp
import com.mindup.mindup.viewmodel.DiaryViewModel

private data class HomeMoodOption(
    val label: String,
    val icon: ImageVector,
    val color: Color
)

private val homeMoodOptions = listOf(
    HomeMoodOption("muito mal", Icons.Default.SentimentVeryDissatisfied, Color(0xFFFF5252)),
    HomeMoodOption("mal", Icons.Default.SentimentDissatisfied, Color(0xFFFF9800)),
    HomeMoodOption("neutro", Icons.Default.SentimentNeutral, Color(0xFFFFEB3B)),
    HomeMoodOption("bem", Icons.Default.SentimentSatisfied, Color(0xFF8BC34A)),
    HomeMoodOption("muito bem", Icons.Default.SentimentVerySatisfied, Color(0xFF4CAF50))
)

@Composable
fun HomeScreen(
    userName: String = "Usuário",
    welcomeMessage: String? = null,
    textLines: List<String> = emptyList(),
    onNavigateToMetas: () -> Unit = {},
    onNavigateToDiario: () -> Unit = {},
    onNavigateToPerfil: () -> Unit = {},
    bottomBar: @Composable () -> Unit = { CustomBottomNavigation(onNavigateToDiario = onNavigateToDiario) },
    diaryViewModel: DiaryViewModel = viewModel()
) {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current
    val auth = remember { if (isPreview) null else FirebaseAuth.getInstance() }
    val currentUser = auth?.currentUser
    val displayedUserName = remember(currentUser?.uid, userName) {
        if (userName.isNotBlank() && userName != "Usuário") userName
        else currentUser?.displayName?.takeIf { it.isNotBlank() }
            ?: currentUser?.email?.substringBefore("@")?.takeIf { it.isNotBlank() }
            ?: userName
    }
    val uiState by diaryViewModel.uiState.collectAsState()
    val dbHelper = remember(context) { DBHelper(context) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, dbHelper) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                diaryViewModel.loadDiaryStatus(dbHelper)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            diaryViewModel.clearErrorMessage()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = bottomBar
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MindUpBackgroundBrush())
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(Modifier.height(22.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.mindup_logo),
                    contentDescription = "Mind-Up",
                    modifier = Modifier.size(48.dp),
                    tint = Color.Unspecified
                )
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        text = welcomeMessage ?: "Olá, $displayedUserName",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Como você está se sentindo?",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                }
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil",
                    tint = RosaMindUp,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(onClick = onNavigateToPerfil)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(10.dp)
                        .size(22.dp)
                )
            }

            if (textLines.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
                textLines.forEach { line ->
                    Text(line, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                }
            }

            Spacer(Modifier.height(24.dp))
            Text(
                text = "Check-in de hoje",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = if (uiState.diaryAlreadyExists) "Seu registro de hoje está aqui." else "Reserve um momento para cuidar de você.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
            Spacer(Modifier.height(14.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(Modifier.padding(16.dp)) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(12.dp))
                    }

                    if (uiState.diaryAlreadyExists) {
                        Text(
                            text = "Você já registrou seu dia. Amanhã poderá fazer uma nova entrada.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    } else {
                        Text(
                            text = "Selecione o seu humor:",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        homeMoodOptions.forEach { mood ->
                            val selected = uiState.selectedMood == mood.label
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable(enabled = !uiState.isLoading && !uiState.diaryAlreadyExists) {
                                        diaryViewModel.onMoodSelected(mood.label)
                                    }
                                    .padding(vertical = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(43.dp)
                                        .background(
                                            if (selected) mood.color.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surfaceVariant,
                                            CircleShape
                                        )
                                        .border(
                                            width = if (selected) 2.dp else 1.dp,
                                            color = if (selected) mood.color else MaterialTheme.colorScheme.outlineVariant,
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = mood.icon,
                                        contentDescription = mood.label,
                                        tint = if (selected) mood.color else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                                Spacer(Modifier.height(5.dp))
                                Text(
                                    text = mood.label,
                                    color = if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 8.sp,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(14.dp))
                    OutlinedTextField(
                        value = uiState.notes,
                        onValueChange = diaryViewModel::onNotesChanged,
                        enabled = !uiState.isLoading && !uiState.diaryAlreadyExists,
                        placeholder = { Text("Como foi seu dia?", fontSize = 13.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(104.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = RosaMindUp,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = {
                            diaryViewModel.saveDiaryEntry(dbHelper) {
                                Toast.makeText(context, "Entrada salva com sucesso!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        enabled = !uiState.isLoading && !uiState.isSaving && !uiState.diaryAlreadyExists,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RosaMindUp)
                    ) {
                        Text(
                            text = when {
                                uiState.isSaving -> "SALVANDO..."
                                uiState.diaryAlreadyExists -> "JÁ REGISTRADO HOJE"
                                else -> "SALVAR MEU DIA"
                            },
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (uiState.diaryAlreadyExists) {
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "ABRIR DIÁRIO COMPLETO  →",
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .clickable(onClick = onNavigateToDiario)
                                .padding(8.dp),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Text(
                text = "Continue sua jornada",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HomeShortcutCard(
                    title = "Minhas metas",
                    subtitle = "Pequenos passos, grandes conquistas",
                    icon = Icons.Default.Place,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToMetas
                )
                HomeShortcutCard(
                    title = "Diário completo",
                    subtitle = "Veja seu registro e histórico",
                    icon = Icons.Default.Psychology,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToDiario
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun HomeShortcutCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(14.dp)) {
            Icon(icon, contentDescription = null, tint = RosaMindUp, modifier = Modifier.size(23.dp))
            Spacer(Modifier.height(10.dp))
            Text(title, color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp, lineHeight = 14.sp)
        }
    }
}

@Composable
fun CustomBottomNavigation(
    onNavigateToDiario: () -> Unit = {}
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Início", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = RosaMindUp,
                selectedTextColor = RosaMindUp,
                indicatorColor = RosaMindUp.copy(alpha = 0.2f)
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Place, contentDescription = "Metas") },
            label = { Text("Metas", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant)
        )
        NavigationBarItem(
            selected = false,
            onClick = onNavigateToDiario,
            icon = { Icon(Icons.Default.Star, contentDescription = "Diário") },
            label = { Text("Diário", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant)
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Relatórios") },
            label = { Text("Relatórios", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant)
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MindUpTheme {
        HomeScreen(userName = "Guilherme")
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenWithTextLinesPreview() {
    MindUpTheme {
        HomeScreen(
            userName = "Guilherme",
            textLines = listOf(
                "Espaço reservado para anotações do dia.",
                "Suas metas e reflexões aparecem aqui."
            )
        )
    }
}

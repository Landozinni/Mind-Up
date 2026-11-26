package com.mindup.mindup.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mindup.mindup.MindUpApplication
import com.mindup.mindup.R
import com.mindup.mindup.components.BottomBar
import com.mindup.mindup.components.GoalCard
import com.mindup.mindup.components.GoalDialog
import com.mindup.mindup.model.Goal
import com.mindup.mindup.navigation.Routes
import com.mindup.mindup.ui.theme.MindUpBackgroundBrush
import com.mindup.mindup.viewmodel.GoalViewModel
import com.mindup.mindup.viewmodel.GoalViewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun GoalsScreen(
    bottomBar: @Composable () -> Unit = { BottomBar(currentRoute = Routes.METAS) },
    viewModel: GoalViewModel? = null
) {
    val application = LocalContext.current.applicationContext as? MindUpApplication
    val activeViewModel = viewModel ?: if (application != null) {
        val factory = remember(application) { GoalViewModelFactory(application.container.goalRepository) }
        viewModel(factory = factory)
    } else null

    val goalsState by (activeViewModel?.goals ?: flowOf(emptyList<Goal>())).collectAsState(initial = null)
    val goals = goalsState.orEmpty()
    val message by (activeViewModel?.message ?: MutableStateFlow<String?>(null)).collectAsState()

    var dialogGoal by remember { mutableStateOf<Goal?>(null) }
    var creating by remember { mutableStateOf(false) }
    var deleteGoal by remember { mutableStateOf<Goal?>(null) }
    var selectedFilter by remember { mutableStateOf("Todas") }

    if (creating || dialogGoal != null) {
        GoalDialog(
            existing = dialogGoal,
            onDismiss = {
                creating = false
                dialogGoal = null
            },
            onSave = { goal ->
                if (dialogGoal == null) activeViewModel?.insertGoal(goal)
                else activeViewModel?.updateGoal(goal)
                creating = false
                dialogGoal = null
            }
        )
    }

    deleteGoal?.let { goal ->
        AlertDialog(
            onDismissRequest = { deleteGoal = null },
            title = { Text("Excluir meta?") },
            text = { Text("A meta \"${goal.title}\" será removida permanentemente.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        activeViewModel?.deleteGoal(goal)
                        deleteGoal = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteGoal = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    val today = remember { SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).format(Date()) }
    fun goalStatus(goal: Goal): String = when {
        goal.currentValue >= goal.targetValue -> "Concluída"
        goal.dueDate.isNotBlank() && goal.dueDate < today -> "Atrasada"
        else -> "Em andamento"
    }

    val filterMapping = listOf(
        "Todas" to null,
        "Em andamento" to "Em andamento",
        "Concluídas" to "Concluída",
        "Atrasadas" to "Atrasada"
    )
    val targetStatus = filterMapping.firstOrNull { it.first == selectedFilter }?.second
    val visibleGoals = if (targetStatus == null) goals else goals.filter { goalStatus(it) == targetStatus }

    Scaffold(
        bottomBar = bottomBar,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { creating = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Criar nova meta")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MindUpBackgroundBrush())
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(22.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.icone_meta),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Metas",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Acompanhe seus objetivos e hábitos",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filterMapping.forEach { (label, _) ->
                    FilterChip(
                        selected = selectedFilter == label,
                        onClick = { selectedFilter = label },
                        label = { Text(label, maxLines = 1) }
                    )
                }
            }

            Spacer(Modifier.height(6.dp))

            AnimatedVisibility(
                visible = message != null,
                enter = fadeIn(tween(180)) + expandVertically(tween(180)),
                exit = fadeOut(tween(140)) + shrinkVertically(tween(140))
            ) {
                message?.let { feedback ->
                    val isError = feedback.startsWith("Não")
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isError) Color(0xFFFFE9E7) else Color(0xFFEAF7EF)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = feedback,
                                modifier = Modifier.weight(1f),
                                color = Color(0xFF303039),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            TextButton(onClick = { activeViewModel?.clearMessage() }) {
                                Text("OK")
                            }
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 4.dp, bottom = 88.dp)
            ) {
                if (goalsState == null) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 64.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = "Carregando suas metas...",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else if (goals.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 56.dp, start = 16.dp, end = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(84.dp)
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.sprout),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(52.dp)
                                )
                            }
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = "Nenhuma meta cadastrada",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "Crie sua primeira meta para acompanhar seu progresso no dia a dia.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(Modifier.height(20.dp))
                            Button(
                                onClick = { creating = true },
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null)
                                Spacer(Modifier.width(6.dp))
                                Text("Criar primeira meta")
                            }
                        }
                    }
                } else if (visibleGoals.isEmpty()) {
                    item {
                        Text(
                            text = "Nenhuma meta nesta categoria.",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 36.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    items(visibleGoals, key = { it.id }) { goal ->
                        GoalCard(
                            goal = goal,
                            status = goalStatus(goal),
                            onEdit = { dialogGoal = goal },
                            onDelete = { deleteGoal = goal },
                            onProgress = { value ->
                                val target = goal.targetValue.coerceAtLeast(1)
                                val current = value.coerceIn(0, target)
                                activeViewModel?.updateGoal(
                                    goal.copy(
                                        currentValue = current,
                                        progress = current.toFloat() / target,
                                        progressText = "$current de $target"
                                    )
                                )
                            },
                            modifier = Modifier.animateItem(
                                fadeInSpec = tween(180),
                                fadeOutSpec = tween(140),
                                placementSpec = tween(220)
                            )
                        )
                    }
                }
            }
        }
    }
}

package com.mindup.mindup.views

import androidx.compose.foundation.background
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mindup.mindup.MindUpApplication
import com.mindup.mindup.components.BottomBar
import com.mindup.mindup.components.GoalCard
import com.mindup.mindup.components.GoalDialog
import com.mindup.mindup.model.Goal
import com.mindup.mindup.navigation.Routes
import com.mindup.mindup.ui.theme.MindUpBackgroundBrush
import com.mindup.mindup.viewmodel.GoalViewModel
import com.mindup.mindup.viewmodel.GoalViewModelFactory
import kotlinx.coroutines.flow.flowOf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun GoalsScreen(bottomBar: @Composable () -> Unit = { BottomBar(currentRoute = Routes.METAS) }, viewModel: GoalViewModel? = null) {
    val application = LocalContext.current.applicationContext as? MindUpApplication
    val activeViewModel = viewModel ?: if (application != null) {
        val factory = remember(application) { GoalViewModelFactory(application.container.goalRepository) }
        viewModel(factory = factory)
    } else null
    val goalsState by (activeViewModel?.goals ?: flowOf(emptyList<Goal>())).collectAsState(initial = null)
    val goals = goalsState.orEmpty()
    val message by (activeViewModel?.message ?: kotlinx.coroutines.flow.MutableStateFlow<String?>(null)).collectAsState()
    var dialogGoal by remember { mutableStateOf<Goal?>(null) }
    var creating by remember { mutableStateOf(false) }
    var deleteGoal by remember { mutableStateOf<Goal?>(null) }
    var filter by remember { mutableStateOf("Todas") }

    if (creating || dialogGoal != null) {
        GoalDialog(existing = dialogGoal, onDismiss = { creating = false; dialogGoal = null }, onSave = { goal ->
            if (dialogGoal == null) activeViewModel?.insertGoal(goal) else activeViewModel?.updateGoal(goal)
            creating = false; dialogGoal = null
        })
    }
    deleteGoal?.let { goal ->
        AlertDialog(onDismissRequest = { deleteGoal = null }, title = { Text("Excluir esta meta?") }, text = { Text("A meta \"${goal.title}\" será removida permanentemente.") }, confirmButton = { TextButton(onClick = { activeViewModel?.deleteGoal(goal); deleteGoal = null }, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) { Text("Excluir") } }, dismissButton = { TextButton(onClick = { deleteGoal = null }) { Text("Cancelar") } })
    }

    val today = remember { SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).format(Date()) }
    fun status(goal: Goal): String = when {
        goal.currentValue >= goal.targetValue -> "Concluída"
        goal.dueDate.isNotBlank() && goal.dueDate < today -> "Atrasada"
        else -> "Em andamento"
    }
    val filters = listOf("Todas", "Em andamento", "Concluídas", "Atrasadas")
    val visibleGoals = goals.filter { filter == "Todas" || status(it) == filter.removeSuffix("s") }
        .let { list -> if (filter == "Concluídas") goals.filter { status(it) == "Concluída" } else if (filter == "Atrasadas") goals.filter { status(it) == "Atrasada" } else list }

    Scaffold(bottomBar = bottomBar, floatingActionButton = {
        FloatingActionButton(onClick = { creating = true }, containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary) { Icon(Icons.Default.Add, contentDescription = "Criar nova meta") }
    }) { padding ->
        Column(Modifier.fillMaxSize().background(MindUpBackgroundBrush()).padding(padding).padding(horizontal = 20.dp)) {
            Spacer(Modifier.height(20.dp))
            Text("🎯 Metas", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Spacer(Modifier.height(6.dp))
            Text("Pequenos hábitos constroem grandes mudanças.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 15.sp)
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                filters.forEach { option ->
                    FilterChip(selected = filter == option, onClick = { filter = option }, label = { Text(option, maxLines = 1) })
                }
            }
            AnimatedVisibility(
                visible = message != null,
                enter = fadeIn(tween(180)) + expandVertically(tween(180)),
                exit = fadeOut(tween(140)) + shrinkVertically(tween(140))
            ) {
                if (message != null) {
                Card(Modifier.fillMaxWidth().padding(bottom = 8.dp), colors = CardDefaults.cardColors(containerColor = if (message!!.startsWith("Não")) Color(0xFFFFE9E7) else Color(0xFFEAF7EF)), shape = RoundedCornerShape(12.dp)) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(message!!, Modifier.weight(1f), color = Color(0xFF303039))
                        TextButton(onClick = { activeViewModel?.clearMessage() }) { Text("OK") }
                    }
                }
                }
            }
            LazyColumn(Modifier.fillMaxSize()) {
                if (goalsState == null) item {
                    Box(Modifier.fillMaxWidth().padding(top = 56.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF7C55D9))
                        Text("Carregando suas metas…", Modifier.padding(top = 76.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else if (goals.isEmpty()) item {
                    Column(Modifier.fillMaxWidth().padding(top = 56.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🌱", fontSize = 56.sp)
                        Spacer(Modifier.height(12.dp))
                        Text("Sua próxima conquista começa aqui", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(Modifier.height(8.dp))
                        Text("Crie sua primeira meta e acompanhe cada passo.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(18.dp))
                        Button(onClick = { creating = true }) { Icon(Icons.Default.Add, contentDescription = null); Spacer(Modifier.width(6.dp)); Text("Criar primeira meta") }
                    }
                } else if (visibleGoals.isEmpty()) item {
                    Text("Nenhuma meta nesta categoria de status.", Modifier.fillMaxWidth().padding(vertical = 36.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else items(visibleGoals, key = { it.id }) { goal ->
                    GoalCard(goal = goal, status = status(goal), onEdit = { dialogGoal = goal }, onDelete = { deleteGoal = goal }, onProgress = { value ->
                        val target = goal.targetValue.coerceAtLeast(1)
                        val current = value.coerceIn(0, target)
                        activeViewModel?.updateGoal(goal.copy(currentValue = current, progress = current.toFloat() / target, progressText = "$current de $target"))
                    }, modifier = Modifier.animateItem(
                        fadeInSpec = tween(180),
                        fadeOutSpec = tween(140),
                        placementSpec = tween(220)
                    ))
                }
                item { Spacer(Modifier.height(88.dp)) }
            }
        }
    }
}

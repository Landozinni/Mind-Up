package com.mindup.mindup.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindup.mindup.components.BottomBar
import com.mindup.mindup.components.GoalCard
import com.mindup.mindup.components.GoalDialog
import com.mindup.mindup.model.Goal
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mindup.mindup.MindUpApplication
import com.mindup.mindup.viewmodel.GoalViewModel
import com.mindup.mindup.viewmodel.GoalViewModelFactory

@Composable
fun GoalsScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as? MindUpApplication

    // Safety check for Previews: application context might not be MindUpApplication
    if (application == null) {
        GoalsScreenContent(
            goals = emptyList(),
            onSaveGoal = {}
        )
        return
    }

    val factory = remember {
        GoalViewModelFactory(application.container.goalRepository)
    }

    val viewModel: GoalViewModel = viewModel(factory = factory)
    val goals by viewModel.goals.collectAsState(initial = emptyList())

    GoalsScreenContent(
        goals = goals,
        onSaveGoal = { goal -> viewModel.insertGoal(goal) }
    )
}

@Composable
fun GoalsScreenContent(
    goals: List<Goal>,
    onSaveGoal: (Goal) -> Unit
) {
    var showDialog by remember {
        mutableStateOf(false)
    }

    if (showDialog) {
        GoalDialog(
            onDismiss = { showDialog = false },
            onSave = { goal ->
                onSaveGoal(goal)
                showDialog = false
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Color(0xFF8B5CF6)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Nova Meta",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFFF9F5FF),
                            Color.White
                        )
                    )
                )
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "🎯 Metas",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Pequenos hábitos constroem grandes mudanças.",
                color = Color.Gray,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { showDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Nova Meta")
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn {
                if (goals.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 80.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🎯",
                                fontSize = 64.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Você ainda não possui metas.",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Clique em \"Nova Meta\" para criar sua primeira meta.",
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    items(goals) { goal ->
                        GoalCard(
                            emoji = goal.emoji,
                            title = goal.title,
                            description = goal.description,
                            progress = goal.progress,
                            progressText = goal.progressText
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(90.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GoalsScreenPreview() {
    // In Preview, we call the Content directly to avoid ViewModel dependency issues
    GoalsScreenContent(
        goals = listOf(
            Goal(emoji = "🎯", title = "Meta de Teste", description = "Descrição da meta de teste")
        ),
        onSaveGoal = {}
    )
}

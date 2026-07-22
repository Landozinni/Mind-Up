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

@Composable
fun GoalsScreen() {

    val goals = remember {
        mutableStateListOf<Goal>()
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    if (showDialog) {

        GoalDialog(

            onDismiss = {
                showDialog = false
            },

            onSave = { goal ->

                goals.add(goal)

                showDialog = false

            }

        )

    }

    Scaffold(

        bottomBar = {
            BottomBar()
        },

        floatingActionButton = {

            FloatingActionButton(
                onClick = {
                    showDialog = true
                },
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
                onClick = {
                    showDialog = true
                },
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
    GoalsScreen()
}

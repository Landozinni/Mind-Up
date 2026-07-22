package com.mindup.mindup.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mindup.mindup.model.Goal

@Composable
fun GoalDialog(
    onDismiss: () -> Unit,
    onSave: (Goal) -> Unit
) {

    var emoji by remember {
        mutableStateOf("🎯")
    }

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    AlertDialog(

        onDismissRequest = onDismiss,

        title = {
            Text(
                text = "Nova Meta",
                fontWeight = FontWeight.Bold
            )
        },

        text = {

            Column {

                OutlinedTextField(
                    value = emoji,
                    onValueChange = {
                        emoji = it
                    },
                    label = {
                        Text("Emoji")
                    },
                    placeholder = {
                        Text("🎯")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    label = {
                        Text("Nome da Meta")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                    },
                    label = {
                        Text("Descrição")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

            }

        },

        confirmButton = {

            Button(

                onClick = {

                    if (title.isNotBlank()) {

                        onSave(

                            Goal(
                                emoji = emoji,
                                title = title,
                                description = description
                            )

                        )

                    }

                }

            ) {

                Text("Salvar")

            }

        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text("Cancelar")

            }

        }

    )

}
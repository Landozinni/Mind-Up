package com.mindup.mindup.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mindup.mindup.model.Goal
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun GoalDialog(
    onDismiss: () -> Unit,
    onSave: (Goal) -> Unit,
    existing: Goal? = null
) {
    var emoji by remember(existing) { mutableStateOf(existing?.emoji ?: "🎯") }
    var title by remember(existing) { mutableStateOf(existing?.title.orEmpty()) }
    var description by remember(existing) { mutableStateOf(existing?.description.orEmpty()) }
    var category by remember(existing) { mutableStateOf(existing?.category ?: "Pessoal") }
    var target by remember(existing) { mutableStateOf((existing?.targetValue ?: 1).toString()) }
    var dueDate by remember(existing) { mutableStateOf(existing?.dueDate.orEmpty()) }
    var frequency by remember(existing) { mutableStateOf(existing?.frequency.orEmpty()) }
    var error by remember(existing) { mutableStateOf<String?>(null) }
    val categories = listOf("Pessoal", "Saúde", "Estudos", "Carreira", "Relacionamentos", "Outro")
    val emojis = listOf("🎯", "🌱", "📚", "💪", "🧘", "💼", "💛", "✨")
    val colors = MaterialTheme.colorScheme

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp).heightIn(max = 720.dp),
            shape = RoundedCornerShape(28.dp),
            color = colors.surface,
            tonalElevation = 8.dp
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 16.dp, top = 22.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(if (existing == null) "Nova meta" else "Editar meta", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = colors.onSurface)
                        Spacer(Modifier.height(4.dp))
                        Text("Defina um passo de cada vez.", style = MaterialTheme.typography.bodyMedium, color = colors.onSurfaceVariant)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar", tint = colors.onSurfaceVariant)
                    }
                }
                HorizontalDivider(color = colors.outlineVariant.copy(alpha = .6f))

                Column(
                    modifier = Modifier.weight(1f, fill = false).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SectionLabel("DÊ UM NOME À SUA META")
                        Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Surface(shape = RoundedCornerShape(16.dp), color = colors.secondaryContainer) {
                                    Text(emoji, modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp), fontSize = 25.sp)
                                }
                                var emojiMenu by remember { mutableStateOf(false) }
                                TextButton(onClick = { emojiMenu = true }, contentPadding = PaddingValues(horizontal = 2.dp)) { Text("Ícone", fontSize = 12.sp) }
                                DropdownMenu(expanded = emojiMenu, onDismissRequest = { emojiMenu = false }) {
                                    emojis.chunked(4).forEach { row ->
                                        Row {
                                            row.forEach { choice ->
                                                DropdownMenuItem(
                                                    text = { Text(choice, fontSize = 22.sp) },
                                                    onClick = { emoji = choice; emojiMenu = false },
                                                    modifier = Modifier.width(56.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            OutlinedTextField(
                                value = title,
                                onValueChange = { title = it; error = null },
                                modifier = Modifier.weight(1f),
                                label = { Text("Título") },
                                placeholder = { Text("Ex.: Ler mais") },
                                singleLine = true,
                                isError = error?.contains("título", ignoreCase = true) == true,
                                shape = RoundedCornerShape(16.dp)
                            )
                        }
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Descrição (opcional)") },
                            placeholder = { Text("O que essa meta significa para você?") },
                            minLines = 2,
                            maxLines = 3,
                            shape = RoundedCornerShape(16.dp)
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SectionLabel("CATEGORIA")
                        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            categories.forEach { option ->
                                FilterChip(
                                    selected = category == option,
                                    onClick = { category = option },
                                    label = { Text(option) },
                                    leadingIcon = if (category == option) ({ Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(16.dp)) }) else null
                                )
                            }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SectionLabel("ALVO")
                        OutlinedTextField(
                            value = target,
                            onValueChange = { target = it.filter(Char::isDigit).take(6); error = null },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Quantidade ou etapas") },
                            placeholder = { Text("Ex.: 12 livros ou 5 km") },
                            leadingIcon = { Icon(Icons.Default.Flag, contentDescription = null) },
                            supportingText = { Text("Use um número para acompanhar seu progresso.") },
                            singleLine = true,
                            isError = error?.contains("quantidade", ignoreCase = true) == true,
                            shape = RoundedCornerShape(16.dp)
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SectionLabel("PLANEJAMENTO · OPCIONAL")
                        OutlinedTextField(
                            value = dueDate,
                            onValueChange = { dueDate = it.take(10); error = null },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Prazo") },
                            placeholder = { Text("AAAA-MM-DD") },
                            leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null) },
                            supportingText = { Text("Digite no formato AAAA-MM-DD.") },
                            singleLine = true,
                            isError = error?.contains("data", ignoreCase = true) == true,
                            shape = RoundedCornerShape(16.dp)
                        )
                        OutlinedTextField(
                            value = frequency,
                            onValueChange = { frequency = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Frequência") },
                            placeholder = { Text("Ex.: 3 vezes por semana") },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                    error?.let { Text(it, color = colors.error, style = MaterialTheme.typography.bodySmall) }
                }

                HorizontalDivider(color = colors.outlineVariant.copy(alpha = .6f))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss, modifier = Modifier.weight(1f)) { Text("Cancelar") }
                    Button(
                        onClick = {
                            val amount = target.toIntOrNull()
                            val validDate = dueDate.isBlank() || runCatching {
                                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).apply { isLenient = false }
                                formatter.parse(dueDate) != null && formatter.format(formatter.parse(dueDate)!!) == dueDate
                            }.getOrDefault(false)
                            error = when {
                                title.isBlank() -> "Informe um título para sua meta."
                                amount == null || amount <= 0 -> "A quantidade deve ser maior que zero."
                                !validDate -> "Use uma data válida no formato AAAA-MM-DD."
                                else -> null
                            }
                            if (error == null) {
                                val safeTarget = amount!!
                                val current = (existing?.currentValue ?: 0).coerceIn(0, safeTarget)
                                onSave(Goal(id = existing?.id ?: 0, emoji = emoji, title = title.trim(), description = description.trim(), currentValue = current, targetValue = safeTarget, progress = current.toFloat() / safeTarget, progressText = "$current de $safeTarget", category = category, dueDate = dueDate, frequency = frequency.trim()))
                            }
                        },
                        modifier = Modifier.weight(1.35f).height(52.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) { Text(if (existing == null) "Criar meta" else "Salvar alterações", fontWeight = FontWeight.SemiBold) }
                }
            }
        }
    }
}

@Composable
private fun SectionLabel(label: String) {
    Text(label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = MaterialTheme.colorScheme.primary)
}

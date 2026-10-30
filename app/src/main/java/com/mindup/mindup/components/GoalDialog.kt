package com.mindup.mindup.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
    var error by remember { mutableStateOf<String?>(null) }
    var categoryMenu by remember { mutableStateOf(false) }
    val categories = listOf("Pessoal", "Saúde", "Estudos", "Carreira", "Relacionamentos", "Outro")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (existing == null) "Nova meta" else "Editar meta", fontWeight = FontWeight.Bold) },
        text = {
            Column(Modifier.fillMaxWidth().heightIn(max = 520.dp).verticalScroll(rememberScrollState())) {
                OutlinedTextField(emoji, { emoji = it.take(4) }, label = { Text("Emoji (opcional)") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(title, { title = it; error = null }, label = { Text("Título *") }, singleLine = true, modifier = Modifier.fillMaxWidth(), isError = error != null)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(description, { description = it }, label = { Text("Descrição (opcional)") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
                Spacer(Modifier.height(8.dp))
                Box {
                    OutlinedButton(onClick = { categoryMenu = true }, modifier = Modifier.fillMaxWidth()) { Text("Categoria: $category") }
                    DropdownMenu(expanded = categoryMenu, onDismissRequest = { categoryMenu = false }) {
                        categories.forEach { item -> DropdownMenuItem(text = { Text(item) }, onClick = { category = item; categoryMenu = false }) }
                    }
                }
                OutlinedTextField(target, { target = it.filter(Char::isDigit).take(6) }, label = { Text("Quantidade ou etapas *") }, supportingText = { Text("Ex.: 12 livros, 5 km ou 8 etapas") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(dueDate, { dueDate = it.take(10); error = null }, label = { Text("Prazo (AAAA-MM-DD, opcional)") }, placeholder = { Text("2026-12-31") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(frequency, { frequency = it }, label = { Text("Frequência (opcional)") }, placeholder = { Text("Ex.: 3 vezes por semana") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                error?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
            }
        },
        confirmButton = {
            Button(onClick = {
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
                    onSave(Goal(id = existing?.id ?: 0, emoji = emoji.ifBlank { "🎯" }, title = title.trim(), description = description.trim(), currentValue = current, targetValue = safeTarget, progress = current.toFloat() / safeTarget, progressText = "$current de $safeTarget", category = category, dueDate = dueDate, frequency = frequency.trim()))
                }
            }) { Text("Salvar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

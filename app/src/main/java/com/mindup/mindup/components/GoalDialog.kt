package com.mindup.mindup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mindup.mindup.R
import com.mindup.mindup.model.Goal
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun GoalDialog(
    onDismiss: () -> Unit,
    onSave: (Goal) -> Unit,
    existing: Goal? = null
) {
    var title by remember(existing) { mutableStateOf(existing?.title.orEmpty()) }
    var description by remember(existing) { mutableStateOf(existing?.description.orEmpty()) }
    var category by remember(existing) { mutableStateOf(existing?.category ?: "Pessoal") }
    var target by remember(existing) { mutableStateOf((existing?.targetValue ?: 1).toString()) }
    var dueDateField by remember(existing) {
        val digits = existing?.dueDate?.let(::dateForDisplay)?.filter(Char::isDigit).orEmpty()
        mutableStateOf(TextFieldValue(digits, selection = TextRange(digits.length)))
    }
    var frequency by remember(existing) { mutableStateOf(existing?.frequency.orEmpty()) }
    var error by remember(existing) { mutableStateOf<String?>(null) }
    val categories = listOf("Pessoal", "Saúde", "Estudos", "Carreira", "Relacionamentos", "Outro")
    val colors = MaterialTheme.colorScheme

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .heightIn(max = 700.dp),
            shape = RoundedCornerShape(24.dp),
            color = colors.surface,
            tonalElevation = 6.dp
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 12.dp, top = 18.dp, bottom = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(colors.primary.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icone_meta),
                            contentDescription = null,
                            tint = colors.primary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = if (existing == null) "Nova meta" else "Editar meta",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = colors.onSurfaceVariant
                        )
                    }
                }

                HorizontalDivider(color = colors.outlineVariant.copy(alpha = 0.5f))

                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it; error = null },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Título") },
                        placeholder = { Text("Ex.: Ler 12 livros no ano") },
                        singleLine = true,
                        isError = error?.contains("título", ignoreCase = true) == true,
                        shape = RoundedCornerShape(14.dp)
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Descrição (opcional)") },
                        placeholder = { Text("Detalhes sobre sua meta") },
                        minLines = 2,
                        maxLines = 3,
                        shape = RoundedCornerShape(14.dp)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Categoria",
                            style = MaterialTheme.typography.labelLarge,
                            color = colors.onSurfaceVariant
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            categories.forEach { option ->
                                FilterChip(
                                    selected = category == option,
                                    onClick = { category = option },
                                    label = { Text(option) }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = target,
                        onValueChange = { target = it.filter(Char::isDigit).take(6); error = null },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Quantidade ou etapas") },
                        placeholder = { Text("Ex.: 10") },
                        leadingIcon = { Icon(Icons.Default.Flag, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        isError = error?.contains("quantidade", ignoreCase = true) == true,
                        shape = RoundedCornerShape(14.dp)
                    )

                    OutlinedTextField(
                        value = dueDateField,
                        onValueChange = { input ->
                            val digits = input.text.filter(Char::isDigit).take(8)
                            val selection = TextRange(
                                input.selection.start.coerceAtMost(digits.length),
                                input.selection.end.coerceAtMost(digits.length)
                            )
                            dueDateField = input.copy(text = digits, selection = selection)
                            error = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Prazo (opcional)") },
                        placeholder = { Text("DD/MM/AAAA") },
                        leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        visualTransformation = DateInputVisualTransformation(),
                        singleLine = true,
                        isError = error?.contains("data", ignoreCase = true) == true,
                        shape = RoundedCornerShape(14.dp)
                    )

                    OutlinedTextField(
                        value = frequency,
                        onValueChange = { frequency = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Frequência (opcional)") },
                        placeholder = { Text("Ex.: 3 vezes por semana") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp)
                    )

                    error?.let {
                        Text(it, color = colors.error, style = MaterialTheme.typography.bodySmall)
                    }
                }

                HorizontalDivider(color = colors.outlineVariant.copy(alpha = 0.5f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = {
                            val amount = target.toIntOrNull()
                            val dueDate = dueDateField.text.let(::formatDateInput)
                            val storedDate = dueDate.toStoredDate()
                            val validDate = dueDateField.text.isBlank() || storedDate != null
                            error = when {
                                title.isBlank() -> "Informe um título para sua meta."
                                amount == null || amount <= 0 -> "A quantidade deve ser maior que zero."
                                !validDate -> "Digite uma data válida no formato DD/MM/AAAA."
                                else -> null
                            }
                            if (error == null) {
                                val safeTarget = amount!!
                                val current = (existing?.currentValue ?: 0).coerceIn(0, safeTarget)
                                onSave(
                                    Goal(
                                        id = existing?.id ?: 0,
                                        emoji = "",
                                        title = title.trim(),
                                        description = description.trim(),
                                        currentValue = current,
                                        targetValue = safeTarget,
                                        progress = current.toFloat() / safeTarget,
                                        progressText = "$current de $safeTarget",
                                        category = category,
                                        dueDate = storedDate.orEmpty(),
                                        frequency = frequency.trim()
                                    )
                                )
                            }
                        },
                        modifier = Modifier
                            .weight(1.3f)
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = if (existing == null) "Criar meta" else "Salvar",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

private fun formatDateInput(input: String): String {
    val digits = input.filter(Char::isDigit).take(8)
    return when {
        digits.length <= 2 -> digits
        digits.length <= 4 -> "${digits.take(2)}/${digits.drop(2)}"
        else -> "${digits.take(2)}/${digits.substring(2, 4)}/${digits.drop(4)}"
    }
}

private class DateInputVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text
        val display = formatDateInput(digits)
        val mapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val safeOffset = offset.coerceIn(0, digits.length)
                val firstSlash = digits.length > 2 && safeOffset >= 2
                val secondSlash = digits.length > 4 && safeOffset >= 4
                return safeOffset + (if (firstSlash) 1 else 0) + (if (secondSlash) 1 else 0)
            }

            override fun transformedToOriginal(offset: Int): Int {
                val safeOffset = offset.coerceIn(0, display.length)
                val firstSlash = digits.length > 2 && safeOffset >= 3
                val secondSlash = digits.length > 4 && safeOffset >= 6
                return (safeOffset - (if (firstSlash) 1 else 0) - (if (secondSlash) 1 else 0))
                    .coerceIn(0, digits.length)
            }
        }
        return TransformedText(AnnotatedString(display), mapping)
    }
}

private fun String.toStoredDate(): String? {
    if (length != 10) return null
    return runCatching {
        val displayFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT).apply { isLenient = false }
        val parsed = displayFormat.parse(this) ?: return null
        if (displayFormat.format(parsed) != this) return null
        SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).format(parsed)
    }.getOrNull()
}

private fun dateForDisplay(date: String): String {
    if (date.isBlank()) return ""
    val isoFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).apply { isLenient = false }
    val displayFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)
    return runCatching {
        val parsed = isoFormat.parse(date) ?: return date
        if (isoFormat.format(parsed) == date) displayFormat.format(parsed) else date
    }.getOrDefault(date)
}

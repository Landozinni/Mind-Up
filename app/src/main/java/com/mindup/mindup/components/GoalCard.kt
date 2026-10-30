package com.mindup.mindup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindup.mindup.model.Goal

@Composable
fun GoalCard(
    goal: Goal,
    status: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onProgress: (Int) -> Unit
) {
    val ratio = (goal.currentValue.toFloat() / goal.targetValue.coerceAtLeast(1)).coerceIn(0f, 1f)
    Card(Modifier.fillMaxWidth().padding(vertical = 7.dp), shape = RoundedCornerShape(22.dp), elevation = CardDefaults.cardElevation(3.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(48.dp).clip(CircleShape).background(Color(0xFFF4F0FF)), contentAlignment = Alignment.Center) { Text(goal.emoji, fontSize = 25.sp) }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(goal.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("${goal.category} • $status", color = if (status == "Atrasada") Color(0xFF9A3412) else Color(0xFF51456A), style = MaterialTheme.typography.bodySmall)
                }
                TextButton(onClick = onEdit) { Text("Editar") }
                TextButton(onClick = onDelete, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) { Text("Excluir") }
            }
            if (goal.description.isNotBlank()) {
                Spacer(Modifier.height(10.dp)); Text(goal.description, color = Color(0xFF55515B))
            }
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Progresso: ${goal.currentValue.coerceAtLeast(0)} de ${goal.targetValue.coerceAtLeast(1)}", fontWeight = FontWeight.SemiBold)
                Text("${(ratio * 100).toInt()}%", fontWeight = FontWeight.Bold, color = Color(0xFF6541B5))
            }
            Spacer(Modifier.height(6.dp))
            LinearProgressIndicator(progress = { ratio }, modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(50)).semantics { progressBarRangeInfo = ProgressBarRangeInfo(ratio, 0f..1f) }, color = Color(0xFF7C55D9), trackColor = Color(0xFFE9E3F3))
            Spacer(Modifier.height(8.dp))
            val details = listOfNotNull(goal.dueDate.takeIf { it.isNotBlank() }?.let { "Prazo: $it" }, goal.frequency.takeIf { it.isNotBlank() }?.let { "Frequência: $it" }).joinToString("  ·  ")
            if (details.isNotBlank()) Text(details, style = MaterialTheme.typography.bodySmall, color = Color(0xFF625D68))
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(if (ratio >= 1f) "Meta concluída! 🎉" else "Atualizar progresso", modifier = Modifier.weight(1f), fontWeight = if (ratio >= 1f) FontWeight.SemiBold else FontWeight.Normal)
                OutlinedButton(onClick = { onProgress((goal.currentValue - 1).coerceAtLeast(0)) }, enabled = goal.currentValue > 0, contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)) { Text("−1") }
                Spacer(Modifier.width(8.dp))
                Button(onClick = { onProgress((goal.currentValue + 1).coerceAtMost(goal.targetValue)) }, enabled = goal.currentValue < goal.targetValue, contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)) { Text("+1") }
            }
        }
    }
}

package com.mindup.mindup.views

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindup.mindup.components.BottomBar
import com.mindup.mindup.ui.theme.MindUpBackgroundBrush
import kotlinx.coroutines.delay

private data class Practice(
    val title: String,
    val subtitle: String,
    val duration: String,
    val category: String,
    val colors: List<Color>,
    val steps: List<String>,
    val breathing: Boolean = false
)

private val practices = listOf(
    Practice(
        title = "Respiração 4–4–6",
        subtitle = "Uma pausa guiada para desacelerar o ritmo.",
        duration = "1 min",
        category = "RESPIRAÇÃO",
        colors = listOf(Color(0xFFB9DDF5), Color(0xFFB8C3F4)),
        steps = listOf("Sente-se de um jeito confortável e solte os ombros.", "Inspire pelo nariz por 4 segundos.", "Segure o ar por 4 segundos, sem forçar.", "Expire devagar por 6 segundos. Repita por 4 ciclos."),
        breathing = true
    ),
    Practice(
        title = "Respiração quadrada",
        subtitle = "Quatro tempos para inspirar, segurar e soltar.",
        duration = "2 min",
        category = "RESPIRAÇÃO",
        colors = listOf(Color(0xFFB9E7DE), Color(0xFFA8D9E6)),
        steps = listOf("Escolha uma posição confortável.", "Inspire contando até 4.", "Segure contando até 4, sem tensão.", "Expire contando até 4 e espere mais 4 antes da próxima inspiração.")
    ),
    Practice(
        title = "Aterramento 5–4–3–2–1",
        subtitle = "Use os sentidos para se reconectar com o agora.",
        duration = "3 min",
        category = "PRESENTE",
        colors = listOf(Color(0xFFB7D9C0), Color(0xFFE0DFA9)),
        steps = listOf("Observe 5 coisas que você consegue ver.", "Perceba 4 coisas que consegue tocar.", "Escute 3 sons ao seu redor.", "Identifique 2 cheiros e 1 sabor. Vá no seu próprio ritmo.")
    ),
    Practice(
        title = "Escaneamento corporal",
        subtitle = "Faça uma pausa e note as sensações do corpo.",
        duration = "5 min",
        category = "RELAXAMENTO",
        colors = listOf(Color(0xFFE8C9DA), Color(0xFFC8C4EC)),
        steps = listOf("Acomode-se sentado ou deitado.", "Leve a atenção aos pés e note as sensações, sem julgar.", "Suba devagar pelas pernas, barriga, mãos e ombros.", "Termine com uma respiração tranquila e escolha um alongamento suave, se quiser.")
    ),
    Practice(
        title = "Pausa de atenção plena",
        subtitle = "Um exercício breve para observar pensamentos e emoções.",
        duration = "2 min",
        category = "AUTOCUIDADO",
        colors = listOf(Color(0xFFF0D8B7), Color(0xFFEAC2C5)),
        steps = listOf("Pare por um momento e note como você está.", "Perceba os pensamentos que aparecem, sem precisar resolvê-los agora.", "Note o contato dos pés com o chão.", "Escolha uma pequena coisa que pode fazer por você neste momento.")
    ),
    Practice(
        title = "Um gesto de gentileza",
        subtitle = "Encontre uma frase acolhedora para este momento.",
        duration = "3 min",
        category = "AUTOCUIDADO",
        colors = listOf(Color(0xFFF5D9A9), Color(0xFFEFC4D1)),
        steps = listOf("Pense em algo que tem sido difícil hoje.", "Imagine o que você diria a alguém querido na mesma situação.", "Escolha uma frase simples e diga-a para si, como: ‘posso dar um passo de cada vez’.", "Respire com calma e anote a frase para lembrar depois.")
    )
)

@Composable
fun PracticesScreen(
    bottomBar: @Composable () -> Unit = { BottomBar(currentRoute = com.mindup.mindup.navigation.Routes.PRATICAS) },
    onOpenDass21: () -> Unit = {}
) {
    var selected by remember { mutableStateOf<Practice?>(null) }

    Scaffold(
        bottomBar = bottomBar,
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        if (selected == null) {
            Column(
                modifier = Modifier.fillMaxSize().background(MindUpBackgroundBrush()).padding(padding)
                    .verticalScroll(rememberScrollState()).padding(horizontal = 20.dp)
            ) {
                Spacer(Modifier.height(24.dp))
                Text("Práticas", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.height(6.dp))
                Text("Exercícios curtos para cuidar de você, no seu tempo.", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(20.dp))

                Card(
                    modifier = Modifier.fillMaxWidth().clickable { selected = practices.first() },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        Modifier.fillMaxWidth().background(Brush.linearGradient(practices.first().colors)).padding(20.dp)
                    ) {
                        Column {
                            Text("PARA COMEÇAR", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = Color(0xFF34485B))
                            Spacer(Modifier.height(12.dp))
                            Text("Respire.\nVolte para você.", fontSize = 26.sp, lineHeight = 31.sp, fontWeight = FontWeight.Bold, color = Color(0xFF27394C))
                            Spacer(Modifier.height(14.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Começar exercício", fontWeight = FontWeight.SemiBold, color = Color(0xFF27394C))
                                Spacer(Modifier.width(6.dp))
                                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color(0xFF27394C), modifier = Modifier.size(18.dp))
                            }
                        }
                        Icon(Icons.Default.Spa, contentDescription = null, tint = Color(0xFF6657A8), modifier = Modifier.align(Alignment.CenterEnd).padding(end = 8.dp).size(58.dp))
                    }
                }

                Spacer(Modifier.height(24.dp))
                Text("Explore as práticas", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.height(12.dp))
                practices.forEach { practice ->
                    PracticeCard(practice = practice, onClick = { selected = practice })
                    Spacer(Modifier.height(12.dp))
                }

                Card(
                    modifier = Modifier.fillMaxWidth().clickable(onClick = onOpenDass21),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .72f))
                ) {
                    Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(25.dp))
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Questionário DASS-21", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                            Text("Acesse sua autoavaliação emocional.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Icon(Icons.Default.ArrowForward, contentDescription = "Abrir DASS-21", tint = MaterialTheme.colorScheme.primary)
                    }
                }
                Spacer(Modifier.height(20.dp))
            }
        } else {
            PracticeDetail(
                practice = selected!!,
                contentPadding = padding,
                onBack = { selected = null }
            )
        }
    }
}

@Composable
private fun PracticeCard(practice: Practice, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                Modifier.fillMaxWidth().height(118.dp).background(Brush.linearGradient(practice.colors)).padding(16.dp)
            ) {
                Text(practice.category, modifier = Modifier.align(Alignment.TopStart).clip(CircleShape).background(Color.White.copy(alpha = .72f)).padding(horizontal = 10.dp, vertical = 5.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = .8.sp, color = Color(0xFF39414C))
                Icon(Icons.Default.Spa, contentDescription = null, tint = Color.White.copy(alpha = .85f), modifier = Modifier.align(Alignment.CenterEnd).padding(end = 8.dp).size(48.dp))
                Row(Modifier.align(Alignment.BottomStart), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Timer, contentDescription = null, tint = Color(0xFF39414C), modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(5.dp))
                    Text(practice.duration, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF39414C))
                }
            }
            Column(Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                Text(practice.title, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.height(4.dp))
                Text(practice.subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun PracticeDetail(practice: Practice, contentPadding: PaddingValues, onBack: () -> Unit) {
    var elapsed by remember(practice.title) { mutableIntStateOf(0) }
    var running by remember(practice.title) { mutableStateOf(false) }
    var completed by remember(practice.title) { mutableStateOf(false) }
    val totalSeconds = if (practice.breathing) 56 else 0

    LaunchedEffect(running) {
        if (running && practice.breathing) {
            while (elapsed < totalSeconds) {
                delay(1_000)
                elapsed += 1
            }
            running = false
            completed = true
        }
    }

    val phasePosition = elapsed % 14
    val phase = when {
        elapsed >= totalSeconds && completed -> "Prática concluída"
        phasePosition < 4 -> "Inspire pelo nariz"
        phasePosition < 8 -> "Segure sem forçar"
        else -> "Expire devagar"
    }
    val phaseScale by animateFloatAsState(
        targetValue = when {
            !running && elapsed == 0 -> 1f
            phasePosition < 4 -> 1.22f
            phasePosition < 8 -> 1.22f
            else -> .86f
        },
        animationSpec = tween(900),
        label = "breathingCircle"
    )

    Column(
        modifier = Modifier.fillMaxSize().background(MindUpBackgroundBrush()).padding(contentPadding)
            .verticalScroll(rememberScrollState()).padding(horizontal = 20.dp)
    ) {
        if (practice.breathing) {
            BreathExercise(
                title = practice.title,
                phase = phase,
                elapsed = elapsed,
                running = running,
                completed = completed,
                scale = phaseScale,
                onBack = onBack,
                onSeek = { delta -> elapsed = (elapsed + delta).coerceIn(0, totalSeconds); completed = false },
                onToggle = {
                    if (completed) { elapsed = 0; completed = false; running = true }
                    else running = !running
                }
            )
            Spacer(Modifier.height(20.dp))
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("Como praticar", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    practice.steps.forEachIndexed { index, step ->
                        Row(verticalAlignment = Alignment.Top) {
                            Box(Modifier.size(25.dp).clip(CircleShape).background(MaterialTheme.colorScheme.secondaryContainer), contentAlignment = Alignment.Center) { Text("${index + 1}", color = MaterialTheme.colorScheme.onSecondaryContainer, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                            Spacer(Modifier.width(11.dp)); Text(step, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, lineHeight = 21.sp)
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("Faça apenas o que for confortável para você. Você pode parar ou adaptar o exercício a qualquer momento.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(24.dp))
            return@Column
        }

        Spacer(Modifier.height(10.dp))
        TextButton(onClick = onBack, contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp)) {
            Text("←  Todas as práticas", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
        }
        Box(Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(24.dp)).background(Brush.linearGradient(practice.colors)), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Spa, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(66.dp))
        }
        Spacer(Modifier.height(18.dp))
        Text(practice.category, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(5.dp))
        Text(practice.title, fontSize = 25.sp, lineHeight = 31.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        Spacer(Modifier.height(8.dp))
        Text(practice.subtitle, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(18.dp))
        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text("Como praticar", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                practice.steps.forEachIndexed { index, step ->
                    Row(verticalAlignment = Alignment.Top) {
                        Box(Modifier.size(25.dp).clip(CircleShape).background(MaterialTheme.colorScheme.secondaryContainer), contentAlignment = Alignment.Center) {
                            Text("${index + 1}", color = MaterialTheme.colorScheme.onSecondaryContainer, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.width(11.dp))
                        Text(step, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, lineHeight = 21.sp)
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(
                onClick = { completed = !completed },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(16.dp)
        ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(if (completed) "Prática concluída" else "Concluí minha prática")
        }
        Spacer(Modifier.height(20.dp))
        Text("Faça apenas o que for confortável para você. Você pode parar ou adaptar o exercício a qualquer momento.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun BreathExercise(
    title: String, phase: String, elapsed: Int, running: Boolean, completed: Boolean,
    scale: Float, onBack: () -> Unit, onSeek: (Int) -> Unit, onToggle: () -> Unit
) {
    val phasePosition = elapsed % 14
    val remaining = when { phasePosition < 4 -> 4 - phasePosition; phasePosition < 8 -> 8 - phasePosition; else -> 14 - phasePosition }
    Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(30.dp)).background(Brush.verticalGradient(listOf(Color(0xFF191735), Color(0xFF39306D), Color(0xFF7765A9))))) {
        Canvas(Modifier.fillMaxWidth().height(535.dp)) {
            val w = size.width; val h = size.height
            val hills = Path().apply { moveTo(0f, h*.68f); cubicTo(w*.22f,h*.56f,w*.25f,h*.78f,w*.52f,h*.68f); cubicTo(w*.76f,h*.58f,w*.8f,h*.65f,w,h*.57f); lineTo(w,h); lineTo(0f,h); close() }
            drawPath(hills, Color(0xFF29244F))
            val front = Path().apply { moveTo(0f,h*.78f); cubicTo(w*.24f,h*.64f,w*.34f,h*.85f,w*.62f,h*.75f); cubicTo(w*.8f,h*.69f,w*.9f,h*.78f,w,h*.7f); lineTo(w,h); lineTo(0f,h); close() }
            drawPath(front, Color(0xFF1C1A3B))
            drawLine(Color(0xFFBFA7F4).copy(alpha=.65f), androidx.compose.ui.geometry.Offset(0f,h*.84f), androidx.compose.ui.geometry.Offset(w,h*.84f), strokeWidth=2.dp.toPx())
        }
        Column(Modifier.fillMaxWidth().padding(horizontal = 22.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Text("‹", fontSize = 34.sp, color = Color.White) }
                Spacer(Modifier.weight(1f))
                Text("RESPIRAÇÃO GUIADA", fontSize = 11.sp, letterSpacing = 1.4.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD7C6FF))
                Spacer(Modifier.weight(1f)); Spacer(Modifier.size(48.dp))
            }
            Text(title, fontSize = 25.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            Text("Respire. Relaxe. Volte para o presente.", fontSize = 14.sp, color = Color(0xFFC9B8F2), modifier = Modifier.padding(top = 5.dp))
            Spacer(Modifier.height(52.dp))
            Box(Modifier.size(250.dp), contentAlignment = Alignment.Center) {
                Box(Modifier.fillMaxSize().scale(scale).clip(CircleShape).background(Brush.radialGradient(listOf(Color(0xFFB9A0F6).copy(alpha=.32f), Color.Transparent))))
                Canvas(Modifier.fillMaxSize()) { drawCircle(brush = Brush.sweepGradient(listOf(Color(0xFF8067C5), Color(0xFFE1C9FF), Color(0xFF8067C5))), style = Stroke(width = 8.dp.toPx())) }
                Box(Modifier.size(218.dp).scale(scale).clip(CircleShape).background(Color(0xFF302859).copy(alpha=.78f)), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(if (completed) "Concluído" else if (running) phase.removeSuffix(" pelo nariz").removeSuffix(" sem forçar").removeSuffix(" devagar") else "Encontre seu ritmo", fontSize = 22.sp, fontWeight = FontWeight.Medium, color = Color.White)
                        Spacer(Modifier.height(8.dp))
                        Text(if (completed) "Muito bem" else if (running) "$remaining segundos" else "4 ciclos · no seu tempo", color = Color(0xFFD2BEFF), fontSize = 15.sp)
                    }
                }
                Box(Modifier.align(Alignment.TopCenter).offset(y = (-4).dp).size(16.dp).clip(CircleShape).background(Color.White))
            }
            Spacer(Modifier.height(38.dp))
            Row(Modifier.fillMaxWidth().clip(CircleShape).background(Color(0xFF17152F).copy(alpha=.74f)).padding(horizontal=20.dp, vertical=11.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
                TextButton(onClick = { onSeek(-15) }) { Text("↶ 15", color = Color.White) }
                FilledIconButton(onClick = onToggle, modifier = Modifier.size(64.dp), colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color(0xFF6654A0), contentColor = Color.White)) { Icon(if (running) Icons.Default.CheckCircle else Icons.Default.PlayArrow, contentDescription = if (running) "Pausar" else "Começar", modifier = Modifier.size(30.dp)) }
                TextButton(onClick = { onSeek(15) }) { Text("15 ↷", color = Color.White) }
            }
            Spacer(Modifier.height(23.dp))
            Text("${elapsed / 14} de 4 ciclos", color = Color(0xFFD2BEFF), fontSize = 13.sp)
            Spacer(Modifier.height(16.dp))
        }
    }
}

package com.mindup.mindup.views

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    darkThemeEnabled: Boolean = false,
    onDarkThemeChange: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current
    val auth = remember { if (isPreview) null else FirebaseAuth.getInstance() }
    val user = auth?.currentUser
    val profilePreferences = remember(user?.uid) {
        context.getSharedPreferences("mindup_profile_${user?.uid ?: "preview"}", android.content.Context.MODE_PRIVATE)
    }
    val colors = MaterialTheme.colorScheme
    val textColor = colors.onBackground
    val mutedColor = colors.onSurfaceVariant
    val cardColor = colors.surface
    val iconBackground = if (darkThemeEnabled) Color(0xFF332B42) else Color(0xFFF4F0FF)

    var displayName by remember(user?.uid) {
        mutableStateOf(user?.displayName?.takeIf { it.isNotBlank() }
            ?: user?.email?.substringBefore("@")?.takeIf { it.isNotBlank() }
            ?: "Usuário")
    }
    var showEditDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var newName by remember(showEditDialog) { mutableStateOf(displayName) }
    var bio by remember(user?.uid) { mutableStateOf(profilePreferences.getString("bio", "").orEmpty()) }
    var newBio by remember(showEditDialog) { mutableStateOf(bio) }
    var photoUriString by remember(user?.uid) { mutableStateOf(profilePreferences.getString("photo_uri", null)) }
    var currentPassword by remember(showPasswordDialog) { mutableStateOf("") }
    var newPassword by remember(showPasswordDialog) { mutableStateOf("") }
    var confirmPassword by remember(showPasswordDialog) { mutableStateOf("") }

    val photoPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } catch (_: SecurityException) {
                // The selected image can still be used for this session if persistable access is unavailable.
            }
            photoUriString = uri.toString()
            profilePreferences.edit().putString("photo_uri", photoUriString).apply()
        }
    }
    val profilePhoto by produceState<ImageBitmap?>(initialValue = null, photoUriString) {
        value = photoUriString?.let { storedUri ->
            withContext(Dispatchers.IO) {
                runCatching {
                    context.contentResolver.openInputStream(Uri.parse(storedUri))
                        ?.use { stream -> BitmapFactory.decodeStream(stream)?.asImageBitmap() }
                }.getOrNull()
            }
        }
    }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Editar perfil") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Nome") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = newBio,
                        onValueChange = { newBio = it },
                        label = { Text("Sobre mim") },
                        placeholder = { Text("Uma frase sobre você") },
                        minLines = 2,
                        maxLines = 3
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val cleanName = newName.trim()
                    val currentUser = auth?.currentUser
                    if (cleanName.isBlank()) {
                        Toast.makeText(context, "Digite um nome.", Toast.LENGTH_SHORT).show()
                    } else if (currentUser == null) {
                        Toast.makeText(context, "Entre na sua conta para editar o perfil.", Toast.LENGTH_SHORT).show()
                    } else {
                        val request = UserProfileChangeRequest.Builder()
                            .setDisplayName(cleanName)
                            .build()
                        currentUser.updateProfile(request).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                displayName = cleanName
                                bio = newBio.trim()
                                profilePreferences.edit().putString("bio", bio).apply()
                                showEditDialog = false
                                Toast.makeText(context, "Perfil atualizado.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Não foi possível atualizar o perfil.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }) { Text("Salvar") }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) { Text("Cancelar") }
            }
        )
    }

    if (showPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            title = { Text("Alterar senha") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        label = { Text("Senha atual") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("Nova senha") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirme a nova senha") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val currentUser = auth?.currentUser
                    val email = currentUser?.email
                    when {
                        currentPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank() ->
                            Toast.makeText(context, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()
                        newPassword.length < 6 ->
                            Toast.makeText(context, "A nova senha precisa ter pelo menos 6 caracteres.", Toast.LENGTH_SHORT).show()
                        newPassword != confirmPassword ->
                            Toast.makeText(context, "As senhas novas não coincidem.", Toast.LENGTH_SHORT).show()
                        currentUser == null || email == null ->
                            Toast.makeText(context, "Entre novamente para alterar sua senha.", Toast.LENGTH_SHORT).show()
                        else -> {
                            val credential = EmailAuthProvider.getCredential(email, currentPassword)
                            currentUser.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                                if (!reauthTask.isSuccessful) {
                                    Toast.makeText(context, "A senha atual está incorreta.", Toast.LENGTH_SHORT).show()
                                } else {
                                    currentUser.updatePassword(newPassword).addOnCompleteListener { updateTask ->
                                        if (updateTask.isSuccessful) {
                                            showPasswordDialog = false
                                            Toast.makeText(context, "Senha alterada com sucesso.", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Não foi possível alterar a senha. Tente entrar novamente.", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }) { Text("Salvar senha") }
            },
            dismissButton = {
                TextButton(onClick = { showPasswordDialog = false }) { Text("Cancelar") }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(colors.background, colors.surface)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = textColor)
                }
                Text("Meu perfil", fontSize = 24.sp, fontWeight = FontWeight.SemiBold,
                    color = textColor, modifier = Modifier.padding(start = 4.dp))
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier.size(96.dp).clip(CircleShape).background(Color(0xFF8B5CF6))
                            .clickable { photoPicker.launch(arrayOf("image/*")) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (profilePhoto != null) {
                            Image(
                                bitmap = profilePhoto!!,
                                contentDescription = "Foto de perfil",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(Icons.Default.Person, contentDescription = "Escolher foto de perfil", tint = Color.White,
                                modifier = Modifier.size(48.dp))
                        }
                    }
                    Surface(shape = CircleShape, color = Color(0xFF8B5CF6), modifier = Modifier.size(32.dp)) {
                        IconButton(onClick = { photoPicker.launch(arrayOf("image/*")) }) {
                            Icon(Icons.Default.AddAPhoto, contentDescription = "Trocar foto", tint = Color.White,
                                modifier = Modifier.size(18.dp))
                        }
                    }
                }
                Spacer(Modifier.height(14.dp))
                Text(displayName, fontSize = 22.sp, fontWeight = FontWeight.SemiBold, color = textColor)
                Spacer(Modifier.height(4.dp))
                Text(user?.email ?: "", fontSize = 14.sp, color = mutedColor)
                if (bio.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(bio, fontSize = 14.sp, color = mutedColor)
                }
            }

            SectionTitle("Minha conta", textColor)
            ProfileOption(Icons.Default.Edit, "Editar perfil", "Altere seu nome de exibição", cardColor,
                iconBackground, textColor, mutedColor, onClick = { showEditDialog = true })
            ProfileOption(Icons.Default.Lock, "Alterar senha", "Atualize sua senha de acesso", cardColor,
                iconBackground, textColor, mutedColor, onClick = { showPasswordDialog = true })

            Spacer(Modifier.height(20.dp))
            SectionTitle("Preferências", textColor)
            PreferenceSwitch(Icons.Default.Notifications, "Notificações", "Receber lembretes e avisos",
                notificationsEnabled, cardColor, iconBackground, textColor, mutedColor,
                onCheckedChange = { notificationsEnabled = it })
            PreferenceSwitch(Icons.Default.DarkMode, "Modo escuro", "Alterar aparência do aplicativo",
                darkThemeEnabled, cardColor, iconBackground, textColor, mutedColor,
                onCheckedChange = onDarkThemeChange)

            Spacer(Modifier.height(20.dp))
            SectionTitle("Outros", textColor)
            ProfileOption(Icons.Default.PrivacyTip, "Privacidade", "Gerencie seus dados e informações",
                cardColor, iconBackground, textColor, mutedColor, onClick = {})
            ProfileOption(Icons.Default.Info, "Sobre o Mind-Up", "Informações sobre o aplicativo",
                cardColor, iconBackground, textColor, mutedColor, onClick = {})

            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                    .clickable(onClick = onLogout).padding(vertical = 16.dp, horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Logout, contentDescription = "Sair", tint = Color(0xFFB45F5F),
                    modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(14.dp))
                Text("Sair da conta", fontSize = 15.sp, fontWeight = FontWeight.Medium,
                    color = Color(0xFFB45F5F))
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SectionTitle(title: String, color: Color) {
    Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = color,
        modifier = Modifier.padding(bottom = 10.dp))
}

@Composable
private fun ProfileOption(
    icon: ImageVector,
    title: String,
    subtitle: String,
    cardColor: Color,
    iconBackground: Color,
    textColor: Color,
    mutedColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(cardColor)
            .clickable(onClick = onClick).padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(42.dp).clip(RoundedCornerShape(12.dp)).background(iconBackground),
            contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = Color(0xFF8B5CF6), modifier = Modifier.size(21.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(title, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = textColor)
            Spacer(Modifier.height(2.dp))
            Text(subtitle, fontSize = 12.sp, color = mutedColor)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = mutedColor,
            modifier = Modifier.size(20.dp))
    }
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun PreferenceSwitch(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    cardColor: Color,
    iconBackground: Color,
    textColor: Color,
    mutedColor: Color,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(cardColor).padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(42.dp).clip(RoundedCornerShape(12.dp)).background(iconBackground),
            contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = Color(0xFF8B5CF6), modifier = Modifier.size(21.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(title, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = textColor)
            Spacer(Modifier.height(2.dp))
            Text(subtitle, fontSize = 12.sp, color = mutedColor)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
    Spacer(Modifier.height(8.dp))
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}

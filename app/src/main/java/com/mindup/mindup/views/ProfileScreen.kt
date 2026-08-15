package com.mindup.mindup.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

private val OffWhite = Color(0xFFF8F7F3)
private val DarkText = Color(0xFF292929)
private val GrayText = Color(0xFF777777)
private val CardColor = Color(0xFFFFFFFF)
private val AccentColor = Color(0xFF8A9A7B)

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onChangePassword: () -> Unit = {},
    onLogout: () -> Unit = {}
) {

    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = OffWhite
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {

            // Topo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = DarkText
                    )
                }

                Text(
                    text = "Meu perfil",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkText,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            // Perfil
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(AccentColor),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Foto de perfil",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Eduardo",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkText
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "eduardo@email.com",
                    fontSize = 14.sp,
                    color = GrayText
                )
            }

            // Informações
            Text(
                text = "Minha conta",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkText,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            ProfileOption(
                icon = Icons.Default.Edit,
                title = "Editar perfil",
                subtitle = "Altere seus dados pessoais",
                onClick = onEditProfile
            )

            ProfileOption(
                icon = Icons.Default.Lock,
                title = "Alterar senha",
                subtitle = "Atualize sua senha de acesso",
                onClick = onChangePassword
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Preferências
            Text(
                text = "Preferências",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkText,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            PreferenceSwitch(
                icon = Icons.Default.Notifications,
                title = "Notificações",
                subtitle = "Receber lembretes e avisos",
                checked = notificationsEnabled,
                onCheckedChange = {
                    notificationsEnabled = it
                }
            )

            PreferenceSwitch(
                icon = Icons.Default.DarkMode,
                title = "Modo escuro",
                subtitle = "Alterar aparência do aplicativo",
                checked = darkModeEnabled,
                onCheckedChange = {
                    darkModeEnabled = it
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Outros
            Text(
                text = "Outros",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkText,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            ProfileOption(
                icon = Icons.Default.PrivacyTip,
                title = "Privacidade",
                subtitle = "Gerencie seus dados e informações",
                onClick = {}
            )

            ProfileOption(
                icon = Icons.Default.Info,
                title = "Sobre o Mind-Up",
                subtitle = "Informações sobre o aplicativo",
                onClick = {}
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Sair
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        onLogout()
                    }
                    .padding(vertical = 16.dp, horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Sair",
                    tint = Color(0xFFB45F5F),
                    modifier = Modifier.size(22.dp)
                )

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                    text = "Sair da conta",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFB45F5F)
                )
            }
        }
    }
}


@Composable
private fun ProfileOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardColor)
            .clickable {
                onClick()
            }
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(OffWhite),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AccentColor,
                modifier = Modifier.size(21.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = DarkText
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = GrayText
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = GrayText,
            modifier = Modifier.size(20.dp)
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
}


@Composable
private fun PreferenceSwitch(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardColor)
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(OffWhite),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AccentColor,
                modifier = Modifier.size(21.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = DarkText
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = GrayText
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
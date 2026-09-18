package com.mindup.mindup.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindup.mindup.R
import com.mindup.mindup.ui.theme.AzulMindUp
import com.mindup.mindup.ui.theme.MindUpFont
import com.mindup.mindup.ui.theme.RosaMindUp
import com.mindup.mindup.ui.theme.RoxoMindUp
import com.mindup.mindup.ui.theme.White
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import com.google.firebase.auth.FirebaseAuth
@Composable
fun LoginScreen(
    onEntrar: () -> Unit,
    onCriarConta: () -> Unit,
    onEsqueceuSenha: () -> Unit
) {

    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var mostrarSenha by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current
    val auth = remember { if (isPreview) null else FirebaseAuth.getInstance() }

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
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 28.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(30.dp))

        Image(
            painter = painterResource(R.drawable.mindup_logo),
            contentDescription = null,
            modifier = Modifier.size(180.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            text = "Mind Up",
            color = Color(0xFF8B5CF6),
            fontFamily = MindUpFont,
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            textAlign = TextAlign.Center,
            letterSpacing = 0.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Bem-vindo de volta!",
            color = Color(0xFF2D2A32),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(28.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },

            modifier = Modifier.fillMaxWidth(),

            singleLine = true,

            shape = RoundedCornerShape(16.dp),

            placeholder = {
                Text("E-mail", color = Color.Gray)
            },

            leadingIcon = {
                Icon(
                    Icons.Default.Email,
                    contentDescription = null,
                    tint = Color(0xFF8B5CF6)
                )
            },

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),

            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,

                focusedBorderColor = Color(0xFF8B5CF6),
                unfocusedBorderColor = Color(0xFFE5E7EB),

                focusedTextColor = Color(0xFF1F2937),
                unfocusedTextColor = Color(0xFF1F2937)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },

            modifier = Modifier.fillMaxWidth(),

            singleLine = true,

            shape = RoundedCornerShape(16.dp),

            placeholder = {
                Text("Senha", color = Color.Gray)
            },

            leadingIcon = {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color(0xFF8B5CF6)
                )
            },

            trailingIcon = {

                IconButton(
                    onClick = {
                        mostrarSenha = !mostrarSenha
                    }
                ) {

                    Icon(
                        imageVector =
                            if (mostrarSenha)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = Color.Gray
                    )

                }

            },

            visualTransformation =
                if (mostrarSenha)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),

            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,

                focusedBorderColor = Color(0xFF8B5CF6),
                unfocusedBorderColor = Color(0xFFE5E7EB),

                focusedTextColor = Color(0xFF1F2937),
                unfocusedTextColor = Color(0xFF1F2937)
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Esqueceu sua senha?",
            fontSize = 15.sp,
            color = Color(0xFF8B5CF6),
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.End)
                .clickable {
                    onEsqueceuSenha()
                }
        )

        Spacer(modifier = Modifier.height(28.dp))
        Button(
            onClick = {

                if (email.isBlank() || senha.isBlank()) {

                    Toast.makeText(
                        context,
                        "Preencha todos os campos.",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    auth?.signInWithEmailAndPassword(email, senha)
                        ?.addOnCompleteListener { task ->

                            if (task.isSuccessful) {

                                Toast.makeText(
                                    context,
                                    "Login realizado com sucesso!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                onEntrar()

                            } else {

                                Toast.makeText(
                                    context,
                                    "E-mail ou senha inválidos.",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                        }

                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(27.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF8B5CF6)
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = "Entrar",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Não possui conta?",
            color = Color(0xFF6B7280),
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Criar Conta",
            color = Color(0xFF8B5CF6),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                onCriarConta()
            }
        )

        Spacer(modifier = Modifier.height(30.dp))

    }

}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onEntrar = {},
        onCriarConta = {},
        onEsqueceuSenha = {}
    )
}

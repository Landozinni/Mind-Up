
package com.mindup.mindup.views

import android.widget.Toast
import androidx.compose.ui.platform.LocalInspectionMode
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.mindup.mindup.R
import com.mindup.mindup.ui.theme.AzulMindUp
import com.mindup.mindup.ui.theme.MindUpFont
import com.mindup.mindup.ui.theme.RosaMindUp
import com.mindup.mindup.ui.theme.White

@Composable
fun PasswordReset(
    onVoltar: () -> Unit
) {

    var email by remember { mutableStateOf("") }

    val context = androidx.compose.ui.platform.LocalContext.current
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
            modifier = Modifier.size(160.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "Mind Up",
            color = Color(0xFF8B5CF6),
            fontFamily = MindUpFont,
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Esqueceu sua senha?",
            color = Color(0xFF2D2A32),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Digite seu e-mail e enviaremos um link para você redefinir sua senha.",
            color = Color(0xFF6B7280),
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.fillMaxWidth()
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
                    imageVector = Icons.Default.Email,
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

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {

                if (email.isBlank()) {

                    Toast.makeText(
                        context,
                        "Digite seu e-mail.",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    auth?.sendPasswordResetEmail(email)
                        ?.addOnCompleteListener { task ->

                            if (task.isSuccessful) {

                                Toast.makeText(
                                    context,
                                    "E-mail de recuperação enviado!",
                                    Toast.LENGTH_LONG
                                ).show()

                                onVoltar()

                            } else {

                                Toast.makeText(
                                    context,
                                    "Não foi possível enviar o e-mail.",
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
                text = "Enviar link",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Voltar para o login",
            color = Color(0xFF8B5CF6),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clickable {
                    onVoltar()
                }
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    PasswordReset(
        onVoltar = {}
    )
}

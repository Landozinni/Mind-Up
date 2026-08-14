
package com.mindup.mindup.views

import android.widget.Toast
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
                brush = Brush.linearGradient(
                    colors = listOf(
                        RosaMindUp,
                        AzulMindUp
                    )
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 30.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(30.dp))

        Image(
            painter = painterResource(R.drawable.mindup_logo),
            contentDescription = null,
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "Mind Up",
            color = White,
            fontFamily = MindUpFont,
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "Esqueceu sua senha?",
            color = White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Digite seu e-mail e enviaremos um link para você redefinir sua senha.",
            color = White,
            fontSize = 17.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(20.dp),

            placeholder = {
                Text("E-mail")
            },

            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null
                )
            },

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),

            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,

                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,

                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(25.dp))

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
                .height(60.dp),

            shape = RoundedCornerShape(30.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                AzulMindUp,
                                RosaMindUp
                            )
                        ),
                        shape = RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "Enviar link",
                    color = White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "Voltar para o login",
            color = White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(
                    color = Color.Transparent
                )
                .padding(8.dp)
                .then(
                    Modifier
                )
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

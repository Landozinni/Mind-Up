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
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindup.mindup.R
import com.mindup.mindup.ui.theme.AzulMindUp
import com.mindup.mindup.ui.theme.MindUpFont
import com.mindup.mindup.ui.theme.RosaMindUp
import com.mindup.mindup.ui.theme.White
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.ui.text.input.TextFieldValue

fun dataNascimentoValida(data: String): Boolean {

    if (data.length != 10) return false

    val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    formato.isLenient = false

    return try {
        formato.parse(data)
        true
    } catch (e: Exception) {
        false
    }
}

@Composable
    fun CadastroScreen(
        onEntrarClick: () -> Unit,
        onCriarContaClick: () -> Unit
) {

    var nome by remember { mutableStateOf("") }
    var nascimento by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var mostrarSenha by remember { mutableStateOf(false) }

    val auth = remember { FirebaseAuth.getInstance() }

    val context = LocalContext.current

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

        Spacer(modifier = Modifier.height(35.dp))

        Image(
            painter = painterResource(R.drawable.mindup_logo),
            contentDescription = null,
            modifier = Modifier.size(150.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "Mind Up",
            color = White,
            fontSize = 42.sp,
            fontFamily = MindUpFont,
            letterSpacing = 0.sp


        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Crie sua conta",
            color = White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },

            modifier = Modifier.fillMaxWidth(),

            singleLine = true,

            shape = RoundedCornerShape(20.dp),

            placeholder = {
                Text("Nome completo")
            },

            leadingIcon = {
                Icon(
                    Icons.Default.Badge,
                    contentDescription = null
                )
            },

            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,

                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,

                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        OutlinedTextField(
            value = nascimento,
            onValueChange = { valor ->

                val numeros = valor.text
                    .filter { it.isDigit() }
                    .take(8)

                val dataFormatada = when {
                    numeros.length <= 2 ->
                        numeros

                    numeros.length <= 4 ->
                        "${numeros.substring(0, 2)}/${numeros.substring(2)}"

                    else ->
                        "${numeros.substring(0, 2)}/${numeros.substring(2, 4)}/${numeros.substring(4)}"
                }

                nascimento = TextFieldValue(
                    text = dataFormatada,
                    selection = androidx.compose.ui.text.TextRange(dataFormatada.length)
                )
            },

            

            modifier = Modifier.fillMaxWidth(),

            singleLine = true,

            shape = RoundedCornerShape(20.dp),

            placeholder = {
                Text("Data de nascimento")
            },

            leadingIcon = {
                Icon(
                    Icons.Default.CalendarMonth,
                    contentDescription = null
                )
            },

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
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

        Spacer(modifier = Modifier.height(18.dp))

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
                    Icons.Default.Email,
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

        Spacer(modifier = Modifier.height(18.dp))
        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },

            modifier = Modifier.fillMaxWidth(),

            singleLine = true,

            shape = RoundedCornerShape(20.dp),

            placeholder = {
                Text("Senha")
            },

            leadingIcon = {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = null
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
                        contentDescription = null
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

                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,

                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(35.dp))

        Button(
            onClick = {

                if (
                    nome.isBlank() ||
                    nascimento.text.isBlank() ||
                    email.isBlank() ||
                    senha.isBlank()
                ) {

                    Toast.makeText(
                        context,
                        "Preencha todos os campos.",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    if (!dataNascimentoValida(nascimento.text)) {

                        Toast.makeText(
                            context,
                            "Digite uma data válida (dd/MM/yyyy).",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@Button
                    }

                    auth.createUserWithEmailAndPassword(email, senha)
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {

                                Toast.makeText(
                                    context,
                                    "Conta criada com sucesso!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                onCriarContaClick()

                            } else {

                                val mensagem = when {

                                    task.exception?.message?.contains(
                                        "email address is already in use",
                                        ignoreCase = true
                                    ) == true ->
                                        "Este e-mail já está cadastrado."

                                    task.exception?.message?.contains(
                                        "password should be at least 6",
                                        ignoreCase = true
                                    ) == true ->
                                        "A senha deve ter pelo menos 6 caracteres."

                                    task.exception?.message?.contains(
                                        "badly formatted",
                                        ignoreCase = true
                                    ) == true ->
                                        "E-mail inválido."

                                    else ->
                                        "Erro ao criar conta."
                                }

                                Toast.makeText(
                                    context,
                                    mensagem,
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
                            colors = listOf(AzulMindUp, RosaMindUp)
                        ),
                        shape = RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Criar Conta",
                    color = White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Já possui uma conta?",
            color = White,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Entrar",
            color = White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                onEntrarClick()
            }
        )

        Spacer(modifier = Modifier.height(30.dp))

    }

}

@Preview(showBackground = true)
@Composable
fun CadastroScreenPreview() {
    CadastroScreen(
        onEntrarClick = {},
        onCriarContaClick = {}
    )
}
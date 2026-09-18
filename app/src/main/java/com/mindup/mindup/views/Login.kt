package com.mindup.mindup.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindup.mindup.R
import com.mindup.mindup.ui.theme.AzulMindUp
import com.mindup.mindup.ui.theme.RosaMindUp
import com.mindup.mindup.ui.theme.RoxoMindUp
import com.mindup.mindup.ui.theme.White
import com.mindup.mindup.ui.theme.MindUpFont

@Composable
fun Login(
    onEntrar: () -> Unit,
    onCriarConta: () -> Unit
){
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
          .padding(horizontal = 24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Image(
            painter = painterResource(R.drawable.mindup_logo),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Fit
        )
        Text(
            text = "Mind Up",
            color = Color(0xFF8B5CF6),
            fontSize = 48.sp,
            fontFamily = MindUpFont,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            textAlign = TextAlign.Center,
            letterSpacing = 0.sp
        )
        Text(
            text = "Cuide da sua mente, transforme seus dias!",
            color = Color(0xFF2D2A32),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            textAlign = TextAlign.Center,
            lineHeight = 32.sp
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                onEntrar()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(28.dp),
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
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = {
                onCriarConta()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White,
                contentColor = Color(0xFF8B5CF6)
            ),
            border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF8B5CF6))
        ) {
            Text(
                text = "Criar Conta",
                color = Color(0xFF8B5CF6),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}




@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    Login(
        onEntrar = {},
        onCriarConta = {}
    )
}


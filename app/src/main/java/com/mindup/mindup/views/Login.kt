package com.mindup.mindup.views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
      modifier = Modifier.fillMaxSize().background(
          brush = Brush.linearGradient(
              colors = listOf(
                  RosaMindUp,
                  AzulMindUp,


              )
          )
      ).verticalScroll(rememberScrollState())
    )

    {
        Spacer(modifier = Modifier.height(40.dp))
        Image(
            painter = painterResource(R.drawable.mindup_logo),
            contentDescription = null,
            modifier = Modifier.size(240.dp)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Fit
        )
        Text(
            text = buildAnnotatedString {
                append("Mind Up")
            },
            color = White,
            fontSize = 50.sp,
            fontFamily = MindUpFont,
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            textAlign = TextAlign.Center,
            letterSpacing = (0.sp)
        )
        Text(
            text = buildAnnotatedString {
                append("Cuide da sua mente, transforme seus dias!")
            },
            color = White,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                Log.d("TESTE", "clicou no botão")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 30.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            contentPadding = PaddingValues()
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
                    text = "Entrar",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,

                )


                }
            }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                onCriarConta()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 30.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            contentPadding = PaddingValues()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                White,
                                White
                            )
                        ),
                        shape = RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Criar Conta",
                    color = RoxoMindUp,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
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


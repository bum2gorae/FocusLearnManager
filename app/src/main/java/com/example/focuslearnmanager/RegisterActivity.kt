package com.example.focuslearnmanager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.focuslearnmanager.ui.theme.FocusLearnManagerTheme

@Composable
fun SignUpScreen(navController: NavHostController) {
    var companyCode by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var companyEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var duplicateCheck by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(90.dp))
        Text(
            text = "계정 생성",
            color = Color.Blue,
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "관리자 계정 생성",
            color = Color.Black,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
        TextInputField(
            label = "회사코드",
            value = companyCode,
            onValueChange = { companyCode = it },
            modifier = Modifier
                .size(width = 250.dp, height = 60.dp)
                .background(Color.Transparent)
        )
        TextInputField(
            label = "회사명",
            value = companyName,
            onValueChange = { companyName = it },
            modifier = Modifier
                .size(width = 250.dp, height = 60.dp)
                .background(Color.Transparent)
        )
        TextInputField(
            label = "회사 이메일",
            value = companyEmail,
            onValueChange = { companyEmail = it },
            modifier = Modifier
                .size(width = 250.dp, height = 60.dp)
                .background(Color.Transparent)
        )
        TextInputField(
            label = "비밀번호",
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .size(width = 250.dp, height = 60.dp)
                .background(Color.Transparent)
        )
        TextInputField(
            label = "비밀번호 확인",
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .size(width = 250.dp, height = 60.dp)
                .background(Color.Transparent)
        )
        Spacer(modifier = Modifier.size(20.dp))
        Box(
            modifier = Modifier
                .size(width = 80.dp, height = 60.dp)
        ) {
            Button(
                onClick = { /* Handle sign up logic */ },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text(text = "등록", color = Color.White)
            }
        }
        Text(
            text = if (duplicateCheck) {
                "계정이 이미 있습니다"
            } else {
                ""
            },
            color = Color.Black,
            modifier = Modifier
                .padding(top = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    FocusLearnManagerTheme {
        val navController = rememberNavController()
        SignUpScreen(navController)
    }
}

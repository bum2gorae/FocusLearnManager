package com.example.focuslearnmanager

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun LoginScreen() {
    var userId by rememberSaveable { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp), // Adjust padding to position text as needed
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(120.dp))
            Text(
                text = "FOCUS LEARN",
                fontSize = 29.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0047AB)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "법정 의무 교육 플랫폼에 오신 것을 환영합니다",
                fontSize = 11.sp,
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextInputField(
                label = "ID 입력",
                value = userId,
                onValueChange = { userId = it },
                modifier = Modifier
                    .size(width = 260.dp, height = 60.dp)
                    .background(Color.Transparent)
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextInputField(
                label = "PW 입력",
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .size(width = 260.dp, height = 60.dp)
                    .background(Color.Transparent)
            )
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "회원가입",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0047AB),
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 30.dp)
            )
            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = { /* 로그인 처리 */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0000CD) // This sets the background color to a blue shade
                ),
                shape = RoundedCornerShape(8.dp), // Apply rounded corners
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .width(180.dp)

            ) {
                Text(
                    text = "로그인",
                    color = Color.White,
                    fontWeight = FontWeight.Bold, // Make the text bold
                    fontSize = 20.sp, // Increase the font size
                    modifier = Modifier
                        .padding(vertical = 6.dp) // Increase the padding around the text
                )
            }


            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = "C Intel",
            fontSize = 14.sp,
            color = Color.Blue,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
fun TextInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    modifier: Modifier
) {
    Box(
        modifier = Modifier.padding(10.dp)
    ) {
        val backgroundColor = Color(0xFFE4E9FB)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(
                    text = label,
                    fontSize = 14.sp
                )
            },
            visualTransformation = visualTransformation,
            modifier = modifier,
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedPlaceholderColor = Color.Blue
            ),
            textStyle = TextStyle(
                fontSize = 14.sp
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}


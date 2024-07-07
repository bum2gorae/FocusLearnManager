package com.example.focuslearnmanager

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.focuslearnmanager.ui.theme.FocusLearnManagerTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FocusLearnManagerTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.focuslearnback),
                        contentDescription = "background",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    SignUpScreen()
                }
            }
        }
    }
}



@Composable
fun SignUpScreen() {
    var companyCode by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var companyEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var checkCode by remember { mutableStateOf("")} //에러코드 발생시 입력
    val checkCodeMap = mapOf("" to "",
        "E01" to "등록된 계정이 있습니다.", //에러코드 관리
        "E02" to "없는 회사코드입니다",
        "E03" to "회사명이 일치하지 않습니다")
    val fireDB = Firebase.firestore
    val context = LocalContext.current
    val contextAct = LocalContext.current as Activity?

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
                onClick = {
                    val resultDB = fireDB.collection("Company").document(companyCode)
                    resultDB.get().addOnSuccessListener { document ->
                        if (document.exists()){
                            val companyNameDB = document.get("CompanyName")
                            if (companyNameDB==companyName) {
                                if (document.get("Password")==null) {
                                    val setData = mutableMapOf("companyEmail" to companyEmail,
                                        "Password" to password,
                                        "CompanyName" to companyName)
                                    resultDB.set(setData)
                                    checkCode = ""
                                    Toast.makeText(context, "등록되었습니다", Toast.LENGTH_SHORT).show()
                                    contextAct?.finish()
                                } else checkCode = "E01"; //계정 존재
                            } else checkCode = "E03"; //회사명 불일치
                        } else checkCode = "E02"; //없는 회사 코드
                    }
                },
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
            text = checkCodeMap.get(checkCode)!!,
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
        SignUpScreen()
    }
}


package com.example.focuslearnmanager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.focuslearnmanager.ui.theme.FocusLearnManagerTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class MainActivity : ComponentActivity() {
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
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        var userId by remember { mutableStateOf("") }
                        var password by remember { mutableStateOf("") }
                        val contextAct = LocalContext.current as Activity?
                        val fireDB = Firebase.firestore
                        LoginScreen(userId,
                            password,
                            onIDChange = { userId = it },
                            onPasswordChange = { password = it }
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 20.dp)
                        ) {
                            ToRegisterBox(modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 30.dp)
                                .clickable {
                                    val intent = Intent(contextAct, RegisterActivity::class.java)
                                    contextAct?.startActivity(intent)
                                })
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        LoginBtnBox(
                            onLoginBtnSuccess = {
                                fireDB.collection("Company").document(userId)
                                    .get().addOnSuccessListener { document ->
                                        if (document.exists()) {
                                            val passwordDB = document.get("Password")
                                            if (password == passwordDB) {
                                                val intent = Intent(contextAct, MainScreenActivity::class.java)
                                                intent.putExtra("companyCode", userId)
                                                contextAct?.startActivity(intent)
                                            }
                                        }
                                    }

                            }
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(40.dp),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "C Intel",
                            fontSize = 14.sp,
                            color = Color.Blue,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(
    userId: String,
    password: String,
    onIDChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp), // Adjust padding to position text as needed
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(150.dp))
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
        Spacer(modifier = Modifier.height(80.dp))
        TextInputField(
            label = "회사코드 입력",
            value = userId,
            onValueChange = { onIDChange(it) },
            modifier = Modifier
                .size(width = 260.dp, height = 60.dp)
                .background(Color.Transparent)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextInputField(
            label = "PW 입력",
            value = password,
            onValueChange = { onPasswordChange(it) },
            modifier = Modifier
                .size(width = 260.dp, height = 60.dp)
                .background(Color.Transparent)
        )
    }
}


@Composable
fun ToRegisterBox(modifier: Modifier) {
    Box(modifier = modifier) {
        Text(
            text = "회원가입",
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF0047AB)
        )
    }
}

@Composable
fun LoginBtnBox(
    onLoginBtnSuccess: () -> Unit
) {
    Button(
        onClick = { onLoginBtnSuccess() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF0000CD) // This sets the background color to a blue shade
        ),
        shape = RoundedCornerShape(8.dp), // Apply rounded corners
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .width(160.dp)

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
                focusedContainerColor = backgroundColor,
                unfocusedIndicatorColor = Color.Transparent,
                focusedPlaceholderColor = Color.Blue,
            ),
            textStyle = TextStyle(
                fontSize = 14.sp
            ),
        )
    }
}



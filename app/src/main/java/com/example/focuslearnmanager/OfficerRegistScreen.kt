package com.example.focuslearnmanager

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.focuslearnmanager.ui.theme.FocusLearnManagerTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun OfficerRegistScreen(paddingValues: PaddingValues, companyCode: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            //contents
            var searchText by remember {
                mutableStateOf("")
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "직원 관리",
                    fontSize = 29.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0047AB)
                )
            }
            Spacer(modifier = Modifier.size(30.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier.weight(0.75f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    TextField(
                        value = searchText, onValueChange = { searchText = it },
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(width = 200.dp, height = 55.dp)
                            .border(
                                BorderStroke(width = 1.dp, color = Color.Black),
                                shape = RoundedCornerShape(20.dp)
                            ),
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedPlaceholderColor = Color.Black.copy(alpha = 0.5f),
                            disabledPlaceholderColor = Color.Black,
                            focusedPlaceholderColor = Color.Black,
                            unfocusedContainerColor = Color.White
                        )
                    )
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 10.dp)
                    ) {
                        Icon(painter = painterResource(id = R.drawable.icon_search),
                            contentDescription = "search",
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { })
                    }
                }
                Spacer(modifier = Modifier.size(10.dp))
                Row(
                    modifier = Modifier
                        .clickable { }
                        .weight(0.25f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text("필터", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Icon(
                        painter = painterResource(id = R.drawable.icon_downarrow),
                        contentDescription = "filter",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.size(10.dp))
            TableRow(
                department = "부서",
                companyID = "사번",
                officerName = "이름",
                officerPosition = "직책",
                lectureName = "강의명",
                lectureStatus = "생년월일",
                fontWeight = FontWeight.SemiBold
            )

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Log.d("test", companyCode)
                var dataMap by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }

                LaunchedEffect(companyCode) {
                    fetchData(companyCode) { fetchedData ->
                        dataMap = fetchedData
                    }
                }
                dataMap.forEach { datamap ->
                    val lectureCodeMap =
                        datamap["LectureCode"] as? Map<String, Boolean> ?: emptyMap()
                    lectureCodeMap.keys.forEach { lecture ->
                        TableRow(
                            department = datamap.get("Department").toString(),
                            companyID = datamap.get("ID").toString(),
                            officerName = datamap.get("Department").toString(),
                            officerPosition = datamap.get("Name").toString(),
                            lectureName = lecture,
                            lectureStatus = datamap.get("SecurityNumber").toString().substring(0, 6)
                        )

                    }
                }
            }
        }
    }
}

fun fetchData(
    companyCode: String, onDataFetched: (List<Map<String, Any>>) -> Unit
) {
    val fireDB =
        Firebase.firestore.collection("Company").document(companyCode).collection("Employee")

    fireDB.get().addOnSuccessListener { document ->
        val dataMap = mutableListOf<Map<String, Any>>()
        document.documents.forEach { it ->
            val data = it.data.orEmpty().toMutableMap()
            Log.d("test", "${data["ID"]}")
            data["Name"] = it.id
            Log.d("test", "${data["Name"]}")
            dataMap.add(data)
        }
        onDataFetched(dataMap)
    }
}


@Composable
fun OfficerRegistScaffoldScreen(companyCode: String) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Color.Blue
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_add),
                    contentDescription = "officer add",
                    tint = Color.White
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        content = { paddingValues ->
            var department by remember { mutableStateOf("") }
            var ID by remember { mutableStateOf("") }
            var name by remember { mutableStateOf("") }
            var phoneCall by remember { mutableStateOf("") }
            var position by remember { mutableStateOf("") }
            var securityNumber by remember { mutableStateOf("") }
            val lectureCode = remember { mutableMapOf<String, Boolean>() }
            val lectureStatus = mapOf(
                "장애인인식개선" to false,
                "직장내성희롱" to false,
                "산업안전법" to false,
                "개인정보보호" to false
            )
            var code1 by remember { mutableStateOf(false) }
            var code2 by remember { mutableStateOf(false) }
            var code3 by remember { mutableStateOf(false) }
            var code4 by remember { mutableStateOf(false) }
            if (showDialog) {
                AlertDialog(

                    onDismissRequest = { showDialog = false },
                    title = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "직원 등록")
                        }
                    },
                    text = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {


                            TextInputField(
                                label = "부서",
                                value = department,
                                onValueChange = { department = it },
                                modifier = Modifier.height(60.dp)
                            )
                            TextInputField(
                                label = "사번",
                                value = ID,
                                onValueChange = { ID = it },
                                modifier = Modifier.height(60.dp)
                            )
                            TextInputField(
                                label = "이름",
                                value = name,
                                onValueChange = { name = it },
                                modifier = Modifier.height(60.dp)
                            )
                            TextInputField(
                                label = "직책",
                                value = position,
                                onValueChange = { position = it },
                                modifier = Modifier.height(60.dp)
                            )
                            TextInputField(
                                label = "전화번호",
                                value = phoneCall,
                                onValueChange = { phoneCall = it },
                                modifier = Modifier.height(60.dp)
                            )
                            TextInputField(
                                label = "주민번호",
                                value = securityNumber,
                                onValueChange = { securityNumber = it },
                                modifier = Modifier.height(60.dp)
                            )
                            Row {
                                code1 = lectureRadioButton(label = "장애인인식개선")
                                code2 = lectureRadioButton(label = "직장내성희롱")
                            }
                            Row {
                                code3 = lectureRadioButton(label = "산업안전법")
                                code4 = lectureRadioButton(label = "개인정보보호")
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                val employeeDB = Firebase.firestore
                                    .collection("Company")
                                    .document(companyCode)
                                    .collection("Employee")
                                    .document(name)
                                employeeDB.get().addOnSuccessListener { document ->
                                    if (document.exists()) {
                                        val employSecurityNumber = document.get("SecurityNumber")
                                        if (securityNumber == employSecurityNumber) {

                                        } else {
                                            lectureCode.putAll(
                                                mapOf(
                                                    "장애인인식개선" to code1,
                                                    "직장내성희롱" to code2,
                                                    "산업안전법" to code3,
                                                    "개인정보보호" to code4
                                                )
                                            )
                                            val setData = mutableMapOf(
                                                "Department" to department,
                                                "ID" to ID,
                                                "Position" to position,
                                                "Phonecall" to phoneCall,
                                                "SecurityNumber" to securityNumber,
                                                "LectureCode" to lectureCode,
                                                "LectureStatus" to lectureStatus
                                            )
                                            employeeDB.set(setData)
                                        }

                                    } else {
                                        lectureCode.putAll(
                                            mapOf(
                                                "장애인인식개선" to code1,
                                                "직장내성희롱" to code2,
                                                "산업안전법" to code3,
                                                "개인정보보호" to code4
                                            )
                                        )
                                        val setData = mutableMapOf(
                                            "Department" to department,
                                            "ID" to ID,
                                            "Position" to position,
                                            "Phonecall" to phoneCall,
                                            "SecurityNumber" to securityNumber,
                                            "LectureCode" to lectureCode,
                                            "LectureStatus" to lectureStatus
                                        )
                                        employeeDB.set(setData)
                                    }
                                }


                                showDialog = false
                            }) {
                            Text("등록")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                showDialog = false
                            }) {
                            Text("취소")
                        }
                    }
                )
            }
            OfficerRegistScreen(paddingValues, companyCode)

        }
    )
}

@Composable
fun lectureRadioButton(label: String): Boolean {
    var isSelected by remember {
        mutableStateOf(false)
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = label)

        RadioButton(selected = isSelected, onClick = { isSelected = !isSelected })
    }
    return isSelected
}


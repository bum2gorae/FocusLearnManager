package com.example.focuslearnmanager

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore@Composable
fun OfficerRegistScreen(
    paddingValues: PaddingValues,
    companyCode: String,
    refreshTrigger: Boolean,
    onRefreshTrigger: () -> Unit,
    onNavRefreshTrigger: () -> Unit
) {


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding()
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
                            .size(width = 200.dp, height = 45.dp)
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
                        ),
                        textStyle = TextStyle(
                            fontSize = 12.sp
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
                "사번",
                "이름",
                "부서",
                "직책",
                "아이디",
                "생년월일",
                fontWeight = FontWeight.SemiBold
            )

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                val showDialog = remember { mutableStateOf(false) }
                val selectedData = remember { mutableStateOf<Map<String, Any>?>(null) }
                var dataMap by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }

                LaunchedEffect(companyCode, refreshTrigger) {
                    fetchData(companyCode) { fetchedData ->
                        dataMap = fetchedData
                    }
                }
                dataMap.forEach { datamap ->
                    TableRow(
                        datamap.get("CompanyNumber").toString(),
                        datamap.get("Name").toString(),
                        datamap.get("Department").toString(),
                        datamap.get("Position").toString(),
                        datamap.get("ID").toString(),
                        datamap.get("SecurityNumber").toString()
                            .substring(0, 6),
                        modifier = Modifier.clickable {
                            selectedData.value = datamap
                            showDialog.value = true
                        }
                    )
                }
                var department by remember { mutableStateOf("") }
                var companyNumber by remember { mutableStateOf("") }
                var ID by remember { mutableStateOf("") }
                var name by remember { mutableStateOf("") }
                var phoneCall by remember { mutableStateOf("") }
                var position by remember { mutableStateOf("") }
                var securityNumber by remember { mutableStateOf("") }
                val lectureCode = remember { mutableMapOf<String, Boolean>() }
                val lectureStatus = remember {mutableMapOf<String, Boolean>()}
                var code1 by remember { mutableStateOf(false) }
                var code2 by remember { mutableStateOf(false) }
                var code3 by remember { mutableStateOf(false) }
                var code4 by remember { mutableStateOf(false) }
                if (showDialog.value) {
                    AlertDialog(
                        containerColor = Color.White,
                        onDismissRequest = {
                            showDialog.value = false
                        },

                        confirmButton = {
                            Button(onClick = {
                                val employeeDB = Firebase.firestore
                                    .collection("Company")
                                    .document(companyCode)
                                    .collection("Employee")
                                    .document(name)
                                lectureCode.putAll(
                                    mapOf(
                                        "장애인인식개선" to code1,
                                        "직장내성희롱" to code2,
                                        "산업안전법" to code3,
                                        "개인정보보호" to code4
                                    )
                                )
                                val tempLectureStatus =
                                    selectedData.value?.get("LectureStatus") as? Map<*, *>
                                tempLectureStatus?.let {
                                    lectureStatus["장애인인식개선"] = tempLectureStatus["장애인인식개선"] as Boolean
                                    lectureStatus["직장내성희롱"] = tempLectureStatus["직장내성희롱"] as Boolean
                                    lectureStatus["산업안전법"] = tempLectureStatus["산업안전법"] as Boolean
                                    lectureStatus["개인정보보호"] = tempLectureStatus["개인정보보호"] as Boolean
                                }
                                val setData = mutableMapOf(
                                    "Department" to department,
                                    "CompanyNumber" to companyNumber,
                                    "ID" to ID,
                                    "Position" to position,
                                    "Phonecall" to phoneCall,
                                    "SecurityNumber" to securityNumber,
                                    "LectureCode" to lectureCode,
                                    "LectureStatus" to lectureStatus
                                )
                                employeeDB.set(setData)
                                onRefreshTrigger()
                                onNavRefreshTrigger()
                                showDialog.value = false
                            }) {
                                Text("수정")
                            }
                            Button(
                                onClick = {
                                    Firebase.firestore
                                        .collection("Company")
                                        .document(companyCode)
                                        .collection("Employee")
                                        .document(name)
                                        .delete()

                                    showDialog.value = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Red
                                )
                            ) {
                                Text("삭제")
                            }
                        },
                        dismissButton = {
                            Button(onClick = { showDialog.value = false }) {
                                Text("취소")
                            }
                        },
                        title = {
                            Text(text = "Data Details")
                        },
                        text = {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            )
                            {
                                var code1default = false
                                var code2default = false
                                var code3default = false
                                var code4default = false
                                department = selectedData.value?.get("Department").toString()
                                companyNumber = selectedData.value?.get("CompanyNumber").toString()
                                ID = selectedData.value?.get("ID").toString()
                                name = selectedData.value?.get("Name").toString()
                                phoneCall = selectedData.value?.get("Phonecall").toString()
                                position = selectedData.value?.get("Position").toString()
                                securityNumber =
                                    selectedData.value?.get("SecurityNumber").toString()
                                val tempLectureCode =
                                    selectedData.value?.get("LectureCode") as? Map<*, *>
                                tempLectureCode?.let {
                                    code1default = tempLectureCode["장애인인식개선"] as Boolean
                                    code2default = tempLectureCode["직장내성희롱"] as Boolean
                                    code3default = tempLectureCode["산업안전법"] as Boolean
                                    code4default = tempLectureCode["개인정보보호"] as Boolean
                                }

                                TextInputField(
                                    label = "사번",
                                    value = companyNumber,
                                    onValueChange = { companyNumber = it },
                                    modifier = Modifier.height(58.dp),
                                    fontsize = 12.sp
                                )
                                TextInputField(
                                    label = "이름",
                                    value = name,
                                    onValueChange = { name = it },
                                    modifier = Modifier.height(58.dp),
                                    fontsize = 12.sp
                                )
                                TextInputField(
                                    label = "부서",
                                    value = department,
                                    onValueChange = { department = it },
                                    modifier = Modifier.height(58.dp),
                                    fontsize = 12.sp
                                )
                                TextInputField(
                                    label = "직책",
                                    value = position,
                                    onValueChange = { position = it },
                                    modifier = Modifier.height(58.dp),
                                    fontsize = 12.sp
                                )
                                TextInputField(
                                    label = "전화번호",
                                    value = phoneCall,
                                    onValueChange = { phoneCall = it },
                                    modifier = Modifier.height(58.dp),
                                    fontsize = 12.sp
                                )
                                TextInputField(
                                    label = "주민번호",
                                    value = securityNumber,
                                    onValueChange = { securityNumber = it },
                                    modifier = Modifier.height(58.dp),
                                    fontsize = 12.sp
                                )
                                TextInputField(
                                    label = "아이디",
                                    value = ID,
                                    onValueChange = { ID = it },
                                    modifier = Modifier.height(58.dp),
                                    fontsize = 12.sp
                                )
                                Row {
                                    code1 = lectureRadioButton(label = "장애인인식개선", code1default)
                                    code2 = lectureRadioButton(label = "직장내성희롱", code2default)
                                }
                                Row {
                                    code3 = lectureRadioButton(label = "산업안전법", code3default)
                                    code4 = lectureRadioButton(label = "개인정보보호", code4default)
                                }
                            }
                        }
                    )
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
            data["Name"] = it.id
            dataMap.add(data)
        }
        onDataFetched(dataMap)
    }
}


@Composable
fun OfficerRegistScaffoldScreen(companyCode: String,
                                onNavRefreshTrigger: () -> Unit,
                                onNavRefreshTrigger2: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var refreshTrigger by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.Transparent,
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
            var companyNumber by remember { mutableStateOf("") }
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
                    containerColor = Color.White,
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = Color.White),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            TextInputField(
                                label = "사번",
                                value = companyNumber,
                                onValueChange = { companyNumber = it },
                                modifier = Modifier.height(58.dp),
                                fontsize = 12.sp
                            )
                            TextInputField(
                                label = "이름",
                                value = name,
                                onValueChange = { name = it },
                                modifier = Modifier.height(58.dp),
                                fontsize = 12.sp
                            )
                            TextInputField(
                                label = "부서",
                                value = department,
                                onValueChange = { department = it },
                                modifier = Modifier.height(58.dp),
                                fontsize = 12.sp
                            )
                            TextInputField(
                                label = "직책",
                                value = position,
                                onValueChange = { position = it },
                                modifier = Modifier.height(58.dp),
                                fontsize = 12.sp
                            )
                            TextInputField(
                                label = "전화번호",
                                value = phoneCall,
                                onValueChange = { phoneCall = it },
                                modifier = Modifier.height(58.dp),
                                fontsize = 12.sp
                            )
                            TextInputField(
                                label = "주민번호",
                                value = securityNumber,
                                onValueChange = { securityNumber = it },
                                modifier = Modifier.height(58.dp),
                                fontsize = 12.sp
                            )
                            TextInputField(
                                label = "아이디",
                                value = ID,
                                onValueChange = { ID = it },
                                modifier = Modifier.height(58.dp),
                                fontsize = 12.sp
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
                                                "CompanyNumber" to companyNumber,
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
                                            "CompanyNumber" to companyNumber,
                                            "ID" to ID,
                                            "Position" to position,
                                            "Phonecall" to phoneCall,
                                            "SecurityNumber" to securityNumber,
                                            "LectureCode" to lectureCode,
                                            "LectureStatus" to lectureStatus
                                        )
                                        employeeDB.set(setData)
                                        refreshTrigger = !refreshTrigger
                                        onNavRefreshTrigger()
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
            OfficerRegistScreen(paddingValues, companyCode, refreshTrigger,
                onRefreshTrigger = { refreshTrigger = !refreshTrigger },
                { onNavRefreshTrigger2() })
        }
    )
}

@Composable
fun lectureRadioButton(label: String, default:Boolean = false): Boolean {
    var isSelected by remember {
        mutableStateOf(default)
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = label)

        RadioButton(selected = isSelected, onClick = { isSelected = !isSelected })
    }
    return isSelected
}


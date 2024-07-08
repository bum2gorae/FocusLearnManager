package com.example.focuslearnmanager

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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


@Composable
fun StatusListScreen(companyCode: String) {
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
                text = "직원 교육 현황",
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
            "강의명",
            "진행상태",
            fontWeight = FontWeight.SemiBold
        )

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            var dataMap by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
            LaunchedEffect(companyCode) {
                fetchData(companyCode) { fetchedData ->
                    dataMap = fetchedData
                }
            }
            dataMap.forEach { datamap ->
                val lectureCodeMap =
                    datamap["LectureCode"] as? Map<String, Boolean> ?: emptyMap()
                val lectureStatusMap =
                    datamap["LectureStatus"] as? Map<String, Boolean> ?: emptyMap()
                lectureCodeMap.keys.forEach { key ->
                    val status = lectureCodeMap[key]
                    if (status==true) {
                        TableRow(
                            datamap.get("Department").toString(),
                            datamap.get("CompanyNumber").toString(),
                            datamap.get("Department").toString(),
                            datamap.get("Name").toString(),
                            key,
                            if (lectureStatusMap[key]==true) "수료" else "진행중",
                            color = if (lectureStatusMap[key]==true) Color.Red else Color.Blue
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun TableCell(
    label: String,
    modifier: Modifier,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = Color.Black
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            label, fontSize = 14.sp, fontWeight = fontWeight,
            color = color
        )
    }
}


@Composable
fun TableRow(
    data1: String,
    data2: String,
    data3: String,
    data4: String,
    data5: String,
    data6: String,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = Color.Black,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        TableCell(
            label = data1, modifier = Modifier
                .weight(0.13f)
                .size(40.dp),
            fontWeight
        )
        TableCell(
            label = data2, modifier = Modifier
                .weight(0.13f)
                .size(40.dp),
            fontWeight
        )
        TableCell(
            label = data3, modifier = Modifier
                .weight(0.12f)
                .size(40.dp),
            fontWeight
        )
        TableCell(
            label = data4, modifier = Modifier
                .weight(0.12f)
                .size(40.dp),
            fontWeight
        )
        TableCell(
            label = data5, modifier = Modifier
                .weight(0.25f)
                .size(40.dp),
            fontWeight
        )
        TableCell(
            label = data6, modifier = Modifier
                .weight(0.15f)
                .size(40.dp),
            fontWeight,
            color = color
        )
    }
}

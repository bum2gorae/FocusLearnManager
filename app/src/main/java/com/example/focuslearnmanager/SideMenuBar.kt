package com.example.focuslearnmanager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


enum class FocusLearnManagerScreen() {
    start,
    StatusList,
    OfficerRegist,
    Report,
    Notice
}

@Composable
fun FocusLearnNavi(navController: NavHostController,
                   companyCode : String) {
    NavHost(navController = navController, startDestination = FocusLearnManagerScreen.start.name) {
        composable(FocusLearnManagerScreen.start.name) { MainScreen() }
        composable(FocusLearnManagerScreen.StatusList.name) { StatusListScreen(companyCode) }
        composable(FocusLearnManagerScreen.OfficerRegist.name) { OfficerRegistScaffoldScreen(companyCode) }
        // Add more destinations similarly.
    }
}


class SideMenuBar {
    @Composable
    fun DrawerContents(
        scope: CoroutineScope, drawerState: DrawerState, navController: NavController
    ) {
        ModalDrawerSheet(
            modifier = Modifier
                .padding(top = 45.dp, bottom = 30.dp)
                .fillMaxHeight(0.75f)
                .fillMaxWidth(0.75f)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(80.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(0.8f)
                    ) {
                        Text(text = "FOCUS LEARN", color = Color.Blue)
                        Text(text = "목록", fontWeight = FontWeight.Bold)
                    }
                    Box(
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.icon_menu
                            ),
                            contentDescription = "menu",
                            tint = Color.Black,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    scope.launch {
                                        drawerState.apply { //navigation drawer open
                                            if (isClosed) open() else close()
                                        }
                                    }
                                }
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    thickness = 1.dp,
                    color = Color.Gray
                )
                SideBarContent(navController = navController, scope = scope, drawerState = drawerState, label = "직원 아이디 생성", FocusLearnManagerScreen.OfficerRegist.name)
                SideBarContent(navController = navController, scope = scope, drawerState = drawerState, label = "직원 교육 현황", FocusLearnManagerScreen.StatusList.name)
                SideBarContent(navController = navController, scope = scope, drawerState = drawerState, label = "교육 이수 통계 및 보고서", FocusLearnManagerScreen.StatusList.name)

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(top = 10.dp),
                    thickness = 1.dp,
                    color = Color.Gray
                )
                SideBarContent(navController = navController, scope = scope, drawerState = drawerState, label = "공지사항 / 알림", FocusLearnManagerScreen.StatusList.name)
            }
        }
    }

    @Composable
    fun MenuIconBox(scope: CoroutineScope, drawerState: DrawerState) {
        Box(modifier = Modifier
            .padding(10.dp)
            .size(40.dp)
            .clickable {

            }) {
            Icon(
                painter = painterResource(
                    id = R.drawable.icon_menu
                ),
                contentDescription = "menu",
                tint = Color.Black,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        scope.launch {
                            drawerState.apply { //navigation drawer open
                                if (isClosed) open() else close()
                            }
                        }
                    }
            )
        }
    }

    @Composable
    fun TopBar(scope: CoroutineScope, drawerState: DrawerState) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 70.dp, bottom = 10.dp, start = 15.dp, end = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SideMenuBar().MenuIconBox(scope, drawerState)
            Box(modifier = Modifier
                .padding(10.dp)
                .size(45.dp)
                .clickable {

                }) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.icon_person
                    ),
                    contentDescription = "person",
                    tint = Color.Magenta,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    @Composable
    fun SideBarContent(navController: NavController,
                       scope: CoroutineScope,
                       drawerState: DrawerState,
                       label : String,
                       destiny: String) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(50.dp)
                .padding(top = 15.dp)
        ) {
            var onStarClicked by remember {mutableStateOf(false)}
            Box(modifier = Modifier
                .size(35.dp)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        onStarClicked = !onStarClicked
                    })
                }) {
                Icon(
                    painter = painterResource(id = if (onStarClicked) {
                        R.drawable.icon_star
                    } else R.drawable.icon_starline),
                    contentDescription = "star",
                    tint = if (onStarClicked) {
                        colorResource(id = R.color.gold)
                    } else Color.Black
                )
            }
            Text(text = label,
                modifier = Modifier.clickable {
                    navController.navigate(destiny)
                    scope.launch {
                        drawerState.apply { //navigation drawer open
                            if (isClosed) open() else close()
                        }
                    }
                })
        }
    }
}
package com.example.focuslearnmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.focuslearnmanager.ui.theme.FocusLearnManagerTheme


class MainScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FocusLearnManagerTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val companyCode = intent.getStringExtra("companyCode")

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        SideMenuBar().DrawerContents(scope, drawerState, navController)
                    },
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        //배경 설정
                        Image(
                            painter = painterResource(id = R.drawable.focuslearnback),
                            contentDescription = "background",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        //contents
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = Color.Transparent)
                                .padding(top = 15.dp, start = 10.dp, end = 10.dp)
                        ) {
                            SideMenuBar().TopBar(scope = scope, drawerState = drawerState)
                            FocusLearnNavi(navController, companyCode!!)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
    }
}
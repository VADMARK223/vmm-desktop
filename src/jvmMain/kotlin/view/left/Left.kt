package view.left

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import common.UsersRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import view.common.Search
import view.window.Window
import view.window.WindowState
import view.window.WindowType

/**
 * @author Markitanov Vadim
 * @since 23.05.2022
 */
@Composable
fun Left() {
    val searchState = remember { mutableStateOf(TextFieldValue("")) }

    Box(modifier = Modifier.width(450.dp)) {
        val scope = rememberCoroutineScope()
        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopBar(scope, scaffoldState, searchState)
            },
            drawerBackgroundColor = Color(23, 33, 43),
            drawerContent = {
                DrawerView(scope, scaffoldState)
            },
        ) {
            Conversations(searchState)
        }
    }
}

@Composable
fun TopBar(scope: CoroutineScope, scaffoldState: ScaffoldState, searchState: MutableState<TextFieldValue>) {
    TopAppBar(
        title = {
            Search(searchState)
        },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }) {
                Icon(Icons.Filled.Menu, "Menu", tint = Color(69, 80, 91))
            }
        },
        backgroundColor = Color(23, 33, 43),
        contentColor = Color.White
    )
}

@Composable
fun DrawerView(scope: CoroutineScope, scaffoldState: ScaffoldState) {
    Column {
        Box(Modifier.padding(start = 12.dp, top = 12.dp)) {
            val size = 50.dp
            Box(
                Modifier
                    .size(size)
                    .clip(RoundedCornerShape(size))
            ) {
                Image(
                    painter = painterResource("vadmark.jpg"),
                    contentDescription = "",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.width(size),
                )
            }
        }

        if (UsersRepo.current().value != null) {
            Text(UsersRepo.current().value?.name as String, color = Color.White, modifier = Modifier.padding(12.dp))
            Divider()
        }

        Spacer(Modifier.height(12.dp))

        Box(Modifier.fillMaxWidth().clickable {
            scope.launch {
                scaffoldState.drawerState.close()
            }
            Window.state.value = WindowState(WindowType.NEW_CONVERSATION)
        }) {
            Text("New conversation", color = Color.White, modifier = Modifier.padding(12.dp))
        }

        Box(Modifier.fillMaxWidth().clickable {
            scope.launch {
                scaffoldState.drawerState.close()
            }
            Window.state.value = WindowState(WindowType.NEW_PRIVATE_CONVERSATION)
        }) {
            Text("New Private Conversation", color = Color.White, modifier = Modifier.padding(12.dp))
        }

        Spacer(Modifier.weight(1f))

        Text("Version 0.0.1", color = Color.White, modifier = Modifier.padding(12.dp))
    }
}
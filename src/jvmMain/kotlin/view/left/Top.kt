package view.left

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import repository.UsersRepo
import view.common.Search
import view.window.Window
import view.window.WindowState
import view.window.WindowType

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
@Composable
fun Top(usersRepo: UsersRepo, searchState: MutableState<TextFieldValue>) {
    val expanded = remember { mutableStateOf(false) }

    Box(Modifier.background(Color(23, 33, 43))) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    expanded.value = true
                }
            ) {
                Icon(
                    Icons.Filled.Menu, contentDescription = "Menu", tint = Color(69, 80, 91)
                )
            }

            Search(searchState)
        }

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            }
        ) {

            DropdownMenuItem(onClick = {
                expanded.value = false
                Window.state.value = WindowState(WindowType.NEW_CONVERSATION)
            }) {
                Text(text = "New Conversation")
            }

            DropdownMenuItem(onClick = {
                expanded.value = false
                Window.state.value = WindowState(WindowType.NEW_PRIVATE_CONVERSATION)
            }) {
                Text(text = "New Private Conversation")
            }

            DropdownMenuItem(onClick = {
                Window.state.value = WindowState(WindowType.NEW_PRIVATE_CONVERSATION)
                expanded.value = false
            }) {
                Text(text = "User list")
            }

            DropdownMenuItem(onClick = {
                println("Click ava")
            }) {
                val vadmarkModifier = Modifier.width(50.dp).clip(RoundedCornerShape(25.dp))
                Image(
                    painter = painterResource("vadmark.jpg"),
                    contentDescription = "",
                    modifier = vadmarkModifier,
                    contentScale = ContentScale.FillWidth
                )
                Spacer(Modifier.width(5.dp))
                Column {
                    Text("Id: ${usersRepo.current().value?.id}")
                    Text("First name: ${usersRepo.current().value?.firstName}")
                    Text("Last name: ${usersRepo.current().value?.lastName}")
                }
            }
        }
    }
}
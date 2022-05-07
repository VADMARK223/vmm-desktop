package view.left

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import repository.ConversationsRepo
import repository.UsersRepo
import view.common.ContactState

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
@Composable
fun Top(contactState: MutableState<ContactState>, repo: ConversationsRepo, usersRepo: UsersRepo) {
    val mainOutput = remember { mutableStateOf(TextFieldValue("")) }
    val expanded = remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = mainOutput.value,
            onValueChange = {
                mainOutput.value = it
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search") },
            maxLines = 1,
            shape = RoundedCornerShape(0.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color(23, 33, 43),
                textColor = Color.White
            ),
            leadingIcon = {
                IconButton(
                    onClick = {
                        expanded.value = true
                    }
                ) {
                    Icon(Icons.Filled.Menu, contentDescription = "Menu")
                }
            }
        )

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            }
        ) {
            DropdownMenuItem(onClick = {
                repo.create()
                expanded.value = false
            }) {
                Text(text = "New Conversation")
            }
            DropdownMenuItem(onClick = {
                contactState.value = ContactState.CREATE
                expanded.value = false
            }) {
                Text(text = "Add user")
            }

            DropdownMenuItem (onClick = {
                println("Click ava")
            }){
                val vadmarkModifier = Modifier.width(50.dp).clip(RoundedCornerShape(25.dp))
                Image(
                    painter = painterResource("vadmark.jpg"),
                    contentDescription = "",
                    modifier = vadmarkModifier,
                    contentScale = ContentScale.FillWidth
                )
                Spacer(Modifier.width(5.dp))
                Text("Id: ${usersRepo.current().value?.id}")
            }
        }
    }
}
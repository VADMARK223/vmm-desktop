package view.right

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import repository.UsersRepo

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
@Composable
fun Top(repo: UsersRepo) {
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
                repo.addUser()
                expanded.value = false
            }) {
                Text(text = "Add user")
            }
        }
    }
}
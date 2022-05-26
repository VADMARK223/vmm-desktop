package view.window

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import common.User
import common.UsersRepo
import service.generateContactCredentials
import view.common.ContactState
import kotlin.random.Random

/**
 * @author Markitanov Vadim
 * @since 26.05.2022
 */
@Composable
fun EditContact(user: User) {
    val contactState = mutableStateOf(ContactState.EDIT)
    Column (Modifier.height(300.dp)) {
        Text(
            text = when (contactState.value) {
                ContactState.CREATE -> "New Contact"
                ContactState.EDIT -> "Edit contact name"
                else -> ""
            },
            color = Color.White
        )

        val firstName = remember {
            mutableStateOf(
                TextFieldValue(
                    if (contactState.value == ContactState.EDIT)
                        user.firstName
                    //UsersRepo.selected.value?.firstName as String
                    else
                        if (generateContactCredentials()) "First#" + Random.nextInt(10) else ""
                )
            )
        }
        val firstNameEmpty = remember { mutableStateOf(false) }
        TextField(
            value = firstName.value,
            onValueChange = {
                firstName.value = it
            },
            label = {
                Text("First name")
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(23, 33, 43),
                textColor = Color.White
            ),
            isError = firstNameEmpty.value
        )

        val lastName = remember {
            mutableStateOf(
                TextFieldValue(
                    if (contactState.value == ContactState.EDIT)
                        user.lastName
                    else
                        if (generateContactCredentials()) "Last#" + Random.nextInt(10) else ""
                )
            )
        }
        val lastNameEmpty = remember { mutableStateOf(false) }
        TextField(
            value = lastName.value,
            onValueChange = {
                lastName.value = it
            },
            label = {
                Text("Last name")
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(23, 33, 43),
                textColor = Color.White
            ),
            isError = lastNameEmpty.value
        )

        val phoneNumber = remember { mutableStateOf(TextFieldValue("")) }
        TextField(
            value = phoneNumber.value,
            onValueChange = {
                phoneNumber.value = it
            },
            label = {
                Text("Phone Number")
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(23, 33, 43),
                textColor = Color.White
            )
        )

        Spacer(Modifier.weight(1F))

        Row(
            modifier = Modifier.align(Alignment.End),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    Window.show(WindowType.VIEW_PROFILE, user)
                },
            ) {
                Text("CANCEL")
            }
            Button(
                onClick = {
                    if (contactState.value == ContactState.CREATE) {
                        firstNameEmpty.value = false
                        lastNameEmpty.value = false
                        if (firstName.value.text.isEmpty()) {
                            firstNameEmpty.value = true
                        } else if (lastName.value.text.isEmpty()) {
                            lastNameEmpty.value = true
                        } else {
//                                    UsersRepo.addUser(firstName.value.text, lastName.value.text)
                            contactState.value = ContactState.HIDE
                        }
                    } else if (contactState.value == ContactState.EDIT) {
                        user.firstName = firstName.value.text
                        user.lastName = lastName.value.text
                        UsersRepo.update(user)
                        Window.show(WindowType.VIEW_PROFILE, user)
                    }
                },
            ) {
                Text(
                    text = when (contactState.value) {
                        ContactState.CREATE -> "CREATE"
                        ContactState.EDIT -> "DONE"
                        else -> ""
                    }
                )
            }
        }
    }
}
package view.right.info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import db.Conversation
import kotlinx.datetime.toJavaLocalDateTime
import view.common.ContactState
import java.time.format.DateTimeFormatter

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
@Composable
fun Info(conversation: Conversation?, contactState: MutableState<ContactState>) {
    val expanded = remember { mutableStateOf(false) }
    val menuItems = InfoAction.values()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(23, 33, 43))
            .padding(4.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(
                text = conversation?.name ?: "",
                style = MaterialTheme.typography.h6,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White
            )

            var infoText = ""
            if (conversation != null) {
                val formatter = DateTimeFormatter
                    .ofPattern("hh:mm:ss")
                infoText =
                    "Creation time: ${formatter.format(conversation.createTime.toJavaLocalDateTime())} Owner: ${conversation.ownerId}"
                if (!conversation.isPrivate) {
                    infoText += " Members: ${conversation.membersCount}"
                }
            }

            Text(
                text = infoText,
                style = MaterialTheme.typography.overline,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Gray
            )
        }

        IconButton(
            modifier = Modifier.align(Alignment.CenterEnd),
            onClick = {
                expanded.value = true
            }
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                tint = Color.Gray
            )
        }
        Box(modifier = Modifier.align(Alignment.BottomEnd)) {
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = {
                    expanded.value = false
                }
            ) {
                menuItems.forEach {
                    DropdownMenuItem(onClick = {
                        when (it) {
                            InfoAction.VIEW_PROFILE -> {
                                println("View pro")
                            }

                            InfoAction.EDIT_CONTACT -> {
                                contactState.value = ContactState.EDIT
                            }
                        }

                        expanded.value = false
                    }) {
                        Text(text = it.text)
                    }
                }
            }
        }
    }
}
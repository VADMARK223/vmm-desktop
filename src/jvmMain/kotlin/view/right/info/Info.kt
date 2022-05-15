package view.right.info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.datetime.toJavaLocalDateTime
import repository.ConversationsRepo
import java.time.format.DateTimeFormatter

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
@Composable
fun Info(conversationsRepo: ConversationsRepo) {
    println("INFO REDRAW")
    val expanded = remember { mutableStateOf(false) }
    val menuItems = InfoAction.values()

    val conversation = conversationsRepo.selected()

    if (conversation.value == null) {
        return
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(23, 33, 43))
            .padding(4.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(
                text = conversation.value?.name ?: "",
                style = MaterialTheme.typography.h6,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White
            )

            var infoText: String
            val formatter = DateTimeFormatter
                .ofPattern("hh:mm:ss")
            infoText =
                if (conversation.value == null) ""
                else "Creation time: ${formatter.format(conversation.value?.createTime?.toJavaLocalDateTime())} Owner: ${conversation.value?.ownerId}"

            if (conversation.value != null && conversation.value?.companionId == null) {
                infoText += " Members: ${conversation.value?.membersCount}"
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
                                println("Edit contact.")
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
package view.item.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import db.User
import repository.UsersRepo
import view.common.ContactState
import kotlin.random.Random

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
@Composable
fun UserItem(user: User, modifier: Modifier) {
    val expanded = remember { mutableStateOf(false) }
    val menuItems = UserAction.values()
    val bgColor = remember { mutableStateOf(Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))) }

    Box(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val size = 30.dp
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(size))
            ) {
                val avaText = user.lastName.first().toString() + user.firstName.first().toString()
                Text(
                    text = avaText,
                    color = Color.White,
                    modifier = Modifier
                        .background(
                            color = bgColor.value
                        )
                        .padding(size / 2)
                )
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = user.id.value.toString() + " " + user.lastName + " " + user.firstName,
                    style = MaterialTheme.typography.h6,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )
            }

        }
        IconButton(
            modifier = Modifier.align(Alignment.CenterEnd),
            onClick = { expanded.value = true }
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "image",
                tint = Color.White
            )
        }

        DropdownMenu(
            expanded = expanded.value,
            offset = DpOffset((-40).dp, (-10).dp),
            onDismissRequest = {
                expanded.value = false
            }
        ) {
            menuItems.forEach {
                DropdownMenuItem(onClick = {
                    when (it) {
                        UserAction.REMOVE -> {
                            UsersRepo.remove(user)
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
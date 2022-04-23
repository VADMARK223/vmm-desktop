package view.item.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import db.User

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
@Composable
fun UserItem(user: User, modifier: Modifier) {
    val expanded = remember { mutableStateOf(false) }
    val menuItems = UserAction.values()

    Box(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val vadmarkModifier = Modifier.width(50.dp).clip(RoundedCornerShape(25.dp))
            Image(
                painter = painterResource("vadmark.jpg"),//painterResource("vadmark.jpg"),
                contentDescription = "",
                modifier = vadmarkModifier,
                contentScale = ContentScale.FillWidth
            )

            Column {
                Text(
                    text = user.lastName + " " + user.firstName,
                    style = MaterialTheme.typography.h6,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )
                /*Text(
                    text = "${Random.nextInt(1, 10)} minutes ago",
                    style = MaterialTheme.typography.overline,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray
                )*/
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
                        UserAction.EDIT -> println("Edit user: " + user.id)
                        UserAction.REMOVE -> println("Remove user: " + user.id)
                    }

                    expanded.value = false
                }) {
                    Text(text = it.text)
                }
            }
        }
    }
}
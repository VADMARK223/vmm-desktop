package view.window

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import common.User
import view.common.Avatar
import java.awt.Cursor

/**
 * @author Markitanov Vadim
 * @since 20.05.2022
 */
@Composable
fun ViewProfile(user: User) {
    Column(Modifier.width(400.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "User Info",
                color = Color.White,
                style = MaterialTheme.typography.h6
            )

            Spacer(Modifier.weight(1F))

            IconButton(
                modifier = Modifier.pointerHoverIcon(PointerIcon(Cursor(Cursor.HAND_CURSOR))),
                onClick = {
                    Window.show(WindowType.EDIT_CONTACT, user)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }

            IconButton(
                modifier = Modifier.pointerHoverIcon(PointerIcon(Cursor(Cursor.HAND_CURSOR))),
                onClick = {
                    Window.hide()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
        Spacer(Modifier.height(12.dp))

        Divider()

        Spacer(Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(user.name)
            Spacer(Modifier.width(12.dp))
            Text(
                text = user.name,
                color = Color.White,
                style = MaterialTheme.typography.h6
            )
        }
    }
}
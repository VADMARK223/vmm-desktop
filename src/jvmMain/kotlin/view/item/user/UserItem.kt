package view.item.user

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import common.User
import view.common.Avatar

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
@Composable
fun UserItem(user: User, modifier: Modifier) {
    Box(modifier) {
        Row(
            modifier = Modifier
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Avatar(user.name, mutableStateOf(user.online).value, user)

            Column {
                Text(
                    text = user.id.toString() + " " + user.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )
            }

        }
    }
}
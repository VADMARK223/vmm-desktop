package view.right

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import repository.UsersRepo
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
@Composable
fun UserInfo() {
    val selectedUser = UsersRepo.selected.value

    Box(
        modifier = Modifier
            .background(Color(23, 33, 43))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(Color(23, 33, 43)),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
//                val minutesAgoText =
//                    if (selectedUser.minutesAgo == 0) "last seen recently" else "${selectedUser.minutesAgo} minutes ago (${selectedUser.activityTime})"
//                val minutesAgoText = "Last activity: ${selectedUser?.activityTime}"
                val pattern = "dd.MM.yyyy"
                val formatter = DateTimeFormatter.ofPattern(pattern)
                    .withZone(ZoneId.systemDefault())
                val createTime = "Create time: " + formatter.format(selectedUser?.activityTime)
                Text(
                    text = "${selectedUser?.lastName} ${selectedUser?.firstName}",
                    style = MaterialTheme.typography.h6,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )
                Text(
                    text = createTime,
                    style = MaterialTheme.typography.overline,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray
                )
            }
        }
    }
}
package view.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import data.Message
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
@Composable
fun MessageItem(message: Message) {
    Column(
        modifier = Modifier
            .background(
                color = when (message.isMy) {
                    true -> Color(43, 82, 120)
                    else -> Color(24, 37, 51)
                },
                shape = RoundedCornerShape(5.dp)
            )
            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
    ) {
        Text(text = message.text, color = Color.White)
        Row(
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(Date()),
                style = MaterialTheme.typography.overline,
                color = Color.Gray
            )
        }
    }
}
package view.left

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import db.Conversation
import kotlin.random.Random

/**
 * @author Markitanov Vadim
 * @since 29.04.2022
 */
@Composable
fun ConversationItem(conversation: Conversation, modifier: Modifier) {
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
//                val avaText = user.firstName.first().toString() + user.lastName.first().toString()
                val avaText = conversation.name.first().toString()
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
                    text = conversation.id.toString() + " " + conversation.name,
                    style = MaterialTheme.typography.h6,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )
            }

        }
    }
}
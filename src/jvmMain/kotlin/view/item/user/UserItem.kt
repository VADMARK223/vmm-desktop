package view.item.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import model.User
import kotlin.random.Random

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
@Composable
fun UserItem(user: User, modifier: Modifier) {
    val bgColor = remember { mutableStateOf(Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))) }

    Box(modifier) {
        Row(
            modifier = Modifier
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val size = 30.dp
            Box{
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(size))
                ) {
                    val avaText = user.firstName.first().toString() + user.lastName.first().toString()
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

                if (user.online) {
                    Box(
                        modifier = Modifier.size(15.dp).clip(RoundedCornerShape(100.dp)).align(Alignment.BottomEnd)
                            .background(Color.Green)
                    )
                }
            }

            Column {
                Text(
                    text = user.id.toString() + " " + user.firstName + " " + user.lastName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )
            }

        }
    }
}
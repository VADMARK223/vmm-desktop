package view.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random

/**
 * @author Markitanov Vadim
 * @since 15.05.2022
 */
@Composable
fun Avatar(text:String, online:Boolean) {
    val bgColor = remember { mutableStateOf(Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))) }

    val size = 30.dp
    Box{
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(size))
        ) {
            Text(
                text = text,
                color = Color.White,
                modifier = Modifier
                    .background(
                        color = bgColor.value
                    )
                    .padding(size / 2)
            )
        }

        if (online) {
            Box(
                modifier = Modifier.size(15.dp).clip(RoundedCornerShape(100.dp)).align(Alignment.BottomEnd)
                    .background(Color.Green)
            )
        }
    }

//    Text(
//        text = text,
//        color = Color.White,
//        modifier = Modifier
//            .background(
//                color = bgColor.value
//            )
//            .padding(size / 2)
//    )
}
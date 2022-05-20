package view.window

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * @author Markitanov Vadim
 * @since 20.05.2022
 */
@Composable
fun ViewGroupInfo() {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.5F))
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                Window.hide()
            }
    ) {
        Box(
            modifier = Modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {}
                .align(Alignment.Center)
                .clip(RoundedCornerShape(5.dp))
                .background(Color(23, 33, 43))
                .padding(25.dp)

        ) {
            Text("VIEW GROUP INFO")
        }
    }
}
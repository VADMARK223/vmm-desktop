package view.right

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

/**
 * @author Markitanov Vadim
 * @since 28.05.2022
 */
//@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditMessageInfo() {
    val oldMessage = remember { InputMessageState.textOutput.value.text }
    Row(
        modifier = Modifier.background(Color(23, 33, 43)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(12.dp))
        Icon(
            Icons.Default.Edit,
            contentDescription = "Edit",
            tint = Color(82, 136, 193)
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text("Edit message", color = Color(82, 136, 193))
            Text(oldMessage, color = Color.White)
        }

        Spacer(Modifier.weight(1F))
//        CompositionLocalProvider(
//            LocalMinimumTouchTargetEnforcement provides false
//        ) {
        IconButton(
            onClick = {
                InputMessageState.editMessage.value = null
                InputMessageState.textOutput.value = TextFieldValue("")
            }) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.Gray
            )
        }
//        }
    }

}
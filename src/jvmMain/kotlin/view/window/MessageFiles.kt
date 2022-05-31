package view.window

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import common.MessagesRepo
import org.jetbrains.skia.Image

/**
 * @author Markitanov Vadim
 * @since 31.05.2022
 */
@Composable
fun MessageFiles(byteArray: ByteArray?) {
    if (byteArray == null) {
        throw RuntimeException("File is null.")
    }
    Column(Modifier.height(300.dp)) {
        Text(
            text = "1 files selected",
            color = Color.White
        )

        Image(
            modifier = Modifier.width(130.dp),
            bitmap = Image.makeFromEncoded(byteArray).toComposeImageBitmap(),
            contentDescription = "Image"
        )

        val comment = remember { mutableStateOf(TextFieldValue("")) }
        TextField(
            value = comment.value,
            onValueChange = {
                comment.value = it
            },
            label = {
                Text("Comment")
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(23, 33, 43),
                textColor = Color.White
            )
        )

        Spacer(Modifier.weight(1F))

        Row(
            modifier = Modifier.align(Alignment.End),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    Window.hide()
                },
            ) {
                Text("Cancel")
            }
            Button(
                onClick = {
                    println("Send")
                    if (comment.value.text.isNotEmpty()) {
                        MessagesRepo.put(comment.value.text, byteArray)
                    }
                    Window.hide()
                },
            ) {
                Text(
                    text = "Send"
                )
            }
        }
    }
}
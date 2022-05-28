package view.right

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import common.Message

object InputMessageState {
    val textOutput = mutableStateOf(TextFieldValue(""))
    val editMessage = mutableStateOf<Message?>(null)
}
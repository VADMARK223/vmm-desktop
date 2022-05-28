package view.right

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue

object InputMessageState {
    val textOutput = mutableStateOf(TextFieldValue(""))
    val editMode = mutableStateOf(false)
}
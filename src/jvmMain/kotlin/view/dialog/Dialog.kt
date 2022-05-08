package view.dialog

import androidx.compose.runtime.mutableStateOf

object Dialog {
    val state = mutableStateOf(DialogState(DialogType.HIDE))

    fun hide() {
        state.value = DialogState(DialogType.HIDE)
    }
}
package view.window

import androidx.compose.runtime.mutableStateOf

object Window {
//    val state = mutableStateOf(WindowState(WindowType.ADD_MEMBERS, "AA"))
    val state = mutableStateOf(WindowState(WindowType.HIDE))

    fun hide() {
        state.value = WindowState(WindowType.HIDE)
    }
}
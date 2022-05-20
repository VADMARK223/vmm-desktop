package view.window

import androidx.compose.runtime.mutableStateOf

object Window {
    val state = mutableStateOf(WindowState(WindowType.HIDE))

    fun setState(windowType: WindowType) {
        state.value = WindowState(windowType)
    }

    fun hide() {
        state.value = WindowState(WindowType.HIDE)
    }
}
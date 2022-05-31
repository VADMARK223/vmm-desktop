package view.window

import androidx.compose.runtime.mutableStateOf

object Window {
    val state = mutableStateOf(WindowState(WindowType.HIDE))

    fun show(type: WindowType, data: Any? = null) {
        state.value = WindowState(type, data)
    }

    fun hide() {
        state.value = WindowState(WindowType.HIDE)
    }
}
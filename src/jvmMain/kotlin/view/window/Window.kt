package view.window

import androidx.compose.runtime.mutableStateOf

object Window {
    val state = mutableStateOf(WindowState(WindowType.HIDE))

    fun setState(windowType: WindowType) {
        state.value = WindowState(windowType)
    }

    fun show(type: WindowType, data: Any? = null) {
        state.value = WindowState(type, data)
    }

    fun hide() {
        state.value = WindowState(WindowType.HIDE)
    }
}
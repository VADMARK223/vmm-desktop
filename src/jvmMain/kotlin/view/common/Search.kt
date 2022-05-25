package view.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import service.printDraw
import java.awt.Cursor
import java.awt.Cursor.HAND_CURSOR

/**
 * @author Markitanov Vadim
 * @since 08.05.2022
 */
@Composable
fun Search(state: MutableState<TextFieldValue>) {
    printDraw()
    TextField(
        value = state.value,
        onValueChange = {
            state.value = it
        },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search") },
        textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search icon"
            )
        },
        trailingIcon = {
            if (state.value != TextFieldValue()) {
                IconButton(
                    modifier = Modifier.pointerHoverIcon(PointerIcon(Cursor(HAND_CURSOR))),
                    onClick = {
                        state.value = TextFieldValue()
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier.padding(5.dp).size(24.dp)
                    )
                }
            }
        },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            backgroundColor = Color(23, 33, 43),
            textColor = Color.White
        )
    )
}
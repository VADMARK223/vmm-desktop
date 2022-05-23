package view.left

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import service.printDraw

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
@Composable
fun Left() {
    printDraw()
    Column(modifier = Modifier.width(450.dp)) {
        val searchState = remember { mutableStateOf(TextFieldValue("")) }
        Top(searchState)
        Conversations(searchState)
    }
}
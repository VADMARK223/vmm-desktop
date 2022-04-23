package view.left

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import db.User

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
@Composable
fun Left( modifier: Modifier, onUserClick: (User) -> Unit) {
    Column(modifier = Modifier.width(300.dp)) {
        Top()
        Users(
            modifier = modifier,
            onUserClick = onUserClick
        )
    }
}
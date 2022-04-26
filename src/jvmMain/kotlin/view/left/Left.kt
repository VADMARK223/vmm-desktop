package view.left

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import db.User
import view.common.ContactState

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
@Composable
fun Left(modifier: Modifier, onUserClick: (User) -> Unit, contactState: MutableState<ContactState>) {
    Column(modifier = Modifier.width(450.dp)) {
        Top(contactState)
        Users(
            modifier = modifier,
            onUserClick = onUserClick
        )
    }
}
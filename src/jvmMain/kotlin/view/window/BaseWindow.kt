package view.window

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import common.User

/**
 * @author Markitanov Vadim
 * @since 24.05.2022
 */
@Composable
fun BaseWindow() {
    if (Window.state.value.type == WindowType.HIDE) {
        return
    }

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.5F))
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                Window.hide()
            }
    ) {
        Box(
            modifier = Modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {}
                .align(Alignment.Center)
                .clip(RoundedCornerShape(5.dp))
                .background(Color(23, 33, 43))
                .padding(15.dp)

        ) {
            when (Window.state.value.type) {
                WindowType.SELECT_CURRENT_USER -> SelectCurrentUser()
                WindowType.NEW_CONVERSATION -> NewConversation()
                WindowType.NEW_PRIVATE_CONVERSATION -> NewPrivateConversation()
                WindowType.ADD_MEMBERS -> AddMembers(Window.state.value.data as String)
                WindowType.VIEW_PROFILE -> ViewProfile(Window.state.value.data as User)
                WindowType.VIEW_GROUP_INFO -> ViewGroupInfo()
                WindowType.EDIT_CONTACT -> EditContact(Window.state.value.data as User)
                WindowType.MESSAGE_FILES -> MessageFiles(Window.state.value.data as ByteArray)
                else -> Window.hide()
            }
        }
    }
}
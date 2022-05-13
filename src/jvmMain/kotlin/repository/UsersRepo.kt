package repository

import androidx.compose.runtime.MutableState
import model.User

/**
 * @author Markitanov Vadim
 * @since 07.05.2022
 */
interface UsersRepo {
    fun current(): MutableState<User?>
    fun all():List<User>
    fun addListener(listener: (Long) -> Unit)
    fun setCurrentUser(user: User)
    fun update(entity: User?)
}
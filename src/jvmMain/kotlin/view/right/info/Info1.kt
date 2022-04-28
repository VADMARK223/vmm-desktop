package view.right.info

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import db.TempUser

/**
 * @author Markitanov Vadim
 * @since 29.04.2022
 */
@Composable
fun Info1(user: TempUser?) {
    Text(text = "${user?.firstName} ${user?.lastName}")
}
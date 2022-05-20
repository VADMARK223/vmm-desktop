package view.right.info

/**
 * @author Markitanov Vadim
 * @since 25.04.2022
 */
enum class InfoAction(val text:String, val isConversation: Boolean) {
    VIEW_PROFILE("View profile", false),
    VIEW_GROUP_INFO("View group info", true)
//    EDIT_CONTACT("Edit contact")
}
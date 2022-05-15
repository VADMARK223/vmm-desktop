package view.left.item

/**
 * @author Markitanov Vadim
 * @since 01.05.2022
 */
enum class ConversationAction(val text: String, val isConversation: Boolean) {
    LEAVE_GROUP("Leave group", true),

    CLEAR_HISTORY("Clear history", false),
    LEAVE_CHAT("Leave chat", false)
}
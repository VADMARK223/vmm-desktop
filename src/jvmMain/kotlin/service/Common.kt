package service

/**
 * @author Markitanov Vadim
 * @since 24.04.2022
 */
fun generateContactCredentials(): Boolean {
    return true
}

fun requestDefaultUser(): Boolean {
    return true
}

fun printDraw() {
    if (needPrintDraw()) {
        val funName = Thread.currentThread().stackTrace[2].methodName
        println("${funName.uppercase()} DRAW.")
    }
}

private fun needPrintDraw(): Boolean {
    return false
}
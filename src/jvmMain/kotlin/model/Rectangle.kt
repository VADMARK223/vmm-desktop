package model

/**
 * @author Markitanov Vadim
 * @since 21.05.2022
 */
class Rectangle(private val w: Int, private val h: Int) {
    val area: Int
        get() = this.w * this.h
}
package dev.joaop.plugpag_flutter

import java.util.Stack

object PreviousTransactions {
    // ---------------------------------------------------------------------------------------------
    // Class attributes
    // ---------------------------------------------------------------------------------------------
    private val sStack = Stack<Array<String?>>()
    // ---------------------------------------------------------------------------------------------
    // Stacking
    // ---------------------------------------------------------------------------------------------
    /**
     * Pushes a value into the stack.
     *
     * @param value Value to be pushed.
     */
    fun push(value: Array<String?>) {
        sStack.push(value)
    }

    /**
     * Pops a value from the stack.
     *
     * @return Popped value.
     */
    fun pop(): Array<String?>? {
        var value: Array<String?>? = null
        if (sStack.size > 0) {
            value = sStack.pop()
        }
        return value
    }
}
package org.deblock.exercise

import org.junit.jupiter.api.Assertions.assertEquals
import java.math.BigDecimal

interface SpecBase {

    fun BigDecimal.assertEqualsTo(other: Number) {
        assertEquals(
            0,
            this.compareTo(other.toString().toBigDecimal()),
            "Expected $this to be equal to $other"
        )
    }

    fun withClue(message: String, block: () -> Unit) {
        println("\n=========== $message ===========")
        block()
        Thread.sleep(2)
        println()
    }
}
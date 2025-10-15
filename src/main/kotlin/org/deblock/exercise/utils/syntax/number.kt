package org.deblock.exercise.utils.syntax

import java.math.BigDecimal

object number {

    val Int.bd: BigDecimal get() = this.toBigDecimal()

    val Double.bd: BigDecimal get() = this.toBigDecimal()

}
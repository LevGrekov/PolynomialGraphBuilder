package drawing.math

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.ulp

fun Double.eq(other: Double, eps: Double) = abs(this - other) < eps

infix fun Double.eq(other: Double) = abs(this - other) < max(ulp, other.ulp) * 10.0

fun Double.neq(other: Double, eps: Double) = !this.eq(other, eps)

infix fun Double.neq(other: Double) = !this.eq(other)

infix fun Double.mod(other: Double) = this.rem(other)

infix fun Double.modEq(other: Double) = abs(this.rem(other)) < max(ulp, other.ulp) * 10.0

fun Double.modNeq(other: Double) = !this.modEq(other)
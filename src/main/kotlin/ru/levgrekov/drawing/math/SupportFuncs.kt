package ru.levgrekov.drawing.math

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.ulp

fun Double.eq(other: Double, eps: Double) = abs(this - other) < eps

infix fun Double.eq(other: Double) = abs(this - other) < max(ulp, other.ulp) * 10.0

fun Double.neq(other: Double, eps: Double) = !this.eq(other, eps)

infix fun Double.neq(other: Double) = !this.eq(other)

fun factorial(n: Int): Int {
    var result = 1
    for (i in 1..n) {
        result *= i
    }
    return result
}

fun calculateDerivativeAtPoint(
    f: (Double) -> Double,
    x: Double,
    h: Double = 1e-5): Double
        = (f(x + h) - f(x - h)) / (2 * h)


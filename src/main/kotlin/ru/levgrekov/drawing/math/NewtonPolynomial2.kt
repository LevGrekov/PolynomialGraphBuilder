package ru.levgrekov.polynomial.ru.levgrekov.polynomial.math

import Polynomial
import ru.levgrekov.drawing.math.eq

class NewtonPolynomial2(points: Map<Double,Double>?) : Polynomial() {
    private val n: Int
        get() = _points.size

    private val _points: MutableList<Pair<Double,Double>> = mutableListOf()
    val points: Map<Double,Double>
        get()= _points.toMap()

    private val lastFundPoly: Polynomial = Polynomial(1.0);
    init {
        points?.let {
            _coeffs[0] = 0.0
            this.addPoints(it.toList().toMutableList())
        }
    }
    private fun fundPoly(j: Int): Polynomial {
        if (j != 1) {
            lastFundPoly *= Polynomial(-_points[j-2].first, 1.0)
        }
        return lastFundPoly
    }
    private fun dividedDifference(k: Int): Double = (0..k).sumOf { j ->
        val multiplication = (0..k)
            .filter { i -> i != j }
            .fold(1.0) { acc, i -> acc * (_points[j].first - _points[i].first)}
        println("divDiffMult: $multiplication")
        _points[j].second / multiplication
    }
    fun addPoint(x: Double, f: Double) {
        if (_points.any { it.first eq x }) return
        _points.add(Pair(x, f))
        this += fundPoly(n) * dividedDifference(n - 1)
    }
    fun addPoints( pointsList: List<Pair<Double,Double>>) =
        pointsList.forEach{this.addPoint(it.first,it.second)}
}


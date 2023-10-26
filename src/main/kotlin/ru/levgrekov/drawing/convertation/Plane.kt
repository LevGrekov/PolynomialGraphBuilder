package ru.levgrekov.drawing.convertation

import kotlin.math.abs

data class Plane(
    var xMin: Double,
    var xMax: Double,
    var yMin: Double,
    var yMax: Double,

    var width: Float,
    var height: Float,
) {
    val deltaX = abs(xMax-xMin)
    val deltaY = abs(yMax-yMin)
    val xDen: Double
        get() = width/deltaX
    val yDen: Double
        get() = height/deltaY

    val x0 : Float
        get() = Converter.xCrt2Scr(0.0, this)
    val y0 : Float
        get() = Converter.yCrt2Scr(0.0, this)
}
package ru.levgrekov.drawing

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import ru.levgrekov.drawing.convertation.Converter
import ru.levgrekov.drawing.convertation.Plane
import ru.levgrekov.polynomial.ru.levgrekov.polynomial.math.NewtonPolynomial2

class PolynomialPainter(
    val polynomial: NewtonPolynomial2,
    private val derivative: Int,
    private val showPoints: Boolean,
    private val showDerivative: Boolean,
    var polynomialColor: Color,
    var derivativeColor: Color,
) : Painter {
    companion object{
        const val POINT_R = 5f
        const val POLYNOMIAL_WIDTH = 2f
        val POINT_COLOR = Color.Red
    }

    var plane: Plane? = null
    override fun paint(scope: DrawScope) {
        if(polynomial.points.isNotEmpty()){
            paintPolynomial(scope)
            if(showPoints){
                paintPoints(scope)
            }
        }
    }

    private fun paintPolynomial(scope: DrawScope){
        plane?.let{
            for (i in 0 until it.width.toInt()){

                val x = Converter.xScr2Crt(i.toFloat(),it)
                val xNext = Converter.xScr2Crt((i+1).toFloat(),it)
                val scrY = Converter.yCrt2Scr(polynomial(x),it).coerceIn(-it.height,2*it.height)
                val scrYNext = Converter.yCrt2Scr(polynomial(xNext),it).coerceIn(-it.height,2*it.height)

                scope.drawLine(
                    polynomialColor,
                    Offset(i.toFloat(),scrY ),
                    Offset((i+1).toFloat(),scrYNext ),
                    POLYNOMIAL_WIDTH)

                if(showDerivative){
                    val scrYDeriva =
                        Converter.yCrt2Scr(polynomial.derivative(derivative)(x),it).coerceIn(-it.height,2*it.height)
                    val scrYDerivaNext =
                        Converter.yCrt2Scr(polynomial.derivative(derivative)(xNext),it).coerceIn(-it.height,2*it.height)

                    scope.drawLine(
                        derivativeColor,
                        Offset(i.toFloat(),scrYDeriva ),
                        Offset((i+1).toFloat(),scrYDerivaNext ),
                        POLYNOMIAL_WIDTH)
                }
            }
        }
    }

    private fun paintPoints(scope: DrawScope){
        plane?.let { plane ->
            polynomial.points.forEach{
                val scrX = Converter.xCrt2Scr(it.key,plane)
                val scrY = Converter.yCrt2Scr(it.value,plane)

                if( scrX in POINT_R..plane.width - POINT_R &&
                    scrY in POINT_R..plane.height - POINT_R)

                scope.apply {
                    drawCircle(POINT_COLOR, POINT_R, Offset(scrX,scrY))
                    drawCircle(
                        color = Color.Black,
                        radius = POINT_R,
                        center = Offset(scrX, scrY),
                        style = Stroke(1f)
                    )
                }

            }
        }
    }
}
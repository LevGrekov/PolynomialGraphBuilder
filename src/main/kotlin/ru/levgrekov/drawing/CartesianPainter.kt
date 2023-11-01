package ru.levgrekov.drawing

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import org.jetbrains.skia.Font
import org.jetbrains.skia.Paint
import org.jetbrains.skia.TextLine
import ru.levgrekov.drawing.convertation.Converter
import ru.levgrekov.drawing.convertation.Plane
import ru.levgrekov.drawing.convertation.Stepic
import java.text.DecimalFormat


open class CartesianPainter(val showGrid: Boolean) : Painter {

    companion object{
        private val paint = Paint().apply {
            color = Color.Black.toArgb()
        }
        private val font = Font().apply {
            size = 24f
        }
        private val df =  DecimalFormat("#.##########")
        val AXIS_COLOR = Color.Red
        val BLUE_COLOR = Color.Blue
        val RED_COLOR = Color.Red
        val GRID_COLOR = Color.LightGray
        const val GRID_WIDTH = 0.7f
    }

    var plane: Plane? = null


    override fun paint(scope: DrawScope) {
        plane?.let{
            val stepY = Stepic.getStepInnovation(it.deltaY)
            val stepX = Stepic.getStepInnovation(it.deltaX)
            if(showGrid) drawGrid(scope,5.0,stepX,stepY) else drawTips(scope,5.0,stepX,stepY)
            paintAxis(scope)
            paintLabels(scope,stepX,stepY)
        }
    }

    private fun paintLabels(scope: DrawScope,stepX: Double,stepY: Double) {

        plane?.let {
            var x = Stepic.getLowerLim(it.xMin,stepX)
            var y = Stepic.getLowerLim(it.yMin,stepY)
            val x0 = it.x0.coerceIn(0f, it.width)
            val y0 = it.y0.coerceIn(0f, it.height)

            while (x < it.xMax){
                if(x!=0.0){
                    val xPos = Converter.xCrt2Scr(x, it)
                    val tl = TextLine.make(df.format(x),font)
                    if(xPos > tl.width && xPos < it.width - tl.width){
                        if(y0 == 0f){
                            drawLabel(scope,tl,xPos,0f, TextAlign.Center)
                        }
                        else{
                            drawLabel(scope,tl,xPos,y0, TextAlign.Justify)
                        }
                    }
                }
                x+=stepX
            }
            while (y < it.yMax){
                val yPos = Converter.yCrt2Scr(y, it)
                val tl = TextLine.make(df.format(y),font)
                println("x0 = $x0 widht = ${it.width}")
                if(yPos > tl.height && yPos < it.height - tl.height ){
                    if(x0 == it.width){
                        drawLabel(scope,tl,it.width,yPos,TextAlign.Right )
                    }
                    else{
                        drawLabel(scope,tl,x0,yPos,TextAlign.Left )
                    }
                }
                y+=stepY
            }
        }
    }

    private fun paintAxis(scope: DrawScope) {

        plane?.let {
            val x0 = it.x0.coerceIn(0f, it.width)
            val y0 = it.y0.coerceIn(0f, it.height)
            scope.apply {
                drawLine(
                    AXIS_COLOR,
                    Offset(0f, y0),
                    Offset(size.width, y0),
                    2f)
                drawLine(
                    AXIS_COLOR,
                    Offset(x0, 0f),
                    Offset(x0, size.height),
                    2f)
            }
        }
    }

    private fun drawGrid(scope: DrawScope, inStep:Double,stepX:Double,stepY:Double) {
        plane?.let {
            var x = Stepic.getLowerLim(it.xMin,stepX)
            while (x < it.xMax){
                val xPos = Converter.xCrt2Scr(x, it)
                scope.apply {
                    drawLine(
                        GRID_COLOR,
                        Offset(xPos, 0f),
                        Offset(xPos, size.height),
                        GRID_WIDTH)
                }
                x+=stepX
            }

            var y = Stepic.getLowerLim(it.yMin,stepY)
            while (y < it.yMax){
                val yPos = Converter.yCrt2Scr(y, it)
                scope.apply {
                    drawLine(
                        GRID_COLOR,
                        Offset(0f, yPos),
                        Offset(it.width, yPos),
                        GRID_WIDTH)
                }
                y+=stepY
            }
        }
    }
    private fun drawTips(scope: DrawScope, inStep:Double,stepX:Double,stepY:Double){
        plane?.let {
            var step = stepX/inStep
            var x = Stepic.getLowerLim(it.xMin,step)

            while (x < Stepic.getLowerLim(it.xMin,stepX)){
                val xInPos = Converter.xCrt2Scr(x, it)
                scope.apply {
                    drawLine(GRID_COLOR, Offset(xInPos, it.y0 + 4f), Offset (xInPos, it.y0-4f), 1f)
                }
                x += step
                println(x)
            }
            while (x <= it.xMax) {
                val xPos = Converter.xCrt2Scr(x, it)
                scope.apply {
                    drawLine(RED_COLOR, Offset(xPos, it.y0 + 10f), Offset (xPos, it.y0-10f), 1f)
                }
                var innerX = x + step
                while (innerX < x+stepX){
                    val xInPos = Converter.xCrt2Scr(innerX, it)
                    scope.apply {
                        drawLine(GRID_COLOR, Offset(xInPos, it.y0 + 4f), Offset (xInPos, it.y0-4f), 1f)
                    }
                    innerX += step
                }
                x += stepX
            }

            step = stepY/inStep
            var y = Stepic.getLowerLim(it.yMin,step)

            while (y < Stepic.getLowerLim(it.xMin,stepX)){
                val yInPos = Converter.xCrt2Scr(y, it)
                scope.apply {
                    drawLine(GRID_COLOR, Offset( it.y0 + 4f, yInPos), Offset ( it.y0-4f,yInPos), 1f)
                }
                y += step
            }
            while (y <= it.yMax) {
                val yPos = Converter.yCrt2Scr(y, it)
                scope.apply {
                    drawLine(RED_COLOR, Offset( it.x0 + 10f,yPos), Offset (it.x0-10f,yPos), 1f)
                }
                var innerY = y + step
                while (innerY < y+stepY){
                    val yInPos = Converter.yCrt2Scr(innerY, it)
                    scope.apply {
                        drawLine(GRID_COLOR, Offset( it.x0 + 4f,yInPos), Offset (it.x0-4f,yInPos), 1f)
                    }
                    innerY += step
                }
                y += stepY
            }
        }
    }
    private fun drawLabel(scope: DrawScope, textLine : TextLine, x: Float, y:Float, yourTextAlign: TextAlign){
        scope.drawIntoCanvas { canvas ->
            val xOffset : Float = when (yourTextAlign) {
                TextAlign.Left, TextAlign.Start -> 0f
                TextAlign.Right, TextAlign.End -> textLine.width
                TextAlign.Center, TextAlign.Justify -> textLine.width / 2f
                else -> 0f
            }
            var yOffset = 0f
            if(yourTextAlign == TextAlign.Center){
                yOffset = textLine.height
            }
            val actualX = x - xOffset
            val actualY = y + yOffset

            canvas.nativeCanvas.drawTextLine(textLine, actualX, actualY, paint )

        }
    }
}

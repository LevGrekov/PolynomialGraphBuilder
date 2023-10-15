package drawing

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import drawing.convertation.Converter
import drawing.convertation.Plane
import drawing.math.Stepic
import org.jetbrains.skia.*
import java.text.DecimalFormat


open class CartesianPainter() : Painter {

    var plane: Plane? = null
    private val df =  DecimalFormat("#.##############")

    private val paint = Paint().apply {
        color = Color.Black.toArgb()
    }
    private val font = Font().apply {
        size = 24f
    }
    override fun paint(scope: DrawScope) {
        plane?.let{
            val stepY = Stepic.getStepInnovation(it.deltaY)
            val stepX = Stepic.getStepInnovation(it.deltaX)
            drawGrid(scope,5.0,stepX,stepY)
            paintAxis(scope)
            paintLabels(scope,stepX,stepY)
        }
    }

    private fun paintLabels(scope: DrawScope,stepX: Double,stepY: Double) {

        plane?.let {
            var x = Stepic.getLowerLim(it.xMin,stepX)
            var y = Stepic.getLowerLim(it.yMin,stepY)
            val x0 = it.x0.coerceIn(1f, it.width-1)
            val y0 = it.y0.coerceIn(1f, it.height-1)

            while (x < it.xMax){
                if(x!=0.0){
                    val xPos = Converter.xCrt2Scr(x, it)
                    val tl = TextLine.make(df.format(x),font)
                    if(xPos > tl.width && xPos < it.width - tl.width){
                        drawLabel(scope,tl,xPos,y0, TextAlign.Justify)
                    }
                }
                x+=stepX
            }
            while (y < it.yMax){
                val yPos = Converter.yCrt2Scr(y, it)
                val tl = TextLine.make(df.format(y),font)
                if(yPos > tl.height && yPos < it.height - tl.height ){
                    drawLabel(scope,tl,x0,yPos, TextAlign.Left)
                }
                y+=stepY
            }
        }
    }

    private fun paintAxis(scope: DrawScope) {

        plane?.let {
            val x0 = it.x0.coerceIn(1f, it.width-1)
            val y0 = it.y0.coerceIn(1f, it.height-1)
            scope.apply {
                drawLine(
                    Color.Red,
                    Offset(0f, y0),
                    Offset(size.width, y0),
                    2f)
                drawLine(
                    Color.Red,
                    Offset(x0, 0f),
                    Offset(x0, size.height),
                    2f)
            }
        }
    }

    private fun drawGrid(scope: DrawScope, inStep:Double,stepX:Double,stepY:Double) {
        plane?.let {
            var x = Stepic.getLowerLim(it.xMin,stepX)
            println(x)
            while (x < it.xMax){
                val xPos = Converter.xCrt2Scr(x, it)
                scope.apply {
                    drawLine(Color.Black,
                        Offset(xPos, 0f),
                        Offset(xPos, size.height),
                        1f)
                }
                x+=stepX
            }

            var y = Stepic.getLowerLim(it.yMin,stepY)
            while (y < it.yMax){
                val yPos = Converter.yCrt2Scr(y, it)
                scope.apply {
                    //Рисуем Линии сетки, на которых будет Label
                    drawLine(Color.Black,
                        Offset(0f, yPos),
                        Offset(it.width, yPos),
                        1f)
                }
                y+=stepY
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
            val actualX = x - xOffset

            canvas.nativeCanvas.drawTextLine(textLine, actualX, y, paint )

        }
    }
}

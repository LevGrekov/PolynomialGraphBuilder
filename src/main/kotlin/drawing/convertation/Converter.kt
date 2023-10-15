package drawing.convertation

object Converter {
    fun xCrt2Scr(x: Double, p: Plane) =
        ((x - p.xMin) * p.xDen).coerceIn(-p.width.toDouble(),2*p.width.toDouble()).toFloat()
    fun yCrt2Scr(y: Double, p: Plane) =
        ((p.yMax - y) * p.yDen).coerceIn(-p.height.toDouble(),2*p.height.toDouble()).toFloat()
    fun xScr2Crt(x: Float, p: Plane) = x / p.xDen + p.xMin
    fun yScr2Crt(y: Float, p: Plane) = p.yMax - y / p.yDen

}


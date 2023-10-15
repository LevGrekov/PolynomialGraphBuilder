package drawing.math

import drawing.convertation.Plane
import java.lang.Math.pow
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

object Stepic {
    private fun getMagnitude(number: Double): Double =
        10.0.pow(floor(log10(abs(number))))

    fun getStepNotWorking(plane: Plane): Double {
        plane.apply {
            return when {
                xDen <= 28.0 -> 1.0 * getMagnitude(deltaX)
                xDen in 28.0..56.0 -> 2.0 * getMagnitude(deltaX)
                xDen in 56.0..140.0 -> 5.0 * getMagnitude(deltaX)
                xDen > 140.0 -> 10.0
                else -> throw Exception("Непонятно как так случилось")
            } / getMagnitude(deltaX)
        }
    }
    fun getStep(delta:Double): Int {
        val mg = Stepic.getMagnitude(delta)
        val a = when (delta) {
            in 28.0..56.0 -> 2 //1
            in 56.0..140.0 -> 5 //2
            in 140.0..280.0 -> 10 //3
            in 280.0..560.0 -> 20 // 4
            in 560.0..1400.0 -> 50 // 5
            in 1400.0..2800.0 -> 100 // 6
            else -> throw Exception("Так не может быть")
        }
        println(a)
        return a
    }
    fun getLowerLim(xMin: Double, step: Double) : Double {
        val ll = step * floor(xMin / step)
        return  if(ll>xMin) ll else ll+step
    }
    fun getStepInnovation(delta: Double,firstEnter: Double = 28.0) : Double{
        val sgn = if((delta/firstEnter).toInt() > 0) 1.0 else -1.0
        var i = 0.0
        while(i!=sgn*100){
            if(delta/firstEnter in (2.0.pow(i - 1.0) * 5.0.pow(i)) .. (2.0.pow(i) * 5.0.pow(i)))
                return 2.0 * 10.0.pow(i)
            if(delta/firstEnter in (2.0.pow(i) * 5.0.pow(i)) .. (2.0.pow(i+1) * 5.0.pow(i)))
                return 5.0 * 10.0.pow(i)
            if(delta/firstEnter in (2.0.pow(i+1) * 5.0.pow(i)) .. (2.0.pow(i) * 5.0.pow(i+1)))
                return 10.0 * 10.0.pow(i)
            i+=sgn
        }
        return delta
    }
}
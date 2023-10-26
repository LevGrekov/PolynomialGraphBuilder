import ru.levgrekov.drawing.math.eq
import ru.levgrekov.drawing.math.factorial
import ru.levgrekov.drawing.math.neq
import kotlin.math.abs
import kotlin.math.pow

open class Polynomial(coeffs: Map<Int, Double>) {
    protected val _coeffs: MutableMap<Int, Double> = mutableMapOf()

    init {
        setFiltered(coeffs)
    }

    protected fun setFiltered(rawCoeffs: Map<Int, Double>){
        _coeffs.clear()
        _coeffs.putAll(rawCoeffs.filter { (k,v) -> v neq 0.0 && k >= 0 }.toMutableMap())
        if(_coeffs.isEmpty()){
            _coeffs[0] = 0.0
        }
    }

    //Свойства
    val coeffs: Map<Int,Double>
        get() = _coeffs.toMap()

    val size : Int
        get() = _coeffs.size
    val highDegree : Int
        get() =  _coeffs.keys.max()?: 0
    val minorDegree : Int
        get() = _coeffs.keys.min()?: 0


    constructor(vararg coeffs: Double) : this (coeffs.mapIndexed { index, value -> index to value }.toMap())
    constructor(coeffs: MutableList<Double>) : this (coeffs.mapIndexed { index, value -> index to value }.toMap())
    constructor(other: Polynomial) : this(HashMap(other._coeffs))

    // Действия со Скаляром
    operator fun times(scalar: Double) = Polynomial(_coeffs.map { (k, v) -> k to scalar * v }.toMap())
    operator fun timesAssign(scalar: Double){
        _coeffs.keys.forEach { _coeffs[it] = _coeffs[it]!! * scalar}
        setFiltered(coeffs)
    }
    operator fun div(scalar: Double) =
        Polynomial(_coeffs.map { (k, v) -> if (scalar eq 0.0) throw ArithmeticException("Division by zero") else k to 1.0 / scalar * v }
            .toMap())

    // Действия с другим полиномом
    operator fun plus(other: Polynomial) = Polynomial(_coeffs.toMutableMap().also {
        other._coeffs.forEach { (k2, v2) -> it[k2] = v2 + (it[k2] ?: 0.0) }
    })
    operator fun plusAssign(other: Polynomial) {
        other._coeffs.forEach { (k2, v2) -> _coeffs[k2] = v2 + (_coeffs[k2] ?: 0.0) }
        setFiltered(coeffs)
    }
    operator fun minus(other: Polynomial) = Polynomial(_coeffs.toMutableMap().also {
        other._coeffs.forEach { (k2, v2) -> it[k2] = -v2 + (it[k2] ?: 0.0) }
    })
    operator fun times(other: Polynomial) = Polynomial(mutableMapOf<Int, Double>().also {
        _coeffs.forEach { (k1, v1) ->
            other._coeffs.forEach { (k2, v2) ->
                it[k1 + k2] = v1 * v2 + (it[k1 + k2] ?: 0.0)
            }
        }
    })
    operator fun timesAssign(other: Polynomial){
        val c = mutableMapOf<Int,Double>()
        _coeffs.forEach { (k1, v1) ->
            other._coeffs.forEach { (k2, v2) ->
                c[k1 + k2] = v1 * v2 + (c[k1 + k2] ?: 0.0)
            }
        }
        _coeffs.apply {
            clear()
            setFiltered(c)
            putAll(c)
        }
    }
    private fun divide(divisor: Polynomial): Pair<Polynomial, Polynomial> {

        if(divisor.coeffs[0] == 0.0) throw  ArithmeticException("forbidden to divide by zero");

        val divisorList = (0..divisor.highDegree).map {divisor._coeffs.getOrDefault(it,0.0)}.toMutableList()
        val remainder = (0..this.highDegree).map {_coeffs.getOrDefault(it,0.0)}.toMutableList()

        val quotient = MutableList(remainder.size - divisor.size + 1){0.0}

        for(i in quotient.indices){
            val coeff : Double = remainder[remainder.size - i - 1] / divisorList.last();
            quotient[quotient.size - i - 1] = coeff;

            for(j in divisorList.indices){
                remainder[remainder.size - i - j - 1] -= coeff * divisorList[divisorList.size - j - 1]
            }
        }

        return Pair(Polynomial(quotient), Polynomial(remainder))
    }
    operator fun rem(other: Polynomial) = divide(other).second;
    operator fun div(other: Polynomial) = divide(other).first;

    // Как функция
    operator fun get(degree: Int) = _coeffs[degree] ?: 0.0
    operator fun invoke(scalar: Double) =
        _coeffs.entries.sumOf { (k, v) -> v * scalar.pow(k) }
    fun derivative(derivOrder: Int): Polynomial {
        val derivativeCoeffs = mutableMapOf<Int, Double>()
        for ((exp, coeff) in _coeffs) {
            if (exp >= derivOrder) {
                val newExp = exp - derivOrder
                val newCoeff = coeff * factorial(exp) / factorial(newExp)
                derivativeCoeffs[newExp] = newCoeff
            }
        }
        return Polynomial(derivativeCoeffs)
    }

    //Переопределение Any

    override operator fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Polynomial) return false
        return this._coeffs == other._coeffs
    }
    override fun hashCode(): Int = _coeffs.keys.hashCode() * 17 + _coeffs.values.hashCode() * 31

    override fun toString() = toString("x")

    fun toString(variable: String) = _coeffs.toSortedMap(reverseOrder()).map{ (k, v) ->
        buildString {
            if (v.neq(0.0, 1e-12)) {
                append(if (v > 0.0 || v.eq(0.0, 1e-12)) if (k != _coeffs.keys.max()) "+" else "" else "-")
                if (abs(v) neq 1.0 || k == 0) append(abs(v))
                if (k != 0) append(variable)
                if (k > 1) append("^$k")
            }
        }
    }.joinToString("")

}

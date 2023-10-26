
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import ru.levgrekov.drawing.CartesianPainter
import ru.levgrekov.drawing.PolynomialPainter
import ru.levgrekov.drawing.controls.NumericUpDownGrid
import ru.levgrekov.drawing.convertation.Converter
import ru.levgrekov.drawing.convertation.Plane
import ru.levgrekov.polynomial.ru.levgrekov.polynomial.math.NewtonPolynomial2
import java.util.*

@Composable
fun appNewDesign(){
    var polyColor by remember { mutableStateOf(Color.Magenta) }
    var derivaColor by remember { mutableStateOf(Color.Blue) }

    var xMin by remember { mutableStateOf(-100.0) }
    var xMax by remember { mutableStateOf(100.0) }
    var yMin by remember { mutableStateOf(-10.0) }
    var yMax by remember { mutableStateOf(10.0) }

    var pt by remember { mutableStateOf<Pair<Float,Float>?>(null) }

    var showGrid by remember { mutableStateOf(true) }

    val cartesianPainter = CartesianPainter(showGrid)
    cartesianPainter.plane = Plane(xMin, xMax, yMin, yMax,0f,0f)

    var showPoints by remember { mutableStateOf(true) }
    var showDerivative by remember { mutableStateOf(true) }

    var poly by remember { mutableStateOf(NewtonPolynomial2(null)) }

    var polySize by remember { mutableStateOf(poly.size) }

    var derivativeOrder by remember { mutableStateOf(1f)}

    val polyPainter = PolynomialPainter(poly,derivativeOrder.toInt(), showPoints, showDerivative,polyColor,derivaColor)
    polyPainter.plane = cartesianPainter.plane

    MaterialTheme {
        Box(
            Modifier
                .background(MaterialTheme.colors.primary)
                .fillMaxSize()) {
            Column(
                Modifier.padding(2.dp).fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Canvas(
                    Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .background(Color.White)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { offset ->
                                    pt = offset.x to offset.y
                                }
                            )
                        }
                        .clipToBounds()
                ) {
                    cartesianPainter.plane?.width = size.width
                    cartesianPainter.plane?.height = size.height
                    polyPainter.plane?.width = size.width
                    polyPainter.plane?.height = size.height
                    pt?.let {
                        poly.addPoint(
                            Converter.xScr2Crt(it.first, polyPainter.plane!!),
                            Converter.yScr2Crt(it.second, polyPainter.plane!!),
                        )
                        pt = null
                    }
                    cartesianPainter.paint(this)
                    polyPainter.paint(this)
                    polySize = poly.size
                }
                Column(Modifier.padding(top = 2.dp).fillMaxWidth()
                    .background(Color(1f, 1f, 1f, 0.9f))
                ) {
                    Row {
                        NumericUpDownGrid(
                            Modifier.weight(3f),
                            xMin = xMin,
                            xMax = xMax,
                            yMin = yMin,
                            yMax = yMax,
                            onXMinChange = { xMin = it },
                            onXMaxChange = { xMax = it },
                            onYMinChange = { yMin = it },
                            onYMaxChange = { yMax = it }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        val random = Random(System.currentTimeMillis())
                        Column {
                            OutlinedButton(
                                onClick = {
                                    // Генерируем случайный цвет
                                    val newColor = Color(
                                        red = random.nextFloat(),
                                        green = random.nextFloat(),
                                        blue = random.nextFloat()
                                    )
                                    polyColor = newColor
                                    polyPainter.polynomialColor = newColor
                                },
                                Modifier.size(30.dp),
                                contentPadding = PaddingValues(8.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = polyColor)
                            ){Text("f")}

                            OutlinedButton(
                                onClick = {
                                    val newColor = Color(
                                        red = random.nextFloat(),
                                        green = random.nextFloat(),
                                        blue = random.nextFloat()
                                    )
                                    derivaColor = newColor
                                    polyPainter.derivativeColor = newColor
                                },
                                Modifier.size(30.dp),
                                contentPadding = PaddingValues(8.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = derivaColor)
                            ) {Text("d")}
                        }

                        Switch(
                            checked = showPoints,
                            onCheckedChange = { showPoints = it },
                            modifier = Modifier.padding(2.dp),
                            colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colors.primary)
                        )
                        Text(
                            text = "Точки",
                            modifier = Modifier.padding(2.dp)
                        )

                        Switch(
                            checked = showGrid,
                            onCheckedChange = { showGrid = it },
                            modifier = Modifier.padding(2.dp),
                            colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colors.primary)
                        )
                        Text(
                            text = "Сетка",
                            modifier = Modifier.padding(2.dp)
                        )

                        Switch(
                            checked = showDerivative,
                            onCheckedChange = { showDerivative = it },
                            modifier = Modifier.padding(2.dp),
                            colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colors.primary)
                        )
                        Text(
                            text = "Производная",
                            modifier = Modifier.padding(2.dp)
                        )
                        Button(
                            onClick = { poly = NewtonPolynomial2(null) },
                            modifier = Modifier.padding(2.dp)
                        ) {
                            Text("Очистить")
                        }

                        if (showDerivative) {
                            Slider(
                                value = derivativeOrder,
                                onValueChange = { newValue ->
                                    derivativeOrder = newValue
                                },
                                valueRange = 0f .. polySize.toFloat(),
                                steps = (polySize - 1)
                            )
                        }
                    }
                }
            }
        }
    }
}

//@Composable
//@Preview
//fun app() {
//
//    var xMin by remember { mutableStateOf(-100.0) }
//    var xMax by remember { mutableStateOf(100.0) }
//    var yMin by remember { mutableStateOf(-10.0) }
//    var yMax by remember { mutableStateOf(10.0) }
//
//    var pt by remember { mutableStateOf<Pair<Float,Float>?>(null) }
//
//    val cartesianPainter = CartesianPainter()
//    cartesianPainter.plane = Plane(xMin, xMax, yMin, yMax,0f,0f)
//
//    val poly = remember { NewtonPolynomial2(null) }
//
//    val polyPainter = PolynomialPainter(poly,1,false,false)
//    polyPainter.plane = cartesianPainter.plane
//    MaterialTheme {
//        Column {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .fillMaxHeight(0.8f)
//                    .padding(16.dp)
//                    .background(
//                        color = Color.White,
//                        shape = RoundedCornerShape(16.dp)
//                    )
//                    .border(
//                        width = 2.dp,
//                        color = MaterialTheme.colors.primary,
//                    )
//            ) {
//                Canvas(
//                    Modifier
//                        .fillMaxSize()
//                        .pointerInput(Unit) {
//                            detectTapGestures(
//                                onTap = { offset ->
//                                    pt = offset.x to offset.y
//                                }
//                            )
//                        }
//                        .clipToBounds()
//                ) {
//                    println(poly)
//                    cartesianPainter.plane?.width = size.width
//                    cartesianPainter.plane?.height = size.height
//                    polyPainter.plane?.width = size.width
//                    polyPainter.plane?.height = size.height
//                    pt?.let {
//                        poly.addPoint(
//                            Converter.xScr2Crt(it.first, polyPainter.plane!!),
//                            Converter.yScr2Crt(it.second, polyPainter.plane!!),
//                        )
//                        pt = null
//                    }
//                    cartesianPainter.paint(this)
//                    polyPainter.paint(this)
//                }
//            }
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(horizontal = 16.dp)
//            ){
//                Row(
//                    modifier = Modifier.fillMaxSize()
//                ) {
//                    numericUpDown(
//                        value = xMin,
//                        onValueChange = {  xMin = it },
//                        modifier = Modifier.weight(1f),
//                        meaning = "X min",
//                        minValue = -Double.MAX_VALUE,
//                        maxValue = xMax
//                    )
//                    numericUpDown(
//                        value = xMax,
//                        onValueChange = { xMax = it },
//                        modifier = Modifier.weight(1f),
//                        meaning = "X max",
//                        minValue = xMin,
//                        maxValue = Double.MAX_VALUE
//                    )
//
//                    numericUpDown(
//                        value = yMin,
//                        onValueChange = { yMin = it },
//                        modifier = Modifier.weight(1f),
//                        meaning = "Y min",
//                        minValue = -Double.MAX_VALUE,
//                        maxValue = yMax
//                    )
//
//                    numericUpDown(
//                        value = yMax,
//                        onValueChange = {  yMax = it },
//                        modifier = Modifier.weight(1f),
//                        meaning = "Y max",
//                        minValue = yMin,
//                        maxValue = Double.MAX_VALUE
//                    )
//                }
//            }
//        }
//    }
//}
//


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "PolynomialBuilder",
        state = WindowState(width = 600.dp, height = 500.dp)
//        resizable = false,
    ) {
        appNewDesign()
    }
}











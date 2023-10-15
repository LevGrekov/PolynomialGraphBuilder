import drawing.CartesianPainter
import drawing.convertation.Plane
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application



@Composable
@Preview
fun app() {

    var xMin by remember { mutableStateOf(-100.0) }
    var xMax by remember { mutableStateOf(100.0) }
    var yMin by remember { mutableStateOf(-10.0) }
    var yMax by remember { mutableStateOf(10.0) }

    val p = CartesianPainter()
    p.plane = Plane(xMin, xMax, yMin, yMax,0f,0f)


    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .padding(16.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colors.primary,
                )
        ) {
            Canvas(Modifier.fillMaxSize()) {
                p.plane?.width = size.width
                p.plane?.height = size.height
                p.paint(this)

            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ){
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                numericUpDown(
                    value = xMin,
                    onValueChange = {  xMin = it },
                    modifier = Modifier.weight(1f),
                    meaning = "X min",
                    minValue = -Double.MAX_VALUE,
                    maxValue = xMax
                )

                numericUpDown(
                    value = xMax,
                    onValueChange = { xMax = it },
                    modifier = Modifier.weight(1f),
                    meaning = "X max",
                    minValue = xMin,
                    maxValue = Double.MAX_VALUE
                )

                numericUpDown(
                    value = yMin,
                    onValueChange = { yMin = it },
                    modifier = Modifier.weight(1f),
                    meaning = "Y min",
                    minValue = -Double.MAX_VALUE,
                    maxValue = yMax
                )

                numericUpDown(
                    value = yMax,
                    onValueChange = {  yMax = it },
                    modifier = Modifier.weight(1f),
                    meaning = "Y max",
                    minValue = yMin,
                    maxValue = Double.MAX_VALUE
                )
            }
        }
    }
}

@Composable
fun numericUpDown(
    value: Double,
    meaning: String,
    onValueChange: (Double) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp),
    minValue: Double,
    maxValue: Double,
    step: Double = 1.0
) {
    var text by remember { mutableStateOf(value.toString()) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                val filteredText = it.filter { char ->
                    char.isDigit() || char == '.' || (char == '-' && it.indexOf(char) == 0)
                }
                text = filteredText
                val newValue = filteredText.toDoubleOrNull()
                if (newValue != null && newValue >= minValue && newValue <= maxValue) {
                    onValueChange(newValue)
                }
            },
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape)
                .padding(8.dp)
                .clip(shape),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = {
                    val newValue = value - step
                    if (newValue >= minValue + step ) {
                        onValueChange(newValue)
                        text = newValue.toString()
                    }
                },
                modifier = Modifier.size(18.dp),
                content = {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Decrease",
                        tint = MaterialTheme.colors.primary
                    )
                }
            )

            Text(meaning)

            IconButton(
                onClick = {
                    val newValue = value + step
                    if (newValue <= maxValue - step) {
                        onValueChange(newValue)
                        text = newValue.toString()
                    }
                },
                modifier = Modifier.size(18.dp),
                content = {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Increase",
                        tint = MaterialTheme.colors.primary,
                    )
                },
            )
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "PolynomialBuilder",
//        resizable = false,

    ) {
        app()
    }
}











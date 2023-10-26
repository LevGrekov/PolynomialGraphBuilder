package ru.levgrekov.drawing.controls

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable
fun NumericUpDownGrid(
    modifier: Modifier = Modifier,
    xMin: Double,
    xMax: Double,
    yMin: Double,
    yMax: Double,
    onXMinChange: (Double) -> Unit,
    onXMaxChange: (Double) -> Unit,
    onYMinChange: (Double) -> Unit,
    onYMaxChange: (Double) -> Unit,
) {
    Column(
        Modifier.padding(5.dp)
    ) {
        Row(Modifier.padding(2.dp)) {
            numericUpDown(
                value = xMin,
                onValueChange = onXMinChange,
                modifier = Modifier.weight(1f),
                meaning = "X min",
                minValue = -Double.MAX_VALUE,
                maxValue = xMax
            )
            numericUpDown(
                value = xMax,
                onValueChange = onXMaxChange,
                modifier = Modifier.weight(1f),
                meaning = "X max",
                minValue = xMin,
                maxValue = Double.MAX_VALUE
            )
        }
        Row(Modifier.padding(2.dp)) {
            numericUpDown(
                value = yMin,
                onValueChange = onYMinChange,
                modifier = Modifier.weight(1f),
                meaning = "Y min",
                minValue = -Double.MAX_VALUE,
                maxValue = yMax
            )
            numericUpDown(
                value = yMax,
                onValueChange = onYMaxChange,
                modifier = Modifier.weight(1f),
                meaning = "Y max",
                minValue = yMin,
                maxValue = Double.MAX_VALUE
            )
        }
    }
}
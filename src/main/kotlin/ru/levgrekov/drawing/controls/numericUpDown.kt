package ru.levgrekov.drawing.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

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

    Row(
        modifier = modifier.padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(meaning, modifier = Modifier.padding(end = 8.dp))

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
                .weight(1f)
                .background(Color.White, shape)
                .clip(shape),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
        )

        Column {
            Button(
                onClick = {
                    val newValue = value - step
                    if (newValue >= minValue + step) {
                        onValueChange(newValue)
                        text = newValue.toString()
                    }
                },
                modifier = Modifier.size(28.dp),
                contentPadding = PaddingValues(8.dp),

            ) {
                Text(text = "-")
            }

            Button(
                onClick = {
                    val newValue = value + step
                    if (newValue <= maxValue - step) {
                        onValueChange(newValue)
                        text = newValue.toString()
                    }
                },
                modifier = Modifier.size(28.dp),
                contentPadding = PaddingValues(8.dp),
            ) {
                Text(text = "+")
            }
        }

    }
}

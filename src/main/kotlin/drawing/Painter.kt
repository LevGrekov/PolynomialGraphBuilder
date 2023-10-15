package drawing

import androidx.compose.ui.graphics.drawscope.DrawScope

interface Painter {
    fun paint(scope: DrawScope)
}
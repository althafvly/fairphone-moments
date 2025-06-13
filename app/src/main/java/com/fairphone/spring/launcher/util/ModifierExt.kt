package com.fairphone.spring.launcher.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp

fun Modifier.gradientBorder(
    borderWidth: Dp,
    brush: Brush,
    cornerRadius: Dp
): Modifier = this.then(
    Modifier.drawBehind {
        val stroke = borderWidth.toPx()
        val halfStroke = stroke / 2
        drawRoundRect(
            brush = brush,
            topLeft = Offset(halfStroke, halfStroke),
            size = Size(size.width - stroke, size.height - stroke),
            cornerRadius = CornerRadius(cornerRadius.toPx()),
            style = Stroke(stroke)
        )
    }
)
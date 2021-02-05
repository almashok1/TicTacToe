package kz.adamant.tictactoe.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import kz.adamant.tictactoe.R


class DrawLine(
    context: Context,
    private val startX: Float,
    private val startY: Float,
    private val endX: Float,
    private val endY: Float
) : View(context) {
    var paint: Paint = Paint()

    override fun onDraw(canvas: Canvas) {
        canvas.drawLine(
            startX,
            startY,
            endX,
            endY,
            paint
        )
    }

    init {
        paint.strokeWidth = 50.0f
        paint.color = ContextCompat.getColor(context, R.color.teal_200)
    }
}
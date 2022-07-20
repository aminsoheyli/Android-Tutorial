package com.aminsoheyli.paint

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private lateinit var drawPath: CustomPath
    private lateinit var drawPaint: Paint
    private lateinit var canvasBitmap: Bitmap
    private lateinit var canvasPaint: Paint
    private lateinit var canvas: Canvas
    private var brushSize: Float = 0f
    private var color: Int = Color.BLACK
    private var paths = ArrayList<CustomPath>()
    private var undoPaths = ArrayDeque<CustomPath>()

    init {
        setupDrawing()
    }

    private fun setupDrawing() {
        drawPath = CustomPath(color, brushSize)
        drawPaint = Paint()
        drawPaint.apply {
            color = this@DrawingView.color
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }
        canvasPaint = Paint(Paint.DITHER_FLAG)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(canvasBitmap)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(canvasBitmap, 0f, 0f, canvasPaint)
        for (path in paths)
            drawPath(canvas, path)
        if (!drawPath.isEmpty)
            drawPath(canvas, drawPath)
        drawPaint.strokeWidth = drawPath.brushTickness
        drawPaint.color = drawPaint.color
        canvas.drawPath(drawPath, drawPaint)
    }

    private fun drawPath(canvas: Canvas, path: CustomPath) {
        drawPaint.strokeWidth = path.brushTickness
        drawPaint.color = path.color
        canvas.drawPath(path, drawPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x
        val y = event?.y

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> drawPath.apply {
                color = this@DrawingView.color
                brushTickness = brushSize
                reset()
                if (x != null && y != null)
                    moveTo(x, y)
            }
            MotionEvent.ACTION_MOVE -> if (x != null && y != null) drawPath.lineTo(x, y)
            MotionEvent.ACTION_UP -> {
                paths.add(drawPath)
                drawPath = CustomPath(color, brushSize)
            }
            else -> return false
        }
        invalidate()
        return true
    }

    fun setColor(newColor: String) {
        color = Color.parseColor(newColor)
        drawPaint.color = color
    }

    fun setBrushSize(newSize: Float) {
        brushSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            newSize,
            resources.displayMetrics
        )
        drawPaint.strokeWidth = brushSize
    }

    fun undo() {
        if (paths.size > 0) {
            undoPaths.add(paths.removeAt(paths.size - 1))
            invalidate()
        }
    }

    fun redo() {
        if (undoPaths.size > 0) {
            paths.add(undoPaths.removeLast())
            invalidate()
        }
    }

    internal inner class CustomPath(var color: Int, var brushTickness: Float) : Path()
}
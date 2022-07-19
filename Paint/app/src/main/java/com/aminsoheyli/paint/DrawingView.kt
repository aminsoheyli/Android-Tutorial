package com.aminsoheyli.paint

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var drawPath: CustomPath? = null
    private var drawPaint: Paint? = null
    private var canvasBitmap: Bitmap? = null
    private var canvasPaint: Paint? = null
    private var brushSize: Float = 0f
    private var color: Int = Color.BLACK
    private var canvas: Canvas? = null
    private var paths = ArrayList<CustomPath>()

    init {
        setupDrawing()
    }

    private fun setupDrawing() {
        drawPath = CustomPath(color, brushSize)
        drawPaint = Paint()
        drawPaint!!.apply {
            color = this@DrawingView.color
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }
        canvasPaint = Paint(Paint.DITHER_FLAG)
        brushSize = 20f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(canvasBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(canvasBitmap!!, 0f, 0f, canvasPaint)
        if (!drawPath!!.isEmpty) {
            drawPaint!!.strokeWidth = drawPath!!.brushTickness
            drawPaint!!.color = drawPaint!!.color
            canvas.drawPath(drawPath!!, drawPaint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x
        val y = event?.y

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> drawPath!!.apply {
                color = this@DrawingView.color
                brushTickness = brushSize
                reset()
                if (x != null && y != null)
                    moveTo(x, y)
            }
            MotionEvent.ACTION_MOVE -> if (x != null && y != null) drawPath!!.lineTo(x, y)
            MotionEvent.ACTION_UP -> drawPath = CustomPath(color, brushSize)
            else -> return false
        }
        invalidate()
        return true
    }

    internal inner class CustomPath(var color: Int, var brushTickness: Float) : Path()
}
package com.github.spinel.sidebar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.TextView


class SideBar : View {
    private var latestPress = -1
    private val paint = Paint()
    private var paintColorNormal = 0
    private var paintColorPressed = 0
    private var textSize = 0f
    var callback = { _: Int, _: String -> }
    var initials = arrayOf<String>()
    var hintView: TextView? = null

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val values = context.obtainStyledAttributes(attrs, R.styleable.SideBar)

        paintColorNormal = values.getColor(R.styleable.SideBar_textColorNormal, Color.parseColor("#A9A9A9"))
        paintColorPressed = values.getColor(R.styleable.SideBar_textColorPressed, Color.parseColor("#4169E1"))
        textSize = values.getDimension(R.styleable.SideBar_textSize, 35f)

        values.recycle()
    }

    constructor(context: Context) : super(context)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec))
    }

    private fun measureWidth(width: Int): Int {
        val specMode = View.MeasureSpec.getMode(width)
        val specSize = View.MeasureSpec.getSize(width)
        if (specMode == View.MeasureSpec.AT_MOST) {
            return dp2px(textSize)
        }

        return specSize
    }

    private fun measureHeight(height: Int): Int {
        var result = initials.size * (textSize.toInt() + 5)
        val specMode = View.MeasureSpec.getMode(height)
        val specSize = View.MeasureSpec.getSize(height)
        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            if (specMode == View.MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }

        return result
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val itemHeight = height / if (initials.isEmpty()) 1 else initials.size

        for (pos in initials.indices) {
            paint.color = paintColorNormal
            paint.typeface = Typeface.DEFAULT_BOLD
            paint.textSize = textSize
            paint.isAntiAlias = true
            if (pos == latestPress) {
                paint.color = paintColorPressed
                paint.isFakeBoldText = true
            }
            val x = width / 2 - paint.measureText(initials[pos]) / 2
            val y = (itemHeight * (pos + 1)).toFloat()
            canvas.drawText(initials[pos], x, y, paint)

            paint.reset()
        }

    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val currentPress = latestPress
        val pos = (event.y / height * initials.size).toInt()

        when (event.action) {
            MotionEvent.ACTION_UP -> {
                hintView?.visibility = View.GONE

                latestPress = -1
                invalidate()
            }

            else -> {
                if (currentPress != pos) {
                    if (pos >= 0 && pos < initials.size) {
                        hintView?.text = initials[pos]
                        hintView?.visibility = View.VISIBLE
                        callback.invoke(pos, initials[pos])

                        latestPress = pos
                        invalidate()
                    }
                }
            }
        }
        return true
    }

    private fun dp2px(dpValue: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.resources.displayMetrics).toInt()
    }
}
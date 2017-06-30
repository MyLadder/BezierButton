package com.goyourfly.bezier_button

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator

/**
 * Created by gaoyufei on 2017/6/30.
 */

class BezierView : View {
    val paint: Paint = Paint()
    val path: Path = Path()
    var testOffset = 0F
    var animator: ValueAnimator? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, style: Int) : super(context, attrs, style) {
        paint.isAntiAlias = true
        paint.isDither = true
        paint.color = Color.RED
        paint.strokeWidth = 10F
        paint.style = Paint.Style.FILL
    }

    fun measureBezier(width: Int, height: Int, padding: Int, offsetH: Float, offsetV: Float) {
        val rate = 0.5F
        val bound = RectF(
                padding.toFloat() - offsetH * rate,
                padding.toFloat() - offsetV * rate,
                width.toFloat() - padding + offsetH * rate,
                height.toFloat() - padding + offsetV * rate)
        path.reset()
        path.moveTo(bound.left, bound.top)
        val point1X = bound.centerX()
        val point1Y = bound.top - offsetV
        path.quadTo(point1X, point1Y, bound.right, bound.top)
        val point2X = bound.right + offsetH
        val point2Y = bound.centerY()
        path.quadTo(point2X, point2Y, bound.right, bound.bottom)
        val point3X = bound.centerX()
        val point3Y = bound.bottom + offsetV
        path.quadTo(point3X, point3Y, bound.left, bound.bottom)
        val point4X = bound.left - offsetH
        val point4Y = bound.centerY()
        path.quadTo(point4X, point4Y, bound.left, bound.top)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        measureBezier(width, height, 50, testOffset, testOffset)
        canvas.drawPath(path, paint)
        Log.d("...", "onDraw:$testOffset")
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                animator = ValueAnimator.ofFloat(0F, 50F)
                animator?.addUpdateListener {
                    val value = it.animatedValue as Float
                    testOffset = value
                    invalidate()
                }
                animator?.interpolator = DecelerateInterpolator()
                animator?.duration = 100
                animator?.start()
            }
            MotionEvent.ACTION_UP , MotionEvent.ACTION_CANCEL -> {
                animator?.cancel()
                animator = ValueAnimator.ofFloat(testOffset, 0F)
                animator?.addUpdateListener {
                    val value = it.animatedValue as Float
                    testOffset = value
                    invalidate()
                }
                animator?.interpolator = DecelerateInterpolator()
                animator?.duration = 200
                animator?.start()
            }
        }
        return true
    }

}

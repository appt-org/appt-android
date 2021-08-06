package nl.appt.views.gestures

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.accessibility.AccessibilityEvent
import androidx.core.content.ContextCompat
import nl.appt.R
import nl.appt.accessibility.Accessibility
import nl.appt.accessibility.isTalkBackEnabled
import nl.appt.model.AccessibilityGesture
import nl.appt.model.Gesture
import nl.appt.model.Touch

interface GestureViewCallback {
    fun correct(gesture: Gesture)
    fun incorrect(gesture: Gesture, feedback: String)
}

/**
 * Created by Jan Jaap de Groot on 12/10/2020
 * Copyright 2020 Stichting Appt
 */
abstract class GestureView(val gesture: Gesture, context: Context) : View(context) {

    private val TAG = "GestureView"
    private val CLASS_NAME = GestureView::class.java.name

    private val paint: Paint by lazy {
        val paint = Paint()
        paint.color = ContextCompat.getColor(context, R.color.primary)
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = 20f
        paint.isAntiAlias = true
        paint
    }
    private var touches = mutableMapOf<Int, ArrayList<Touch>>()

    /** Drawing **/

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        touches.clear()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val offset = paint.strokeWidth / 2

        touches.entries.forEach { entry ->
            val touches = entry.value

            // Draw circle(s) around first touch.
            touches.firstOrNull()?.let { touch ->
                var i = 0
                while(i < touch.taps) {
                    val radius = 50f * (i+1)
                    canvas.drawCircle(touch.x.toFloat() - offset, touch.y.toFloat() - offset, radius, paint)
                    i++
                }
            }

            // Draw lines if there are more than 5 touches.
            if (touches.size >= 5) {
                touches.forEachIndexed { index, touch1 ->
                    if (index < touches.size - 1) {
                        val touch2 = touches[index+1]

                        canvas.drawLine(
                            touch1.x.toFloat() - offset,
                            touch1.y.toFloat() - offset,
                            touch2.x.toFloat() - offset,
                            touch2.y.toFloat() - offset,
                            paint
                        )
                    }
                }
            }
        }
    }

//    private fun drawLine(canvas: Canvas, paint: Paint, touch1: Touch, touch2: Touch) {
//        val offset = paint.strokeWidth / 2
//
//        canvas.drawLine(
//            touch1.x.toFloat() - offset,
//            touch1.y.toFloat() - offset,
//            touch2.x.toFloat() - offset,
//            touch2.y.toFloat() - offset,
//            paint
//        )
//    }

    fun showTouches(event: MotionEvent?, taps: Int = 1, longPress: Boolean = false) {
        if (event != null) {
            touches.clear()
            for (i in 0 until event.pointerCount) {
                val id = event.getPointerId(i)
                val coordinates = touches[id] ?: arrayListOf()

                val x = event.getX(i).toInt()
                val y = event.getY(i).toInt()

                coordinates.add(Touch(x, y, taps, longPress))
                touches[id] = coordinates
            }
            invalidate()
        }
    }

    fun showTouches(touches: MutableMap<Int, ArrayList<Touch>>) {
        this.touches = touches
        invalidate()
    }

    /** Motion events **/

    override fun onGenericMotionEvent(event: MotionEvent?): Boolean {
        Log.d(TAG, "onGenericMotionEvent: $event")
        return super.onGenericMotionEvent(event)
    }

    override fun onHoverEvent(event: MotionEvent?): Boolean {
        Log.d(TAG, "onHoverEvent: $event")

        if (Accessibility.isTalkBackEnabled(context) && event != null) {
            when (event.action) {
                MotionEvent.ACTION_HOVER_ENTER -> {
                    event.action = MotionEvent.ACTION_DOWN
                }
                MotionEvent.ACTION_HOVER_MOVE -> {
                    event.action = MotionEvent.ACTION_MOVE
                }
                MotionEvent.ACTION_HOVER_EXIT -> {
                    event.action = MotionEvent.ACTION_UP
                }
            }
            return onTouchEvent(event)
        }

        return super.onHoverEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d(TAG, "onTouchEvent: $event")

        if (event != null && event.pointerCount > 0) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                touches.clear()
            }

            for (i in 0 until event.pointerCount) {
                val id = event.getPointerId(i)
                val coordinates = touches[id] ?: arrayListOf()

                val x = event.getX(i).toInt()
                val y = event.getY(i).toInt()

                Log.d(TAG, "Point index $i with id $id touches position ($x, $y)")

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        coordinates.add(Touch(x, y))
                    }
                    MotionEvent.ACTION_MOVE -> {
                        coordinates.add(Touch(x, y))
                    }
                    MotionEvent.ACTION_UP -> {
                        coordinates.add(Touch(x, y))
                    }
                }

                this.touches[id] = coordinates
            }
            invalidate()
        }

        return super.onTouchEvent(event)
    }

    /** Accessibility events **/

    override fun onInitializeAccessibilityEvent(event: AccessibilityEvent?) {
        event?.className = CLASS_NAME
        super.onInitializeAccessibilityEvent(event)
    }

    override fun onPopulateAccessibilityEvent(event: AccessibilityEvent?) {
        event?.className = CLASS_NAME
        super.onPopulateAccessibilityEvent(event)
    }

    abstract fun onAccessibilityGesture(gesture: AccessibilityGesture)

    /** Callback **/

    var callback: GestureViewCallback? = null

    open fun correct() {
        callback?.correct(gesture)
    }

    open fun incorrect(feedback: String = "Geen feedback") {
        callback?.incorrect(gesture, feedback)
    }
}
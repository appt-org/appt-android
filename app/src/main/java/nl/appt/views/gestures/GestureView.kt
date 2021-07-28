package nl.appt.views.gestures

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
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
        paint.strokeWidth = 20F
        paint
    }
    private var touches = mutableMapOf<Int, ArrayList<Point>>()


    /** Touch events **/

    override fun onHoverEvent(event: MotionEvent?): Boolean {
        Log.d(TAG, "onHoverEvent: $event")

        if (Accessibility.isTalkBackEnabled(context)) {
            onTouchEvent(event)
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
                val path = touches[id] ?: arrayListOf()

                val x = event.getX(i).toInt()
                val y = event.getY(i).toInt()

                Log.d(TAG, "Point index $i with id $id touches position ($x, $y)")

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        path.add(Point(x, y))
                    }
                    MotionEvent.ACTION_MOVE -> {
                        path.add(Point(x, y))
                    }
                    MotionEvent.ACTION_UP -> {
                        // Ignored
                    }
                }

                touches[id] = path
            }
            invalidate()
        }

        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        touches.entries.forEach { entry ->
            val points = entry.value

            points.forEachIndexed { index, point1 ->
                val x1 = point1.x.toFloat()
                val y1 = point1.y.toFloat()

                if (index < points.size-1) {
                    val point2 = points[index+1]
                    val x2 = point2.x.toFloat()
                    val y2 = point2.y.toFloat()

                    canvas.drawLine(x1, y1, x2, y2, paint)
                }
            }
        }
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
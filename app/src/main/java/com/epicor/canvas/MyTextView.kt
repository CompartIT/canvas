package com.epicor.canvas

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView


internal class MyTextView(context: Context) : AppCompatTextView(context) {
    private var lastX = 0
    private var lastY = 0
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = x
                lastY = y
            }

            MotionEvent.ACTION_MOVE -> {
                val offsetX = x - lastX
                val offsetY = y - lastY
                layout(left + offsetX, top + offsetY, right + offsetX, bottom + offsetY)
            }

            else -> {}
        }
        return true
    }
}
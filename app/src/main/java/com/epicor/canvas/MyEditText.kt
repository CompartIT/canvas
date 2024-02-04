package com.epicor.canvas

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
class MyEditText : AppCompatEditText {
    private var dX = 0f
    private var dY = 0f
    private var lastAction = 0
    private var downTime: Long = 0

    interface OnMyEditTextClickListener {
        fun onMyEditTextClick(index: Int)
    }


    var onMyEditTextClickListener: OnMyEditTextClickListener? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init(context: Context) {
        background = null
        setOnTouchListener { view, event ->
            val X = event.rawX
            val Y = event.rawY
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {
                    val index = this.tag as Int
                    onMyEditTextClickListener?.onMyEditTextClick(index)
                    Log.e("测试点击1","你点击的是第 $index  个 MyEditText")
                    if (System.currentTimeMillis() - downTime < 200) {
                        setFocusableInTouchMode(true)
                        requestFocus()
                        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
                        text?.length?.let { setSelection(it) }
                    }
                    downTime = System.currentTimeMillis()
                    dX = X - view.x
                    dY = Y - view.y
                    lastAction = MotionEvent.ACTION_DOWN
                }
                MotionEvent.ACTION_MOVE -> {
                    view.x = X - dX
                    view.y = Y - dY
                    lastAction = MotionEvent.ACTION_MOVE
                }

            }
            true
        }
    }
}


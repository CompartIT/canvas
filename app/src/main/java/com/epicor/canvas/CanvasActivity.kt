package com.epicor.canvas

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.KeyboardUtils
import com.epicor.canvas.databinding.ActivityCanvasBinding
import java.text.SimpleDateFormat
import java.util.Date


class CanvasActivity : AppCompatActivity(), MyEditText.OnMyEditTextClickListener {


    private var mTag: Int = 0

    private lateinit var mBinding: ActivityCanvasBinding
    private val editTextList = mutableListOf<MyEditText>()

    private var progressValue:Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCanvasBinding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)

        mBinding.mAddTextView?.setOnClickListener { addView() }
        mBinding.mEditTextSize?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                p0?.progress?.toFloat()?.let { changeTextSizeOfView(mTag, it) }
                mBinding.mTextViewNum?.text = p0?.progress.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        mBinding.mCanvas?.setOnClickListener {
            KeyboardUtils.hideSoftInput(this)
        }

        mBinding.mAddTextViewSize?.setOnClickListener {
            progressValue = mBinding.mEditTextSize?.progress
            mBinding.mEditTextSize?.progress = progressValue?.plus(1)!!
        }


        mBinding.mRemoveTextViewSize?.setOnClickListener {
            progressValue = mBinding.mEditTextSize?.progress
            mBinding.mEditTextSize?.progress = progressValue?.minus(1)!!
        }

    }

    private fun addView() {
        val child = MyEditText(this)
        child.onMyEditTextClickListener = this
        child.textSize = 20f
        child.setTextColor(Color.BLACK)
        val currentTime: String = dateToStamp(System.currentTimeMillis())
        child.setText(currentTime)
        child.tag = editTextList.size  // 设置唯一的标识符
        mBinding.mCanvas?.addView(child)
        editTextList.add(child)  // 将新创建的 MyEditText 添加到列表中
    }


    private fun changeTextSizeOfView(index: Int, size: Float) {
        if (index in editTextList.indices) {
            editTextList[index].setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        }
    }


    @SuppressLint("SimpleDateFormat")
    fun dateToStamp(s: Long): String {
        val res: String = try {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = Date(s)
            simpleDateFormat.format(date)
        } catch (e: Exception) {
            return ""
        }
        return res
    }

    override fun onMyEditTextClick(index: Int) {
        mTag = index
    }

}
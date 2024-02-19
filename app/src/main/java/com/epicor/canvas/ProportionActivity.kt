package com.epicor.canvas

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.epicor.canvas.databinding.ActivityProportionBinding
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException
import java.io.InputStream


class ProportionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProportionBinding

    private var mProportion = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProportionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        try {
            val inputStream: InputStream = this.assets.open("test1.html")
            val document: Document = Jsoup.parse(inputStream, "UTF-8", "") // 解析 HTML

            // 获取整个界面宽高
            val widthOrHeight: Elements = document.select("AreaSize")
            val regex = Regex("\\d+") // 匹配一个或多个数字
            val matches = regex.findAll(widthOrHeight.toString())
            val numbers = matches.map { it.value }.toList()

            var mWith = 0.0
            if (numbers.size >= 2) {
                mWith = numbers[1].toDouble()
            } else {
                println("Jsoup 无法从字符串中提取出足够的数字。")
            }

            // 获取 标签宽高
            val labelWidth: String = document.select("LabelPage").select("LabelWidth").text()
            val labelHeight: String = document.select("LabelPage").select("LabelHeight").text()
            // 获取 计算比例
            mProportion = mWith / labelWidth.toDouble()

            val mVCanvas = binding.mCanvas?.layoutParams
            mVCanvas?.width = labelWidth.toDouble().toInt().mmToPx()
            mVCanvas?.height = labelHeight.toDouble().toInt().mmToPx()
            binding.mCanvas?.layoutParams = mVCanvas

            // 获取 标签子类数量
            val mNumber = document.select("ObjectList").attr("Count").toInt()

            val drawObject = document.select("DrawObject")
            for (i in 0 until mNumber) {
                val mType = drawObject[i].select("Name").text()

                if (mType.contains("WinText")) {
                    val mTextView = TextView(this)
                    mTextView.text = drawObject[i].select("OriginalData").text()
                    mTextView.textSize = (drawObject[i].select("Font").attr("FontSize").toFloat()*1.5).toFloat()
                    mTextView.setTextColor(Color.BLACK)
                    if (drawObject[i].select("Font").attr("FontStyle").contains("Bold")) {
                        mTextView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD)
                    }

                    val layoutParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.leftMargin = (drawObject[i].select("StartX").text().toInt() / mProportion).toInt().mmToPx()
                    layoutParams.topMargin = (drawObject[i].select("StartY").text().toInt() / mProportion).toInt().mmToPx()

                    binding.mCanvas?.addView(mTextView, layoutParams)

                }

                if (mType.contains("Barcode")) {
                    val mImageView = ImageView(this)
                    val decodedBytes: ByteArray = Base64.decode(drawObject[i].select("BarcodeImage").text(), Base64.DEFAULT)
                    val decodedBitmap =
                        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                    mImageView.setImageBitmap(decodedBitmap)
                    val layoutParams = RelativeLayout.LayoutParams(
                        (drawObject[i].select("Width").text().toInt() / mProportion).toInt().mmToPx(),
                        (drawObject[i].select("Height").text().toInt() / mProportion ).toInt().mmToPx()
                    )
                    layoutParams.leftMargin = (drawObject[i].select("StartX").text().toInt() / mProportion).toInt().mmToPx()
                    layoutParams.topMargin = (drawObject[i].select("StartY").text().toInt() / mProportion).toInt().mmToPx()

                    mImageView.scaleType = ImageView.ScaleType.CENTER_CROP

                    binding.mCanvas?.addView(mImageView,layoutParams)
                }

                if (mType.contains("Image")){
                    val mImageView = ImageView(this)
                    val decodedBytes: ByteArray = Base64.decode(drawObject[i].select("OriginalImage").text(), Base64.DEFAULT)
                    val decodedBitmap =
                        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                    mImageView.setImageBitmap(decodedBitmap)
                    val layoutParams = RelativeLayout.LayoutParams(
                        (drawObject[i].select("Width").text().toInt() / mProportion).toInt().mmToPx(),
                        (drawObject[i].select("Height").text().toInt() / mProportion ).toInt().mmToPx()
                    )
                    layoutParams.leftMargin = (drawObject[i].select("StartX").text().toInt() / mProportion).toInt().mmToPx()
                    layoutParams.topMargin = (drawObject[i].select("StartY").text().toInt() / mProportion).toInt().mmToPx()

                    mImageView.scaleType = ImageView.ScaleType.CENTER_CROP

                    binding.mCanvas?.addView(mImageView,layoutParams)
                }
            }
            // 关闭 InputStream
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun Int.mmToPx(): Int {
        val dpi = Resources.getSystem().displayMetrics.densityDpi
        return this * dpi / 31
    }

}
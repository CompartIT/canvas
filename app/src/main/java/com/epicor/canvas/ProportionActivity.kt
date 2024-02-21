package com.epicor.canvas

import android.app.AlertDialog
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import com.epicor.canvas.databinding.ActivityProportionBinding
import com.gprinter.utils.Command
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException
import java.io.InputStream


class ProportionActivity : AppCompatActivity() {

    private var printer: Printer? = null
    private val portManager = Printer.getPortManager()

    private lateinit var binding: ActivityProportionBinding

    private var mProportion = 0.0


    private var handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                0x00 -> {
                    val tip = msg.obj as String
                    Toast.makeText(this@ProportionActivity, tip, Toast.LENGTH_SHORT).show()
                }

                0x01 -> {
                    when (msg.arg1) {
                        -1 -> { //获取状态失败
                            val alertDialog = AlertDialog.Builder(this@ProportionActivity)
                                .setTitle(getString(R.string.tip))
                                .setMessage(getString(R.string.status_fail))
                                .setIcon(R.mipmap.ic_launcher)
                                .setPositiveButton(
                                    getString(R.string.ok)
                                ) //添加"Yes"按钮
                                { _, _ -> }
                                .create()
                            alertDialog.show()
                            return
                        }

                        1 -> {
                            Toast.makeText(
                                this@ProportionActivity,
                                getString(R.string.status_feed),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            return
                        }

                        0 -> { //状态正常
                            Toast.makeText(
                                this@ProportionActivity,
                                getString(R.string.status_normal),
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }

                        -2 -> { //状态缺纸
                            Toast.makeText(
                                this@ProportionActivity,
                                getString(R.string.status_out_of_paper),
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }

                        -3 -> { //状态开盖
                            Toast.makeText(
                                this@ProportionActivity,
                                getString(R.string.status_open),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            return
                        }

                        -4 -> {
                            Toast.makeText(
                                this@ProportionActivity,
                                getString(R.string.status_overheated),
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }
                    }
                }

                0x02 -> {
                    Thread {
                        if (portManager != null) {
                            Printer.close()
                            Printer.getInstance().devices
                        }
                    }.start()
//                    tvState.setText(getString(R.string.not_connected))
                }

                0x03 -> {
                    val message = msg.obj as String
                    val alertDialog = AlertDialog.Builder(this@ProportionActivity)
                        .setTitle(getString(R.string.tip))
                        .setMessage(message)
                        .setIcon(R.mipmap.ic_launcher)
                        .setPositiveButton(
                            getString(R.string.ok)
                        ) //添加"Yes"按钮
                        { _, _ -> }
                        .create()
                    alertDialog.show()
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProportionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initData()

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

        binding.mPrint?.setOnClickListener {


            repeat(1) {
                val thread = Thread {
                    portManager?.let {
                        Log.e("测试是否进来","进来了1")
                        //打印前后查询打印机状态，部分老款打印机不支持查询请去除下面查询代码
                        val command: Command = Printer.getPrinterCommand()
                        val status: Int = Printer.getPrinterState(command, 2000)
                        if (status != 0) { //打印机处于不正常状态,则不发送打印任务
                            val msg = Message()
                            msg.what = 0x01
                            msg.arg1 = status
                            handler.sendMessage(msg)
                            return@Thread
                        }
                        val bitmap = binding.mCanvas?.let { it1 -> getBitmapFromView(it1) }
                        //***************************************************************
                        val result: Boolean = portManager.writeDataImmediately(
                            PrintContent.getXmlBitmap(this,bitmap)
                        )

                        runOnUiThread {
                            // 在这里更新UI
                            binding.mImage?.setImageBitmap(bitmap)
                        }


                        if (result) {
                            ToastUtils.showShort(getString(R.string.send_success))
                        } else {
                            ToastUtils.showShort(getString(R.string.send_fail))
                        }
                    }
                }
                thread.start()
                thread.join() // 等待当前线程执行完毕
            }


        }
    }

    private fun getBitmapFromView(view: View): Bitmap? {
        val mCanvas = binding.mCanvas
        mCanvas?.isDrawingCacheEnabled = true
        mCanvas?.buildDrawingCache()
        val bitmap = mCanvas?.drawingCache?.let { Bitmap.createBitmap(it) }
        mCanvas?.isDrawingCacheEnabled = false
        return  bitmap
    }


    private fun Int.mmToPx(): Int {
        val dpi = Resources.getSystem().displayMetrics.densityDpi
        return this * dpi / 31
    }

    private fun initData() {
        printer = Printer.getInstance()
    }

}
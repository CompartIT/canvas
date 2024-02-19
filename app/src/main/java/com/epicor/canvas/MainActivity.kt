package com.epicor.canvas

import android.content.Intent
import android.hardware.usb.UsbDevice
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.epicor.canvas.databinding.ActivityMainBinding
import com.gprinter.bean.PrinterDevices
import com.gprinter.utils.CallbackListener
import com.gprinter.utils.Command
import com.gprinter.utils.ConnMethod

class MainActivity : AppCompatActivity(), CallbackListener {


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.mUSB?.setOnClickListener {
            startActivityForResult(Intent(this, BluetoothDeviceActivity::class.java), 0x01)
        }

        binding.mCanvas?.setOnClickListener {
            startActivity(Intent(this,CanvasActivity::class.java))
        }

        binding.Proportion?.setOnClickListener { startActivity(Intent(this,ProportionActivity::class.java)) }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                0x01 -> {
                    val name = data!!.getStringExtra(BluetoothDeviceActivity.USB_NAME)
//                    binding.mTopText?.text = name
                    val usbDevice: UsbDevice = Utils.getUsbDeviceFromName(this, name)
                    val usb = PrinterDevices.Build()
                        .setContext(this)
                        .setConnMethod(ConnMethod.USB)
                        .setUsbDevice(usbDevice)
                        .setCommand(Command.TSC)
                        .setCallbackListener(this)
                        .build()
                    Printer.connect(usb)
                }

            }
        }

    }

    override fun onConnecting() {
        Log.e("设备连接", "onConnecting")
    }

    override fun onCheckCommand() {
        Log.e("设备连接", "onCheckCommand")
    }

    override fun onSuccess(printerDevices: PrinterDevices?) {
        Log.e("设备连接", "onSuccess")
    }

    override fun onReceive(data: ByteArray?) {
        Log.e("设备连接", "onReceive")
    }

    override fun onFailure() {
        Log.e("设备连接", "onFailure")
    }

    override fun onDisconnect() {
        Log.e("设备连接", "onDisconnect")
    }
}
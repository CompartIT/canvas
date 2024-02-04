package com.epicor.canvas


import android.annotation.SuppressLint
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.epicor.canvas.databinding.ActivityBluetoothDeviceBinding


@Suppress("DEPRECATION")
class BluetoothDeviceActivity : AppCompatActivity() {


    private val TAG: String = BluetoothDeviceActivity::class.java.simpleName

    /**
     * Member fields
     */
    private var adapter: ArrayAdapter<String>? = null

    companion object {
        const val USB_NAME = "usb_name"
    }

    private var manager: UsbManager? = null

    private lateinit var binding: ActivityBluetoothDeviceBinding

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBluetoothDeviceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initView()

        getUsbDeviceList()

    }

    private fun initView() {
        adapter = ArrayAdapter(this, R.layout.text_item)
        binding.listView.onItemClickListener = mDeviceClickListener
        binding.listView.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        if (manager != null) {
            manager = null
        }
    }

    /**
     * 获取USB设备列表
     */
    fun getUsbDeviceList() {
        manager = getSystemService(USB_SERVICE) as UsbManager
        // Get the list of attached devices
        val devices = manager!!.deviceList
        val deviceIterator: Iterator<UsbDevice> = devices.values.iterator()
        val count = devices.size
        Log.d(TAG, "count $count")
        if (count > 0) {
            while (deviceIterator.hasNext()) {
                val device = deviceIterator.next()
                val devicename = device.deviceName
                if (checkUsbDevicePidVid(device)) {
                    adapter!!.add(devicename)
                }
            }
        } else {
            val noDevices = resources.getText(R.string.none_usb_device).toString()
            Log.d(TAG, "noDevices $noDevices")
            adapter!!.add(noDevices)
        }
    }


    /**
     * 检查USB设备的PID与VID
     * @param dev
     * @return
     */
    fun checkUsbDevicePidVid(dev: UsbDevice): Boolean {
        val pid = dev.productId
        val vid = dev.vendorId
        return vid == 34918 && pid == 256 || vid == 1137 && pid == 85 || vid == 6790 && pid == 30084 || vid == 26728 && pid == 256 || vid == 26728 && pid == 512 || vid == 26728 && pid == 256 || vid == 26728 && pid == 768 || vid == 26728 && pid == 1024 || vid == 26728 && pid == 1280 || vid == 26728 && pid == 1536
    }

    private val mDeviceClickListener =
        OnItemClickListener { av, v, arg2, arg3 -> // Cancel discovery because it's costly and we're about to connect
            // Get the device MAC address, which is the last 17 chars in the View
            val info = (v as TextView).text.toString()
            val noDevices = resources.getText(R.string.none_usb_device).toString()
            if (info != noDevices) {
                // Create the result Intent and include the MAC address
                val intent = Intent()
                intent.putExtra(USB_NAME, info)
                // Set result and finish this Activity
                setResult(RESULT_OK, intent)
                finish()
            }

        }

}
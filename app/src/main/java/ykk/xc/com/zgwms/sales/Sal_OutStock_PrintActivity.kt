package ykk.xc.com.zgwms.sales

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.hardware.usb.UsbManager
import android.os.Handler
import android.os.Message
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import butterknife.OnClick
import com.gprinter.command.EscCommand
import com.gprinter.command.LabelCommand
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import kotlinx.android.synthetic.main.sal_out_stock_print.*
import okhttp3.*
import ykk.xc.com.zgwms.R
import ykk.xc.com.zgwms.bean.ExpressNoData
import ykk.xc.com.zgwms.bean.User
import ykk.xc.com.zgwms.comm.BaseActivity
import ykk.xc.com.zgwms.comm.BaseFragment
import ykk.xc.com.zgwms.comm.Comm
import ykk.xc.com.zgwms.sales.adapter.Sal_OutStock_SaoMaPrintAdapter
import ykk.xc.com.zgwms.util.JsonUtil
import ykk.xc.com.zgwms.util.LogUtil
import ykk.xc.com.zgwms.util.blueTooth.*
import java.io.IOException
import java.lang.ref.WeakReference
import kotlin.collections.ArrayList

/**
 * 快递单打印（已经预约的）
 */
class Sal_OutStock_PrintActivity : BaseActivity() {

    companion object {
        private val SUCC1 = 200
        private val UNSUCC1 = 500

        private val SETFOCUS = 1
        private val SAOMA = 2
        private val WRITE_CODE = 3
    }
    private val context = this
    private val listDatas = ArrayList<ExpressNoData>()
    private var mAdapter: Sal_OutStock_SaoMaPrintAdapter? = null
    private val okHttpClient = OkHttpClient()
    private var isTextChange: Boolean = false // 是否进入TextChange事件
    private var user: User? = null

    // 蓝牙打印用到的
    private var isConnected: Boolean = false // 蓝牙是否连接标识
    private val id = 0 // 设备id
    private var threadPool: ThreadPool? = null
    private val CONN_STATE_DISCONN = 0x007 // 连接状态断开
    private val PRINTER_COMMAND_ERROR = 0x008 // 使用打印机指令错误
    private val CONN_PRINTER = 0x12
    private var listMap = ArrayList<ExpressNoData>() // 打印保存的数据

    // 消息处理
    private val mHandler = MyHandler(this)

    private class MyHandler(activity: Sal_OutStock_PrintActivity) : Handler() {
        private val mActivity: WeakReference<Sal_OutStock_PrintActivity>

        init {
            mActivity = WeakReference(activity)
        }

        override fun handleMessage(msg: Message) {
            val m = mActivity.get()
            if (m != null) {
                m.hideLoadDialog()

                var errMsg: String? = null
                var msgObj: String? = null
                if (msg.obj is String) {
                    msgObj = msg.obj as String
                }
                when (msg.what) {
                    SUCC1 -> { // 扫码成功 进入
                        m.listDatas.clear()
                        val list = JsonUtil.strToList(msgObj, ExpressNoData::class.java)
                        m.listDatas.addAll(list)
                        m.mAdapter!!.notifyDataSetChanged()
                    }
                    UNSUCC1 -> { // 扫码失败
                        m.listDatas.clear()
                        m.mAdapter!!.notifyDataSetChanged()

                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "很抱歉，没有找到数据！"
                        Comm.showWarnDialog(m.context, errMsg)
                    }
                    SETFOCUS -> { // 当弹出其他窗口会抢夺焦点，需要跳转下，才能正常得到值
                        m.setFocusable(m.et_getFocus)
                        m.setFocusable(m.et_code)
                    }
                    SAOMA -> { // 扫码之后
                        // 执行查询方法
                        m.run_smDatas()
                    }
                }
            }
        }
    }

    override fun setLayoutResID(): Int {
        return R.layout.sal_out_stock_print
    }

    override fun initView() {
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter = Sal_OutStock_SaoMaPrintAdapter(context!!, listDatas)
        recyclerView.adapter = mAdapter
        // 设值listview空间失去焦点
        recyclerView.isFocusable = false

        // 行事件
        mAdapter!!.setCallBack(object : Sal_OutStock_SaoMaPrintAdapter.MyCallBack {
            override fun onPrint(entity: ExpressNoData, position: Int) {
                setPrintData(entity)
                listDatas[position].isCheck = true
                mAdapter!!.notifyDataSetChanged()
            }
        })
    }

    override fun initData() {
        hideSoftInputMode(et_code)
        mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
        getUserInfo()
    }


    // 监听事件
    @OnClick(R.id.btn_close, R.id.btn_scan, R.id.btn_clone)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_close -> {
                closeHandler(mHandler)
                context.finish()
            }
            R.id.btn_scan -> { // 调用摄像头扫描（物料）
                ScanUtil.startScan(context, BaseFragment.CAMERA_SCAN, HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create());
            }
            R.id.btn_clone -> {
                et_code.setText("")
                listDatas.clear()
                mAdapter!!.notifyDataSetChanged()
                mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
            }
        }
    }

    override fun setListener() {
        val click = View.OnClickListener { v ->
            setFocusable(et_getFocus)
            when (v.id) {
                R.id.et_code -> setFocusable(et_code)
            }
        }
        et_code!!.setOnClickListener(click)

        // 物料---数据变化
        et_code!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0) return
                if (!isTextChange) {
                    isTextChange = true
                    mHandler.sendEmptyMessageDelayed(SAOMA, 300)
                }
            }
        })
        // 物料---长按输入条码
        et_code!!.setOnLongClickListener {
            showInputDialog("输入条码号", getValues(et_code), "none", WRITE_CODE)
            true
        }
        // 物料---焦点改变
        et_code.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                lin_focusMtl.setBackgroundResource(R.drawable.back_style_red_focus)
            } else {
                if (lin_focusMtl != null) {
                    lin_focusMtl.setBackgroundResource(R.drawable.back_style_gray4)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                BaseFragment.CAMERA_SCAN -> {// 扫一扫成功  返回
                    val hmsScan = data!!.getParcelableExtra(ScanUtil.RESULT) as HmsScan
                    if (hmsScan != null) {
                        setTexts(et_code, hmsScan.originalValue)
                    }
                }
                WRITE_CODE -> {// 输入条码  返回
                    val bundle = data!!.extras
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        setTexts(et_code, value.toUpperCase())
                    }
                }
                /*蓝牙连接*/
                Constant.BLUETOOTH_REQUEST_CODE -> {
                    /*获取蓝牙mac地址*/
                    val macAddress = data!!.getStringExtra(BluetoothDeviceListDialog.EXTRA_DEVICE_ADDRESS)
                    //初始化话DeviceConnFactoryManager
                    DeviceConnFactoryManager.Build()
                            .setId(id)
                            //设置连接方式
                            .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.BLUETOOTH)
                            //设置连接的蓝牙mac地址
                            .setMacAddress(macAddress)
                            .build()
                    //打开端口
                    DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].openPort()
                }
            }
            mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
        }
    }

    /**
     * 扫码查询对应的方法
     */
    private fun run_smDatas() {
        isTextChange = false
        showLoadDialog("加载中...", false)
        var mUrl = getURL("appPrint/printExpressNoBySaoMa")
        val formBody = FormBody.Builder()
                .add("barcode", getValues(et_code))
                .add("userName", user!!.username)
                .build()

        val request = Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNSUCC1)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                LogUtil.e("run_smDatas --> onResponse", result)
                if (!JsonUtil.isSuccess(result)) {
                    val msg = mHandler.obtainMessage(UNSUCC1, result)
                    mHandler.sendMessage(msg)
                    return
                }
                val msg = mHandler.obtainMessage(SUCC1, result)
                mHandler.sendMessage(msg)
            }
        })
    }

    /**
     * 设置打印数据
     */
    fun setPrintData(data: ExpressNoData) {
        listMap.clear()
        listMap.add(data)

        if (isConnected) {
            startPrint(listMap)
        } else {
            // 打开蓝牙配对页面
            startActivityForResult(Intent(this, BluetoothDeviceListDialog::class.java), Constant.BLUETOOTH_REQUEST_CODE)
        }
    }

    /**
     * 原丰蜜打印
     */
    private fun startPrint(list: List<ExpressNoData>) {
        list.forEach {
            val curDate = Comm.getSysDate(0)
            val tsc = LabelCommand()
            setTscBegin(tsc, 10)
            // --------------- 打印区-------------Begin

            // 上下流水结构，先画线后打印其他
            // （左）画竖线
            tsc.addBar(20, 200, 2, 610)
            // （右上）画横线
            tsc.addBar(20, 200, 560, 2)
            // （右）画竖线
            tsc.addBar(580, 200, 2, 610)
            // （左下）画横线
            tsc.addBar(20, 810, 560, 2)

            // 面横线（寄方月结上边）
            tsc.addBar(20, 380, 560, 2)
            // 面横线（寄方月结下边）1横线
            tsc.addBar(20, 420, 200, 2)
            // 面横线（寄方月结下边）2横线
            tsc.addBar(20, 480, 200, 2)
            // 画竖线（寄方月结右边）
            tsc.addBar(220, 380, 2, 170)
            // 画竖线（二维码右面）
            tsc.addBar(400, 380, 2, 170)
            /*
            // 画竖线（已验视右边）
            tsc.addBar(450, 380, 2, 170)
            // 画横线（AB表下面）
            tsc.addBar(452, 480, 128, 2)
            */
            // 画横线（寄件人上面）
            tsc.addBar(20, 550, 560, 2)
            // 画横线（寄件人下面）
            tsc.addBar(20, 680, 560, 2)


            // 打印时间
            tsc.addText(220, 40, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "打印时间："+curDate)
            // （左）条码
            tsc.add1DBarcode(40, 90, LabelCommand.BARCODETYPE.CODE39, 80, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0, 2, 5, it.getT01())   // 顺丰快递单
            // 目的地
            tsc.addText(50, 210, LabelCommand.FONTTYPE.FONT_2, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_3, LabelCommand.FONTMUL.MUL_3, it.getT02()+"")   // 目的地
            tsc.addText(472, 210, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2, "电商")   // 电商标快
            tsc.addText(472, 260, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2, "标快")   // 电商标快

            // 收件人图片
            val shouBit = BitmapFactory.decodeResource(resources, R.drawable.shunfeng_shou)
            if(shouBit != null) {
                tsc.addBitmap(40, 290, LabelCommand.BITMAP_MODE.OVERWRITE, 53, shouBit)
            }
            tsc.addText(115, 290, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, it.getT10()+"") // 收方人
            tsc.addText(260, 290, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, it.getT11()+"") // 收方电话
            // 收件地址超长，计算自动换行（计算两行）
            val t12 = it.getT12()
            val t12Len = t12!!.length
            if(t12Len > 20) { // 每一行最多显示20个字符串
                tsc.addText(115, 320, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, t12.substring(0, 20)+ "") // 收方地址
                if(t12.substring(20, t12Len).trim().length > 0) {
                    tsc.addText(115, 350, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, t12.substring(20, t12Len)+ "") // 收方地址
                }
            } else {
                tsc.addText(115, 320, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, t12+ "") // 收方地址
            }

            // 寄付月结
            tsc.addText(60, 386, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, it.getT05()+"")
            // 寄付月结下面1
            tsc.addText(100, 435, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2, "020")
            // 寄付月结下面2
            tsc.addText(40, 498, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2, it.destTeamCode)
            // 顺丰二维码图片
            if(it.twoDimensionCode.length > 0) {
                tsc.addQRCode(230, 390, LabelCommand.EEC.LEVEL_L, 4, LabelCommand.ROTATION.ROTATION_0, it.twoDimensionCode)
            }
            // 显示销售订单号
            val listSalOrderNo = it.salOrderNo.split(",")
            var yCoune = 0
            for (s in listSalOrderNo) {
                tsc.addText(408, (386+yCoune), LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, s + "")
                yCoune += 30
                if(yCoune == 150) break
            }

            // 寄件人图片
            val jiBit = BitmapFactory.decodeResource(resources, R.drawable.shunfeng_ji)
            tsc.addBitmap(40, 580, LabelCommand.BITMAP_MODE.OVERWRITE, 53, jiBit)
            // 寄方人
            tsc.addText(115, 566, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, it.getT15()+"")
            // 寄方电话
            tsc.addText(260, 566, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, it.getT16()+"")
            // 寄方地址
            // 寄方地址超长，计算自动换行（计算两行）
            val t17 = it.getT17()
            val t17Len = t17!!.length
            if(t17Len > 20) {
                tsc.addText(115, 590, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, t17.substring(0, 20)+ "") // 寄方地址
                if(t17.substring(20, t17Len).trim().length > 0) {
                    tsc.addText(115, 620, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, t17.substring(20, t17Len)+ "") // 寄方地址
                }
            } else {
                tsc.addText(115, 590, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, t17+ "") // 收方地址
            }

            // 寄托物
            tsc.addText(30, 690, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "寄托物："+it.t18+"")
            tsc.addText(30, 720, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "增值服务： ")
            tsc.addText(30, 750, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "备注： ")

            // --------------- 打印区-------------End
            setTscEnd(tsc)
        }
    }

    /**
     * 打印前段配置
     * @param tsc
     */
    private fun setTscBegin(tsc: LabelCommand, gap: Int) {
        // 设置标签尺寸，按照实际尺寸设置
        tsc.addSize(75, 105)
        // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addGap(gap)
        // 设置打印方向
        tsc.addDirection(LabelCommand.DIRECTION.FORWARD, LabelCommand.MIRROR.NORMAL)
        // 开启带Response的打印，用于连续打印
        tsc.addQueryPrinterStatus(LabelCommand.RESPONSE_MODE.ON)
        // 设置原点坐标
        tsc.addReference(0, 0)
        // 撕纸模式开启
        tsc.addTear(EscCommand.ENABLE.ON)
        // 清除打印缓冲区
        tsc.addCls()
    }

    /**
     * 打印后段配置
     * @param tsc
     */
    private fun setTscEnd(tsc: LabelCommand) {
        // 打印标签
        tsc.addPrint(1, 1)
        // 打印标签后 蜂鸣器响

        tsc.addSound(2, 100)
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255)
        val datas = tsc.command
        // 发送数据
        if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] == null) {
            return
        }
        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(datas)
    }

    /**
     * 蓝牙监听广播
     */
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            when (action) {
                // 蓝牙连接断开广播
                UsbManager.ACTION_USB_DEVICE_DETACHED, BluetoothDevice.ACTION_ACL_DISCONNECTED -> mHandler.obtainMessage(CONN_STATE_DISCONN).sendToTarget()
                DeviceConnFactoryManager.ACTION_CONN_STATE -> {
                    val state = intent.getIntExtra(DeviceConnFactoryManager.STATE, -1)
                    val deviceId = intent.getIntExtra(DeviceConnFactoryManager.DEVICE_ID, -1)
                    when (state) {
                        DeviceConnFactoryManager.CONN_STATE_DISCONNECT -> if (id == deviceId) {
                            tv_connState.setText(getString(R.string.str_conn_state_disconnect))
                            tv_connState.setTextColor(Color.parseColor("#666666")) // 未连接-灰色
                            isConnected = false
                        }
                        DeviceConnFactoryManager.CONN_STATE_CONNECTING -> {
                            tv_connState.setText(getString(R.string.str_conn_state_connecting))
                            tv_connState.setTextColor(Color.parseColor("#6a5acd")) // 连接中-紫色
                            isConnected = false
                        }
                        DeviceConnFactoryManager.CONN_STATE_CONNECTED -> {
                            //                            tv_connState.setText(getString(R.string.str_conn_state_connected) + "\n" + getConnDeviceInfo());
                            tv_connState.setText(getString(R.string.str_conn_state_connected))
                            tv_connState.setTextColor(Color.parseColor("#008800")) // 已连接-绿色
                            // 连接成功，开始打印
                            startPrint(listMap)

                            isConnected = true
                        }
                        DeviceConnFactoryManager.CONN_STATE_FAILED -> {
                            Utils.toast(context, getString(R.string.str_conn_fail))
                            tv_connState.setText(getString(R.string.str_conn_state_disconnect))
                            tv_connState.setTextColor(Color.parseColor("#666666")) // 未连接-灰色
                            isConnected = false
                        }
                        else -> {
                        }
                    }
                }
            }
        }
    }

    /**
     * 得到用户对象
     */
    private fun getUserInfo() {
        if (user == null) user = showUserByXml()
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter()
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        filter.addAction(DeviceConnFactoryManager.ACTION_CONN_STATE)
        registerReceiver(receiver, filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            closeHandler(mHandler)
            context.finish()
        }
        return false
    }

    override fun onDestroy() {
        closeHandler(mHandler)
        DeviceConnFactoryManager.closeAllPort()
        if (threadPool != null) {
            threadPool!!.stopThreadPool()
        }
        super.onDestroy()
    }

}

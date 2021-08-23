package ykk.xc.com.zgwms.sales

import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.*
import android.graphics.BitmapFactory
import android.graphics.Color
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import butterknife.OnClick
import com.gprinter.command.EscCommand
import com.gprinter.command.LabelCommand
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import kotlinx.android.synthetic.main.sal_out_stock_saoma2.*
import okhttp3.*
import ykk.xc.com.zgwms.R
import ykk.xc.com.zgwms.basics.Logistics_DialogActivity
import ykk.xc.com.zgwms.basics.Stock_DialogActivity
import ykk.xc.com.zgwms.bean.*
import ykk.xc.com.zgwms.bean.k3Bean.StockPosition_K3
import ykk.xc.com.zgwms.bean.k3Bean.Stock_K3
import ykk.xc.com.zgwms.bean.sales.SalOrder
import ykk.xc.com.zgwms.bean.sales.SalOrderEntry
import ykk.xc.com.zgwms.comm.BaseActivity
import ykk.xc.com.zgwms.comm.BaseFragment
import ykk.xc.com.zgwms.comm.Comm
import ykk.xc.com.zgwms.sales.adapter.Sal_OutStock_SaoMa2_Adapter
import ykk.xc.com.zgwms.util.BigdecimalUtil
import ykk.xc.com.zgwms.util.JsonUtil
import ykk.xc.com.zgwms.util.LogUtil
import ykk.xc.com.zgwms.util.basehelper.BaseRecyclerAdapter
import ykk.xc.com.zgwms.util.blueTooth.*
import ykk.xc.com.zgwms.util.blueTooth.Constant.MESSAGE_UPDATE_PARAMETER
import ykk.xc.com.zgwms.util.blueTooth.DeviceConnFactoryManager.CONN_STATE_FAILED
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

/**
 * 日期：2019-10-16 09:14
 * 描述：材料出库扫描
 * 作者：ykk
 */
class Sal_OutStock_SaoMa2Activity : BaseActivity() {

    companion object {
        private val SEL_STOCK = 60
        private val SEL_LOGISTICS = 62
        private val SUCC1 = 200
        private val UNSUCC1 = 500
        private val SUCC2 = 201
        private val UNSUCC2 = 501
        private val SUCC3 = 202
        private val UNSUCC3 = 502
        private val SAVE = 203
        private val UNSAVE = 503
        private val UPLOAD = 204
        private val UNUPLOAD = 504

        private val SETFOCUS = 1
        private val SAOMA = 2
        private val WRITE_CODE = 3
        private val RESULT_NUM = 4
        private val SM_RESULT_NUM = 5
        private val RESULT_YUYUE = 6
    }

    private val context = this
    private val TAG = "Sal_OutStock_SaoMa2Activity"
    private var okHttpClient: OkHttpClient? = null
    private var mAdapter: Sal_OutStock_SaoMa2_Adapter? = null
    private val checkDatas = ArrayList<ICStockBillEntry>()
    private var curPos = -1 // 当前行
    private var user: User? = null
    private var smFlag = '1' // 扫描类型1：销售订单号，2：物料扫描
    private var isTextChange: Boolean = false // 是否进入TextChange事件
    private val icstockBill = ICStockBill()
    private var salOrder :SalOrder? = null
    private var stock:Stock_K3? = null
    private var stockPos: StockPosition_K3? = null
    private var logistics :Logistics? = null    // 快递公司
    private var bt :BarCodeTable? = null

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
    private class MyHandler(activity: Sal_OutStock_SaoMa2Activity) : Handler() {
        private val mActivity: WeakReference<Sal_OutStock_SaoMa2Activity>

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
                    SUCC1 -> { // 销售订单号--扫码成功 进入
                        val list = JsonUtil.strToList(msgObj, SalOrderEntry::class.java)
                        m.salOrder = list[0].salOrder
                        var deliveryWayRemarkT = m.isNULLS(m.salOrder!!.deliveryWayRemark)
                        if(!deliveryWayRemarkT.equals("快递")) {// 非快递的无需预约
                            m.salOrder!!.deliveryWayRemark = "物流"
                            m.btn_appointment.visibility = View.INVISIBLE
                        }
                        m.tv_deliveryWayRemark.text = Html.fromHtml("发货方式:&nbsp;<font color='#6a5acd'>"+deliveryWayRemarkT+"</font>")
                        m.addICStockBillEntry(list)
                    }
                    UNSUCC1 -> { // 销售订单号--扫码失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "很抱歉，没有找到数据！"
                        Comm.showWarnDialog(m.context, errMsg)
                    }
                    SUCC2 -> { // 扫码成功 进入
                        val bt = JsonUtil.strToObject(msgObj, BarCodeTable::class.java)
                        m.bt = bt
                        m.getMtl()
                    }
                    UNSUCC2 -> { // 扫码失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "很抱歉，没有找到数据！"
                        Comm.showWarnDialog(m.context, errMsg)
                    }
                    SAVE -> { // 保存成功 进入
                        val strId_pdaNo = JsonUtil.strToString(msgObj)
                        if(m.icstockBill.id == 0) {
                            val arr = strId_pdaNo.split(":") // id和pdaNo数据拼接（1:IC201912121）
                            m.icstockBill.id = m.parseInt(arr[0])
                            m.icstockBill.pdaNo = arr[1]
                        }

                        m.toasts("已保存，正在上传")
                        m.btn_save.visibility = View.GONE
                        m.btn_upload.visibility = View.VISIBLE
                        m.btn_appointment.visibility = View.INVISIBLE

                        m.run_uploadToK3(m.icstockBill.id.toString())
                    }
                    UNSAVE -> { // 保存失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "保存失败！"
                        Comm.showWarnDialog(m.context, errMsg)
                    }
                    UPLOAD -> { // 上传成功 进入
                        m.reset()
                        m.toasts("已上传")
                    }
                    UNUPLOAD -> { // 上传失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "服务器繁忙，请稍后再试！"
                        if(errMsg!!.indexOf("库存") > -1) {
                            m.btn_upload.visibility = View.GONE
                        }
                        Comm.showWarnDialog(m.context, errMsg)
                    }
                    SUCC3 -> { // 预约数据 进入
                        m.toasts("预约成功，准备打印...")
                        val list = JsonUtil.strToList(msgObj, ExpressNoData::class.java)
                        m.print(list) // 打印
                        val strExpressNo = StringBuffer()
                        strExpressNo.append(m.isNULLS(m.icstockBill.expressNo))
                        list.forEach {
                            if(strExpressNo.indexOf(it.t01) == -1) { // 不存在的时候，就保存起来
                                if(strExpressNo.length == 0) {
                                    strExpressNo.append(it.t01)
                                } else {
                                    strExpressNo.append("," + it.t01)
                                }
                            }
                        }
                        m.icstockBill.expressNo = strExpressNo.toString()
                        // 预约成功了，只能点击保存，不能扫码
                        m.et_code.isEnabled = false
                        m.btn_scan.isEnabled = false
                        m.lin_focusMtl.setBackgroundResource(R.drawable.back_style_gray1a)
                    }
                    UNSUCC3 -> { // 预约数据  失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "app预约失败！"
                        Comm.showWarnDialog(m.context, errMsg)
                    }
                    SETFOCUS -> { // 当弹出其他窗口会抢夺焦点，需要跳转下，才能正常得到值
                        m.setFocusable(m.et_getFocus)
                        when(m.smFlag) {
                            '1' -> m.setFocusable(m.et_orderNo)
                            '2' -> m.setFocusable(m.et_code)
                        }
                    }
                    SAOMA -> { // 扫码之后
                        when(m.smFlag) {
                            '1' -> {
                                m.run_findBarocdeByOrderNo()
                            }
                            '2' -> {
                                m.run_findBarcode()
                            }
                        }
                    }
                    m.CONN_STATE_DISCONN -> if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[m.id] != null) {
                        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[m.id].closePort(m.id)
                    }
                    m.PRINTER_COMMAND_ERROR -> Utils.toast(m.context, m.getString(R.string.str_choice_printer_command))
                    m.CONN_PRINTER -> Utils.toast(m.context, m.getString(R.string.str_cann_printer))
                    MESSAGE_UPDATE_PARAMETER -> {
                        val strIp = msg.data.getString("Ip")
                        val strPort = msg.data.getString("Port")
                        //初始化端口信息
                        DeviceConnFactoryManager.Build()
                                //设置端口连接方式
                                .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.WIFI)
                                //设置端口IP地址
                                .setIp(strIp)
                                //设置端口ID（主要用于连接多设备）
                                .setId(m.id)
                                //设置连接的热点端口号
                                .setPort(Integer.parseInt(strPort))
                                .build()
                        m.threadPool = ThreadPool.getInstantiation()
                        m.threadPool!!.addTask(Runnable { DeviceConnFactoryManager.getDeviceConnFactoryManagers()[m.id].openPort() })
                    }
                }
            }
        }
    }

    override fun setLayoutResID(): Int {
        return R.layout.sal_out_stock_saoma2
    }

    override fun initView() {
        if (okHttpClient == null) {
            okHttpClient = OkHttpClient.Builder()
                    //                .connectTimeout(10, TimeUnit.SECONDS) // 设置连接超时时间（默认为10秒）
                    .writeTimeout(120, TimeUnit.SECONDS) // 设置写的超时时间
                    .readTimeout(120, TimeUnit.SECONDS) //设置读取超时时间
                    .build()
        }

        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter = Sal_OutStock_SaoMa2_Adapter(context, checkDatas)
        recyclerView.adapter = mAdapter
        // 设值listview空间失去焦点
        recyclerView.isFocusable = false

        // 行事件
        mAdapter!!.setCallBack(object : Sal_OutStock_SaoMa2_Adapter.MyCallBack {
            override fun onDelete(entity: ICStockBillEntry, pos: Int) {
                if(isNULLS(checkDatas[pos].icstockBill.expressNo).length > 0) {
                    Comm.showWarnDialog(context,"预约过的数据不能删除！")
                    return
                }
                checkDatas.removeAt(pos)
                mAdapter!!.notifyDataSetChanged()
            }
        })
        // 输入数量
        mAdapter!!.onItemClickListener = BaseRecyclerAdapter.OnItemClickListener { adapter, holder, view, pos ->
            if(checkDatas[pos].icstockBill.id > 0) return@OnItemClickListener
            val mtl = checkDatas[pos].material
            if(mtl.isSnManager == 1 || mtl.isBatchManager == 1) return@OnItemClickListener

            curPos = pos
            showInputDialog("出库数", checkDatas[pos].fqty.toString(), "0.0", RESULT_NUM)
        }
        // 长按选择仓库
        mAdapter!!.onItemLongClickListener = BaseRecyclerAdapter.OnItemLongClickListener{ adapter, holder, view, pos ->
            if(checkDatas[pos].icstockBill.id > 0) return@OnItemLongClickListener

            curPos = pos
            val bundle = Bundle()
            bundle.putInt("fuseOrgId", icstockBill.fstockOrgId)
            showForResult(Stock_DialogActivity::class.java, SEL_STOCK, bundle)
        }
    }

    override fun initData() {
        getUserInfo()
        bundle()
        hideSoftInputMode(et_orderNo)
        mHandler.sendEmptyMessage(SETFOCUS)

        if(user!!.organization == null) {
            Comm.showWarnDialog(context,"登陆的用户没有维护组织，请在WMS维护！3秒后自动关闭...")
            mHandler.postDelayed(Runnable {
                context!!.finish()
            },3000)
            return
        }

        icstockBill.billType = "XSCK" // 销售出库
        icstockBill.ftranType = 30
        icstockBill.fselTranType = 1
        icstockBill.empNumber = user!!.kdUserNumber
        icstockBill.empName = user!!.kdUserName
        icstockBill.stockManagerNumber = user!!.kdUserNumber
        icstockBill.stockManagerName = user!!.kdUserName
        icstockBill.createUserId = user!!.id
        icstockBill.createUserName = user!!.username

        icstockBill.fstockOrgId = user!!.organizationId
        icstockBill.fstockOutOrgId = user!!.organizationId
        icstockBill.stockOrg = user!!.organization
        icstockBill.stockOutOrg = user!!.organization
        icstockBill.fbillTypeNumber = "XSCKD01_SYS" // 生产订单据类型（标准生产入库）
        icstockBill.fownerType = "BD_OwnerOrg"
        icstockBill.fownerNumber = user!!.organization.fnumber
        icstockBill.fownerName = user!!.organization.fname
        icstockBill.expressNo = ""

        // 显示记录的本地仓库
        val saveDefaultStock = getResStr(R.string.saveDefaultStock)
        val spfStock = spf(saveDefaultStock)
        // 显示仓库---------------------
        if(spfStock.contains(icstockBill.stockOrg.fnumber+"_BIND_SAL_OUT_STOCK")) {
            stock = showObjectByXml(Stock_K3::class.java, icstockBill.stockOrg.fnumber+"_BIND_SAL_OUT_STOCK", saveDefaultStock)
        }
    }

    private fun bundle() {
        val bundle = context.intent.extras
        if (bundle != null) {
        }
    }

    @OnClick(R.id.btn_close, R.id.btn_orderScan, R.id.btn_scan, R.id.btn_save, R.id.btn_clone, R.id.btn_upload, R.id.btn_appointment)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_close -> { // 关闭
                if (checkDatas.size > 0) {
                    val build = AlertDialog.Builder(context)
                    build.setIcon(R.drawable.caution)
                    build.setTitle("系统提示")
                    build.setMessage("您有未保存的数据，继续关闭吗？")
                    build.setPositiveButton("是", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            context.finish()
                        }
                    })
                    build.setNegativeButton("否", null)
                    build.setCancelable(false)
                    build.show()

                } else {
                    context.finish()
                }
            }
            R.id.btn_appointment -> { // 预约
                if(checkDatas.size == 0) {
                    Comm.showWarnDialog(context,"请先扫描条码，然后预约！")
                    return
                }
                /*if(salOrderEntry!!.combineSalOrderId > 0 && checkDatas.size < salOrderEntry!!.combineSalOrderSumRow) {
                    toasts("当前货物未扫完，不能预约！")
                    val map = HashMap<Int, Boolean>()
                    checkDatas.forEach {
                        map.put(it.forderEntryId, true)
                    }
                    val bundle = Bundle()
                    bundle.putInt("combineSalOrderId", salOrderEntry!!.combineSalOrderId)
                    bundle.putSerializable("mapEntryId", map)
                    show(Sal_OutStock_MtlShowActivity::class.java, bundle)
                    return
                }*/
                showForResult(Logistics_DialogActivity::class.java, SEL_LOGISTICS, null)
            }
            R.id.btn_orderScan -> { // 调用摄像头扫描（订单号）
                smFlag = '1'
                ScanUtil.startScan(context, BaseFragment.CAMERA_SCAN, HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create())
            }
            R.id.btn_scan -> { // 调用摄像头扫描（物料）
                smFlag = '2'
                ScanUtil.startScan(context, BaseFragment.CAMERA_SCAN, HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create())
            }
            R.id.btn_save -> { // 保存
                if(checkDatas.size == 0) {
                    Comm.showWarnDialog(context,"请扫描条码！")
                    return
                }
                checkDatas.forEachIndexed { index, it ->
                    if(it.fqty == 0.0) {
                        Comm.showWarnDialog(context,"第"+(index+1)+"行，请输入数量！")
                        return
                    }
                    if(it.fqty > it.fsourceQty) {
                        Comm.showWarnDialog(context,"第"+(index+1)+"行，出库数量不能大于源单数！")
                        return
                    }
                    if(it.fdcStockId == 0) {
                        Comm.showWarnDialog(context,"第"+(index+1)+"行，请选择仓库！")
                        return
                    }
                    it.icstockBill = icstockBill
                }
                if(salOrder!!.deliveryWayRemark.equals("快递") && isNULLS(icstockBill.expressNo).length == 0) {
                    Comm.showWarnDialog(context,"请先预约快递单号！")
                    return
                }
                /*if(deliveryWayRemark.equals("物流") && salOrderEntry!!.combineSalOrderId > 0 && checkDatas.size < salOrderEntry!!.combineSalOrderSumRow) {
                    toasts("当前货物未扫完，请检查！")
                    val map = HashMap<Int, Boolean>()
                    checkDatas.forEach {
                        map.put(it.forderEntryId, true)
                    }

                    val bundle = Bundle()
                    bundle.putInt("combineSalOrderId", salOrderEntry!!.combineSalOrderId)
                    bundle.putSerializable("mapEntryId", map)
                    show(Sal_OutStock_MtlShowActivity::class.java, bundle)
                    return
                }*/
                run_save()
            }
            R.id.btn_upload -> { // 上传
                run_uploadToK3(icstockBill.id.toString())
            }
            R.id.btn_clone -> { // 重置
                if (checkDatas.size > 0) {
                    val build = AlertDialog.Builder(context)
                    build.setIcon(R.drawable.caution)
                    build.setTitle("系统提示")
                    build.setMessage("您有未保存的数据，继续重置吗？")
                    build.setPositiveButton("是") { dialog, which -> reset() }
                    build.setNegativeButton("否", null)
                    build.setCancelable(false)
                    build.show()

                } else reset()
            }
        }
    }

    private fun reset() {
        btn_save.visibility = View.VISIBLE
        btn_upload.visibility = View.GONE
        btn_appointment.visibility = View.VISIBLE
        et_orderNo.setText("")
        et_orderNo.isEnabled = true
        btn_orderScan.isEnabled = true
        lin_focusOrderNo.setBackgroundResource(R.drawable.back_style_blue)
        et_code.setText("")
        et_code.isEnabled = false
        btn_scan.isEnabled = false
        lin_focusMtl.setBackgroundResource(R.drawable.back_style_gray1a)
        tv_deliveryWayRemark.text = "发货方式："
        icstockBill.id = 0
        icstockBill.expressNo = ""
        checkDatas.clear()
        salOrder = null
        bt = null
        smFlag = '1'
        mHandler.sendEmptyMessage(SETFOCUS)

        mAdapter!!.notifyDataSetChanged()
    }

    override fun setListener() {
        val click = View.OnClickListener { v ->
            setFocusable(et_getFocus)
            when (v.id) {
                R.id.et_orderNo -> setFocusable(et_orderNo)
                R.id.et_code -> setFocusable(et_code)
            }
        }
        et_orderNo.setOnClickListener(click)
        et_code.setOnClickListener(click)

        // 订单号---数据变化
        et_orderNo!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0) return
                if (!isTextChange) {
                    isTextChange = true
                    smFlag = '1'
                    mHandler.sendEmptyMessageDelayed(SAOMA, 300)
                }
            }
        })
        // 订单号---长按输入条码
        et_orderNo!!.setOnLongClickListener {
            smFlag = '1'
            showInputDialog("输入条码号", getValues(et_orderNo), "none", WRITE_CODE)
            true
        }
        // 订单号---焦点改变
        et_orderNo.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                lin_focusOrderNo.setBackgroundResource(R.drawable.back_style_red_focus)
            } else {
                if (lin_focusOrderNo != null) {
                    lin_focusOrderNo.setBackgroundResource(R.drawable.back_style_gray4)
                }
            }
        }

        // 物料---数据变化
        et_code!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0) return
                if (!isTextChange) {
                    isTextChange = true
                    smFlag = '2'
                    mHandler.sendEmptyMessageDelayed(SAOMA, 300)
                }
            }
        })
        // 物料---长按输入条码
        et_code!!.setOnLongClickListener {
            smFlag = '2'
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

    /**
     *  扫码之后    物料启用批次
     */
    fun setBatchCode(fqty : Double) {
        val entryBarcode = ICStockBillEntry_Barcode()
        entryBarcode.parentId = 0
        entryBarcode.barcode = getValues(et_code)
        entryBarcode.batchCode = bt!!.batchCode
        entryBarcode.snCode = ""
        entryBarcode.fqty = fqty
        entryBarcode.isUniqueness = 'Y'
        entryBarcode.againUse = 1
        entryBarcode.createUserName = user!!.username
        entryBarcode.billType = "XSCK"

        var addVal = BigdecimalUtil.add(checkDatas[curPos].fqty, fqty)
        checkDatas[curPos].fqty = addVal
        checkDatas[curPos].icstockBillEntry_Barcodes.add(entryBarcode)
        mAdapter!!.notifyDataSetChanged()
    }

    /**
     *  扫码之后    物料启用序列号
     */
    fun setSnCode() {
        val entryBarcode = ICStockBillEntry_Barcode()
        entryBarcode.parentId = 0
        entryBarcode.barcode = getValues(et_code)
        entryBarcode.batchCode = ""
        entryBarcode.snCode = bt!!.snCode
        entryBarcode.fqty = 1.0
        entryBarcode.isUniqueness = 'Y'
        entryBarcode.againUse = 1
        entryBarcode.createUserName = user!!.username
        entryBarcode.billType = "XSCK"

        var addVal = BigdecimalUtil.add(checkDatas[curPos].fqty, 1.0)
        checkDatas[curPos].fqty = addVal
        checkDatas[curPos].icstockBillEntry_Barcodes.add(entryBarcode)
        mAdapter!!.notifyDataSetChanged()
    }

    /**
     *  扫码之后    物料未启用
     */
    fun unStartBatchOrSnCode(fqty : Double) {
        val entryBarcode = ICStockBillEntry_Barcode()
        entryBarcode.parentId = 0
        entryBarcode.barcode = getValues(et_code)
        entryBarcode.batchCode = ""
        entryBarcode.snCode = ""
        entryBarcode.fqty = fqty
        entryBarcode.isUniqueness = 'N'
        entryBarcode.againUse = 1
        entryBarcode.createUserName = user!!.username
        entryBarcode.billType = "XSCK"

        var addVal = BigdecimalUtil.add(checkDatas[curPos].fqty, fqty)
        checkDatas[curPos].fqty = addVal
        checkDatas[curPos].icstockBillEntry_Barcodes.add(entryBarcode)
        mAdapter!!.notifyDataSetChanged()
    }

    private fun getMtl() {
        var isModify = false // 是修改，否提示
        curPos = -1
        var mtl = bt!!.mtl
        // 判断扫描的物料是否与行里面的一样
        checkDatas.forEachIndexed { index, it ->
            if(it.mtlId == mtl.fmaterialId) {
                curPos = index
                mtl = checkDatas[index].material
                isModify = true
            }
        }
        if(!isModify) {
            Comm.showWarnDialog(context,"扫描的条码与订单不匹配，请检查！")
            return
        }

        // 判断条码是否存在（启用批次，序列号）
        if (checkDatas[curPos].icstockBillEntry_Barcodes.size > 0 && (mtl.isBatchManager == 1 || mtl.isSnManager == 1)) {
            checkDatas[curPos].icstockBillEntry_Barcodes.forEach {
                if (getValues(et_code).length > 0 && getValues(et_code) == it.barcode) {
                    Comm.showWarnDialog(context,"条码已使用！")
                    return
                }
            }
        }

        if(mtl.isBatchManager == 1) { // 启用批次号
            val showInfo:String = "<font color='#666666'>批次号：</font>" + bt!!.batchCode
            showInputDialog("数量", showInfo, bt!!.materialCalculateNumber.toString(), "0.0", SM_RESULT_NUM)
//            setBatchCode(salOrderEntry!!.fqty)

        } else if(mtl.isSnManager == 1) { // 启用序列号
            setSnCode()

        } else { // 未启用
            // 出库数小于源单数
            if(checkDatas[curPos].fqty < checkDatas[curPos].fsourceQty) {
                unStartBatchOrSnCode(checkDatas[curPos].fsourceQty)
            }
        }
    }

    private fun setICStockBIll(salOrderEntry : SalOrderEntry) {
        val salOrder = salOrderEntry.salOrder
        icstockBill.empNumber = salOrder.saleMan.fnumber
        icstockBill.empName = salOrder.saleMan.fname
        icstockBill.fstockOrgId = salOrderEntry.fstockOrgId
        icstockBill.fstockOutOrgId = salOrder.saleOrg.forgId
        icstockBill.stockOrg = salOrderEntry.stockOrg
        icstockBill.stockOutOrg = salOrder.saleOrg
        icstockBill.fbillTypeNumber = "XSCKD01_SYS" // 销售订单据类型（标准销售出库单）
        icstockBill.fownerType = "BD_OwnerOrg"
        icstockBill.fownerNumber = salOrderEntry.stockOrg.fnumber
        icstockBill.fownerName = salOrderEntry.stockOrg.fname

        icstockBill.fcustId = salOrder.fcustId
        icstockBill.deliveryWayNumber = salOrder.deliveryWayNumber
        icstockBill.deliveryWayName = salOrder.deliveryWayName
        icstockBill.expressNo = salOrder.expressNo
        icstockBill.receiveAddress = salOrder.receiveAddress
        icstockBill.receiveContact = salOrder.receiveContact
        icstockBill.receivePhone = Comm.getValidMobile(salOrder.receiveTel)
        icstockBill.combineSalOrderId = salOrderEntry.combineSalOrderId
        if(salOrder.saleDept != null) {
            icstockBill.recDeptId = salOrder.saleDept.fdeptId
            icstockBill.recDept = salOrder.saleDept
        }
    }

    /**
     * 新增行数据
     */
    private fun addICStockBillEntry(list : List<SalOrderEntry>) {
        // 设置表头信息
        setICStockBIll(list[0])

        list.forEach {
            val entry = ICStockBillEntry()
            entry.icstockBill = icstockBill
            entry.icstockBillId = 0
            entry.mtlId = it.fmaterialId
            if(stock != null) {
                entry.fdcStockId = stock!!.fstockId
                entry.stock = stock
            }
            entry.fprice = it.fprice
            entry.funitId = it.funitId
            entry.frowType = it.frowType
            entry.fsourceInterId = it.fid
            entry.fsourceEntryId = it.fentryId
            entry.fsourceSeq = it.fseq
            entry.fsourceBillNo = it.salOrder.fbillNo
            entry.fsourceQty = it.usableQty
            entry.forderInterId = it.fid
            entry.forderEntryId = it.fentryId
            entry.forderBillNo = it.salOrder.fbillNo
            entry.forderSeq = it.fseq
            entry.forderDate = it.salOrder.fdate.substring(0,10)
            entry.fisFree = it.fisFree
            if(it.fisFree == 1) {
                entry.fqty = it.usableQty
            }
            entry.remark = it.fnote
            entry.custMtlId = it.fmapId
            entry.fsrcBillTypeId = "SAL_SaleOrder"
            entry.fentity_Link_FRuleId = "SaleOrder-OutStock"
            entry.fentity_Link_FSTableName = "T_SAL_ORDERENTRY"
            if(it.material.personal > 0 && isNULLS(it.personalCarVersionNumber).length > 0) {
                entry.carVersionNumber = it.personalCarVersionNumber
                entry.carVersionLocation = it.personalCarVersionLocation
                entry.carVersionName = it.personalCarVersionName
            }
            entry.fownerNumber = icstockBill.stockOrg.fnumber
            entry.fownerName = icstockBill.stockOrg.fname
            entry.fauxpropid_103_number = isNULLS(it.fauxpropid_103_number)
            entry.fauxpropid_103_name = isNULLS(it.fauxpropid_103_name)
            entry.fauxpropid_104_number = isNULLS(it.fauxpropid_104_number)
            entry.fauxpropid_104_name = isNULLS(it.fauxpropid_104_name)
            entry.fauxpropid_105_number = isNULLS(it.fauxpropid_105_number)
            entry.fauxpropid_105_name = isNULLS(it.fauxpropid_105_name)
            entry.fauxpropid_106_number = isNULLS(it.fauxpropid_106_number)
            entry.fauxpropid_106_name = isNULLS(it.fauxpropid_106_name)

            entry.material = it.material
            entry.unit = it.unit
            checkDatas.add(entry)
        }
        mAdapter!!.notifyDataSetChanged()

        // 把销售订单扫描关闭
        btn_orderScan.isEnabled = false
        et_orderNo.isEnabled = false
        lin_focusOrderNo.setBackgroundResource(R.drawable.back_style_gray1a)

        btn_scan.isEnabled = true
        et_code.isEnabled = true
        lin_focusMtl.setBackgroundResource(R.drawable.back_style_blue)
        smFlag = '2'
        mHandler.sendEmptyMessage(SETFOCUS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) return
            when (requestCode) {
                SEL_STOCK -> {// 仓库	返回
                    val stock = data!!.getSerializableExtra("obj") as Stock_K3
                    checkDatas[curPos].fdcStockId = stock.fstockId
                    checkDatas[curPos].stock = stock
                    mAdapter!!.notifyDataSetChanged()
                }
                RESULT_NUM -> { // 数量	返回
                    val bundle = data!!.getExtras()
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        val num = parseDouble(value)
                        checkDatas[curPos].fqty = num
                        mAdapter!!.notifyDataSetChanged()
                    }
                }
                SM_RESULT_NUM -> { // 扫码数量	    返回
                    val bundle = data!!.getExtras()
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        val num = parseDouble(value)
                        setBatchCode(num)
                    }
                }
                WRITE_CODE -> {// 输入条码  返回
                    val bundle = data!!.extras
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        when(smFlag) {
                            '1' -> setTexts(et_orderNo, value.toUpperCase())
                            '2' -> setTexts(et_code, value.toUpperCase())
                        }
                    }
                }
                BaseFragment.CAMERA_SCAN -> {// 扫一扫成功  返回
                    val hmsScan = data!!.getParcelableExtra(ScanUtil.RESULT) as HmsScan
                    if (hmsScan != null) {
                        when(smFlag) {
                            '1' -> setTexts(et_orderNo, hmsScan.originalValue)
                            '2' -> setTexts(et_code, hmsScan.originalValue)
                        }
                    }
                }
                SEL_LOGISTICS -> {// 物流公司	返回
                    logistics = data!!.getSerializableExtra("obj") as Logistics
//                    showInputDialog("预约个数", "1", "0", RESULT_YUYUE)
                    run_saoOutStock_appointment(checkDatas[0].forderBillNo, 1)
                }
                RESULT_YUYUE -> { // 预约数量返回
                    val bundle = data!!.getExtras()
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        var num = parseInt(value)
                        if (num == 0) {
                            num = 1
                        }
                        run_saoOutStock_appointment(checkDatas[0].forderBillNo, num)
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
        }
        mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
    }

    /**
     * 扫码销售订单号条码
     */
    private fun run_findBarocdeByOrderNo() {
        isTextChange = false

        showLoadDialog("加载中...", false)
        var mUrl = getURL("salOrder/findBarocdeByOrderNo")
        val formBody = FormBody.Builder()
                .add("barcode", getValues(et_orderNo))
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
                LogUtil.e("run_findBarocdeByOrderNo --> onResponse", result)
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
     * 扫码查询对应的方法
     */
    private fun run_findBarcode() {
        isTextChange = false

        showLoadDialog("加载中...", false)
        var mUrl = getURL("salOrder/findBarcodeByMtl")
        val formBody = FormBody.Builder()
                .add("barcode", getValues(et_code))
                .build()

        val request = Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNSUCC2)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                LogUtil.e("run_findBarcode --> onResponse", result)
                if (!JsonUtil.isSuccess(result)) {
                    val msg = mHandler.obtainMessage(UNSUCC2, result)
                    mHandler.sendMessage(msg)
                    return
                }
                val msg = mHandler.obtainMessage(SUCC2, result)
                mHandler.sendMessage(msg)
            }
        })
    }

    /**
     * 保存
     */
    private fun run_save() {
        showLoadDialog("保存中...", false)
        val mUrl = getURL("stockBill_WMS/saveGroup")

        val mJson = JsonUtil.objectToString(checkDatas)
        val formBody = FormBody.Builder()
                .add("strJson", mJson)
                .build()

        val request = Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNSAVE)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                if (!JsonUtil.isSuccess(result)) {
                    val msg = mHandler.obtainMessage(UNSAVE, result)
                    mHandler.sendMessage(msg)
                    return
                }
                val msg = mHandler.obtainMessage(SAVE, result)
                LogUtil.e("run_save --> onResponse", result)
                mHandler.sendMessage(msg)
            }
        })
    }

    /**
     * 上传单据
     */
    private fun run_uploadToK3(icstockBillId: String) {
        showLoadDialog("上传中...", false)
        val mUrl = getURL("stockBill_WMS/uploadToK3_SalOutStock")
        val formBody = FormBody.Builder()
                .add("strId", icstockBillId)
                .build()

        val request = Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNUPLOAD)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                LogUtil.e("run_uploadToK3 --> onResponse", result)
                if (!JsonUtil.isSuccess(result)) {
                    val msg = mHandler.obtainMessage(UNUPLOAD, result)
                    mHandler.sendMessage(msg)
                    return
                }
                val msg = mHandler.obtainMessage(UPLOAD, result)
                mHandler.sendMessage(msg)
            }
        })
    }

    /**
     * 查询预约的数据
     */
    fun run_saoOutStock_appointment(salOrderNo :String, num :Int) {
        val mapBarcode = HashMap<String, String>()
        checkDatas.forEach {
            for (icstockBillEntry_Barcode in it.icstockBillEntry_Barcodes) {
                mapBarcode[icstockBillEntry_Barcode.barcode] = it.forderBillNo
            }
        }
        showLoadDialog("准备打印...", false)
        val mUrl = getURL("appPrint/saoOutStock_appointment")
        var strLogistics = JsonUtil.objectToString(logistics!!)
        val formBody = FormBody.Builder()
                .add("so_id", salOrderNo)
                .add("num", num.toString())
                .add("strLogistics", strLogistics)      // 快递公司
                .add("address", icstockBill.receiveAddress)             // 收件地址
                .add("receiverName", icstockBill.receiveContact)        // 收件人
//                .add("receiverZip", icstockBill.receiveAddress)                   // 收件地址邮编
                .add("receiverMobile", icstockBill.receivePhone)      // 收件人电话
                .add("sendMan", salOrder!!.createMan.fname)        // 寄件人
                .add("sendPhone", salOrder!!.createMan.fmobile)    // 寄件人电话
                .add("sendCustName", salOrder!!.saleCust.fname)    // 寄件客户
                .add("userId", user!!.id.toString())
                .add("strBarcode", JsonUtil.objectToString(mapBarcode))
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
                mHandler.sendEmptyMessage(UNSUCC3)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                LogUtil.e("run_saoOutStock_appointment --> onResponse", result)
                if (!JsonUtil.isSuccess(result)) {
                    val msg = mHandler.obtainMessage(UNSUCC3, result)
                    mHandler.sendMessage(msg)
                    return
                }
                val msg = mHandler.obtainMessage(SUCC3, result)
                mHandler.sendMessage(msg)
            }
        })
    }

    /**
     * Fragment回调得到数据
     */
    fun print(list: List<ExpressNoData>) {
        listMap.clear()
        listMap.addAll(list)

        if (isConnected) {
            startPrint(list)
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
            setPrintFormat(curDate, it) // 第一次打印是发货
            setPrintFormat(curDate, it) // 第二次打印是给自己留底
        }
    }

    /**
     * 设置打印的格式
     */
    private fun setPrintFormat(curDate :String, it :ExpressNoData) {
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
     * 得到用户对象
     */
    private fun getUserInfo() {
        if (user == null) user = showUserByXml()
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
                        CONN_STATE_FAILED -> {
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

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        filter.addAction(DeviceConnFactoryManager.ACTION_CONN_STATE)
        registerReceiver(receiver, filter)
        // 判断是否打开蓝牙
        val adapter  = BluetoothAdapter.getDefaultAdapter();
        val isEnable = adapter.isEnabled();
        if(!isEnable) {
            toasts("请打开蓝牙，并连接上打印机，2秒后退出当前界面！")
            mHandler.postDelayed(Runnable {
                context.finish()
            },2000)
        }
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
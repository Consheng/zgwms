package ykk.xc.com.zgwms.warehouse

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import butterknife.OnClick
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import kotlinx.android.synthetic.main.stk_count_input_saoma.*
import okhttp3.*
import ykk.xc.com.zgwms.R
import ykk.xc.com.zgwms.bean.ICStockBill
import ykk.xc.com.zgwms.bean.ICStockBillEntry
import ykk.xc.com.zgwms.bean.ICStockBillEntry_Barcode
import ykk.xc.com.zgwms.bean.User
import ykk.xc.com.zgwms.bean.prod.PrdMoEntry
import ykk.xc.com.zgwms.bean.warehouse.StkCountInput
import ykk.xc.com.zgwms.bean.warehouse.StkCountInputEntry
import ykk.xc.com.zgwms.comm.BaseActivity
import ykk.xc.com.zgwms.comm.BaseFragment
import ykk.xc.com.zgwms.comm.Comm
import ykk.xc.com.zgwms.util.BigdecimalUtil
import ykk.xc.com.zgwms.util.JsonUtil
import ykk.xc.com.zgwms.util.LogUtil
import ykk.xc.com.zgwms.util.basehelper.BaseRecyclerAdapter
import ykk.xc.com.zgwms.warehouse.adapter.Stk_CountInput_SaoMa_Adapter
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * 日期：2019-10-16 09:14
 * 描述：物料盘点扫描
 * 作者：ykk
 */
class StkCountInput_SaoMaActivity : BaseActivity() {

    companion object {
        private val SEL_SOURCE = 60
        private val SUCC1 = 200
        private val UNSUCC1 = 500
        private val SAVE = 201
        private val UNSAVE = 501
        private val UPLOAD = 202
        private val UNUPLOAD = 502
        private val FIND_ENTRY = 203
        private val UNFIND_ENTRY = 503

        private val SETFOCUS = 1
        private val SAOMA = 2
        private val RESULT_NUM = 3
        private val WRITE_CODE = 4
        private val SM_RESULT_NUM = 5
    }

    private val context = this
    private val TAG = "Stk_CountInput_SaoMaActivity"
    private var okHttpClient: OkHttpClient? = null
    private var mAdapter: Stk_CountInput_SaoMa_Adapter? = null
    private val icstockBill = ICStockBill()
    private val checkDatas = ArrayList<ICStockBillEntry>()
    private var curPos = -1 // 当前行
    private var user: User? = null
    private var smqFlag = '1' // 扫描类型1：位置扫描，2：物料扫描
    private var isTextChange: Boolean = false // 是否进入TextChange事件
    private var prdMoEntry :PrdMoEntry? = null

    // 消息处理
    private val mHandler = MyHandler(this)
    private class MyHandler(activity: StkCountInput_SaoMaActivity) : Handler() {
        private val mActivity: WeakReference<StkCountInput_SaoMaActivity>

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
                        val prdEntry = JsonUtil.strToObject(msgObj, PrdMoEntry::class.java)
                        if(m.prdMoEntry != null && m.prdMoEntry!!.fworkshopId != prdEntry.fworkshopId) {
                            Comm.showWarnDialog(m.context,"当前扫描的车间不一致，请检查！")
                            return
                        }
                        m.prdMoEntry = prdEntry
                        m.getMtl()
                    }
                    UNSUCC1 -> { // 扫码失败
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
                        Comm.showWarnDialog(m.context, errMsg)
                    }
                    FIND_ENTRY -> { // 查询分录 成功
                        val list = JsonUtil.strToList(msgObj, StkCountInputEntry::class.java)
                        m.addICStockBillEntry(list)
                    }
                    UNFIND_ENTRY -> { // 查询分录失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "服务器繁忙，请稍后再试！"
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
        return R.layout.stk_count_input_saoma
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
        mAdapter = Stk_CountInput_SaoMa_Adapter(context, checkDatas)
        recyclerView.adapter = mAdapter
        // 设值listview空间失去焦点
        recyclerView.isFocusable = false

        // 行事件
        mAdapter!!.setCallBack(object : Stk_CountInput_SaoMa_Adapter.MyCallBack {
            override fun onDelete(entity: ICStockBillEntry, position: Int) {
                if(icstockBill.id > 0) return
                checkDatas.removeAt(position)
                mAdapter!!.notifyDataSetChanged()
            }
        })
        // 输入数量
        mAdapter!!.onItemClickListener = BaseRecyclerAdapter.OnItemClickListener { adapter, holder, view, pos ->
            if(icstockBill.id > 0) return@OnItemClickListener
            val mtl = checkDatas[pos].material
            if(mtl.isSnManager == 1 || mtl.isBatchManager == 1) return@OnItemClickListener

            curPos = pos
            showInputDialog("盘点数", checkDatas[pos].fqty.toString(), "0.0", RESULT_NUM)
        }
    }

    override fun initData() {
        getUserInfo()
        bundle()
        hideSoftInputMode(et_code)
        mHandler.sendEmptyMessage(SETFOCUS)

        icstockBill.billType = "WLPD" // 物料盘点
        icstockBill.ftranType = 43
        icstockBill.fselTranType = 0
        icstockBill.empNumber = user!!.kdUserNumber
        icstockBill.empName = user!!.kdUserName
        icstockBill.stockManagerNumber = user!!.kdUserNumber
        icstockBill.stockManagerName = user!!.kdUserName
        icstockBill.createUserId = user!!.id
        icstockBill.createUserName = user!!.username

        icstockBill.fstockOrgId = user!!.organizationId
        icstockBill.stockOrg = user!!.organization
        icstockBill.fbillTypeNumber = "WLPDZY01_SYS" // 标准物料盘点作业
        icstockBill.fownerType = "BD_OwnerOrg"
        icstockBill.fownerNumber = user!!.organization.fnumber
        icstockBill.fownerName = user!!.organization.fname
    }

    private fun bundle() {
        val bundle = context.intent.extras
        if (bundle != null) {
        }
    }

    @OnClick(R.id.btn_close, R.id.tv_sel, R.id.btn_scan, R.id.btn_save, R.id.btn_clone, R.id.btn_upload)
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
            R.id.tv_sel -> { // 选单
                if(checkDatas.size > 0) {
                    Comm.showWarnDialog(context,"请先保存当前数据！")
                    return
                }
                showForResult(StkCountInput_List_Dialog::class.java, SEL_SOURCE, null)
            }
            R.id.btn_scan -> { // 调用摄像头扫描（物料）
                ScanUtil.startScan(context, BaseFragment.CAMERA_SCAN, HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create());
            }
            R.id.btn_save -> { // 保存
                if(checkDatas.size == 0) {
                    Comm.showWarnDialog(context,"请选择盘点单！")
                    return
                }
                checkDatas.forEachIndexed { index, it ->
//                    if(it.fqty == 0.0) {
//                        Comm.showWarnDialog(context,"第"+(index+1)+"行，请输入数量！")
//                        return
//                    }
                }
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
        et_code.setText("")
        icstockBill.id = 0
        checkDatas.clear()
        mAdapter!!.notifyDataSetChanged()
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
                    smqFlag = '2'
                    mHandler.sendEmptyMessageDelayed(SAOMA, 300)
                }
            }
        })
        // 物料---长按输入条码
        et_code!!.setOnLongClickListener {
            smqFlag = '2'
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
        entryBarcode.batchCode = prdMoEntry!!.smBatchCode
        entryBarcode.snCode = ""
        entryBarcode.fqty = fqty
        entryBarcode.isUniqueness = 'Y'
        entryBarcode.againUse = 1
        entryBarcode.createUserName = user!!.username
        entryBarcode.billType = "SCRK"

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
        entryBarcode.snCode = prdMoEntry!!.smSnCode
        entryBarcode.fqty = 1.0
        entryBarcode.isUniqueness = 'Y'
        entryBarcode.againUse = 1
        entryBarcode.createUserName = user!!.username
        entryBarcode.billType = "SCRK"

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
        entryBarcode.billType = "SCRK"

        var addVal = BigdecimalUtil.add(checkDatas[curPos].fqty, fqty)
        checkDatas[curPos].fqty = addVal
        checkDatas[curPos].icstockBillEntry_Barcodes.add(entryBarcode)
        mAdapter!!.notifyDataSetChanged()
    }

    private fun getMtl() {
        var isAdd = true // 是新增，否修改
        curPos = -1
        var material = prdMoEntry!!.material
        // 判断扫描的物料是否与行里面的一样
        checkDatas.forEachIndexed { index, it ->
            if(it.fsourceInterId == prdMoEntry!!.fid && it.fsourceEntryId == prdMoEntry!!.fentryId) {
                curPos = index
                material = checkDatas[index].material
                isAdd = false
            }
        }
        // 判断条码是否存在（启用批次，序列号）
        if (!isAdd && checkDatas[curPos].icstockBillEntry_Barcodes.size > 0 && (material.isBatchManager == 1 || material.isSnManager == 1)) {
            checkDatas[curPos].icstockBillEntry_Barcodes.forEach {
                if (getValues(et_code).length > 0 && getValues(et_code) == it.barcode) {
                    Comm.showWarnDialog(context,"条码已使用！")
                    return
                }
            }
        }
        if(isAdd) {
//            addICStockBillEntry()
        }

        if(material.isBatchManager == 1) { // 启用批次号
            val showInfo:String = "<font color='#666666'>批次号：</font>" + prdMoEntry!!.smBatchCode
            showInputDialog("数量", showInfo, "", "0.0", SM_RESULT_NUM)

        } else if(material.isSnManager == 1) { // 启用序列号
            setSnCode()

        } else { // 未启用
            unStartBatchOrSnCode(1.0)
        }
    }

    /**
     * 新增行数据
     */
    private fun addICStockBillEntry(list :List<StkCountInputEntry>) {
        val stkCountInput = list[0].stkCountInput
        icstockBill.fstockOrgId = stkCountInput.fstockOrgId
        icstockBill.stockOrg = stkCountInput.stockOrg
        icstockBill.fownerType = list[0].fownerTypeId
        icstockBill.fownerNumber = list[0].owner_fnumber
        icstockBill.fownerName = list[0].owner_fname
        
        list.forEach {
            val entry = ICStockBillEntry()
            entry.icstockBill = icstockBill
            entry.icstockBillId = 0
            entry.mtlId = it.fmaterialId
            entry.fdcStockId = it.fstockId
            entry.fdcSPId = it.fstockLocId
            entry.stock = it.stock
            entry.stockPos = it.stockPos
            entry.fqty = it.facctQty
            entry.fprice = 0.0
            entry.funitId = it.funitId
            entry.fsourceInterId = it.fid
            entry.fsourceEntryId = it.fentryId
            entry.fsourceBillNo = stkCountInput.fbillNo
            entry.fsourceSeq = it.fseq
            entry.forderInterId = 0
            entry.forderEntryId = 0
            entry.forderBillNo = null
            entry.forderSeq = 0
            entry.fsourceQty = it.facctQty

            entry.material = it.material
            entry.unit = it.unit
            checkDatas.add(entry)
        }

        mAdapter!!.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                SEL_SOURCE -> {// 仓库	返回
                    val stkCountInput = data!!.getSerializableExtra("obj") as StkCountInput
                    tv_sel.text = stkCountInput.fbillNo
                    // 查询分录
                    run_findEntryByParam(stkCountInput.fid.toString())
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
                        setTexts(et_code, value.toUpperCase())
                    }
                }
                BaseFragment.CAMERA_SCAN -> {// 扫一扫成功  返回
                    val hmsScan = data!!.getParcelableExtra(ScanUtil.RESULT) as HmsScan
                    if (hmsScan != null) {
                        setTexts(et_code, hmsScan.originalValue)
                    }
                }
            }
        }
        mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
    }

    /**
     * 查询分录
     */
    private fun run_findEntryByParam(fid: String) {
        showLoadDialog("加载中...", false)
        val mUrl = getURL("stkCountInput/findEntryByParam")
        val formBody = FormBody.Builder()
                .add("fid", fid)
                .build()

        val request = Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNFIND_ENTRY)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                LogUtil.e("run_uploadToK3 --> onResponse", result)
                if (!JsonUtil.isSuccess(result)) {
                    val msg = mHandler.obtainMessage(UNFIND_ENTRY, result)
                    mHandler.sendMessage(msg)
                    return
                }
                val msg = mHandler.obtainMessage(FIND_ENTRY, result)
                mHandler.sendMessage(msg)
            }
        })
    }

    /**
     * 扫码查询对应的方法
     */
    private fun run_smDatas() {
        isTextChange = false
        showLoadDialog("加载中...", false)
        var mUrl = getURL("prdMo/findBarocde")
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
    private fun run_uploadToK3(strId: String) {
        showLoadDialog("上传中...", false)
        val mUrl = getURL("stockBill_WMS/uploadToK3")
        val formBody = FormBody.Builder()
                .add("strId", strId)
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
     * 得到用户对象
     */
    private fun getUserInfo() {
        if (user == null) user = showUserByXml()
    }

    override fun onDestroy() {
        closeHandler(mHandler)
        super.onDestroy()
    }

}
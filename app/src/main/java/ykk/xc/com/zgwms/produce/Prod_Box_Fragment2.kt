package ykk.xc.com.zgwms.produce

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import kotlinx.android.synthetic.main.prod_box_fragment2.*
import okhttp3.*
import ykk.xc.com.zgwms.R
import ykk.xc.com.zgwms.basics.Box_DialogActivity
import ykk.xc.com.zgwms.bean.*
import ykk.xc.com.zgwms.bean.k3Bean.ICItem
import ykk.xc.com.zgwms.bean.prod.PrdMoEntry
import ykk.xc.com.zgwms.bean.sales.DeliveryNoticeEntry
import ykk.xc.com.zgwms.comm.BaseFragment
import ykk.xc.com.zgwms.comm.Comm
import ykk.xc.com.zgwms.produce.adapter.Prod_Box_Fragment2_Adapter
import ykk.xc.com.zgwms.util.BigdecimalUtil
import ykk.xc.com.zgwms.util.JsonUtil
import ykk.xc.com.zgwms.util.LogUtil
import java.io.IOException
import java.lang.ref.WeakReference
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

/**
 * 日期：2019-10-16 09:50
 * 描述：库存装箱
 * 作者：ykk
 */
class Prod_Box_Fragment2 : BaseFragment() {

    companion object {
        private val SEL_BOX = 60
        private val SEL_MTL = 61
        private val SUCC1 = 200
        private val UNSUCC1 = 500
        private val SAVE = 201
        private val UNSAVE = 501
        private val DELETE = 202
        private val UNDELETE = 502
        private val BOX_STATUS = 203
        private val UNBOX_STATUS = 503

        private val SETFOCUS = 1
        private val SAOMA = 2
        private val RESULT_NUM = 3
        private val WRITE_CODE = 4
        private val RESULT_BATCH_NUM = 7
    }
    private val context = this
    private var okHttpClient: OkHttpClient? = null
    private var checkDatas = ArrayList<MaterialBinningRecord>()
    private var mAdapter: Prod_Box_Fragment2_Adapter? = null
    private var mContext: Activity? = null
    private var user: User? = null
    private var boxBarCode:BoxBarCode? = null
    private val df = DecimalFormat("#.######")
    private var parent: Prod_Box_MainActivity? = null
    private var isTextChange: Boolean = false // 是否进入TextChange事件
    private var timesTamp:String? = null // 时间戳
    private var smqFlag = '2' // 扫描类型1：箱子扫描，3：物料扫描
    private var curPos = -1 // 当前行
    private var modifyBoxStatus = 1 // 开箱还是封箱
    private var autoSave = false // 点击封箱自动保存
    private var autoSaveAferBarcode:String? = null
    private var curBoxStatus = 0   // 记录当前扫描箱码的状态
    private var bt :BarCodeTable? = null  // 记录当前物料扫描返回的对象

    // 消息处理
    private val mHandler = MyHandler(this)
    private class MyHandler(activity: Prod_Box_Fragment2) : Handler() {
        private val mActivity: WeakReference<Prod_Box_Fragment2>

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
                        when(m.smqFlag) {
                            '1'-> { // 箱码
                                val boxBarCode = JsonUtil.strToObject(msgObj, BoxBarCode::class.java)
                                // 操作过程中扫了其他箱子
                                if(m.boxBarCode != null && m.boxBarCode!!.status != 2) {
                                    Comm.showWarnDialog(m.mContext, "请先把当前箱子封箱！")
                                    return
                                }
                                // 扫描重复的箱子
                                if(m.boxBarCode != null && boxBarCode.id == m.boxBarCode!!.id) {
                                    return
                                }
                                if(m.autoSaveAferBarcode != null) {
                                    m.reset(0)
                                }
                                m.getBoxBarcode(boxBarCode)
                            }
                            '2'-> { // 发货单
                                val list = JsonUtil.strToList(msgObj, DeliveryNoticeEntry::class.java)
                                if(m.checkDatas.size > 0) {
                                    val strMbrTmp = m.checkDatas[0].fcustId.toString() + m.checkDatas[0].deliveryTypeNumber + m.checkDatas[0].deliveryWayNumber + m.checkDatas[0].deliveryAddress
                                    val strTmp = list[0].deliveryNotice.fcustomerId.toString() + list[0].deliveryNotice.deliveryTypeNumber + list[0].deliveryNotice.deliveryWayNumber + list[0].deliveryNotice.receiveAddress
                                    if (!strMbrTmp.equals(strTmp)) { // 条件不一样不能装箱
                                        Comm.showWarnDialog(m.mContext, "对应的发货通知单（客户+交货类别+交货方式+收货人地址）必须一致才能一起装箱！")
                                        return
                                    }
                                }
                                m.addRow(list)
                            }
                        }
                    }
                    UNSUCC1 -> { // 扫码失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "很抱歉，没有找到数据！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    SAVE -> { // 保存成功 进入
                        m.autoSaveAferBarcode = JsonUtil.strToString(msgObj)
                        if(m.autoSave) {
                            m.run_modifyStatus(m.modifyBoxStatus.toString())
                        } else {
//                            m.reset(1)
                            m.smqFlag = '1'
                            m.boxBarCode = null
                            m.setTexts(m.et_boxCode, m.autoSaveAferBarcode)
                        }
                        m.toasts("保存成功")
                    }
                    UNSAVE -> { // 保存失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "保存失败！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    DELETE -> { // 删除行 进入
                        m.checkDatas.removeAt(m.curPos)
                        m.mAdapter!!.notifyDataSetChanged()
                    }
                    UNDELETE -> { // 删除行  失败
                        Comm.showWarnDialog(m.mContext,"删除出错！")
                    }
                    BOX_STATUS -> { // 开箱和封箱 进入
                        if(m.boxBarCode != null) {
                            m.boxBarCode!!.status = m.modifyBoxStatus // 更新箱子状态
                            m.checkDatas.forEach {
                                it.boxBarCode.status = m.modifyBoxStatus
                            }
                            m.getBoxBarcode_status(m.modifyBoxStatus, true)
                        }
                        if(m.autoSave) {
                            m.smqFlag = '1'
                            m.boxBarCode = null
                            m.setTexts(m.et_boxCode, m.autoSaveAferBarcode)
                        }
                    }
                    UNBOX_STATUS -> { // 开箱和封箱  失败
                        Comm.showWarnDialog(m.mContext,"操作出错！")
                    }
                    SETFOCUS -> { // 当弹出其他窗口会抢夺焦点，需要跳转下，才能正常得到值
                        m.setFocusable(m.et_getFocus)
                        when(m.smqFlag) {
                            '1'-> m.setFocusable(m.et_boxCode)
                            '2'-> m.setFocusable(m.et_code)
                        }
                    }
                    SAOMA -> { // 扫码之后
                        when(m.smqFlag) {
                            '1' -> {
                                if(m.boxBarCode != null && m.boxBarCode!!.status != 2 && m.checkDatas.size > 0) {
                                    Comm.showWarnDialog(m.mContext,"请先保存当前数据！")
                                    m.isTextChange = false
                                    return
                                }
                                if(m.boxBarCode != null && m.boxBarCode!!.status == 2) {
                                    m.isTextChange = false
                                    val barcode = m.getValues(m.et_boxCode)
                                    m.reset(0)
                                    m.setTexts(m.et_boxCode, barcode)
                                    return
                                }
                            }
                        }
                        // 执行查询方法
                        m.run_smDatas()
                    }
                }
            }
        }
    }

    override fun setLayoutResID(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.prod_box_fragment2, container, false)
    }

    override fun initView() {
        mContext = getActivity()
        parent = mContext as Prod_Box_MainActivity

        recyclerView.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        mAdapter = Prod_Box_Fragment2_Adapter(mContext!!, checkDatas)
        recyclerView.adapter = mAdapter
        // 设值listview空间失去焦点
        recyclerView.isFocusable = false

        // 行事件
        mAdapter!!.setCallBack(object : Prod_Box_Fragment2_Adapter.MyCallBack {
            override fun onClickNum(entity: MaterialBinningRecord, position: Int) {
                // 已出入库，不能修改数据
                if((entity.boxBarCode != null && entity.boxBarCode.status == 2) || (boxBarCode != null && entity.boxBarCodeId != boxBarCode!!.id) ) return

                curPos = position
                showInputDialog("数量", entity.fqty.toString(), "0.0", RESULT_NUM)
            }
            override fun onDelete(entity: MaterialBinningRecord, position: Int) {
                // 已出入库，不能修改数据
                if((entity.boxBarCode != null && entity.boxBarCode.status == 2) || (boxBarCode != null && entity.boxBarCodeId != boxBarCode!!.id) ) return

                curPos = position
                // 还没保存的行，就直接删除
                if(entity.id == 0) {
                    checkDatas.removeAt(position)
                    checkDatas.forEachIndexed { index, it ->
                        it.rowNo = (index+1) // 自动算出行号
                    }
                    mAdapter!!.notifyDataSetChanged()

                } else {
                    val build = AlertDialog.Builder(mContext)
                    build.setIcon(R.drawable.caution)
                    build.setTitle("系统提示")
                    build.setMessage("是否删除选中数据？")
                    build.setPositiveButton("是") { dialog, which -> run_removeRow(entity) }
                    build.setNegativeButton("否", null)
                    build.setCancelable(false)
                    build.show()
                }

            }
        })
    }

    override fun initData() {
        if (okHttpClient == null) {
            okHttpClient = OkHttpClient.Builder()
                    //                .connectTimeout(10, TimeUnit.SECONDS) // 设置连接超时时间（默认为10秒）
                    .writeTimeout(30, TimeUnit.SECONDS) // 设置写的超时时间
                    .readTimeout(30, TimeUnit.SECONDS) //设置读取超时时间
                    .build()
        }

        getUserInfo()
        timesTamp = user!!.getId().toString() + "-" + Comm.randomUUID()
        hideSoftInputMode(mContext, et_boxCode)
        hideSoftInputMode(mContext, et_code)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
        }
    }

    @OnClick(R.id.tv_boxSel, R.id.btn_scanBox, R.id.btn_scan, R.id.btn_save, R.id.btn_clone,
             R.id.btn_openBox, R.id.btn_closeBox, R.id.btn_print)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.tv_boxSel -> {
                if(boxBarCode != null) return
                showForResult(Box_DialogActivity::class.java, SEL_BOX, null)
            }
            R.id.btn_scanBox -> { // 调用摄像头扫描（箱码）
                smqFlag = '1'
                ScanUtil.startScan(mContext, BaseFragment.CAMERA_SCAN, HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create())
            }
            R.id.btn_scan -> { // 调用摄像头扫描（物料）
                smqFlag = '2'
                ScanUtil.startScan(mContext, BaseFragment.CAMERA_SCAN, HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create())
            }
            R.id.btn_save -> { // 保存
                if(!checkSave()) return
                // 保存的时候把实际体重，体积记录，新生成箱子用
                autoSave = false
                run_save()
            }
            R.id.btn_openBox -> { // 开箱
                modifyBoxStatus = 1
                run_modifyStatus(modifyBoxStatus.toString())
            }
            R.id.btn_closeBox -> { // 封箱
                if(checkDatas.size == 0) {
                    Comm.showWarnDialog(mContext,"还没有装入物料，不能封箱！")
                    return
                }
                if(!checkSave()) return

                if(checkDatas[0].boxBarCodeId > 0) {
                    modifyBoxStatus = 2
                    autoSave = true
                }
                run_save()
            }
            R.id.btn_print -> { // 打印
                if (boxBarCode == null) {
                    Comm.showWarnDialog(mContext, "请先扫描箱码！")
                    return
                }
                if (checkDatas == null || checkDatas.size == 0) {
                    Comm.showWarnDialog(mContext, "箱子里还没有物料不能打印！")
                    return
                }
                checkDatas.forEach {
                    if(it.id == 0) {
                        Comm.showWarnDialog(mContext,"请先保存当前数据！")
                        return
                    }
                }
                parent!!.setFragment1Print(checkDatas)
            }
            R.id.btn_clone -> { // 重置
                if (checkSaveHint()) {
                    val build = AlertDialog.Builder(mContext)
                    build.setIcon(R.drawable.caution)
                    build.setTitle("系统提示")
                    build.setMessage("您有未保存的数据，继续重置吗？")
                    build.setPositiveButton("是") { dialog, which -> reset(0) }
                    build.setNegativeButton("否", null)
                    build.setCancelable(false)
                    build.show()

                } else {
                    reset(0)
                }
            }
        }
    }

    /**
     * 检查数据
     */
    fun checkSave() : Boolean {
        if(checkDatas.size == 0) {
            Comm.showWarnDialog(mContext,"请扫描物料条码！")
            return false
        }
        if(checkDatas[0].boxId == 0) {
//            lin_box.visibility = View.VISIBLE
            Comm.showWarnDialog(mContext, "请选择包装箱！")
            return false
        }
        return true;
    }

    /**
     * 选择了物料没有点击保存，点击了重置，需要提示
     */
    fun checkSaveHint() : Boolean {
        if(checkDatas.size > 0) {
            return true
        }
        return false
    }

    override fun setListener() {
        val click = View.OnClickListener { v ->
            setFocusable(et_getFocus)
            when (v.id) {
                R.id.et_boxCode -> setFocusable(et_boxCode)
                R.id.et_code -> setFocusable(et_code)
            }
        }
        et_boxCode!!.setOnClickListener(click)
        et_code!!.setOnClickListener(click)

        // 箱码---数据变化
        et_boxCode!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0) return
                if (!isTextChange) {
                    isTextChange = true
                    smqFlag = '1'
                    mHandler.sendEmptyMessageDelayed(SAOMA, 300)
                }
            }
        })
        // 箱码---长按输入条码
        et_boxCode!!.setOnLongClickListener {
            smqFlag = '1'
            showInputDialog("输入条码", getValues(et_boxCode), "none", WRITE_CODE)
            true
        }
        // 箱码---焦点改变
        et_boxCode.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                lin_focusBox.setBackgroundResource(R.drawable.back_style_red_focus)
            } else {
                if (lin_focusBox != null) {
                    lin_focusBox!!.setBackgroundResource(R.drawable.back_style_gray4)
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
                lin_focusDelivery.setBackgroundResource(R.drawable.back_style_red_focus)
            } else {
                if (lin_focusDelivery != null) {
                    lin_focusDelivery!!.setBackgroundResource(R.drawable.back_style_gray4)
                }
            }
        }
    }

    /**
     * 0：表示点击重置，1：表示保存后重置
     */
    private fun reset(flag : Int) {
        tv_boxSel.text = ""
//        lin_box.visibility = View.GONE
        if(autoSaveAferBarcode == null) {
            et_boxCode.setText("")
        }
        et_code.setText("")
        tv_boxName.text = ""
        tv_boxSize.text = ""
        tv_boxSel.text = ""
        tv_custName.text = ""

        modifyBoxStatus = 1
        boxBarCode = null
        checkDatas.clear()
        mAdapter!!.notifyDataSetChanged()
        timesTamp = user!!.getId().toString() + "-" + Comm.randomUUID()
        parent!!.isChange = false
        curBoxStatus = 0
        if(flag == 0) {
            autoSave = false
            autoSaveAferBarcode = null
            getBoxBarcode_status(0, false)
        }

        smqFlag = '2'
        mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
    }

    fun getBoxBarcode(m : BoxBarCode) {
        boxBarCode = m

//        lin_box.visibility = View.GONE
        tv_boxName.text = m.box.boxName
        tv_boxSize.text = m.box.boxSize
        tv_boxSel.text = m.box.boxName
        // 先扫描物料，再扫描箱码
        if(checkDatas.size > 0 && checkDatas[0].boxBarCodeId == 0) {
            checkDatas.forEach {
                it.boxId = m.boxId
                it.boxBarCodeId = m.id
                it.boxBarCode = m
            }
        }
        if(m.listMbr != null && m.listMbr.size > 0) {
            tv_custName.text = m.listMbr[0].cust.fname
            checkDatas.addAll(m.listMbr)
            checkDatas.forEachIndexed { index, it ->
                it.rowNo = (index+1) // 自动算出行号
                it.createUserName = user!!.username
            }
            mAdapter!!.notifyDataSetChanged()
        }
        if(m.status != 2) {
            smqFlag = '2'
            mHandler.sendEmptyMessageDelayed(SETFOCUS,200)
        }
        getBoxBarcode_status(m.status, false)
    }

    private fun getBoxBarcode_status(status : Int, isFocus : Boolean) {
        if(status == 2) { // 封箱
            et_code.isEnabled = false
            btn_scan.isEnabled = false
            tv_boxStatus.text = Html.fromHtml("<font color='#FF2200'>已封箱</font>")
            btn_openBox.visibility = View.VISIBLE
            btn_closeBox.visibility = View.GONE
            btn_save.visibility = View.GONE
            btn_print.visibility = View.VISIBLE
            lin_focusDelivery.setBackgroundResource(R.drawable.back_style_gray3)

            // 已经封箱的箱子超过了2个，就可以装到大箱子里
            smqFlag = '1'
            mHandler.sendEmptyMessageDelayed(SETFOCUS,200)

        } else {
            et_code.isEnabled = true
            btn_scan.isEnabled = true
            btn_openBox.visibility = View.GONE
            btn_closeBox.visibility = View.VISIBLE
            btn_save.visibility = View.VISIBLE
            btn_print.visibility = View.GONE
            lin_focusDelivery.setBackgroundResource(R.drawable.back_style_gray4)
            if(checkDatas.size > 0) {
                tv_boxStatus.text = Html.fromHtml("<font color='#009900'>已开箱</font>")
            } else {
                tv_boxStatus.text = "未开箱"
            }
            if(isFocus) {
                smqFlag = '2'
                mHandler.sendEmptyMessageDelayed(SETFOCUS,200)
            }
        }
        curBoxStatus = status
    }

    /**
     *  扫码之后    物料启用批次
     */
    fun setBatchCode(pos : Int, batchCode : String, fqty : Double) {
        val entryBarcode = MaterialBinningRecord_Barcode()
        entryBarcode.parentId = 0
        entryBarcode.barcode = getValues(et_code)
        entryBarcode.batchCode = batchCode
        entryBarcode.snCode = ""
        entryBarcode.fqty = fqty
        entryBarcode.isUniqueness = 'N'
        entryBarcode.createUserName = user!!.username

        checkDatas[pos].mbrBarcodes.add(entryBarcode)
//        val addVal = BigdecimalUtil.add(checkDatas[pos].fqty, fqty)
//        checkDatas[pos].fqty = addVal
        checkDatas[pos].fqty = fqty
    }

    /**
     *  扫码之后    物料启用序列号
     */
    fun setSnCode(pos : Int, snCode : String) {
        val entryBarcode = MaterialBinningRecord_Barcode()
        entryBarcode.parentId = 0
        entryBarcode.barcode = getValues(et_code)
        entryBarcode.batchCode = ""
        entryBarcode.snCode = snCode
        entryBarcode.fqty = 1.0
        entryBarcode.isUniqueness = 'Y'
        entryBarcode.createUserName = user!!.username

        checkDatas[pos].mbrBarcodes.add(entryBarcode)
        val addVal = BigdecimalUtil.add(checkDatas[pos].fqty, 1.0)
        checkDatas[pos].fqty = addVal
    }

    /**
     *  扫码之后    物料未启用
     */
    fun unSetBatchOrSnCode(pos : Int, fqty : Double) {
        val entryBarcode = MaterialBinningRecord_Barcode()
        entryBarcode.parentId = 0
        entryBarcode.barcode = getValues(et_code)
        entryBarcode.batchCode = ""
        entryBarcode.snCode = ""
        entryBarcode.fqty = fqty
        entryBarcode.isUniqueness = 'N'
        entryBarcode.createUserName = user!!.username

        checkDatas[pos].mbrBarcodes.add(entryBarcode)
        val addVal = BigdecimalUtil.add(checkDatas[pos].fqty, fqty)
        checkDatas[pos].fqty = addVal
    }

    /**
     * 新增行
     */
    private fun addRow(list : List<DeliveryNoticeEntry>) {
        list.forEach {
            val mbr = MaterialBinningRecord()
            mbr.type = '2'
            mbr.packType = 'A'
            mbr.boxId = if(boxBarCode != null) boxBarCode!!.box.id else 0
            mbr.boxBarCodeId = if(boxBarCode != null) boxBarCode!!.id else 0
            mbr.mtlId = it.fmaterialId
            mbr.funitId = it.funitId
            mbr.fsourceInterId = it.fid
            mbr.fsourceEntryId = it.fentryId
            mbr.fsourceNo = it.deliveryNotice.fbillNo
            mbr.fsourceQty = it.fqty
            mbr.fsourceOrgId = it.deliveryNotice.fdeliveryOrgId
            mbr.forderInterId = it.salOrderId
            mbr.forderEntryId = it.salOrderEntryId
            mbr.forderNo = it.fsrcBillNo
            mbr.forderOrgId = it.deliveryNotice.fsaleOrgId
            mbr.createUserName = user!!.username
            mbr.material = it.material
            mbr.boxBarCode = boxBarCode
            mbr.fdeptId = it.deliveryNotice.fdeliveryDeptId
            mbr.fcustId = it.deliveryNotice.fcustomerId
            mbr.deliveryTypeNumber = it.deliveryNotice.deliveryTypeNumber
            mbr.deliveryTypeName = it.deliveryNotice.deliveryTypeName
            mbr.deliveryWayNumber = it.deliveryNotice.deliveryWayNumber
            mbr.deliveryWayName = it.deliveryNotice.deliveryWayName
            mbr.deliveryAddress = it.deliveryNotice.receiveAddress

            mbr.usableQty = it.usableQty
            mbr.fqty = it.usableQty
            if(checkDatas.size == 0) {
                mbr.rowNo = 1
            } else {
                mbr.rowNo = checkDatas[checkDatas.size-1].rowNo + 1
            }
            checkDatas.add(mbr)
        }

        tv_custName.text = list[0].deliveryNotice.deliCust.fname
        mAdapter!!.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SEL_BOX -> {
                if (resultCode == Activity.RESULT_OK) {
                    val box = data!!.getSerializableExtra("obj") as Box
                    tv_boxSel.setText(box.getBoxName())
                    checkDatas.forEach {
                        it.boxId = box.id
                    }
                }
            }
            SEL_MTL -> { //查询物料	返回
                if (resultCode == Activity.RESULT_OK) {
                    val icItem = data!!.getSerializableExtra("obj") as ICItem
                    getMtlAfter(icItem)
                }
            }
            RESULT_NUM -> { // 数量	返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.getExtras()
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        val num = parseDouble(value)
                        if(num <= 0) {
                            Comm.showWarnDialog(mContext,"数量不能小于等于0！")
                            return
                        }
                        checkDatas[curPos].fqty = num
                        mAdapter!!.notifyDataSetChanged()
                    }
                }
            }
            WRITE_CODE -> {// 输入条码  返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.extras
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        when(smqFlag) {
                            '1' -> setTexts(et_boxCode, value.toUpperCase())
                            '2' -> setTexts(et_code, value.toUpperCase())
                        }
                    }
                }
            }
            RESULT_BATCH_NUM -> {// 批次和数量  返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.extras
                    if (bundle != null) {
                        val batchCode = bundle.getString("batchCode")
                        val fqty = bundle.getDouble("fqty")
                        bt!!.batchCode = batchCode
                        bt!!.barcodeQty = fqty
//                        getMaterial()
                    }
                }
            }
        }
        mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
    }

    /**
     * 调用华为扫码接口，返回的值
     */
    fun getScanData(barcode :String) {
        when (smqFlag) {
            '1' -> setTexts(et_boxCode, barcode)
            '2' -> setTexts(et_code, barcode)
        }
    }

    /**
     * 得到扫码或选择数据
     */
    private fun getMtlAfter(icItem : ICItem) {
        parent!!.isChange = true
//        // 满足条件就查询库存
//        if(icItem.inventoryQty <= 0 && icstockBillEntry.fdcStockId > 0 && icstockBillEntry.fitemId > 0) {
//            run_findInventoryQty()
//        }
    }

    /**
     * 扫码查询对应的方法
     */
    private fun run_smDatas() {
        isTextChange = false
        showLoadDialog("加载中...", false)
        var mUrl:String? = null
        var barcode:String? = null
        var type = ""
        var searchSalOrder = "" // 是否查询销售订单信息
        when(smqFlag) {
            '1' -> { // 箱码
                mUrl = getURL("boxBarCode/findBarcode")
                barcode = getValues(et_boxCode)
                type = "2" // 库存装箱
                searchSalOrder = "1"
            }
            '2' -> { // 物料
                mUrl = getURL("deliveryNotice/findBarcodeByBinning")
                barcode = getValues(et_code)
            }
        }
        val formBody = FormBody.Builder()
                .add("barcode", barcode)
//                .add("searchSalOrder", searchSalOrder) // 查询销售订单信息
                .add("type", type)
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
        var mUrl = getURL("materialBinningRecord/save")
        var mJson = JsonUtil.objectToString(checkDatas)
        val formBody = FormBody.Builder()
                .add("strJson", mJson)
                .add("type", "1") // 生产装箱
                .add("checkOverload", "1") // 检查是否超收
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
     * 删除行
     */
    private fun run_removeRow(mbr : MaterialBinningRecord) {
        showLoadDialog("保存中...", false)
        var mUrl = getURL("materialBinningRecord/removeRow")
        var mJson = JsonUtil.objectToString(checkDatas)
        val formBody = FormBody.Builder()
                .add("id", mbr.id.toString())
                .add("boxBarCodeId", mbr.boxBarCodeId.toString())
                .build()

        val request = Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNDELETE)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                if (!JsonUtil.isSuccess(result)) {
                    val msg = mHandler.obtainMessage(UNDELETE, result)
                    mHandler.sendMessage(msg)
                    return
                }
                val msg = mHandler.obtainMessage(DELETE, result)
                LogUtil.e("run_removeRow --> onResponse", result)
                mHandler.sendMessage(msg)
            }
        })
    }

    /**
     * 开箱或者封箱
     */
    private fun run_modifyStatus(status : String) {
        showLoadDialog("保存中...", false)
        var mUrl = getURL("boxBarCode/modifyStatus")
        val formBody = FormBody.Builder()
                .add("id", boxBarCode!!.id.toString())
                .add("status", status)
                .build()

        val request = Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNBOX_STATUS)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                if (!JsonUtil.isSuccess(result)) {
                    val msg = mHandler.obtainMessage(UNBOX_STATUS, result)
                    mHandler.sendMessage(msg)
                    return
                }
                val msg = mHandler.obtainMessage(BOX_STATUS, result)
                LogUtil.e("run_modifyStatus --> onResponse", result)
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

    override fun onDestroyView() {
        closeHandler(mHandler)
        mBinder!!.unbind()
        super.onDestroyView()
    }
}
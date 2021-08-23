package ykk.xc.com.zgwms.purchase

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import butterknife.OnClick
import kotlinx.android.synthetic.main.pur_in_stock_fragment1.*
import kotlinx.android.synthetic.main.pur_in_stock_main.*
import okhttp3.*
import org.greenrobot.eventbus.EventBus
import ykk.xc.com.zgwms.R
import ykk.xc.com.zgwms.basics.Dept_DialogActivity
import ykk.xc.com.zgwms.basics.Operator_DialogActivity
import ykk.xc.com.zgwms.basics.Organization_DialogActivity
import ykk.xc.com.zgwms.basics.Supplier_DialogActivity
import ykk.xc.com.zgwms.bean.EventBusEntity
import ykk.xc.com.zgwms.bean.ICStockBill
import ykk.xc.com.zgwms.bean.User
import ykk.xc.com.zgwms.bean.k3Bean.Department_K3
import ykk.xc.com.zgwms.bean.k3Bean.Operator_K3
import ykk.xc.com.zgwms.bean.k3Bean.Organization_K3
import ykk.xc.com.zgwms.bean.k3Bean.Supplier_K3
import ykk.xc.com.zgwms.bean.pur.POOrder
import ykk.xc.com.zgwms.bean.pur.POOrderEntry
import ykk.xc.com.zgwms.bean.pur.PurReceive
import ykk.xc.com.zgwms.bean.pur.PurReceiveEntry
import ykk.xc.com.zgwms.comm.BaseFragment
import ykk.xc.com.zgwms.comm.Comm
import ykk.xc.com.zgwms.util.JsonUtil
import ykk.xc.com.zgwms.util.LogUtil
import java.io.IOException
import java.lang.ref.WeakReference
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

/**
 * 日期：2019-10-16 09:50
 * 描述：采购入库
 * 作者：ykk
 */
class Pur_InStock_Fragment1 : BaseFragment() {

    companion object {
        private val SEL_PUR_ORG = 60
        private val SEL_REC_ORG = 61
        private val SEL_PUR_DEPT = 62
        private val SEL_REC_DEPT = 63
        private val SEL_SUPP = 64
        private val SEL_PUR = 65
        private val SEL_RECEIVE = 66
        private val SEL_EMP1 = 67
        private val SEL_EMP2 = 68
        private val SAVE = 201
        private val UNSAVE = 501
        private val FIND_SOURCE_PUR = 202
        private val UNFIND_SOURCE_PUR = 502
        private val FIND_SOURCE_RECEIVE = 203
        private val UNFIND_SOURCE_RECEIVE = 503
        private val FIND_ICSTOCKBILL = 204
        private val UNFIND_ICSTOCKBILL = 504
        private val FIND_MINORORG = 205
        private val UNFIND_MINORORG = 505

        private val SETFOCUS = 1
        private val SAOMA = 2
        private val WRITE_CODE = 3
    }

    private val context = this
    private var okHttpClient: OkHttpClient? = null
    private var user: User? = null
    private var mContext: Activity? = null
    private var parent: Pur_InStock_MainActivity? = null
    private val df = DecimalFormat("#.###")
    private var timesTamp:String? = null // 时间戳
    var icstockBill = ICStockBill() // 保存的对象
//    var isReset = false // 是否点击了重置按钮.
    var purReceiveEntryList:List<PurReceiveEntry>? = null
    var poOrderEntryList:List<POOrderEntry>? = null
    private var icstockBillId = 0 // 上个页面传来的id
    private var isTextChange: Boolean = false // 是否进入TextChange事件

    // 消息处理
    private val mHandler = MyHandler(this)

    private class MyHandler(activity: Pur_InStock_Fragment1) : Handler() {
        private val mActivity: WeakReference<Pur_InStock_Fragment1>

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
                    SAVE -> {// 保存成功 进入
                        val strId_pdaNo = JsonUtil.strToString(msgObj)
                        if(m.icstockBill.id == 0) {
                            val arr = strId_pdaNo.split(":") // id和pdaNo数据拼接（1:IC201912121）
                            m.icstockBill.id = m.parseInt(arr[0])
                            m.icstockBill.pdaNo = arr[1]
                            m.tv_pdaNo.text = arr[1]
                        }
                        m.parent!!.isMainSave = true
                        m.parent!!.viewPager.setScanScroll(true); // 放开左右滑动
                        m.toasts("保存成功✔")
                        m.parent!!.fragment2.showLocalStockGroup(m.icstockBill.stockOrg.fnumber)
                        m.setEnables(m.tv_sourceTypeSel, R.drawable.back_style_gray2a, false)
                        m.setEnables(m.tv_sourceOrderSel, R.drawable.back_style_gray2a, false)
                        // 滑动第二个页面
                        m.parent!!.viewPager.setCurrentItem(1, false)
                        m.parent!!.isChange = if(m.icstockBillId == 0) true else false
                    }
                    UNSAVE -> { // 保存失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "保存失败！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    FIND_SOURCE_PUR ->{ // 查询采购订单 返回
                        val list = JsonUtil.strToList(msgObj, POOrderEntry::class.java)
                        m.getPOOrderData(list)
                    }
                    UNFIND_SOURCE_PUR ->{ // 查询采购订单 失败！ 返回
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "很抱歉，没有找到数据，或全部下推！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    FIND_SOURCE_RECEIVE ->{ // 查询收料通知单 返回
                        val list = JsonUtil.strToList(msgObj, PurReceiveEntry::class.java)
                        m.getPurReceiveData(list)
                    }
                    UNFIND_SOURCE_RECEIVE ->{ // 查询收料通知单失败！ 返回
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "很抱歉，没有找到数据，或全部下推！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    FIND_ICSTOCKBILL -> { // 查询主表信息 成功
                        val icsBill = JsonUtil.strToObject(msgObj, ICStockBill::class.java)
                        m.setICStockBill(icsBill)
                    }
                    UNFIND_ICSTOCKBILL -> { // 查询主表信息 失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "查询信息有错误！2秒后自动关闭..."
                        Comm.showWarnDialog(m.mContext, errMsg)
                        m.mHandler.postDelayed(Runnable {
                            m.mContext!!.finish()
                        },2000)
                    }
                    FIND_MINORORG -> { // 查询次要组织信息 成功
                        val org = JsonUtil.strToObject(msgObj, Organization_K3::class.java)
                        m.icstockBill.fstockOutOrgId = org.forgId
                        m.icstockBill.stockOutOrg = org
                        m.tv_purOrgSel.text = org.fname
                    }
                    UNFIND_MINORORG -> { // 查询次要组织 失败

                    }
                    SETFOCUS -> { // 当弹出其他窗口会抢夺焦点，需要跳转下，才能正常得到值
                        m.setFocusable(m.et_getFocus)
//                        m.setFocusable(m.et_code)
                    }
                    SAOMA -> { // 扫码之后
                        // 执行查询方法
//                        m.run_purReceiveList()
                    }
                }
            }
        }
    }

    fun setICStockBill(m : ICStockBill) {
        icstockBill.id = m.id
        icstockBill.pdaNo = m.pdaNo
        icstockBill.billType = m.billType
        icstockBill.ftranType = m.ftranType
        icstockBill.fselTranType = m.fselTranType
        icstockBill.fdate = m.fdate
        icstockBill.fstockOrgId = m.fstockOrgId
        icstockBill.fstockOutOrgId = m.fstockOutOrgId
        icstockBill.fsupplierId = m.fsupplierId
        icstockBill.recDeptId = m.recDeptId
        icstockBill.purDeptId = m.purDeptId
        icstockBill.empNumber = m.empNumber
        icstockBill.empName = m.empName
        icstockBill.stockManagerNumber = m.stockManagerNumber
        icstockBill.stockManagerName = m.stockManagerName

        icstockBill.createUserId = m.createUserId        // 创建人id
        icstockBill.createUserName = m.createUserName        // 创建人
        icstockBill.createDate = m.createDate            // 创建日期
        icstockBill.isToK3 = m.isToK3                   // 是否提交到K3
        icstockBill.k3Number = m.k3Number                // k3返回的单号

        icstockBill.stockOrg = m.stockOrg
        icstockBill.stockOutOrg = m.stockOutOrg
        icstockBill.supplier = m.supplier
        icstockBill.recDept = m.recDept
        icstockBill.purDept = m.purDept

        tv_pdaNo.text = m.pdaNo
        tv_inDateSel.text = m.fdate
        if(m.stockOrg != null) {
            tv_recOrgSel.text = m.stockOrg.fname
            setEnables(tv_recOrgSel, R.drawable.back_style_gray2a, false)
        }
        if(m.stockOutOrg != null) {
            tv_purOrgSel.text = m.stockOutOrg.fname
            setEnables(tv_purOrgSel, R.drawable.back_style_gray2a, false)
        }
        if(m.supplier != null) {
            tv_suppSel.text = m.supplier.fname
            setEnables(tv_suppSel, R.drawable.back_style_gray2a,false)
        }
        if(m.recDept != null) {
            tv_recDeptSel.text = m.recDept.fname
            setEnables(tv_recDeptSel, R.drawable.back_style_gray2a,false)
        }
        if(m.purDept != null) {
            tv_purDeptSel.text = m.purDept.fname
            setEnables(tv_purDeptSel, R.drawable.back_style_gray2a,false)
        }

        setEnables(tv_sourceTypeSel, R.drawable.back_style_gray2a, false)
        setEnables(tv_sourceOrderSel, R.drawable.back_style_gray2a, false)
        when(m.fselTranType) {
            0 -> {
                tv_sourceTypeSel.text = "无源单"
                tv_sourceTitle.text = "无源单"
            }
            1 -> {
                tv_sourceTypeSel.text = "采购订单"
                tv_sourceTitle.text = "采购订单"
            }
            2 -> {
                tv_sourceTypeSel.text = "收料通知单"
                tv_sourceTitle.text = "收料通知单"
            }
        }
        tv_sourceOrderSel.text = Comm.isNULLS(m.strSourceNo)
        tv_emp1Sel.text = m.empName
        tv_emp2Sel.text = m.stockManagerName

        parent!!.isChange = false
        parent!!.isMainSave = true
        parent!!.viewPager.setScanScroll(true); // 放开左右滑动
        EventBus.getDefault().post(EventBusEntity(12)) // 发送指令到fragment3，查询分类信息
    }

    override fun setLayoutResID(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.pur_in_stock_fragment1, container, false)
    }

    override fun initView() {
        mContext = getActivity()
        parent = mContext as Pur_InStock_MainActivity
    }

    override fun initData() {
        if (okHttpClient == null) {
            okHttpClient = OkHttpClient.Builder()
                    //                .connectTimeout(10, TimeUnit.SECONDS) // 设置连接超时时间（默认为10秒）
                    .writeTimeout(120, TimeUnit.SECONDS) // 设置写的超时时间
                    .readTimeout(120, TimeUnit.SECONDS) //设置读取超时时间
                    .build()
        }

        getUserInfo()
//        hideSoftInputMode(mContext, et_code)
        timesTamp = user!!.getId().toString() + "-" + Comm.randomUUID()

        if(user!!.organization == null) {
            Comm.showWarnDialog(mContext,"登陆的用户没有维护组织，请在WMS维护！3秒后自动关闭...")
            mHandler.postDelayed(Runnable {
                mContext!!.finish()
            },3000)
            return
        }

        icstockBill.billType = "CGRK" // 采购订单入库
        icstockBill.ftranType = 1
        icstockBill.fselTranType = 0
        icstockBill.empNumber = user!!.kdUserNumber
        icstockBill.empName = user!!.kdUserName
        icstockBill.stockManagerNumber = user!!.kdUserNumber
        icstockBill.stockManagerName = user!!.kdUserName
        icstockBill.createUserId = user!!.id
        icstockBill.createUserName = user!!.username

        tv_inDateSel.text = Comm.getSysDate(7)
        tv_operationManName.text = user!!.kdAccountName
        tv_emp1Sel.text = user!!.kdUserName
        tv_emp2Sel.text = user!!.kdUserName
        initOrg()

        bundle()
    }

    fun bundle() {
        val bundle = mContext!!.intent.extras
        if(bundle != null) {
            if(bundle.containsKey("id")) { // 查询过来的
                icstockBillId = bundle.getInt("id") // ICStockBill主表id
                // 查询主表信息
                run_findStockBill(icstockBillId)
            }

        }
    }

    private fun initOrg() {
        if(user!!.organization != null) {
            icstockBill.fstockOrgId = user!!.organizationId
            icstockBill.fstockOutOrgId = user!!.organizationId
            icstockBill.stockOrg = user!!.organization
            icstockBill.stockOutOrg = user!!.organization
            tv_recOrgSel.text = user!!.organization.fname
            tv_purOrgSel.text = user!!.organization.fname
            // 查询次要组织信息
            run_findMinorOrg()

        } else {
            toasts("请在PC端维护用户的默认组织！")
            mContext!!.finish()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
        }
    }

    @OnClick(R.id.tv_inDateSel, R.id.tv_sourceTypeSel, R.id.tv_sourceOrderSel, R.id.tv_purOrgSel,R.id.tv_recOrgSel, R.id.tv_suppSel,
             R.id.tv_purDeptSel, R.id.tv_recDeptSel, R.id.tv_emp1Sel, R.id.tv_emp2Sel, R.id.btn_save, R.id.btn_clone)
    fun onViewClicked(view: View) {
        var bundle: Bundle? = null
        when (view.id) {
            R.id.tv_inDateSel -> { // 选择日期
                Comm.showDateDialog(mContext, tv_inDateSel, 0)
            }
            R.id.tv_sourceTypeSel -> { // 源单类型选择
                pop_sourceType(view)
                popWindow!!.showAsDropDown(view)
            }
            R.id.tv_sourceOrderSel -> { // 源单选择
                bundle = Bundle()
                bundle.putInt("fsupplierId", icstockBill.fsupplierId)
                if(icstockBill.fselTranType == 1) {
                    bundle.putInt("frecOrgId" , icstockBill.fstockOrgId)
                    showForResult(Pur_Sel_POOrder_DialogActivity::class.java, SEL_PUR, bundle)
                } else {
                    bundle.putInt("frecOrgId" , icstockBill.fstockOrgId)
                    bundle.putInt("fpurOrgId" , icstockBill.fstockOutOrgId)
                    showForResult(Pur_Sel_PurReceive_DialogActivity::class.java, SEL_RECEIVE, bundle)
                }
            }
            R.id.tv_recOrgSel -> { // 选择收料组织
                showForResult(Organization_DialogActivity::class.java, SEL_REC_ORG, null)
            }
            R.id.tv_purOrgSel -> { // 选择采购组织
                showForResult(Organization_DialogActivity::class.java, SEL_PUR_ORG, null)
            }
            R.id.tv_suppSel -> { // 选择供应商
                bundle = Bundle()
                bundle.putInt("fuseOrgId" ,icstockBill.fstockOrgId)
                showForResult(Supplier_DialogActivity::class.java, SEL_SUPP, bundle)
            }
            R.id.tv_purDeptSel -> { // 选择采购部门
                bundle = Bundle()
                bundle.putInt("fuseOrgId", icstockBill.fstockOutOrgId)
                showForResult(Dept_DialogActivity::class.java, SEL_PUR_DEPT, bundle)
            }
            R.id.tv_recDeptSel -> { // 选择收料部门
                bundle = Bundle()
                bundle.putInt("fuseOrgId", icstockBill.fstockOrgId)
                showForResult(Dept_DialogActivity::class.java, SEL_REC_DEPT, bundle)
            }
            R.id.tv_emp1Sel -> { // 选择采购员
                bundle = Bundle()
                bundle.putInt("fuseOrgId", icstockBill.fstockOutOrgId)
                bundle.putString("foperatorType", "CGY") // 业务员类型 （ 销售员：XSY，采购员：CGY，仓管员：WHY，计划员：JHY ）
                showForResult(Operator_DialogActivity::class.java, SEL_EMP1, bundle)
            }
            R.id.tv_emp2Sel -> { // 选择仓管员
                bundle = Bundle()
                bundle.putInt("fuseOrgId", icstockBill.fstockOutOrgId)
                bundle.putString("foperatorType", "WHY") // 业务员类型 （ 销售员：XSY，采购员：CGY，仓管员：WHY，计划员：JHY ）
                showForResult(Operator_DialogActivity::class.java, SEL_EMP2, bundle)
            }
            R.id.btn_save -> { // 保存
                if(!checkSave(true)) return
                run_save();
            }
            R.id.btn_clone -> { // 重置
                if (parent!!.isChange) {
                    val build = AlertDialog.Builder(mContext)
                    build.setIcon(R.drawable.caution)
                    build.setTitle("系统提示")
                    build.setMessage("您有未保存的数据，继续重置吗？")
                    build.setPositiveButton("是") { dialog, which -> reset() }
                    build.setNegativeButton("否", null)
                    build.setCancelable(false)
                    build.show()

                } else {
                    reset()
                }
            }
        }
    }

    /**
     * 保存检查数据判断
     */
    fun checkSave(isHint :Boolean) : Boolean {
        if(icstockBill.fstockOrgId == 0) {
            if(isHint) Comm.showWarnDialog(mContext, "请选择收料组织！")
            return false;
        }
        if(icstockBill.fstockOutOrgId == 0) {
            if(isHint) Comm.showWarnDialog(mContext, "请选择采购组织！")
            return false;
        }
        if(icstockBill.id == 0 && icstockBill.fselTranType == 1 && (getValues(tv_sourceOrderSel).length == 0 || poOrderEntryList == null)) {
            if(isHint) Comm.showWarnDialog(mContext, "请选择采购订单！")
            return false;
        }
        if(icstockBill.id == 0 && icstockBill.fselTranType == 2 && (getValues(tv_sourceOrderSel).length == 0 || purReceiveEntryList == null)) {
            if(isHint) Comm.showWarnDialog(mContext, "请选择收料通知单！")
            return false;
        }
        if (icstockBill.fsupplierId == 0) {
            if(isHint) Comm.showWarnDialog(mContext, "请选择供应商！")
            return false;
        }
        return true;
    }

    fun reset() {
        icstockBill.fselTranType = 0 // 无源单入库
        setEnables(tv_purOrgSel, R.drawable.back_style_blue2,true)
        setEnables(tv_recOrgSel, R.drawable.back_style_blue2,true)
        setEnables(tv_suppSel, R.drawable.back_style_blue2,true)
        setEnables(tv_purDeptSel, R.drawable.back_style_blue2,true)
        setEnables(tv_recDeptSel, R.drawable.back_style_blue2,true)
        setEnables(tv_sourceTypeSel, R.drawable.back_style_blue2,true)
//        setEnables(tv_sourceOrderSel, R.drawable.back_style_blue2, true)
        setEnables(tv_sourceOrderSel, R.drawable.back_style_gray2a, false)
        isTextChange = false
        parent!!.isMainSave = false
        parent!!.viewPager.setScanScroll(false) // 禁止滑动
        tv_pdaNo.text = ""
        tv_inDateSel.text = Comm.getSysDate(7)
        tv_purOrgSel.text = ""
        tv_recOrgSel.text = ""
        tv_suppSel.text = ""
//        tv_sourceTypeSel.text = "采购订单"
//        tv_sourceTitle.text = "采购订单"
        tv_sourceTypeSel.text = "无源单"
        tv_sourceTitle.text = "无源单"
        tv_sourceOrderSel.text = ""
        tv_purDeptSel.text = ""
        tv_recDeptSel.text = ""
        icstockBill.id = 0
        icstockBill.pdaNo = ""
        icstockBill.fstockOutOrgId = 0
        icstockBill.fstockOrgId = 0
        icstockBill.fsupplierId = 0
        icstockBill.purDeptId = 0
        icstockBill.recDeptId = 0
        icstockBill.stockOutOrg = null
        icstockBill.stockOrg = null
        icstockBill.supplier = null
        icstockBill.purDept = null
        icstockBill.recDept = null
        initOrg()
//        icstockBill.fempId = 0
//        icstockBill.fsmanagerId = 0

        icstockBillId = 0
        poOrderEntryList = null
        purReceiveEntryList = null
        timesTamp = user!!.getId().toString() + "-" + Comm.randomUUID()
        parent!!.isChange = false
        mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
        EventBus.getDefault().post(EventBusEntity(11)) // 发送指令到fragment2，告其清空
    }

    /**
     * 创建PopupWindow 【单据类型选择 】
     */
    private var popWindow: PopupWindow? = null
    private fun pop_sourceType(v: View) {
        if (null != popWindow) {//不为空就隐藏
            popWindow!!.dismiss()
            return
        }
        // 获取自定义布局文件popupwindow_left.xml的视图
        val popV = layoutInflater.inflate(R.layout.pur_in_stock_sourcetype_popwindow, null)
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popWindow = PopupWindow(popV, v.width, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        // 设置动画效果
        // popWindow.setAnimationStyle(R.style.AnimationFade);
        popWindow!!.setBackgroundDrawable(BitmapDrawable())
        popWindow!!.isOutsideTouchable = true
        popWindow!!.isFocusable = true

        popV.findViewById<View>(R.id.btn2).visibility = View.VISIBLE
//        popV.findViewById<View>(R.id.btn3).visibility = View.VISIBLE
        // 点击其他地方消失
        val click = View.OnClickListener { v ->
            when (v.id) {
                R.id.btn1 -> {
                    icstockBill.fselTranType = 0
                    tv_sourceTypeSel.text = "无源单"
                    tv_sourceTitle.text = "无源单"
                    setEnables(tv_sourceOrderSel, R.drawable.back_style_gray2a, false)
                }
                R.id.btn2 -> {
                    icstockBill.fselTranType = 1
                    tv_sourceTypeSel.text = "采购订单"
                    tv_sourceTitle.text = "采购订单"
                    setEnables(tv_sourceOrderSel, R.drawable.back_style_blue2, true)
                    val bundle = Bundle()
                    bundle.putInt("frecOrgId" , icstockBill.fstockOrgId)
                    bundle.putInt("fsupplierId", icstockBill.fsupplierId)
                    showForResult(Pur_Sel_POOrder_DialogActivity::class.java, SEL_PUR, bundle)
                }
                R.id.btn3 -> {
                    icstockBill.fselTranType = 2
                    tv_sourceTypeSel.text = "收料通知单"
                    tv_sourceTitle.text = "收料通知单"
                    setEnables(tv_sourceOrderSel, R.drawable.back_style_blue2, true)
                    val bundle = Bundle()
                    bundle.putInt("frecOrgId" , icstockBill.fstockOrgId)
                    bundle.putInt("fpurOrgId" , icstockBill.fstockOutOrgId)
                    bundle.putInt("fsupplierId", icstockBill.fsupplierId)
                    showForResult(Pur_Sel_PurReceive_DialogActivity::class.java, SEL_RECEIVE, bundle)
                }
            }
            popWindow!!.dismiss()
        }
        popV.findViewById<View>(R.id.btn1).setOnClickListener(click)
        popV.findViewById<View>(R.id.btn2).setOnClickListener(click)
        popV.findViewById<View>(R.id.btn3).setOnClickListener(click)
    }

    /**
     * 采购订单数据返回
     */
    private fun getPOOrderData(list :List<POOrderEntry>) {
        poOrderEntryList = list
        val poOrder = list[0].poOrder

        icstockBill.fstockOutOrgId = poOrder.fpurChaseOrgId
//        icstockBill.fstockOrgId = poOrder.fpurChaseOrgId
        icstockBill.stockOutOrg = poOrder.purOrg
//        icstockBill.stockOrg = poOrder.purOrg
        icstockBill.fsupplierId = poOrder.supplier.fsupplierId
        icstockBill.supplier = poOrder.supplier
        icstockBill.purDeptId = poOrder.fpurChaseDeptId
        icstockBill.purDept = poOrder.purDept
        val billTypeNumber = poOrder.fbillTypeNumber
        // 采购订单单据类型编码（转）采购入库单据类型编码
        if (billTypeNumber == "CGDD07_SYS") { // 采购订单单据类型
            icstockBill.fbillTypeNumber = "RKD07_SYS" // 采购入库单据类型（VMI入库）
            icstockBill.fownerType = "BD_Supplier"
            icstockBill.fownerNumber = poOrder.supplier.fnumber
            icstockBill.fownerName = poOrder.supplier.fname

        } else if (billTypeNumber == "CGDD02_SYS") { // 委外采购订单入库
            icstockBill.fbillTypeNumber = "RKD03_SYS" // 采购入库单据类型（标准采购入库）
            icstockBill.fownerType = "BD_OwnerOrg"
            /*icstockBill.fownerNumber = icstockBill.stockOrg.fnumber
            icstockBill.fownerName = icstockBill.stockOrg.fname*/
            icstockBill.fownerNumber = list[0].requireOrg.fnumber
            icstockBill.fownerName = list[0].requireOrg.fname

        } else {
            icstockBill.fbillTypeNumber = "RKD01_SYS" // 采购入库单据类型（标准采购入库）
            icstockBill.fownerType = "BD_OwnerOrg"
            /*icstockBill.fownerNumber = icstockBill.stockOrg.fnumber
            icstockBill.fownerName = icstockBill.stockOrg.fname*/
            icstockBill.fownerNumber = list[0].requireOrg.fnumber
            icstockBill.fownerName = list[0].requireOrg.fname
        }
        icstockBill.fbusinessTypeNumber = poOrder.fbusinessType

        if(poOrder.purOrg != null) {
            tv_purOrgSel.text = poOrder.purOrg.fname
//            tv_recOrgSel.text = poOrder.purOrg.fname
            setEnables(tv_purOrgSel, R.drawable.back_style_gray2a,false)
            setEnables(tv_recOrgSel, R.drawable.back_style_gray2a,false)
        }
        if(poOrder.supplier != null) {
            tv_suppSel.text = poOrder.supplier.fname
            setEnables(tv_suppSel, R.drawable.back_style_gray2a,false)
        }
        if(poOrder.purDept != null) {
            tv_purDeptSel.text = poOrder.purDept.fname
            setEnables(tv_purDeptSel, R.drawable.back_style_gray2a,false)
        }
        tv_sourceOrderSel.text = poOrder.fbillNo
        // 禁止修改项
        setEnables(tv_sourceTypeSel, R.drawable.back_style_gray2a, false)
        setEnables(tv_sourceOrderSel, R.drawable.back_style_gray2a,false)
        // 是否可以自动保存
//                        if(checkSave(false)) run_save()
    }

    /**
     * 收料通知单数据返回
     */
    private fun getPurReceiveData(list :List<PurReceiveEntry>) {
        purReceiveEntryList = list
        val purReceive = list[0].purReceive

        icstockBill.fstockOutOrgId = purReceive.fpurOrgId
//        icstockBill.fstockOrgId = purReceive.fstockOrgId
        icstockBill.stockOutOrg = purReceive.purOrg
//        icstockBill.stockOrg = purReceive.recOrg
        icstockBill.fsupplierId = purReceive.fsupplierId
        icstockBill.supplier = purReceive.supplier
        icstockBill.purDeptId = purReceive.purDeptId
        icstockBill.purDept = purReceive.purDept
        icstockBill.recDeptId = purReceive.recDeptId
        icstockBill.recDept = purReceive.recDept
        val billTypeNumber = purReceive.getFbillTypeNumber()
        // 收料订单单据类型编码（转）采购入库单据类型编码
        if (billTypeNumber == "SLD06_SYS") { // 收料订单单据类型
            icstockBill.setFbillTypeNumber("RKD07_SYS") // 采购入库单据类型（VMI入库）
            icstockBill.fownerType = "BD_Supplier"
            icstockBill.fownerNumber = purReceive.supplier.fnumber
            icstockBill.fownerName = purReceive.supplier.fname
        } else {
            icstockBill.setFbillTypeNumber("RKD01_SYS") // 采购入库单据类型（标准采购入库）
            icstockBill.fownerType = "BD_OwnerOrg"
            /*icstockBill.fownerNumber = icstockBill.stockOrg.fnumber
            icstockBill.fownerName = icstockBill.stockOrg.fname*/
            icstockBill.fownerNumber = purReceive.needOrg.fnumber
            icstockBill.fownerName = purReceive.needOrg.fname
        }
        icstockBill.setFbusinessTypeNumber(purReceive.getFbusinessType())

        if(purReceive.purOrg != null) {
            tv_purOrgSel.text = purReceive.purOrg.fname
            setEnables(tv_purOrgSel, R.drawable.back_style_gray2a,false)
        }
        if(purReceive.recOrg != null) {
//            tv_recOrgSel.text = purReceive.recOrg.fname
            setEnables(tv_recOrgSel, R.drawable.back_style_gray2a,false)
        }
        if(purReceive.supplier != null) {
            tv_suppSel.text = purReceive.supplier.fname
            setEnables(tv_suppSel, R.drawable.back_style_gray2a,false)
        }
        if(purReceive.purDept != null) {
            tv_purDeptSel.text = purReceive.purDept.fname
            setEnables(tv_purDeptSel, R.drawable.back_style_gray2a,false)
        }
        if(purReceive.recDept != null) {
            tv_recDeptSel.text = purReceive.recDept.fname
            setEnables(tv_recDeptSel, R.drawable.back_style_gray2a,false)
        }
        tv_sourceOrderSel.text = purReceive.fbillNo

        // 禁止修改项
        setEnables(tv_sourceTypeSel, R.drawable.back_style_gray2a, false)
        setEnables(tv_sourceOrderSel, R.drawable.back_style_gray2a,false)

        // 是否可以自动保存
//                        if(checkSave(false)) run_save()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                SEL_REC_ORG -> { // 收料组织
                    val org = data!!.getSerializableExtra("obj") as Organization_K3
                    // 每次选择了新的组织，就清空
                    if(icstockBill.fstockOrgId != org.forgId) {
                        reset()
                    }
                    icstockBill.fstockOrgId = org.forgId
                    icstockBill.stockOrg = org
                    icstockBill.fstockOutOrgId = org.forgId
                    icstockBill.stockOutOrg = org
                    tv_recOrgSel.text = org!!.fname
                    tv_purOrgSel.text = org!!.fname
                }
                SEL_PUR_ORG -> { // 采购组织
                    val org = data!!.getSerializableExtra("obj") as Organization_K3
                    icstockBill.fstockOutOrgId = org.forgId
                    icstockBill.stockOutOrg = org
                    tv_purOrgSel.text = org!!.fname
                }
                SEL_SUPP -> {//查询供应商	返回
                    val supp = data!!.getSerializableExtra("obj") as Supplier_K3
                    icstockBill.fsupplierId = supp.fsupplierId
                    icstockBill.supplier = supp
                    tv_suppSel.text = supp!!.fname
                }
                SEL_PUR -> { // 查询采购订单	返回
//                    val poOrder = data!!.getSerializableExtra("obj") as POOrder
                    val strFid = data!!.getStringExtra("obj")
                    run_poOrderList(strFid)
                }
                SEL_RECEIVE -> { // 查询收料通知单	返回
//                    val PurReceive = data!!.getSerializableExtra("obj") as PurReceive
                    val strFid = data!!.getStringExtra("obj")
                    run_purReceiveList(strFid)
                }
                SEL_PUR_DEPT -> {// 查询部门	返回
                    val dept = data!!.getSerializableExtra("obj") as Department_K3
                    tv_purDeptSel.text = dept!!.fname
                    icstockBill.purDeptId = dept.fdeptId
                    icstockBill.purDept = dept
                }
                SEL_REC_DEPT -> {// 查询部门	返回
                    val dept = data!!.getSerializableExtra("obj") as Department_K3
                    tv_recDeptSel.text = dept!!.fname
                    icstockBill.recDeptId = dept.fdeptId
                    icstockBill.recDept = dept
                }
                SEL_EMP1 -> {//查询采购员	返回
                    val emp = data!!.getSerializableExtra("obj") as Operator_K3
                    tv_emp1Sel.text = emp!!.fname
                    icstockBill.empNumber = emp.fnumber
                    icstockBill.empName = emp.fname
                }
                SEL_EMP2 -> {//查询仓管员	返回
                    val emp = data!!.getSerializableExtra("obj") as Operator_K3
                    tv_emp2Sel.text = emp!!.fname
                    icstockBill.stockManagerNumber = emp.fnumber
                    icstockBill.stockManagerName = emp.fname
                }
                WRITE_CODE -> {// 输入条码  返回
                    val bundle = data!!.extras
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
//                        setTexts(et_code, value.toUpperCase())
                    }
                }
            }
        }

        // 是否可以自动保存
//        if(checkSave(false)) run_save()
    }

    /**
     * 保存
     */
    private fun run_save() {
        icstockBill.fdate = getValues(tv_inDateSel)
        if(icstockBill.fselTranType == 0) { // 如果是无源单
            icstockBill.fbillTypeNumber = "RKD01_SYS" // 采购入库单据类型（标准采购入库）
            icstockBill.fbusinessTypeNumber = "CG"
            icstockBill.fownerType = "BD_OwnerOrg"
            icstockBill.fownerNumber = icstockBill.stockOrg.fnumber
            icstockBill.fownerName = icstockBill.stockOrg.fname
        }
        showLoadDialog("保存中...", false)
        val mUrl = getURL("stockBill_WMS/save")

        val mJson = JsonUtil.objectToString(icstockBill)
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
     * 查询采购订单
     */
    private fun run_poOrderList(fid :String) {
        isTextChange = false
        showLoadDialog("保存中...", false)
        val mUrl = getURL("poOrder/findEntryByParam")

        val formBody = FormBody.Builder()
                .add("strFid", fid)
                .add("frecOrgId", icstockBill.fstockOrgId.toString())
                .build()

        val request = Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNFIND_SOURCE_PUR)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                if (!JsonUtil.isSuccess(result)) {
                    val msg = mHandler.obtainMessage(UNFIND_SOURCE_PUR, result)
                    mHandler.sendMessage(msg)
                    return
                }
                val msg = mHandler.obtainMessage(FIND_SOURCE_PUR, result)
                LogUtil.e("run_poOrderList --> onResponse", result)
                mHandler.sendMessage(msg)
            }
        })
    }

    /**
     * 查询收料通知单
     */
    private fun run_purReceiveList(fid :String) {
        isTextChange = false
        showLoadDialog("保存中...", false)
        val mUrl = getURL("purReceive/findEntryByParam")

        val formBody = FormBody.Builder()
                .add("strFid", fid)
                .add("frecOrgId", icstockBill.fstockOrgId.toString())
                .add("fpurOrgId", icstockBill.fstockOutOrgId.toString())
                .build()

        val request = Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNFIND_SOURCE_RECEIVE)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                if (!JsonUtil.isSuccess(result)) {
                    val msg = mHandler.obtainMessage(UNFIND_SOURCE_RECEIVE, result)
                    mHandler.sendMessage(msg)
                    return
                }
                val msg = mHandler.obtainMessage(FIND_SOURCE_RECEIVE, result)
                LogUtil.e("run_purReceiveList --> onResponse", result)
                mHandler.sendMessage(msg)
            }
        })
    }

    /**
     *  查询主表信息
     */
    private fun run_findStockBill(id: Int) {
        val mUrl = getURL("stockBill_WMS/findStockBill")

        val formBody = FormBody.Builder()
                .add("id", id.toString())
                .build()

        val request = Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNFIND_ICSTOCKBILL)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                if (!JsonUtil.isSuccess(result)) {
                    val msg = mHandler.obtainMessage(UNFIND_ICSTOCKBILL, result)
                    mHandler.sendMessage(msg)
                    return
                }
                val msg = mHandler.obtainMessage(FIND_ICSTOCKBILL, result)
                LogUtil.e("run_findStockBill --> onResponse", result)
                mHandler.sendMessage(msg)
            }
        })
    }

    /**
     *  查询次要组织信息
     */
    private fun run_findMinorOrg() {
        showLoadDialog("加载中...")
        val mUrl = getURL("organization/findMinorOrg")

        val formBody = FormBody.Builder()
                .add("mainOrgId", icstockBill.stockOrg.forgId.toString())
                .add("searchPurOrg", "1") // 查询采购组织
                .build()

        val request = Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNFIND_MINORORG)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                if (!JsonUtil.isSuccess(result)) {
                    mHandler.sendEmptyMessage(UNFIND_MINORORG)
                    return
                }
                val msg = mHandler.obtainMessage(FIND_MINORORG, result)
                LogUtil.e("run_findMinorOrg --> onResponse", result)
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
package ykk.xc.com.zgwms.sales

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
import kotlinx.android.synthetic.main.sal_out_stock_fragment1.*
import kotlinx.android.synthetic.main.sal_out_stock_main.*
import okhttp3.*
import org.greenrobot.eventbus.EventBus
import ykk.xc.com.zgwms.R
import ykk.xc.com.zgwms.basics.*
import ykk.xc.com.zgwms.bean.EventBusEntity
import ykk.xc.com.zgwms.bean.ICStockBill
import ykk.xc.com.zgwms.bean.User
import ykk.xc.com.zgwms.bean.k3Bean.*
import ykk.xc.com.zgwms.bean.sales.DeliveryNoticeEntry
import ykk.xc.com.zgwms.bean.sales.DeliveryType
import ykk.xc.com.zgwms.bean.sales.DeliveryWay
import ykk.xc.com.zgwms.bean.sales.SalOrderEntry
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
 * 描述：销售出库
 * 作者：ykk
 */
class Sal_OutStock_Fragment1 : BaseFragment() {

    companion object {
        private val SEL_PUR_ORG = 60
        private val SEL_REC_ORG = 61
        private val SEL_PUR_DEPT = 62
        private val SEL_REC_DEPT = 63
        private val SEL_CUST = 64
        private val SEL_ORDER = 65
        private val SEL_DELIVERY = 66
        private val SEL_EMP1 = 67
        private val SEL_EMP2 = 68
        private val SEL_ADDRESS = 69
//        private val SEL_DELI_TYPE = 70
        private val SEL_DELI_WAY = 71
        private val SEL_EXPRESSNO = 72
        private val SEL_CONTACT = 73
        private val SEL_PHONE = 74
        private val SAVE = 201
        private val UNSAVE = 501
        private val FIND_SOURCE_ORDER = 202
        private val UNFIND_SOURCE_PUR = 502
        private val FIND_SOURCE_RECEIVE = 203
        private val UNFIND_SOURCE_DELIVERY = 503
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
    private var parent: Sal_OutStock_MainActivity? = null
    private val df = DecimalFormat("#.###")
    private var timesTamp:String? = null // 时间戳
    var icstockBill = ICStockBill() // 保存的对象
    //    var isReset = false // 是否点击了重置按钮.
    var salOrderEntryList:List<SalOrderEntry>? = null
    var deliveryNoticeEntryList:List<DeliveryNoticeEntry>? = null
    private var icstockBillId = 0 // 上个页面传来的id
    private var isTextChange: Boolean = false // 是否进入TextChange事件

    // 消息处理
    private val mHandler = MyHandler(this)

    private class MyHandler(activity: Sal_OutStock_Fragment1) : Handler() {
        private val mActivity: WeakReference<Sal_OutStock_Fragment1>

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
                    FIND_SOURCE_ORDER ->{ // 查询销售订单 返回
                        val list = JsonUtil.strToList(msgObj, SalOrderEntry::class.java)
                        m.getOrderData(list)
                    }
                    UNFIND_SOURCE_PUR ->{ // 查询采购订单 失败！ 返回
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "很抱歉，没有找到数据，或全部下推！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    FIND_SOURCE_RECEIVE ->{ // 查询收料通知单 返回
                        val list = JsonUtil.strToList(msgObj, DeliveryNoticeEntry::class.java)
                        m.getDeliveryNoticeData(list)
                    }
                    UNFIND_SOURCE_DELIVERY ->{ // 查询收料通知单失败！ 返回
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
//                        m.run_deliveryNoticeList()
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
        icstockBill.fcustId = m.fcustId
        icstockBill.recDeptId = m.recDeptId
        icstockBill.strSourceNo = m.strSourceNo
        icstockBill.receiveContact = m.receiveContact
        icstockBill.receivePhone = m.receivePhone
        icstockBill.receiveAddress = m.receiveAddress
        icstockBill.deliveryWayName = m.deliveryWayName
        icstockBill.expressNo = m.expressNo
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
        icstockBill.cust = m.cust
        icstockBill.recDept = m.recDept

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
        if(m.cust != null) {
            tv_custSel.text = m.cust.fname
            setEnables(tv_custSel, R.drawable.back_style_gray2a,false)
        }
        if(m.recDept != null) {
            tv_recDeptSel.text = m.recDept.fname
            setEnables(tv_recDeptSel, R.drawable.back_style_gray2a,false)
        }

        setEnables(tv_sourceTypeSel, R.drawable.back_style_gray2a, false)
        setEnables(tv_sourceOrderSel, R.drawable.back_style_gray2a, false)
        when(m.fselTranType) {
            0 -> {
                tv_sourceTypeSel.text = "无源单"
                tv_sourceTitle.text = "无源单"
            }
            1 -> {
                tv_sourceTypeSel.text = "销售订单"
                tv_sourceTitle.text = "销售订单"
            }
            2 -> {
                tv_sourceTypeSel.text = "发货通知单"
                tv_sourceTitle.text = "发货通知单"
            }
        }
        setEnables(tv_contactSel, R.drawable.back_style_gray2a, false)
        setEnables(tv_phoneSel, R.drawable.back_style_gray2a, false)
        setEnables(tv_receiveAddressSel, R.drawable.back_style_gray2a, false)
        setEnables(tv_deliveryWaySel, R.drawable.back_style_gray2a, false)
        setEnables(tv_expressNoSel, R.drawable.back_style_gray2a, false)

        tv_sourceOrderSel.text = Comm.isNULLS(m.strSourceNo)
        tv_contactSel.text = m.receiveContact
        tv_phoneSel.text = m.receivePhone
        tv_receiveAddressSel.text = m.receiveAddress
        tv_deliveryWaySel.text = m.deliveryWayName
        tv_expressNoSel.text = m.expressNo
        tv_emp1Sel.text = m.empName
        tv_emp2Sel.text = m.stockManagerName

        parent!!.isChange = false
        parent!!.isMainSave = true
        parent!!.viewPager.setScanScroll(true); // 放开左右滑动
        EventBus.getDefault().post(EventBusEntity(12)) // 发送指令到fragment3，查询分类信息
    }

    override fun setLayoutResID(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.sal_out_stock_fragment1, container, false)
    }

    override fun initView() {
        mContext = getActivity()
        parent = mContext as Sal_OutStock_MainActivity
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

        icstockBill.billType = "XSCK" // 销售出库
        icstockBill.ftranType = 30
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

    @OnClick(R.id.tv_inDateSel, R.id.tv_sourceTypeSel, R.id.tv_sourceOrderSel, R.id.tv_purOrgSel,R.id.tv_recOrgSel, R.id.tv_custSel,
             R.id.tv_recDeptSel, R.id.tv_receiveAddressSel, R.id.tv_deliveryWaySel, R.id.tv_expressNoSel,
             R.id.tv_contactSel,R.id.tv_phoneSel, R.id.tv_emp1Sel, R.id.tv_emp2Sel, R.id.btn_save, R.id.btn_clone)
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
                bundle.putInt("fcustId", icstockBill.fcustId)
                if (icstockBill.fselTranType == 1) {
                    bundle.putInt("fstockOrgId", icstockBill.fstockOrgId)
                    showForResult(Sal_Sel_Order_DialogActivity::class.java, SEL_ORDER, bundle)
                } else {
                    bundle.putInt("fdeliveryOrgId", icstockBill.fstockOrgId)
                    bundle.putInt("fsaleOrgId", icstockBill.fstockOutOrgId)
                    bundle.putString("fsrcType", "SAL_SaleOrder")
                    showForResult(Sal_Sel_Delivery_DialogActivity::class.java, SEL_DELIVERY, bundle)
                }
            }
            R.id.tv_recOrgSel -> { // 选择发货组织
                showForResult(Organization_DialogActivity::class.java, SEL_REC_ORG, null)
            }
            R.id.tv_purOrgSel -> { // 选择销售组织
                showForResult(Organization_DialogActivity::class.java, SEL_PUR_ORG, null)
            }
            R.id.tv_custSel -> { // 选择客户
                bundle = Bundle()
                bundle.putInt("fuseOrgId", icstockBill.fstockOutOrgId)
                showForResult(Cust_DialogActivity::class.java, SEL_CUST, bundle)
            }
            R.id.tv_recDeptSel -> { // 选择发货部门
                bundle = Bundle()
                bundle.putInt("fuseOrgId", icstockBill.fstockOrgId)
                showForResult(Dept_DialogActivity::class.java, SEL_REC_DEPT, bundle)
            }
            R.id.tv_receiveAddressSel -> { // 收货人地址
                showInputDialog("收货地址", icstockBill.receiveAddress, "none", SEL_ADDRESS)
            }
            R.id.tv_deliveryWaySel -> { // 交货方式
                if(icstockBill.deliveryWayNumber == null) {
                    Comm.showWarnDialog(mContext,"请选择交货类别！")
                    return
                }
                bundle = Bundle()
                bundle.putString("typeName", "交货方式")
                bundle.putString("parentNumber", icstockBill.deliveryWayNumber)
                showForResult(AssistantData_DialogActivity::class.java, SEL_DELI_WAY, bundle)
            }
            R.id.tv_expressNoSel -> { // 运输单号
                showInputDialog("运输单号", icstockBill.expressNo, "none", SEL_EXPRESSNO)
            }
            R.id.tv_contactSel -> { // 联系人
                showInputDialog("联系人", icstockBill.receiveContact, "none", SEL_CONTACT)
            }
            R.id.tv_phoneSel -> { // 电话
                showInputDialog("电话", icstockBill.receivePhone, "none", SEL_PHONE)
            }
            R.id.tv_emp1Sel -> { // 选择采购员
                bundle = Bundle()
                bundle.putInt("fuseOrgId", icstockBill.fstockOutOrgId)
                bundle.putString("foperatorType", "CGY") // 业务员类型 （ 销售员：XSY，采购员：CGY，仓管员：WHY，计划员：JHY ）
                showForResult(Operator_DialogActivity::class.java, SEL_EMP1, bundle)
            }
            R.id.tv_emp2Sel -> { // 选择仓管员
                bundle = Bundle()
                bundle.putInt("fuseOrgId", icstockBill.fstockOrgId)
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
            if(isHint) Comm.showWarnDialog(mContext, "请选择发货组织！")
            return false;
        }
        if(icstockBill.fstockOutOrgId == 0) {
            if(isHint) Comm.showWarnDialog(mContext, "请选择销售组织！")
            return false;
        }
        if(icstockBill.fselTranType == 1 && (getValues(tv_sourceOrderSel).length == 0)) {
            if(isHint) Comm.showWarnDialog(mContext, "请选择销售订单！")
            return false;
        }
        if(icstockBill.fselTranType == 2 && (getValues(tv_sourceOrderSel).length == 0)) {
            if(isHint) Comm.showWarnDialog(mContext, "请选择发货通知单！")
            return false;
        }
        if (icstockBill.fcustId == 0) {
            if(isHint) Comm.showWarnDialog(mContext, "请选择客户！")
            return false;
        }
        if (isNULLS(icstockBill.receiveAddress).length == 0) {
            if(isHint) Comm.showWarnDialog(mContext, "请输入收货地址！")
            return false;
        }
        return true;
    }

    fun reset() {
        icstockBill.fselTranType = 0 // 无源单入库
        setEnables(tv_purOrgSel, R.drawable.back_style_blue2,true)
        setEnables(tv_recOrgSel, R.drawable.back_style_blue2,true)
        setEnables(tv_custSel, R.drawable.back_style_blue2,true)
        setEnables(tv_recDeptSel, R.drawable.back_style_blue2,true)
        setEnables(tv_deliveryWaySel, R.drawable.back_style_blue2,true)
        setEnables(tv_sourceTypeSel, R.drawable.back_style_blue2,true)
        setEnables(tv_sourceOrderSel, R.drawable.back_style_gray2a, false)
        setEnables(tv_contactSel, R.drawable.back_style_gray2a, true)
        setEnables(tv_phoneSel, R.drawable.back_style_gray2a, true)
        setEnables(tv_receiveAddressSel, R.drawable.back_style_gray2a, true)
        setEnables(tv_deliveryWaySel, R.drawable.back_style_gray2a, true)
        setEnables(tv_expressNoSel, R.drawable.back_style_gray2a, true)
        isTextChange = false
        parent!!.isMainSave = false
        parent!!.viewPager.setScanScroll(false) // 禁止滑动
        tv_pdaNo.text = ""
        tv_inDateSel.text = Comm.getSysDate(7)
        tv_purOrgSel.text = ""
        tv_recOrgSel.text = ""
        tv_custSel.text = ""
        tv_sourceTypeSel.text = "无源单"
        tv_sourceTitle.text = "无源单"
        tv_sourceOrderSel.text = ""
        tv_recDeptSel.text = ""
        tv_receiveAddressSel.text = ""
        tv_deliveryWaySel.text = ""
        tv_expressNoSel.text = ""
        tv_contactSel.text = ""
        tv_phoneSel.text = ""
        icstockBill.id = 0
        icstockBill.pdaNo = ""
        icstockBill.fstockOutOrgId = 0
        icstockBill.fstockOrgId = 0
        icstockBill.fcustId = 0
        icstockBill.recDeptId = 0
        icstockBill.stockOutOrg = null
        icstockBill.stockOrg = null
        icstockBill.cust = null
        icstockBill.recDept = null
        icstockBill.receiveAddress = null
        icstockBill.receiveContact = null
        icstockBill.receivePhone = null
        icstockBill.deliveryWayNumber = null
        icstockBill.deliveryWayName = null
        initOrg()
//        icstockBill.fempId = 0
//        icstockBill.fsmanagerId = 0

        icstockBillId = 0
        salOrderEntryList = null
        deliveryNoticeEntryList = null
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
        val popV = layoutInflater.inflate(R.layout.sal_out_stock_sourcetype_popwindow, null)
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popWindow = PopupWindow(popV, v.width, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        // 设置动画效果
        // popWindow.setAnimationStyle(R.style.AnimationFade);
        popWindow!!.setBackgroundDrawable(BitmapDrawable())
        popWindow!!.isOutsideTouchable = true
        popWindow!!.isFocusable = true

        popV.findViewById<View>(R.id.btn2).visibility = View.VISIBLE
        popV.findViewById<View>(R.id.btn3).visibility = View.VISIBLE
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
                    tv_sourceTypeSel.text = "销售订单"
                    tv_sourceTitle.text = "销售订单"
                    setEnables(tv_sourceOrderSel, R.drawable.back_style_blue2, true)
                    val bundle = Bundle()
                    bundle.putInt("fstockOrgId", icstockBill.fstockOrgId)
                    bundle.putInt("fcustId", icstockBill.fcustId)
                    showForResult(Sal_Sel_Order_DialogActivity::class.java, SEL_ORDER, bundle)
                }
                R.id.btn3 -> {
                    icstockBill.fselTranType = 2
                    tv_sourceTypeSel.text = "发货通知单"
                    tv_sourceTitle.text = "发货通知单"
                    setEnables(tv_sourceOrderSel, R.drawable.back_style_blue2, true)
                    val bundle = Bundle()
                    bundle.putInt("fdeliveryOrgId", icstockBill.fstockOrgId)
                    bundle.putInt("fsaleOrgId", icstockBill.fstockOutOrgId)
                    bundle.putInt("fcustId", icstockBill.fcustId)
                    bundle.putString("fsrcType", "SAL_SaleOrder")
                    showForResult(Sal_Sel_Delivery_DialogActivity::class.java, SEL_DELIVERY, bundle)
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
    private fun getOrderData(list :List<SalOrderEntry>) {
        salOrderEntryList = list
        val salOrder = list[0].salOrder

        icstockBill.fstockOutOrgId = salOrder.fsaleOrgId
//        icstockBill.fstockOrgId = poOrder.fpurChaseOrgId
        icstockBill.stockOutOrg = salOrder.saleOrg
//        icstockBill.stockOrg = poOrder.purOrg
        icstockBill.fcustId = salOrder.saleCust.fcustId
        icstockBill.cust = salOrder.saleCust
        icstockBill.fbillTypeNumber = "XSCKD01_SYS" // 销售出库单据类型（标准采购入库）
        icstockBill.fownerType = "BD_OwnerOrg"
        icstockBill.fownerNumber = salOrder.saleOrg.fnumber
        icstockBill.fownerName = salOrder.saleOrg.fname
        icstockBill.fbusinessTypeNumber = salOrder.fbusinessType
        icstockBill.deliveryWayNumber = salOrder.deliveryWayNumber
        icstockBill.deliveryWayName = salOrder.deliveryWayName
        icstockBill.expressNo = salOrder.expressNo

        if(salOrder.saleCust != null) {
            tv_custSel.text = salOrder.saleCust.fname
            setEnables(tv_custSel, R.drawable.back_style_gray2a,false)
            tv_receiveAddressSel.text = salOrder.saleCust.faddress
            tv_contactSel.text = salOrder.saleCust.contact
            tv_phoneSel.text = salOrder.saleCust.fmobile
            icstockBill.fcustId = salOrder.saleCust.fcustId
            icstockBill.cust = salOrder.saleCust
            icstockBill.receiveAddress = salOrder.saleCust.faddress
            icstockBill.receiveContact = salOrder.saleCust.contact
            icstockBill.receivePhone = salOrder.saleCust.fmobile
        }
        tv_sourceOrderSel.text = salOrder.fbillNo
        tv_deliveryWaySel.text = salOrder.deliveryWayName
        // 禁止修改项
        setEnables(tv_sourceTypeSel, R.drawable.back_style_gray2a, false)
        setEnables(tv_sourceOrderSel, R.drawable.back_style_gray2a,false)
        setEnables(tv_purOrgSel, R.drawable.back_style_gray2a,false)
        setEnables(tv_recOrgSel, R.drawable.back_style_gray2a,false)
        setEnables(tv_deliveryWaySel, R.drawable.back_style_gray2a,false)
        // 是否可以自动保存
//                        if(checkSave(false)) run_save()
    }

    /**
     * 发货通知单数据返回
     */
    private fun getDeliveryNoticeData(list :List<DeliveryNoticeEntry>) {
        deliveryNoticeEntryList = list
        val deliveryNotice = list[0].deliveryNotice

//        icstockBill.fstockOutOrgId = purReceive.fpurOrgId
//        icstockBill.fstockOrgId = purReceive.fstockOrgId
//        icstockBill.stockOutOrg = purReceive.purOrg
//        icstockBill.stockOrg = purReceive.recOrg
        icstockBill.fcustId = deliveryNotice.fcustomerId
        icstockBill.cust = deliveryNotice.deliCust
        icstockBill.fbillTypeNumber = "XSCKD01_SYS" // 销售出库单据类型（标准采购入库）
        icstockBill.fownerType = "BD_OwnerOrg"
        icstockBill.fownerNumber = deliveryNotice.saleOrg.fnumber
        icstockBill.fownerName = deliveryNotice.saleOrg.fname
        icstockBill.fbusinessTypeNumber = deliveryNotice.fbusinessType
        icstockBill.deliveryWayNumber = deliveryNotice.deliveryWayNumber
        icstockBill.deliveryWayName = deliveryNotice.deliveryWayName
        icstockBill.expressNo = deliveryNotice.expressNo

        if(deliveryNotice.deliDept != null) {
            icstockBill.recDeptId = deliveryNotice.deliDept.fdeptId
            icstockBill.recDept = deliveryNotice.deliDept
        }
        if(deliveryNotice.saleDept != null) {
            icstockBill.purDeptId = deliveryNotice.saleDept.fdeptId
            icstockBill.purDept = deliveryNotice.saleDept
        }
        tv_receiveAddressSel.text = deliveryNotice.receiveAddress
        tv_contactSel.text = deliveryNotice.receiveContact
        tv_phoneSel.text = deliveryNotice.receiveTel
        icstockBill.receiveAddress = deliveryNotice.receiveAddress
        icstockBill.receiveContact = deliveryNotice.receiveContact
        icstockBill.receivePhone = deliveryNotice.receiveTel
        tv_sourceOrderSel.text = deliveryNotice.fbillNo
        tv_deliveryWaySel.text = deliveryNotice.deliveryWayName

        // 禁止修改项
        setEnables(tv_sourceTypeSel, R.drawable.back_style_gray2a, false)
        setEnables(tv_sourceOrderSel, R.drawable.back_style_gray2a,false)
        setEnables(tv_purOrgSel, R.drawable.back_style_gray2a,false)
        setEnables(tv_recOrgSel, R.drawable.back_style_gray2a,false)
        setEnables(tv_deliveryWaySel, R.drawable.back_style_gray2a,false)

        // 是否可以自动保存
//                        if(checkSave(false)) run_save()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                SEL_REC_ORG -> { // 发货组织
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
                SEL_PUR_ORG -> { // 销售组织
                    val org = data!!.getSerializableExtra("obj") as Organization_K3
                    tv_purOrgSel.text = org!!.fname
                    tv_custSel.text = ""
                    icstockBill.fstockOutOrgId = org.forgId
                    icstockBill.stockOutOrg = org
                    icstockBill.fcustId = 0
                    icstockBill.cust = null
                    tv_receiveAddressSel.text = ""
                    tv_contactSel.text = ""
                    tv_phoneSel.text = ""
                    icstockBill.receiveAddress = ""
                    icstockBill.receiveContact = ""
                    icstockBill.receivePhone = ""
                }
                SEL_CUST -> { // 查询客户	返回
                    val cust = data!!.getSerializableExtra("obj") as Customer_K3
                    tv_custSel.text = cust.fname
                    tv_receiveAddressSel.text = cust.faddress
                    tv_contactSel.text = cust.contact
                    tv_phoneSel.text = cust.fmobile
                    icstockBill.fcustId = cust.fcustId
                    icstockBill.cust = cust
                    icstockBill.receiveAddress = cust.faddress
                    icstockBill.receiveContact = cust.contact
                    icstockBill.receivePhone = cust.fmobile
                }
                SEL_ORDER -> { // 查询销售订单	返回
//                    val poOrder = data!!.getSerializableExtra("obj") as POOrder
                    val strFid = data!!.getStringExtra("obj")
                    run_orderList(strFid)
                }
                SEL_DELIVERY -> { // 查询收料通知单	返回
//                    val PurReceive = data!!.getSerializableExtra("obj") as PurReceive
                    val strFid = data!!.getStringExtra("obj")
                    run_deliveryNoticeList(strFid)
                }
                SEL_REC_DEPT -> {// 查询部门	返回
                    val dept = data!!.getSerializableExtra("obj") as Department_K3
                    tv_recDeptSel.text = dept!!.fname
                    icstockBill.recDeptId = dept.fdeptId
                    icstockBill.recDept = dept
                }
                SEL_ADDRESS -> { // 收货地址    返回
                    val bundle = data!!.extras
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        tv_receiveAddressSel.text = value
                        icstockBill.receiveAddress = value
                    }
                }
                SEL_DELI_WAY -> { // 交货方式    返回
                    val m = data!!.getSerializableExtra("obj") as AssistantData
                    tv_deliveryWaySel.text = m.fname
                    icstockBill.deliveryWayNumber = m.fnumber
                    icstockBill.deliveryWayName = m.fname
                }
                SEL_EXPRESSNO -> { // 运输单号    返回
                    val bundle = data!!.extras
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        tv_expressNoSel.text = value
                        icstockBill.expressNo = value
                    }
                }
                SEL_CONTACT -> { // 联系人    返回
                    val bundle = data!!.extras
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        tv_contactSel.text = value
                        icstockBill.receiveContact = value
                    }
                }
                SEL_PHONE -> { // 电话    返回
                    val bundle = data!!.extras
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        tv_phoneSel.text = value
                        icstockBill.receivePhone = value
                    }
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
            icstockBill.fbillTypeNumber = "XSCKD01_SYS" // 销售出库单据类型（标准采购入库）
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
     * 查询销售订单
     */
    private fun run_orderList(fid :String) {
        isTextChange = false
        showLoadDialog("保存中...", false)
        val mUrl = getURL("salOrder/findEntryByParam")

        val formBody = FormBody.Builder()
                .add("strFid", fid)
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
                val msg = mHandler.obtainMessage(FIND_SOURCE_ORDER, result)
                LogUtil.e("run_poOrderList --> onResponse", result)
                mHandler.sendMessage(msg)
            }
        })
    }

    /**
     * 查询发货通知单
     */
    private fun run_deliveryNoticeList(fid :String) {
        isTextChange = false
        showLoadDialog("保存中...", false)
        val mUrl = getURL("deliveryNotice/findEntryByParam")

        val formBody = FormBody.Builder()
                .add("strFid", fid)
                .build()

        val request = Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNFIND_SOURCE_DELIVERY)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                if (!JsonUtil.isSuccess(result)) {
                    val msg = mHandler.obtainMessage(UNFIND_SOURCE_DELIVERY, result)
                    mHandler.sendMessage(msg)
                    return
                }
                val msg = mHandler.obtainMessage(FIND_SOURCE_RECEIVE, result)
                LogUtil.e("run_deliveryNoticeList --> onResponse", result)
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
                .add("searchSalOrg", "1") // 查询销售组织
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
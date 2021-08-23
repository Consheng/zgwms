package ykk.xc.com.zgwms.warehouse

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
import kotlinx.android.synthetic.main.pur_in_stock_main.*
import kotlinx.android.synthetic.main.ware_transfer_fragment1.*
import okhttp3.*
import org.greenrobot.eventbus.EventBus
import ykk.xc.com.zgwms.R
import ykk.xc.com.zgwms.basics.Cust_DialogActivity
import ykk.xc.com.zgwms.basics.Operator_DialogActivity
import ykk.xc.com.zgwms.basics.Organization_DialogActivity
import ykk.xc.com.zgwms.basics.Supplier_DialogActivity
import ykk.xc.com.zgwms.bean.EventBusEntity
import ykk.xc.com.zgwms.bean.ICStockBill
import ykk.xc.com.zgwms.bean.User
import ykk.xc.com.zgwms.bean.k3Bean.*
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
 * 描述：直接调拨单
 * 作者：ykk
 */
class Ware_Transfer_Fragment1 : BaseFragment() {

    companion object {
        private val SEL_OUT_ORG = 61
        private val SEL_IN_ORG = 60
        private val SEL_OWNER_ORG = 62
        private val SEL_OWNER_SUPP = 63
        private val SEL_OWNER_CUST = 64
        private val SEL_OWNER_ORG2 = 65
        private val SEL_OWNER_SUPP2 = 66
        private val SEL_OWNER_CUST2 = 67
        private val SEL_EMP1 = 71
        private val SEL_EMP2 = 72
        private val SAVE = 201
        private val UNSAVE = 501
        private val FIND_ICSTOCKBILL = 204
        private val UNFIND_ICSTOCKBILL = 504

        private val SETFOCUS = 1
        private val SAOMA = 2
        private val WRITE_CODE = 3
    }

    private val context = this
    private var okHttpClient: OkHttpClient? = null
    private var user: User? = null
    private var mContext: Activity? = null
    private var parent: Ware_Transfer_MainActivity? = null
    private val df = DecimalFormat("#.###")
    private var timesTamp:String? = null // 时间戳
    var icstockBill = ICStockBill() // 保存的对象
//    var isReset = false // 是否点击了重置按钮.
    private var icstockBillId = 0 // 上个页面传来的id
    private var isTextChange: Boolean = false // 是否进入TextChange事件

    // 消息处理
    private val mHandler = MyHandler(this)

    private class MyHandler(activity: Ware_Transfer_Fragment1) : Handler() {
        private val mActivity: WeakReference<Ware_Transfer_Fragment1>

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
                        if(m.rb_transferType1.isChecked) {
                            m.rb_transferType2.isEnabled = false
                        }
                        if(m.rb_transferType2.isChecked) {
                            m.rb_transferType1.isEnabled = false
                        }
                        // 滑动第二个页面
                        m.parent!!.viewPager.setCurrentItem(1, false)
                        m.parent!!.isChange = if(m.icstockBillId == 0) true else false
                    }
                    UNSAVE -> { // 保存失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "保存失败！"
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
        icstockBill.ftransferType = m.ftransferType
        icstockBill.fdate = m.fdate
        icstockBill.fstockOrgId = m.fstockOrgId
        icstockBill.fstockOutOrgId = m.fstockOutOrgId
        icstockBill.fsupplierId = m.fsupplierId
        icstockBill.fcustId = m.fcustId
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
        icstockBill.fownerType = m.fownerType
        icstockBill.fownerNumber = m.fownerNumber
        icstockBill.fownerName = m.fownerName
        icstockBill.fownerOutType = m.fownerOutType
        icstockBill.fownerOutNumber = m.fownerOutNumber
        icstockBill.fownerOutName = m.fownerOutName

        icstockBill.stockOrg = m.stockOrg
        icstockBill.stockOutOrg = m.stockOutOrg
        icstockBill.supplier = m.supplier
        icstockBill.cust = m.cust
        icstockBill.recDept = m.recDept
        icstockBill.purDept = m.purDept

        tv_pdaNo.text = m.pdaNo
        tv_inDateSel.text = m.fdate
        // 调拨类型：（InnerOrgTransfer:组织内调拨，OverOrgTransfer:跨组织调拨）
        if(m.ftransferType.equals("InnerOrgTransfer")) {
            rb_transferType1.isChecked = true
            rb_transferType2.isChecked = false
            rb_transferType2.isEnabled = false
        } else if(m.ftransferType.equals("OverOrgTransfer")) {
            rb_transferType2.isChecked = true
            rb_transferType1.isChecked = false
            rb_transferType1.isEnabled = false
        }
        if(m.stockOrg != null) {
            tv_stockOrgSel.text = m.stockOrg.fname
            setEnables(tv_stockOrgSel, R.drawable.back_style_gray2a, false)
        }
        if(m.stockOutOrg != null) {
            tv_stockOutOrgSel.text = m.stockOutOrg.fname
            setEnables(tv_stockOutOrgSel, R.drawable.back_style_gray2a, false)
        }

        setEnables(tv_fownerTypeSel, R.drawable.back_style_gray2a, false)
        setEnables(tv_fownerSel, R.drawable.back_style_gray2a, false)
        when(m.fownerType) {
            "BD_OwnerOrg" -> tv_fownerTypeSel.text = "业务组织"
            "BD_Supplier" -> tv_fownerTypeSel.text = "供应商"
            "BD_Customer" -> tv_fownerTypeSel.text = "客户"
        }
        setEnables(tv_fownerOutTypeSel, R.drawable.back_style_gray2a, false)
        setEnables(tv_fownerOutSel, R.drawable.back_style_gray2a, false)
        when(m.fownerOutType) {
            "BD_OwnerOrg" -> tv_fownerOutTypeSel.text = "业务组织"
            "BD_Supplier" -> tv_fownerOutTypeSel.text = "供应商"
            "BD_Customer" -> tv_fownerOutTypeSel.text = "客户"
        }
        tv_fownerOutSel.text = m.fownerOutName
        tv_fownerSel.text = m.fownerName
        tv_emp1Sel.text = m.empName
        tv_emp2Sel.text = m.stockManagerName

        parent!!.isChange = false
        parent!!.isMainSave = true
        parent!!.viewPager.setScanScroll(true); // 放开左右滑动
        EventBus.getDefault().post(EventBusEntity(12)) // 发送指令到fragment3，查询分类信息
    }

    override fun setLayoutResID(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.ware_transfer_fragment1, container, false)
    }

    override fun initView() {
        mContext = getActivity()
        parent = mContext as Ware_Transfer_MainActivity
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

        icstockBill.billType = "ZJDBD" // 直接调拨单
        icstockBill.ftranType = 40
        icstockBill.fselTranType = 0
        icstockBill.fstockDirect = "GENERAL" // 库存方向：（GENERAL:普通，RETURN:退货）
        icstockBill.ftransferType = "InnerOrgTransfer" // 调拨类型：（InnerOrgTransfer:组织内调拨，OverOrgTransfer:跨组织调拨）
//        icstockBill.empNumber = user!!.staff.fstaffNumber
//        icstockBill.empName = user!!.staff.fname
        icstockBill.stockManagerNumber = user!!.kdUserNumber
        icstockBill.stockManagerName = user!!.kdUserName
        icstockBill.createUserId = user!!.id
        icstockBill.createUserName = user!!.username
        icstockBill.fbillTypeNumber = "ZJDB01_SYS"

        tv_inDateSel.text = Comm.getSysDate(7)
        tv_operationManName.text = user!!.kdAccountName
        tv_emp1Sel.text = user!!.kdUserName
        tv_emp2Sel.text = user!!.kdUserName
        initOrg(null)

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

    private fun initOrg(org :Organization_K3?) {
        if(org != null) {
            icstockBill.fstockOrgId = org.forgId
            icstockBill.fstockOutOrgId = org.forgId
            icstockBill.stockOrg = org
            icstockBill.stockOutOrg = org
            icstockBill.fownerType = "BD_OwnerOrg"
            icstockBill.fownerNumber = org.fnumber
            icstockBill.fownerName = org.fname
            icstockBill.fownerOutType = "BD_OwnerOrg"
            icstockBill.fownerOutNumber = org.fnumber
            icstockBill.fownerOutName = org.fname
            tv_stockOrgSel.text = org.fname
            tv_stockOutOrgSel.text = org.fname
            tv_fownerSel.text = org.fname
            tv_fownerOutSel.text = org.fname

        } else if(user!!.organization != null) {
            icstockBill.fstockOrgId = user!!.organizationId
            icstockBill.fstockOutOrgId = user!!.organizationId
            icstockBill.stockOrg = user!!.organization
            icstockBill.stockOutOrg = user!!.organization
            icstockBill.fownerType = "BD_OwnerOrg"
            icstockBill.fownerNumber = user!!.organization.fnumber
            icstockBill.fownerName = user!!.organization.fname
            icstockBill.fownerOutType = "BD_OwnerOrg"
            icstockBill.fownerOutNumber = user!!.organization.fnumber
            icstockBill.fownerOutName = user!!.organization.fname
            tv_stockOrgSel.text = user!!.organization.fname
            tv_stockOutOrgSel.text = user!!.organization.fname
            tv_fownerSel.text = user!!.organization.fname
            tv_fownerOutSel.text = user!!.organization.fname

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

    @OnClick(R.id.tv_inDateSel, R.id.tv_fownerOutTypeSel, R.id.tv_fownerSel, R.id.tv_fownerOutSel, R.id.tv_stockOrgSel, R.id.tv_stockOutOrgSel,
             R.id.tv_emp1Sel, R.id.tv_emp2Sel, R.id.btn_save, R.id.btn_clone)
    fun onViewClicked(view: View) {
        var bundle: Bundle? = null
        when (view.id) {
            R.id.tv_inDateSel -> { // 选择日期
                Comm.showDateDialog(mContext, tv_inDateSel, 0)
            }
            R.id.tv_fownerOutTypeSel -> { // 调出货主类型选择
                pop_fownerType(view)
                popWindow!!.showAsDropDown(view)
            }
            R.id.tv_fownerOutSel -> { // 调出货主选择
                bundle = Bundle()
                bundle.putInt("fuseOrgId" , icstockBill.fstockOutOrgId)
                when(icstockBill.fownerOutType) {
                    "BD_OwnerOrg" -> showForResult(Organization_DialogActivity::class.java, SEL_OWNER_ORG2, null)
                    "BD_Supplier" -> showForResult(Supplier_DialogActivity::class.java, SEL_OWNER_SUPP2, bundle)
                    "BD_Customer" -> showForResult(Cust_DialogActivity::class.java, SEL_OWNER_CUST2, bundle)
                }
            }
            R.id.tv_fownerSel -> { // 调入货主选择
                bundle = Bundle()
                bundle.putInt("fuseOrgId" , icstockBill.fstockOrgId)
                when(icstockBill.fownerType) {
                    "BD_OwnerOrg" -> showForResult(Organization_DialogActivity::class.java, SEL_OWNER_ORG, null)
                    "BD_Supplier" -> showForResult(Supplier_DialogActivity::class.java, SEL_OWNER_SUPP, bundle)
                    "BD_Customer" -> showForResult(Cust_DialogActivity::class.java, SEL_OWNER_CUST, bundle)
                }
            }
            R.id.tv_stockOutOrgSel -> { // 选择调出组织
                showForResult(Organization_DialogActivity::class.java, SEL_OUT_ORG, null)
            }
            R.id.tv_stockOrgSel -> { // 选择调入组织
                showForResult(Organization_DialogActivity::class.java, SEL_IN_ORG, null)
            }
            /*R.id.tv_emp1Sel -> { // 选择领料人
                bundle = Bundle()
//                bundle.putInt("fuseOrgId", icstockBill.fstockOutOrgId)
                showForResult(Staff_DialogActivity::class.java, SEL_EMP1, bundle)
            }*/
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

    override fun setListener() {
        // 组织内调拨
        rb_transferType1.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                val org = icstockBill.stockOutOrg
                reset()
                initOrg(org)
            }
        }
        // 跨组织调拨
        rb_transferType2.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                // 调拨类型：（InnerOrgTransfer:组织内调拨，OverOrgTransfer:跨组织调拨）
                icstockBill.ftransferType = "OverOrgTransfer"
                setEnables(tv_stockOrgSel, R.drawable.back_style_blue2, true)
                setEnables(tv_fownerSel, R.drawable.back_style_blue2, true)
            }
        }
    }

    /**
     * 保存检查数据判断
     */
    fun checkSave(isHint :Boolean) : Boolean {
        if(icstockBill.fstockOutOrgId == 0) {
            if(isHint) Comm.showWarnDialog(mContext, "请选择调出库存组织！")
            return false;
        }
        if(icstockBill.fstockOrgId == 0) {
            if(isHint) Comm.showWarnDialog(mContext, "请选择调入库存组织！")
            return false;
        }
        if(Comm.isNULLS(icstockBill.fownerOutName).length == 0) {
            if(isHint) Comm.showWarnDialog(mContext, "请选择调出货主！")
            return false;
        }
        if(Comm.isNULLS(icstockBill.fownerName).length == 0) {
            if(isHint) Comm.showWarnDialog(mContext, "请选择调入货主！")
            return false;
        }
        // 调拨类型：（InnerOrgTransfer:组织内调拨，OverOrgTransfer:跨组织调拨）
        if(icstockBill.ftransferType.equals("OverOrgTransfer") && (icstockBill.fstockOutOrgId.toString()+"_"+icstockBill.fownerOutNumber).equals(icstockBill.fstockOrgId.toString()+"_"+icstockBill.fownerNumber) ) {
            if(isHint) Comm.showWarnDialog(mContext, "（调出库存组织+调出货主）不能等于（调入库存组织+调入货主）！")
            return false;
        }
        return true;
    }

    fun reset() {
        icstockBill.fstockDirect = "GENERAL" // 库存方向：（GENERAL:普通，RETURN:退货）
        icstockBill.ftransferType = "InnerOrgTransfer" // 调拨类型：（InnerOrgTransfer:组织内调拨，OverOrgTransfer:跨组织调拨）
        rb_transferType1.isChecked = true
        rb_transferType2.isChecked = false
        rb_transferType1.isEnabled = true
        rb_transferType2.isEnabled = true
        setEnables(tv_stockOutOrgSel, R.drawable.back_style_blue2,true)
        setEnables(tv_fownerOutTypeSel, R.drawable.back_style_blue2,true)
        setEnables(tv_fownerOutSel, R.drawable.back_style_blue2, true)
        setEnables(tv_stockOrgSel, R.drawable.back_style_gray2a,false)
        setEnables(tv_fownerTypeSel, R.drawable.back_style_gray2a,false)
        setEnables(tv_fownerSel, R.drawable.back_style_gray2a, false)
        isTextChange = false
        parent!!.isMainSave = false
        parent!!.viewPager.setScanScroll(false) // 禁止滑动
        tv_pdaNo.text = ""
        tv_inDateSel.text = Comm.getSysDate(7)
        tv_stockOutOrgSel.text = ""
        tv_stockOrgSel.text = ""
        tv_fownerOutTypeSel.text = "业务组织"
        tv_fownerTypeSel.text = "业务组织"
        tv_fownerOutSel.text = ""
        tv_fownerSel.text = ""
        icstockBill.id = 0
        icstockBill.pdaNo = ""
        icstockBill.fstockOutOrgId = 0
        icstockBill.fstockOrgId = 0
        icstockBill.fsupplierId = 0
        icstockBill.fcustId = 0
        icstockBill.purDeptId = 0
        icstockBill.recDeptId = 0
        icstockBill.fownerType = ""
        icstockBill.fownerNumber = ""
        icstockBill.fownerName = ""
        icstockBill.fownerOutType = ""
        icstockBill.fownerOutNumber = ""
        icstockBill.fownerOutName = ""
        icstockBill.stockOutOrg = null
        icstockBill.stockOrg = null
        icstockBill.supplier = null
        icstockBill.cust = null
        icstockBill.purDept = null
        icstockBill.recDept = null
        initOrg(null)
//        icstockBill.fempId = 0
//        icstockBill.fsmanagerId = 0

        icstockBillId = 0
        timesTamp = user!!.getId().toString() + "-" + Comm.randomUUID()
        parent!!.isChange = false
        mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
        EventBus.getDefault().post(EventBusEntity(11)) // 发送指令到fragment2，告其清空
    }

    /**
     * 创建PopupWindow 【单据类型选择 】
     */
    private var popWindow: PopupWindow? = null
    private fun pop_fownerType(v: View) {
        if (null != popWindow) {//不为空就隐藏
            popWindow!!.dismiss()
            return
        }
        // 获取自定义布局文件popupwindow_left.xml的视图
        val popV = layoutInflater.inflate(R.layout.ware_owner_type_popwindow, null)
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popWindow = PopupWindow(popV, v.width, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        // 设置动画效果
        // popWindow.setAnimationStyle(R.style.AnimationFade);
        popWindow!!.setBackgroundDrawable(BitmapDrawable())
        popWindow!!.isOutsideTouchable = true
        popWindow!!.isFocusable = true

        // 点击其他地方消失
        val click = View.OnClickListener { v ->
            var bool = true
            when (v.id) {
                R.id.btn1 -> {
//                    bool = icstockBill.fownerOutType.equals("BD_OwnerOrg")
                    tv_fownerOutTypeSel.text = "业务组织"
                    tv_fownerTypeSel.text = "业务组织"
                    icstockBill.fownerOutType = "BD_OwnerOrg"
                    icstockBill.fownerType = "BD_OwnerOrg"
                    icstockBill.fownerOutNumber = icstockBill.stockOutOrg.fnumber
                    icstockBill.fownerOutName = icstockBill.stockOutOrg.fname
                    icstockBill.fownerNumber = icstockBill.stockOrg.fnumber
                    icstockBill.fownerName = icstockBill.stockOrg.fname
                    tv_fownerOutSel.text = icstockBill.fownerOutName
                    tv_fownerSel.text = icstockBill.fownerName
//                    showForResult(Organization_DialogActivity::class.java, SEL_OWNER_ORG2, null)
                }
                R.id.btn2 -> {
                    bool = icstockBill.fownerOutType.equals("BD_Supplier")
                    tv_fownerOutTypeSel.text = "供应商"
                    tv_fownerTypeSel.text = "供应商"
                    icstockBill.fownerOutType = "BD_Supplier"
                    icstockBill.fownerType = "BD_Supplier"
                    val bundle = Bundle()
                    bundle.putInt("fuseOrgId" , icstockBill.fstockOutOrgId)
                    showForResult(Supplier_DialogActivity::class.java, SEL_OWNER_SUPP2, bundle)
                }
                R.id.btn3 -> {
                    bool = icstockBill.fownerOutType.equals("BD_Customer")
                    tv_fownerOutTypeSel.text = "客户"
                    tv_fownerTypeSel.text = "客户"
                    icstockBill.fownerOutType = "BD_Customer"
                    icstockBill.fownerType = "BD_Customer"
                    val bundle = Bundle()
                    bundle.putInt("fuseOrgId" ,icstockBill.fstockOutOrgId)
                    showForResult(Cust_DialogActivity::class.java, SEL_OWNER_CUST2, bundle)
                }
            }
            if(!bool) {
                icstockBill.fownerOutNumber = ""
                icstockBill.fownerOutName = ""
                icstockBill.fownerNumber = ""
                icstockBill.fownerName = ""
                tv_fownerOutSel.text = ""
                tv_fownerSel.text = ""
            }
            popWindow!!.dismiss()
        }
        popV.findViewById<View>(R.id.btn1).setOnClickListener(click)
        popV.findViewById<View>(R.id.btn2).setOnClickListener(click)
        popV.findViewById<View>(R.id.btn3).setOnClickListener(click)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                SEL_OUT_ORG -> { // 调出库存组织
                    val org = data!!.getSerializableExtra("obj") as Organization_K3
                    // 每次选择了新的组织，就清空
                    if(icstockBill.fstockOutOrgId != org.forgId) {
                        reset()
                    }
                    initOrg(org)
                }
                SEL_IN_ORG -> { // 调入组织
                    val org = data!!.getSerializableExtra("obj") as Organization_K3
                    // 每次选择了新的组织，就清空
                    if(icstockBill.fstockOrgId != org.forgId) {
                        icstockBill.fownerNumber = ""
                        icstockBill.fownerName = ""
                        tv_fownerSel.text = ""
                    }
                    icstockBill.fstockOrgId = org.forgId
                    icstockBill.stockOrg = org
                    tv_stockOrgSel.text = org!!.fname
                    // 如果调入货主类型为业务组织，调入货主就为调入组织
                    if(icstockBill.fownerType.equals("BD_OwnerOrg")) {
                        icstockBill.fownerNumber = org.fnumber
                        icstockBill.fownerName = org.fname
                        tv_fownerSel.text = org.fname
                    }
                }
                SEL_OWNER_ORG -> { // 调入货主类型--业务组织
                    val org = data!!.getSerializableExtra("obj") as Organization_K3
                    icstockBill.fownerNumber = org.fnumber
                    icstockBill.fownerName = org.fname
                    tv_fownerSel.text = org!!.fname
                }
                SEL_OWNER_SUPP -> { // 调入货主类型--供应商
                    val org = data!!.getSerializableExtra("obj") as Supplier_K3
                    icstockBill.fownerNumber = org.fnumber
                    icstockBill.fownerName = org.fname
                    tv_fownerSel.text = org!!.fname
                }
                SEL_OWNER_CUST -> { // 调入货主类型--客户
                    val org = data!!.getSerializableExtra("obj") as Customer_K3
                    icstockBill.fownerNumber = org.fnumber
                    icstockBill.fownerName = org.fname
                    tv_fownerSel.text = org!!.fname
                }
                SEL_OWNER_ORG2 -> { // 调出货主类型--业务组织
                    val org = data!!.getSerializableExtra("obj") as Organization_K3
                    icstockBill.fownerOutNumber = org.fnumber
                    icstockBill.fownerOutName = org.fname
                    tv_fownerOutSel.text = org!!.fname
//                    if(rb_transferType1.isChecked) { // 组织内调拨
                        icstockBill.fownerNumber = org.fnumber
                        icstockBill.fownerName = org.fname
                        tv_fownerSel.text = org!!.fname
//                    }
                }
                SEL_OWNER_SUPP2 -> { // 调出货主类型--供应商
                    val org = data!!.getSerializableExtra("obj") as Supplier_K3
                    icstockBill.fownerOutNumber = org.fnumber
                    icstockBill.fownerOutName = org.fname
                    tv_fownerOutSel.text = org!!.fname
//                    if(rb_transferType1.isChecked) { // 组织内调拨
                        icstockBill.fownerNumber = org.fnumber
                        icstockBill.fownerName = org.fname
                        tv_fownerSel.text = org!!.fname
//                    }
                }
                SEL_OWNER_CUST2 -> { // 调出货主类型--客户
                    val org = data!!.getSerializableExtra("obj") as Customer_K3
                    icstockBill.fownerOutNumber = org.fnumber
                    icstockBill.fownerOutName = org.fname
                    tv_fownerOutSel.text = org!!.fname
//                    if(rb_transferType1.isChecked) { // 组织内调拨
                        icstockBill.fownerNumber = org.fnumber
                        icstockBill.fownerName = org.fname
                        tv_fownerSel.text = org!!.fname
//                    }
                }
                SEL_EMP1 -> {//查询领料人	返回
                    val emp = data!!.getSerializableExtra("obj") as Staff_K3
                    tv_emp1Sel.text = emp!!.fname
                    icstockBill.empNumber = emp.fstaffNumber
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
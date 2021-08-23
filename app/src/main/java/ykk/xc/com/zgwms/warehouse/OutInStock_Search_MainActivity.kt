package ykk.xc.com.zgwms.warehouse

import android.graphics.drawable.BitmapDrawable
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import butterknife.OnClick
import kotlinx.android.synthetic.main.ware_outin_stock_search_main.*
import ykk.xc.com.zgwms.R
import ykk.xc.com.zgwms.comm.BaseActivity
import ykk.xc.com.zgwms.util.adapter.BaseFragmentAdapter
import java.text.DecimalFormat
import java.util.*

/**
 * 日期：2019-10-16 09:14
 * 描述：其它出入库查询
 * 作者：ykk
 */
class OutInStock_Search_MainActivity : BaseActivity() {

    val context = this
    private val TAG = "Other_OutInStockSearchActivity"
    private var curRadio: View? = null
    private var curRadioName: TextView? = null
    private val df = DecimalFormat("#.####")
    val listFragment = ArrayList<Fragment>()
    private var pageId = 0 // 上个页面传来的id
    private var billType = "QTRK" // 上个页面传来的
    var fragment1:OutInStock_Search_Fragment1_PurInStock? = null
    var fragment2:OutInStock_Search_Fragment2_QTRK? = null
    var fragment3:OutInStock_Search_Fragment3_QTCK? = null
    var fragment4:OutInStock_Search_Fragment4_ZJDBD? = null
    var fragment5:OutInStock_Search_Fragment5_FBSDCD? = null
    var fragment6:OutInStock_Search_Fragment6_FBSDRD? = null
    var fragment7:OutInStock_Search_Fragment7_CGTL? = null
    var fragment8:OutInStock_Search_Fragment8_SCRK? = null
    var fragment9:OutInStock_Search_Fragment9_ZYDB? = null
    var fragment10:OutInStock_Search_Fragment10_SalOutStock? = null

    override fun setLayoutResID(): Int {
        return R.layout.ware_outin_stock_search_main
    }

    override fun initData() {
        bundle()
//        val listFragment = ArrayList<Fragment>()
//        Bundle bundle2 = new Bundle()
//        bundle2.putSerializable("customer", customer)
//        fragment1.setArguments(bundle2) // 传参数
//        fragment2.setArguments(bundle2) // 传参数

        fragment1 = OutInStock_Search_Fragment1_PurInStock()
        fragment2 = OutInStock_Search_Fragment2_QTRK()
        fragment3 = OutInStock_Search_Fragment3_QTCK()
        fragment4 = OutInStock_Search_Fragment4_ZJDBD()
        fragment5 = OutInStock_Search_Fragment5_FBSDCD()
        fragment6 = OutInStock_Search_Fragment6_FBSDRD()
        fragment7 = OutInStock_Search_Fragment7_CGTL()
        fragment8 = OutInStock_Search_Fragment8_SCRK()
        fragment9 = OutInStock_Search_Fragment9_ZYDB()
        fragment10 = OutInStock_Search_Fragment10_SalOutStock()

        listFragment.add(fragment1!!)
        listFragment.add(fragment2!!)
        listFragment.add(fragment3!!)
        listFragment.add(fragment4!!)
        listFragment.add(fragment5!!)
        listFragment.add(fragment6!!)
        listFragment.add(fragment7!!)
        listFragment.add(fragment8!!)
        listFragment.add(fragment9!!)
        listFragment.add(fragment10!!)

        viewPager.setScanScroll(false) // 禁止左右滑动
        //ViewPager设置适配器
        viewPager.setAdapter(BaseFragmentAdapter(supportFragmentManager, listFragment))
        //ViewPager显示第一个Fragment
        viewPager.setCurrentItem(pageId)
        viewPager.offscreenPageLimit = 10

        //ViewPager页面切换监听
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
//                    0 -> tabChange(viewRadio1!!, tv_radioName1, "表头", 0)
//                    1 -> tabChange(viewRadio2!!, tv_radioName2, "添加分录", 1)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

    }

    private fun bundle() {
        val bundle = context.intent.extras
        if (bundle != null) {
            pageId = bundle.getInt("pageId")
            when(pageId) {
                0 -> tv_billType.text = "采购入库单列表"
                1 -> tv_billType.text = "其它入库单列表"
                2 -> tv_billType.text = "其它出库单列表"
                3 -> tv_billType.text = "直接调拨单列表"
                4 -> tv_billType.text = "分步式调出单列表"
                5 -> tv_billType.text = "分步式调入单列表"
                6 -> tv_billType.text = "采购退料单列表"
                7 -> tv_billType.text = "生产入库单列表"
                8 -> tv_billType.text = "自由调拨单列表"
                9 -> tv_billType.text = "销售出库单列表"
            }
            billType = bundle.getString("billType")
        }
    }

    @OnClick(R.id.btn_close, R.id.tv_billType, R.id.btn_search, R.id.btn_batchUpload)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_close -> {// 关闭
                context.finish()
            }
            R.id.tv_billType -> { // 单据类型选择
                pop_billType(view)
                popWindow!!.showAsDropDown(view)
            }
            R.id.btn_search -> { // 查询
                when(pageId) {
                    0 -> fragment1!!.findFun()
                    1 -> fragment2!!.findFun()
                    2 -> fragment3!!.findFun()
                    3 -> fragment4!!.findFun()
                    4 -> fragment5!!.findFun()
                    5 -> fragment6!!.findFun()
                    6 -> fragment7!!.findFun()
                    7 -> fragment8!!.findFun()
                    8 -> fragment9!!.findFun()
                    9 -> fragment10!!.findFun()
                }
            }
            R.id.btn_batchUpload -> { // 批量上传
                when(pageId) {
                    0 -> fragment1!!.batchUpload()
                    1 -> fragment2!!.batchUpload()
                    2 -> fragment3!!.batchUpload()
                    3 -> fragment4!!.batchUpload()
                    4 -> fragment5!!.batchUpload()
                    5 -> fragment6!!.batchUpload()
                    6 -> fragment7!!.batchUpload()
                    7 -> fragment8!!.batchUpload()
                    8 -> fragment9!!.batchUpload()
                    9 -> fragment10!!.batchUpload()
                }
            }
        }
    }

    /**
     * 创建PopupWindow 【单据类型选择 】
     */
    private var popWindow: PopupWindow? = null
    private fun pop_billType(v: View) {
        if (null != popWindow) {//不为空就隐藏
            popWindow!!.dismiss()
            return
        }
        // 获取自定义布局文件popupwindow_left.xml的视图
        val popV = layoutInflater.inflate(R.layout.ware_outin_stock_search_popwindow, null)
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popWindow = PopupWindow(popV, v.width, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        // 设置动画效果
        // popWindow.setAnimationStyle(R.style.AnimationFade)
        popWindow!!.setBackgroundDrawable(BitmapDrawable())
        popWindow!!.isOutsideTouchable = true
        popWindow!!.isFocusable = true

        popV.findViewById<View>(R.id.tv1).visibility = View.GONE
        popV.findViewById<View>(R.id.tv2).visibility = View.GONE
        popV.findViewById<View>(R.id.tv3).visibility = View.GONE
        popV.findViewById<View>(R.id.tv4).visibility = View.GONE
        popV.findViewById<View>(R.id.tv5).visibility = View.GONE
        popV.findViewById<View>(R.id.tv6).visibility = View.GONE
        popV.findViewById<View>(R.id.tv7).visibility = View.GONE
        popV.findViewById<View>(R.id.tv8).visibility = View.GONE
        popV.findViewById<View>(R.id.tv9).visibility = View.GONE
        popV.findViewById<View>(R.id.tv10).visibility = View.GONE

        if(billType.equals("CGRK")) { // 采购
            popV.findViewById<View>(R.id.tv1).visibility = View.VISIBLE
            popV.findViewById<View>(R.id.tv7).visibility = View.VISIBLE

        } else if(billType.equals("SCRK")) { // 生产
            popV.findViewById<View>(R.id.tv8).visibility = View.VISIBLE

        } else if(billType.equals("XSCK")) { // 销售
            popV.findViewById<View>(R.id.tv10).visibility = View.VISIBLE

        } else if(billType.equals("QTRK")) { // 仓库
            popV.findViewById<View>(R.id.tv2).visibility = View.VISIBLE
            popV.findViewById<View>(R.id.tv3).visibility = View.VISIBLE
            popV.findViewById<View>(R.id.tv4).visibility = View.VISIBLE
            popV.findViewById<View>(R.id.tv5).visibility = View.VISIBLE
            popV.findViewById<View>(R.id.tv6).visibility = View.VISIBLE
            popV.findViewById<View>(R.id.tv9).visibility = View.VISIBLE
        }

        // 点击其他地方消失
        val click = View.OnClickListener { v ->
            when (v.id) {
                R.id.tv1 -> {
                    tv_billType.text = "采购入库单列表"
                    pageId = 0
                }
                R.id.tv2 -> {
                    tv_billType.text = "其它入库单列表"
                    pageId = 1
                }
                R.id.tv3 -> {
                    tv_billType.text = "其它出库单列表"
                    pageId = 2
                }
                R.id.tv4 -> {
                    tv_billType.text = "直接调拨单列表"
                    pageId = 3
                }
                R.id.tv5 -> {
                    tv_billType.text = "分步式调出单列表"
                    pageId = 4
                }
                R.id.tv6 -> {
                    tv_billType.text = "分步式调入单列表"
                    pageId = 5
                }
                R.id.tv7 -> {
                    tv_billType.text = "采购退料单列表"
                    pageId = 6
                }
                R.id.tv8 -> {
                    tv_billType.text = "生产入库单列表"
                    pageId = 7
                }
                R.id.tv9 -> {
                    tv_billType.text = "自由调拨单列表"
                    pageId = 8
                }
                R.id.tv10 -> {
                    tv_billType.text = "销售出库单列表"
                    pageId = 9
                }
            }
            viewPager.setCurrentItem(pageId)
            popWindow!!.dismiss()
        }
        popV.findViewById<View>(R.id.tv1).setOnClickListener(click)
        popV.findViewById<View>(R.id.tv2).setOnClickListener(click)
        popV.findViewById<View>(R.id.tv3).setOnClickListener(click)
        popV.findViewById<View>(R.id.tv4).setOnClickListener(click)
        popV.findViewById<View>(R.id.tv5).setOnClickListener(click)
        popV.findViewById<View>(R.id.tv6).setOnClickListener(click)
        popV.findViewById<View>(R.id.tv7).setOnClickListener(click)
        popV.findViewById<View>(R.id.tv8).setOnClickListener(click)
        popV.findViewById<View>(R.id.tv9).setOnClickListener(click)
        popV.findViewById<View>(R.id.tv10).setOnClickListener(click)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            context.finish()
        }
        return false
    }
}
package ykk.xc.com.zgwms.warehouse.adapter

import android.app.Activity
import android.text.Html
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import ykk.xc.com.zgwms.R
import ykk.xc.com.zgwms.bean.ICStockBill
import ykk.xc.com.zgwms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.zgwms.util.basehelper.BaseRecyclerAdapter
import java.text.DecimalFormat

class OutInStockSearchFragment2_QTRK_Adapter(private val context: Activity, datas: List<ICStockBill>) : BaseArrayRecyclerAdapter<ICStockBill>(datas) {
    private val df = DecimalFormat("#.######")
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.ware_outin_stock_search_fragment1__other_in_stock_item
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: ICStockBill, pos: Int) {
        // 初始化id
        val tv_pdaNo = holder.obtainView<TextView>(R.id.tv_pdaNo)
        val tv_fdate = holder.obtainView<TextView>(R.id.tv_fdate)
        val tv_recOrgName = holder.obtainView<TextView>(R.id.tv_recOrgName)
        val tv_purOrgName = holder.obtainView<TextView>(R.id.tv_purOrgName)
        val tv_suppName = holder.obtainView<TextView>(R.id.tv_suppName)
        val tv_empName = holder.obtainView<TextView>(R.id.tv_empName)
        val tv_stockManagerName = holder.obtainView<TextView>(R.id.tv_stockManagerName)
        val tv_fbillerName = holder.obtainView<TextView>(R.id.tv_fbillerName)
        val tv_search = holder.obtainView<TextView>(R.id.tv_search)
        val tv_upload = holder.obtainView<TextView>(R.id.tv_upload)
        val tv_del = holder.obtainView<TextView>(R.id.tv_del)
        val lin_button = holder.obtainView<LinearLayout>(R.id.lin_button)

        // 赋值
        tv_pdaNo.text = Html.fromHtml("PDA单号:&nbsp;<font color='#000000'>"+entity.pdaNo+"</font>")
        tv_fdate.text = Html.fromHtml("入库日期:&nbsp;<font color='#000000'>"+entity.fdate+"</font>")
        if(entity.stockOrg != null) {
            tv_recOrgName.text = Html.fromHtml("库存组织:&nbsp;<font color='#6a5acd'>"+ entity.stockOrg.fname +"</font>")
        } else {
            tv_recOrgName.text = "库存组织:"
        }
        tv_purOrgName.visibility = View.INVISIBLE
        if(entity.supplier != null) {
            tv_suppName.text = Html.fromHtml("供应商:&nbsp;<font color='#FF4400'>"+ entity.supplier.fname +"</font>")
        } else {
            tv_suppName.text = "供应商:"
        }
        tv_empName.text = Html.fromHtml("验收员:&nbsp;<font color='#000000'>"+ entity.empName +"</font>")
        tv_stockManagerName.text = Html.fromHtml("仓管员:&nbsp;<font color='#000000'>"+ entity.stockManagerName +"</font>")
        tv_fbillerName.text = Html.fromHtml("制单人:&nbsp;<font color='#000000'>"+ entity.createUserName +"</font>")

        if (entity.isShowButton) {
            lin_button!!.setVisibility(View.VISIBLE)
        } else {
            lin_button!!.setVisibility(View.GONE)
        }

        val click = View.OnClickListener { v ->
            when (v.id) {
                R.id.tv_search -> { // 查询
                    if (callBack != null) {
                        callBack!!.onSearch(entity, pos)
                    }
                }
                R.id.tv_upload -> {// 上传
                    if (callBack != null) {
                        callBack!!.onUpload(entity, pos)
                    }
                }
                R.id.tv_del -> { // 删除行
                    if (callBack != null) {
                        callBack!!.onDelete(entity, pos)
                    }
                }
            }
        }
        tv_search.setOnClickListener(click)
        tv_upload!!.setOnClickListener(click)
        tv_del!!.setOnClickListener(click)
    }

    fun setCallBack(callBack: MyCallBack) {
        this.callBack = callBack
    }

    interface MyCallBack {
        fun onSearch(entity: ICStockBill, position: Int)
        fun onUpload(entity: ICStockBill, position: Int)
        fun onDelete(entity: ICStockBill, position: Int)
    }

}

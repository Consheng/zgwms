package ykk.xc.com.zgwms.warehouse.adapter

import android.app.Activity
import android.text.Html
import android.widget.TextView

import ykk.xc.com.zgwms.R
import ykk.xc.com.zgwms.bean.ICStockBillEntry
import ykk.xc.com.zgwms.bean.warehouse.StkCountInput
import ykk.xc.com.zgwms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.zgwms.util.basehelper.BaseRecyclerAdapter

class Stk_CountInput_List_DialogAdapter(private val context: Activity, private val datas: List<StkCountInput>) : BaseArrayRecyclerAdapter<StkCountInput>(datas) {
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.ware_count_input_list_dialog_item
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: StkCountInput, pos: Int) {
        // 初始化id
        val tv_row = holder.obtainView<TextView>(R.id.tv_row)
        val tv_fbillNo = holder.obtainView<TextView>(R.id.tv_fbillNo)
        val tv_date = holder.obtainView<TextView>(R.id.tv_date)
        val tv_stockOrgName = holder.obtainView<TextView>(R.id.tv_stockOrgName)
        val tv_sourceNo = holder.obtainView<TextView>(R.id.tv_sourceNo)
        val tv_sourceName = holder.obtainView<TextView>(R.id.tv_sourceName)

        // 赋值
        tv_row.text = (pos+1).toString()
        tv_fbillNo.text = entity.fbillNo
        tv_date.text = Html.fromHtml("账存日期:&nbsp;<font color='#000000'>"+entity.fbackupDate+"</font>")
        tv_stockOrgName.text = Html.fromHtml("库存组织:&nbsp;<font color='#6a5acd'>"+entity.stockOrg.fname+"</font>")
        tv_sourceNo.text = Html.fromHtml("盘点来源编码:&nbsp;<font color='#000000'>" + entity.fschemeNo + "</font>")
        tv_sourceName.text = Html.fromHtml("盘点来源名称:&nbsp;<font color='#6a5acd'>" + entity.fschemeName + "</font>")

    }

    fun setCallBack(callBack: MyCallBack) {
        this.callBack = callBack
    }

    interface MyCallBack {
        fun onDelete(entity: ICStockBillEntry, position: Int)
    }

}

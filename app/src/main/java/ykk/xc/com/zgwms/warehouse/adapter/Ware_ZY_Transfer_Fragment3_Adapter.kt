package ykk.xc.com.zgwms.warehouse.adapter

import android.app.Activity
import android.text.Html
import android.view.View
import android.widget.TextView
import ykk.xc.com.zgwms.R
import ykk.xc.com.zgwms.bean.ICStockBillEntry
import ykk.xc.com.zgwms.comm.Comm
import ykk.xc.com.zgwms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.zgwms.util.basehelper.BaseRecyclerAdapter
import java.text.DecimalFormat

class Ware_ZY_Transfer_Fragment3_Adapter(private val context: Activity, datas: List<ICStockBillEntry>) : BaseArrayRecyclerAdapter<ICStockBillEntry>(datas) {
    private val df = DecimalFormat("#.######")
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.ware_zy_transfer_fragment3_item
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: ICStockBillEntry, pos: Int) {
        // 初始化id
        val tv_row = holder.obtainView<TextView>(R.id.tv_row)
        val tv_mtlNumber = holder.obtainView<TextView>(R.id.tv_mtlNumber)
        val tv_mtlName = holder.obtainView<TextView>(R.id.tv_mtlName)
        val tv_batchNo = holder.obtainView<TextView>(R.id.tv_batchNo)
        val tv_fmodel = holder.obtainView<TextView>(R.id.tv_fmodel)
        val tv_num = holder.obtainView<TextView>(R.id.tv_num)
        val view_del = holder.obtainView<View>(R.id.view_del)
        val tv_stockName = holder.obtainView<TextView>(R.id.tv_stockName)
        val tv_stockPosName = holder.obtainView<TextView>(R.id.tv_stockPosName)
        val tv_stockName2 = holder.obtainView<TextView>(R.id.tv_stockName2)
        val tv_stockPosName2 = holder.obtainView<TextView>(R.id.tv_stockPosName2)

        // 赋值
        tv_row.text = (pos+1).toString()
        tv_mtlName.text = entity.material.fname
        tv_mtlNumber.text = Html.fromHtml("代码:&nbsp;<font color='#6a5acd'>"+entity.material.fnumber+"</font>")
        if(Comm.isNULLS(entity.strBatchCode).length > 0) {
            tv_batchNo.visibility = View.VISIBLE
            tv_batchNo.text = Html.fromHtml("批次:&nbsp;<font color='#6a5acd'>" + entity.strBatchCode + "</font>")
        } else {
            tv_batchNo.visibility = View.INVISIBLE
        }
        tv_fmodel.text = Html.fromHtml("规格型号:&nbsp;<font color='#6a5acd'>" + entity.material.materialSize + "</font>")

        tv_num.text = Html.fromHtml("调拨数:&nbsp;<font color='#FF0000'>"+ df.format(entity.fqty) +"</font>&nbsp;<font color='#666666'>"+ entity.unit.fname +"</font>")

        // 显示调出仓库组信息
        if(entity.stock2 != null ) {
            tv_stockName2.visibility = View.VISIBLE
            tv_stockName2.text = Html.fromHtml("调出仓库:&nbsp;<font color='#000000'>"+entity.stock2!!.fname+"</font>")
        } else {
            tv_stockName2.visibility = View.INVISIBLE
        }
        if(entity.stockPos2 != null ) {
            tv_stockPosName2.visibility = View.VISIBLE
            tv_stockPosName2.text = Html.fromHtml("库位:&nbsp;<font color='#000000'>"+entity.stockPos2!!.fname+"</font>")
        } else {
            tv_stockPosName2.visibility = View.INVISIBLE
        }

        // 显示调入仓库组信息
        if(entity.stock != null ) {
            tv_stockName.visibility = View.VISIBLE
            tv_stockName.text = Html.fromHtml("调入仓库:&nbsp;<font color='#000000'>"+entity.stock!!.fname+"</font>")
        } else {
            tv_stockName.visibility = View.INVISIBLE
        }
        if(entity.stockPos != null ) {
            tv_stockPosName.visibility = View.VISIBLE
            tv_stockPosName.text = Html.fromHtml("库位:&nbsp;<font color='#000000'>"+entity.stockPos!!.fname+"</font>")
        } else {
            tv_stockPosName.visibility = View.INVISIBLE
        }

        val click = View.OnClickListener { v ->
            when (v.id) {
                R.id.view_del // 删除行
                -> if (callBack != null) {
                    callBack!!.onDelete(entity, pos)
                }
            }
        }
        view_del!!.setOnClickListener(click)
    }

    fun setCallBack(callBack: MyCallBack) {
        this.callBack = callBack
    }

    interface MyCallBack {
        fun onDelete(entity: ICStockBillEntry, position: Int)
    }

}

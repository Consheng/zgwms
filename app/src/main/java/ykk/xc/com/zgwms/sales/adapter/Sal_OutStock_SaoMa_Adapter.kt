package ykk.xc.com.zgwms.sales.adapter

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

class Sal_OutStock_SaoMa_Adapter(private val context: Activity, datas: List<ICStockBillEntry>) : BaseArrayRecyclerAdapter<ICStockBillEntry>(datas) {
    private val df = DecimalFormat("#.######")
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.sal_out_stock_saoma_item
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: ICStockBillEntry, pos: Int) {
        // 初始化id
        val tv_row = holder.obtainView<TextView>(R.id.tv_row)
        val tv_mtlNumber = holder.obtainView<TextView>(R.id.tv_mtlNumber)
        val tv_mtlName = holder.obtainView<TextView>(R.id.tv_mtlName)
        val tv_batchNo = holder.obtainView<TextView>(R.id.tv_batchNo)
        val tv_fmodel = holder.obtainView<TextView>(R.id.tv_fmodel)
        val tv_assistData = holder.obtainView<TextView>(R.id.tv_assistData)
        val tv_num = holder.obtainView<TextView>(R.id.tv_num)
        val tv_sourceQty = holder.obtainView<TextView>(R.id.tv_sourceQty)
        val tv_sourceNo = holder.obtainView<TextView>(R.id.tv_sourceNo)
        val tv_isGift = holder.obtainView<TextView>(R.id.tv_isGift)
        val view_del = holder.obtainView<View>(R.id.view_del)
        val tv_stockName = holder.obtainView<TextView>(R.id.tv_stockName)
        val tv_carVersion = holder.obtainView<TextView>(R.id.tv_carVersion)
        /*val tv_carVersionLocation = holder.obtainView<TextView>(R.id.tv_carVersionLocation)
        val tv_carVersionName = holder.obtainView<TextView>(R.id.tv_carVersionName)*/

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

        var strAuxProp = StringBuilder()
        if(Comm.isNULLS(entity.fauxpropid_103_name).length > 0) strAuxProp.append(entity.fauxpropid_103_name+"&nbsp;&nbsp;/")
        if(Comm.isNULLS(entity.fauxpropid_104_name).length > 0) strAuxProp.append(entity.fauxpropid_104_name+"&nbsp;&nbsp;/")
        if(Comm.isNULLS(entity.fauxpropid_105_name).length > 0) strAuxProp.append(entity.fauxpropid_105_name+"&nbsp;&nbsp;/")
        if(Comm.isNULLS(entity.fauxpropid_106_name).length > 0) strAuxProp.append(entity.fauxpropid_106_name+"/")
        if(strAuxProp.length > 0) strAuxProp.delete(strAuxProp.length-1, strAuxProp.length)

        tv_assistData.text = Html.fromHtml("辅助属性:&nbsp;<font color='#6a5acd'>" + strAuxProp.toString() + "</font>")

        tv_num.text = Html.fromHtml("出库数:&nbsp;<font color='#FF0000'>"+ df.format(entity.fqty) +"</font>")
        tv_sourceQty.text = Html.fromHtml("订单数:&nbsp;<font color='#6a5acd'>"+ df.format(entity.fsourceQty) +"</font>&nbsp;<font color='#666666'>"+ entity.unit.fname +"</font>")
        tv_sourceNo.text = Html.fromHtml("订单:&nbsp;<font color='#000000'>"+ entity.forderBillNo +"</font>")
        /*if(entity.material.isAotuBringOut == 1) {
            tv_isGift.visibility = View.VISIBLE
        } else {
            tv_isGift.visibility = View.INVISIBLE
        }*/

        // 显示仓库组信息
        if(entity.stock != null ) {
            tv_stockName.visibility = View.VISIBLE
            tv_stockName.text = Html.fromHtml("仓库:&nbsp;<font color='#000000'>"+entity.stock!!.fname+"</font>")
        } else {
            tv_stockName.visibility = View.INVISIBLE
        }
        if(Comm.isNULLS(entity.carVersionNumber).length > 0) {
            tv_carVersion.visibility = View.VISIBLE
            /*tv_carVersionLocation.visibility = View.VISIBLE
            tv_carVersionName.visibility = View.VISIBLE*/

            var strCarVersion = StringBuilder()
            if(Comm.isNULLS(entity.carVersionNumber).length > 0) strCarVersion.append(entity.carVersionNumber+"&nbsp;&nbsp;/")
            if(Comm.isNULLS(entity.carVersionName).length > 0) strCarVersion.append(entity.carVersionName+"&nbsp;&nbsp;/")
            if(Comm.isNULLS(entity.carVersionLocation).length > 0) strCarVersion.append(entity.carVersionLocation+"/")
            if(strCarVersion.length > 0) strCarVersion.delete(strCarVersion.length-1, strCarVersion.length)
            tv_carVersion.text = Html.fromHtml("车型版号:&nbsp;<font color='#6a5acd'>"+strCarVersion+"</font>")
            /*tv_carVersionLocation.text = Html.fromHtml("位置:&nbsp;<font color='#6a5acd'>"+entity.carVersionLocation+"</font>")
            tv_carVersionName.text = Html.fromHtml("名称:&nbsp;<font color='#6a5acd'>"+entity.carVersionName+"</font>")*/
        } else {
            tv_carVersion.visibility = View.GONE
            /*tv_carVersionLocation.visibility = View.GONE
            tv_carVersionName.visibility = View.GONE*/
        }

        val view = tv_row!!.getParent() as View
        if(entity.material.isBatchManager == 1 || entity.material.isSnManager == 1) {
            view.setBackgroundResource(R.color.c_eaeaea)
        } else {
            view.setBackgroundResource(R.color.white)
        }

        val click = View.OnClickListener { v ->
            when (v.id) {
                R.id.view_del -> {// 删除行
                    if (callBack != null) {
//                        callBack!!.onDelete(entity, pos)
                    }
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

package ykk.xc.com.zgwms.produce.adapter

import android.app.Activity
import android.text.Html
import android.view.View
import android.widget.TextView
import ykk.xc.com.zgwms.R
import ykk.xc.com.zgwms.bean.MaterialBinningRecord
import ykk.xc.com.zgwms.comm.Comm
import ykk.xc.com.zgwms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.zgwms.util.basehelper.BaseRecyclerAdapter
import java.text.DecimalFormat

class Prod_Box_Fragment1_Adapter(private val context: Activity, datas: List<MaterialBinningRecord>) : BaseArrayRecyclerAdapter<MaterialBinningRecord>(datas) {
    private val df = DecimalFormat("#.######")
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.prod_box_fragment1_item
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: MaterialBinningRecord, pos: Int) {
        // 初始化id
        val tv_row = holder.obtainView<TextView>(R.id.tv_row)
        val tv_mtlNumber = holder.obtainView<TextView>(R.id.tv_mtlNumber)
        val tv_mtlName = holder.obtainView<TextView>(R.id.tv_mtlName)
        val tv_fmodel = holder.obtainView<TextView>(R.id.tv_fmodel)
        val tv_prodNo = holder.obtainView<TextView>(R.id.tv_prodNo)
        val tv_isGift = holder.obtainView<TextView>(R.id.tv_isGift)
        val tv_salOrderNo = holder.obtainView<TextView>(R.id.tv_salOrderNo)
        val tv_prodQty = holder.obtainView<TextView>(R.id.tv_prodQty)
        val tv_num = holder.obtainView<TextView>(R.id.tv_num)
        val tv_useableQty = holder.obtainView<TextView>(R.id.tv_useableQty)
        val view_del = holder.obtainView<View>(R.id.view_del)
        val tv_carVersion = holder.obtainView<TextView>(R.id.tv_carVersion)
        val tv_carVersionLocation = holder.obtainView<TextView>(R.id.tv_carVersionLocation)
        val tv_carVersionName = holder.obtainView<TextView>(R.id.tv_carVersionName)

        // 赋值
        tv_row.text = entity.rowNo.toString()
        tv_mtlName.text = entity.material.fname
        tv_mtlNumber.text = Html.fromHtml("代码:&nbsp;<font color='#6a5acd'>"+entity.material.fnumber+"</font>")
        tv_fmodel.text = Html.fromHtml("规格:&nbsp;<font color='#6a5acd'>"+entity.material.materialSize+"</font>")
        tv_num.text = df.format(entity.fqty)
        tv_prodNo.text = Html.fromHtml("生产单号:&nbsp;<font color='#6a5acd'>"+ entity.fsourceNo +"</font>")
        tv_salOrderNo.text = Html.fromHtml("订单号:&nbsp;<font color='#000000'>"+ entity.forderNo +"</font>")
        tv_prodQty.text = Html.fromHtml("生产数:&nbsp;<font color='#FF2200'>"+ df.format(entity.fsourceQty) +"</font>")
        tv_useableQty.text = Html.fromHtml("可用数:&nbsp;<font color='#009900'>"+ df.format(entity.usableQty) +"</font>")

        if(entity.material.isBatchManager == 1 || entity.material.isSnManager == 1) {
            tv_num.isEnabled = false
            tv_num.setBackgroundResource(R.drawable.back_style_gray3b)
        } else {
            tv_num.isEnabled = true
            tv_num.setBackgroundResource(R.drawable.back_style_blue2)
        }
        if(entity.fsourceInterId == 0) { // 是否为赠品，源单id为0即为赠品
            tv_isGift.visibility = View.VISIBLE
            tv_prodNo.visibility = View.INVISIBLE
        } else {
            tv_isGift.visibility = View.INVISIBLE
            tv_prodNo.visibility = View.VISIBLE
        }
        if(entity.material.personal > 0 && entity.salOrderEntry != null && Comm.isNULLS(entity.salOrderEntry.personalCarVersionNumber).length > 0) {
            tv_carVersion.visibility = View.VISIBLE
            tv_carVersionLocation.visibility = View.VISIBLE
            tv_carVersionName.visibility = View.VISIBLE
            tv_carVersion.text = Html.fromHtml("车型版号:&nbsp;<font color='#6a5acd'>"+entity.salOrderEntry.personalCarVersionNumber+"</font>")
            tv_carVersionLocation.text = Html.fromHtml("车型:&nbsp;<font color='#6a5acd'>"+entity.salOrderEntry.personalCarVersionLocation+"</font>")
            tv_carVersionName.text = Html.fromHtml("车系:&nbsp;<font color='#6a5acd'>"+entity.salOrderEntry.personalCarVersionName+"</font>")
        } else {
            tv_carVersion.visibility = View.GONE
            tv_carVersionLocation.visibility = View.GONE
            tv_carVersionName.visibility = View.GONE
        }

        val click = View.OnClickListener { v ->
            when (v.id) {
                R.id.tv_num -> {// 数量
                    if (callBack != null) {
                        callBack!!.onClickNum(entity, pos)
                    }
                }
                R.id.view_del // 删除行
                -> if (callBack != null) {
                    callBack!!.onDelete(entity, pos)
                }
            }
        }
        tv_num!!.setOnClickListener(click)
        view_del!!.setOnClickListener(click)
    }

    fun setCallBack(callBack: MyCallBack) {
        this.callBack = callBack
    }

    interface MyCallBack {
        fun onClickNum(entity: MaterialBinningRecord, position: Int)
        fun onDelete(entity: MaterialBinningRecord, position: Int)
    }

}

package ykk.xc.com.zgwms.purchase.adapter

import android.app.Activity
import android.text.Html
import android.view.View
import android.widget.TextView
import ykk.xc.com.zgwms.R
import ykk.xc.com.zgwms.bean.warehouse.StkInStock
import ykk.xc.com.zgwms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.zgwms.util.basehelper.BaseRecyclerAdapter

class Pur_Sel_StkInStock_DialogAdapter(private val context: Activity, private val datas: List<StkInStock>) : BaseArrayRecyclerAdapter<StkInStock>(datas) {
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.pur_sel_stkinstock_list_dialog_item
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: StkInStock, pos: Int) {
        // 初始化id
        val tv_row = holder.obtainView<TextView>(R.id.tv_row)
        val tv_fnumber = holder.obtainView<TextView>(R.id.tv_fnumber)
        val tv_fdate = holder.obtainView<TextView>(R.id.tv_fdate)
        val tv_waitUnload = holder.obtainView<TextView>(R.id.tv_waitUnload)

        // 赋值
        tv_row.text = (pos + 1).toString()
//        tv_fnumber.text = entity.fbillNo
        tv_fnumber.text = Html.fromHtml(entity.fbillNo+"<br><font color='#6a5acd'>"+entity.supplier.fname+"</font>")
        tv_fdate.text = entity.fdate
        if(entity.isWmsUploadStatus) {
            tv_waitUnload.visibility = View.VISIBLE
        } else {
            tv_waitUnload.visibility = View.INVISIBLE
        }
        val view = tv_row!!.getParent() as View
        if (entity.isCheck) {
            view.setBackgroundResource(R.drawable.back_style_check1_true)
        } else {
            view.setBackgroundResource(R.drawable.back_style_check1_false)
        }
    }

    fun setCallBack(callBack: MyCallBack) {
        this.callBack = callBack
    }

    interface MyCallBack {
        fun onClick(entity: StkInStock, position: Int)
    }

}

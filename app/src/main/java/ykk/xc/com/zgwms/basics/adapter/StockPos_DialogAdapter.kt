package ykk.xc.com.zgwms.basics.adapter

import android.app.Activity
import android.widget.TextView
import ykk.xc.com.zgwms.R
import ykk.xc.com.zgwms.bean.k3Bean.StockPosition_K3
import ykk.xc.com.zgwms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.zgwms.util.basehelper.BaseRecyclerAdapter

class StockPos_DialogAdapter(private val context: Activity, private val datas: List<StockPosition_K3>) : BaseArrayRecyclerAdapter<StockPosition_K3>(datas) {
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.ab_stock_pos_dialog_item
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: StockPosition_K3, pos: Int) {
        // 初始化id
        val tv_row = holder.obtainView<TextView>(R.id.tv_row)
        val tv_fnumber = holder.obtainView<TextView>(R.id.tv_fnumber)
        val tv_fname = holder.obtainView<TextView>(R.id.tv_fname)
        // 赋值
        tv_row!!.text = (pos + 1).toString()
        tv_fnumber!!.text = entity.fnumber
        tv_fname!!.text = entity.fname
    }

    fun setCallBack(callBack: MyCallBack) {
        this.callBack = callBack
    }

    interface MyCallBack {
        fun onClick(entity: StockPosition_K3, position: Int)
    }

}

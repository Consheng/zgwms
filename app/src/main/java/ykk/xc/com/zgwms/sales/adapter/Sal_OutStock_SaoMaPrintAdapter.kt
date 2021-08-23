package ykk.xc.com.zgwms.sales.adapter

import android.app.Activity
import android.text.Html
import android.view.View
import android.widget.TextView
import ykk.xc.com.zgwms.R
import ykk.xc.com.zgwms.bean.ExpressNoData
import ykk.xc.com.zgwms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.zgwms.util.basehelper.BaseRecyclerAdapter

class Sal_OutStock_SaoMaPrintAdapter(private val context: Activity, private val datas: List<ExpressNoData>) : BaseArrayRecyclerAdapter<ExpressNoData>(datas) {
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.sal_out_stock_print_item
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: ExpressNoData, pos: Int) {
        // 初始化id
        val tv_row = holder.obtainView<TextView>(R.id.tv_row)
        val tv_fnumber = holder.obtainView<TextView>(R.id.tv_fnumber)
        val tv_expressNo = holder.obtainView<TextView>(R.id.tv_expressNo)
        val tv_print = holder.obtainView<TextView>(R.id.tv_print)

        // 赋值
        tv_row.text = (pos + 1).toString()
        tv_fnumber.text = Html.fromHtml(entity.salOrderNo.replace(",","<br>"))
        tv_expressNo.text = entity.t01
        if(entity.isCheck) {
            tv_print.visibility = View.INVISIBLE
        } else {
            tv_print.visibility = View.VISIBLE
        }

        val click = View.OnClickListener { v ->
            when (v.id) {
                R.id.tv_print -> {// 打印快递单
                    if (callBack != null) {
                        callBack!!.onPrint(entity, pos)
                    }
                }
            }
        }
        tv_print.setOnClickListener(click)
    }

    fun setCallBack(callBack: MyCallBack) {
        this.callBack = callBack
    }

    interface MyCallBack {
        fun onPrint(entity: ExpressNoData, position: Int)
    }

}

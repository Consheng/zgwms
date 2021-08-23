package ykk.xc.com.zgwms.produce.adapter

import android.app.Activity
import android.view.View
import android.widget.TextView
import ykk.xc.com.zgwms.R
import ykk.xc.com.zgwms.bean.prod.PrdMo
import ykk.xc.com.zgwms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.zgwms.util.basehelper.BaseRecyclerAdapter

class Prod_Sel_PrdMo_DialogAdapter(private val context: Activity, private val datas: List<PrdMo>) : BaseArrayRecyclerAdapter<PrdMo>(datas) {
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.prod_sel_prdmo_list_dialog_item
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: PrdMo, pos: Int) {
        // 初始化id
        val tv_row = holder.obtainView<TextView>(R.id.tv_row)
        val tv_fnumber = holder.obtainView<TextView>(R.id.tv_fnumber)
        val tv_fdate = holder.obtainView<TextView>(R.id.tv_fdate)
        val tv_waitUnload = holder.obtainView<TextView>(R.id.tv_waitUnload)

        // 赋值
        tv_row!!.setText((pos + 1).toString())
        tv_fnumber!!.setText(entity.fbillNo)
        tv_fdate!!.setText(entity.fdate)
        if(entity.isWmsUploadStatus) {
            tv_waitUnload.visibility = View.VISIBLE
        } else {
            tv_waitUnload.visibility = View.INVISIBLE
        }
    }

    fun setCallBack(callBack: MyCallBack) {
        this.callBack = callBack
    }

    interface MyCallBack {
        fun onClick(entity: PrdMo, position: Int)
    }

}

package ykk.xc.com.zgwms.sales

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.view.View
import butterknife.OnClick
import kotlinx.android.synthetic.main.sal_out_stock_mtl_show.*
import okhttp3.*
import ykk.xc.com.zgwms.R
import ykk.xc.com.zgwms.bean.sales.SalOrderEntry
import ykk.xc.com.zgwms.comm.BaseActivity
import ykk.xc.com.zgwms.comm.Comm
import ykk.xc.com.zgwms.sales.adapter.Sal_OutStock_MtlShow_Adapter
import ykk.xc.com.zgwms.util.JsonUtil
import ykk.xc.com.zgwms.util.LogUtil
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * 日期：2019-10-16 09:14
 * 描述：销售出库扫描（拼单物料显示）
 * 作者：ykk
 */
class Sal_OutStock_MtlShowActivity : BaseActivity() {

    companion object {
        private val SUCC1 = 200
        private val UNSUCC1 = 500
    }

    private val context = this
    private val TAG = "Sal_OutStock_MtlShowActivity"
    private var okHttpClient: OkHttpClient? = null
    private var mAdapter: Sal_OutStock_MtlShow_Adapter? = null
    private val checkDatas = ArrayList<SalOrderEntry>()
    private var mapEntryId :HashMap<Int, Boolean>? = null

    // 消息处理
    private val mHandler = MyHandler(this)
    private class MyHandler(activity: Sal_OutStock_MtlShowActivity) : Handler() {
        private val mActivity: WeakReference<Sal_OutStock_MtlShowActivity>

        init {
            mActivity = WeakReference(activity)
        }

        override fun handleMessage(msg: Message) {
            val m = mActivity.get()
            if (m != null) {
                m.hideLoadDialog()

                var errMsg: String? = null
                var msgObj: String? = null
                if (msg.obj is String) {
                    msgObj = msg.obj as String
                }
                when (msg.what) {
                    SUCC1 -> { // 扫码成功 进入
                        val list = JsonUtil.strToList(msgObj, SalOrderEntry::class.java)
                        list.forEach {
                            if(m.mapEntryId!!.containsKey(it.fentryId)) {
                                it.isCheck = true
                            }
                        }
                        m.checkDatas.clear()
                        m.checkDatas.addAll(list)
                        m.mAdapter!!.notifyDataSetChanged()
                    }
                    UNSUCC1 -> { // 扫码失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "很抱歉，没有找到数据！"
                        Comm.showWarnDialog(m.context, errMsg)
                    }
                }
            }
        }
    }

    override fun setLayoutResID(): Int {
        return R.layout.sal_out_stock_mtl_show
    }

    override fun initView() {
        if (okHttpClient == null) {
            okHttpClient = OkHttpClient.Builder()
                    //                .connectTimeout(10, TimeUnit.SECONDS) // 设置连接超时时间（默认为10秒）
                    .writeTimeout(120, TimeUnit.SECONDS) // 设置写的超时时间
                    .readTimeout(120, TimeUnit.SECONDS) //设置读取超时时间
                    .build()
        }

        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter = Sal_OutStock_MtlShow_Adapter(context, checkDatas)
        recyclerView.adapter = mAdapter
        // 设值listview空间失去焦点
        recyclerView.isFocusable = false

    }

    override fun initData() {
        bundle()

    }

    private fun bundle() {
        val bundle = context.intent.extras
        if (bundle != null) {
            val combineSalOrderId = bundle.getInt("combineSalOrderId")
            if(combineSalOrderId == 0) {
                Comm.showWarnDialog(context, "没有拼单id，无法查询数据！")
                return
            }
            mapEntryId = bundle.getSerializable("mapEntryId") as HashMap<Int, Boolean>
            run_findList(combineSalOrderId)
        }
    }

    @OnClick(R.id.btn_close)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_close -> { // 关闭
                context.finish()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) return
            when (requestCode) {
                /*SEL_STOCK -> {// 仓库	返回
                }*/
            }
        }
    }

    /**
     * 扫码查询对应的方法
     */
    private fun run_findList(combineSalOrderId :Int) {

        showLoadDialog("加载中...", false)
        var mUrl = getURL("salOrder/findMtlMergeList")
        val formBody = FormBody.Builder()
                .add("combineSalOrderId", combineSalOrderId.toString()) // 拼单id
                .build()

        val request = Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNSUCC1)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                LogUtil.e("run_findList --> onResponse", result)
                if (!JsonUtil.isSuccess(result)) {
                    val msg = mHandler.obtainMessage(UNSUCC1, result)
                    mHandler.sendMessage(msg)
                    return
                }
                val msg = mHandler.obtainMessage(SUCC1, result)
                mHandler.sendMessage(msg)
            }
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            closeHandler(mHandler)
            context.finish()
        }
        return false
    }

    override fun onDestroy() {
        closeHandler(mHandler)
        super.onDestroy()
    }

}
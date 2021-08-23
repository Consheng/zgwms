package ykk.xc.com.zgwms.warehouse

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.KeyEvent
import android.view.View
import butterknife.OnClick
import kotlinx.android.synthetic.main.pur_sel_poorder_list_dialog.*
import okhttp3.*
import ykk.xc.com.zgwms.R
import ykk.xc.com.zgwms.bean.warehouse.StkTransferOut
import ykk.xc.com.zgwms.comm.BaseDialogActivity
import ykk.xc.com.zgwms.util.JsonUtil
import ykk.xc.com.zgwms.util.basehelper.BaseRecyclerAdapter
import ykk.xc.com.zgwms.util.xrecyclerview.XRecyclerView
import ykk.xc.com.zgwms.warehouse.adapter.Ware_Sel_TransferOut_DialogAdapter
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

/**
 * 选择分步式调出单dialog
 */
class Ware_Sel_TransferOut_DialogActivity : BaseDialogActivity(), XRecyclerView.LoadingListener {

    private val context = this
    private val listDatas = ArrayList<StkTransferOut>()
    private var mAdapter: Ware_Sel_TransferOut_DialogAdapter? = null
    private val okHttpClient = OkHttpClient()
    private var limit = 1
    private var isRefresh = false
    private var isLoadMore = false
    private var isNextPage = false
    private var fstockOutOrgId = 0   // 调出库存组织
    private var fstockInOrgId = 0   // 调入库存组织
    private var ftransferBizType :String? = null // 调拨类型
    private var fobjectTypeId :String? = null // 业务类型
    private var fownerTypeInId :String? = null // 调入货主类型
    private var fownerTypeOutId :String? = null // 调出货主类型

    // 消息处理
    private val mHandler = MyHandler(this)

    private class MyHandler(activity: Ware_Sel_TransferOut_DialogActivity) : Handler() {
        private val mActivity: WeakReference<Ware_Sel_TransferOut_DialogActivity>

        init {
            mActivity = WeakReference(activity)
        }

        override fun handleMessage(msg: Message) {
            val m = mActivity.get()
            if (m != null) {
                m.hideLoadDialog()
                when (msg.what) {
                    SUCC1 // 成功
                    -> {
//                        val list = JsonUtil.strToList2(msg.obj as String, StkTransferOut::class.java)
                        val list = JsonUtil.strToList(msg.obj as String, StkTransferOut::class.java)
                        m.listDatas.addAll(list!!)
                        m.mAdapter!!.notifyDataSetChanged()

//                        if (m.isRefresh) {
//                            m.xRecyclerView.refreshComplete(true)
//                        } else if (m.isLoadMore) {
//                            m.xRecyclerView.loadMoreComplete(true)
//                        }
//
//                        m.xRecyclerView.isLoadingMoreEnabled = m.isNextPage
                    }
                    UNSUCC1 // 数据加载失败！
                    -> {
                        m.mAdapter!!.notifyDataSetChanged()
                        m.toasts("抱歉，没有加载到数据！")
                    }
                }
            }
        }
    }

    override fun setLayoutResID(): Int {
        return R.layout.ware_sel_transferout_list_dialog
    }

    override fun initView() {
        xRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        xRecyclerView.layoutManager = LinearLayoutManager(context!!)
        mAdapter = Ware_Sel_TransferOut_DialogAdapter(context, listDatas)
        xRecyclerView.adapter = mAdapter
        xRecyclerView.setLoadingListener(context)

        xRecyclerView.isPullRefreshEnabled = false // 上啦刷新禁用
        xRecyclerView.setLoadingMoreEnabled(false); // 不显示下拉刷新的view

        mAdapter!!.onItemClickListener = BaseRecyclerAdapter.OnItemClickListener { adapter, holder, view, pos ->
            val m = listDatas[pos - 1]
            val intent = Intent()
            intent.putExtra("obj", m)
            context.setResult(Activity.RESULT_OK, intent)
            context.finish()
        }
    }

    override fun initData() {
        val bundle = context.intent.extras
        if (bundle != null) {
            fstockOutOrgId = bundle.getInt("fstockOutOrgId")
            fstockInOrgId = bundle.getInt("fstockInOrgId")
            ftransferBizType = bundle.getString("ftransferBizType")
            fobjectTypeId = bundle.getString("fobjectTypeId")
            fownerTypeInId = bundle.getString("fownerTypeInId")
            fownerTypeOutId = bundle.getString("fownerTypeOutId")
        }

        initLoadDatas()
    }

    // 监听事件
    @OnClick(R.id.btn_close, R.id.btn_search, R.id.btn_refresh)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_close -> {
                closeHandler(mHandler)
                context.finish()
            }
            R.id.btn_search -> initLoadDatas()
            R.id.btn_refresh -> initLoadDatas()
        }
    }

    private fun initLoadDatas() {
        limit = 1
        listDatas.clear()
        run_okhttpDatas()
    }

    /**
     * 通过okhttp加载数据
     */
    private fun run_okhttpDatas() {
        showLoadDialog("加载中...", false)
        val mUrl = getURL("stkTransferOut/findPortionByParam_Parent")
        val formBody = FormBody.Builder()
                .add("fstockOutOrgId", fstockOutOrgId.toString())
                .add("fstockInOrgId", fstockInOrgId.toString())
                .add("ftransferBizType", isNULLS(ftransferBizType))
                .add("fobjectTypeId", isNULLS(fobjectTypeId))
                .add("fownerTypeInId", isNULLS(fownerTypeInId))
                .add("fownerTypeOutId", isNULLS(fownerTypeOutId))
                .add("limit", limit.toString())
                .add("pageSize", "30")
                .build()

        val request = Request.Builder()
                .addHeader("cookie", session)
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNSUCC1)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                if (!JsonUtil.isSuccess(result)) {
                    mHandler.sendEmptyMessage(UNSUCC1)
                    return
                }
                isNextPage = JsonUtil.isNextPage(result)

                val msg = mHandler.obtainMessage(SUCC1, result)
                Log.e("run_okhttpDatas --> onResponse", result)
                mHandler.sendMessage(msg)
            }
        })
    }

    override fun onRefresh() {
        isRefresh = true
        isLoadMore = false
        initLoadDatas()
    }

    override fun onLoadMore() {
        isRefresh = false
        isLoadMore = true
        limit += 1
        run_okhttpDatas()
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

    companion object {
        private val SUCC1 = 200
        private val UNSUCC1 = 501
    }
}

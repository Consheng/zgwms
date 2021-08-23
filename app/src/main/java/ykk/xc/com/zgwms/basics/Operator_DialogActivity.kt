package ykk.xc.com.zgwms.basics

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.KeyEvent
import android.view.View
import butterknife.OnClick
import kotlinx.android.synthetic.main.ab_operator_dialog.*
import okhttp3.*
import ykk.xc.com.zgwms.R
import ykk.xc.com.zgwms.basics.adapter.Operator_DialogAdapter
import ykk.xc.com.zgwms.bean.k3Bean.Operator_K3
import ykk.xc.com.zgwms.comm.BaseDialogActivity
import ykk.xc.com.zgwms.util.JsonUtil
import ykk.xc.com.zgwms.util.basehelper.BaseRecyclerAdapter
import ykk.xc.com.zgwms.util.xrecyclerview.XRecyclerView
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

/**
 * 选择业务员dialog
 */
class Operator_DialogActivity : BaseDialogActivity(), XRecyclerView.LoadingListener {

    companion object {
        private val SUCC1 = 200
        private val UNSUCC1 = 501
    }
    private val context = this
    private val listDatas = ArrayList<Operator_K3>()
    private var mAdapter: Operator_DialogAdapter? = null
    private val okHttpClient = OkHttpClient()
    private var limit = 1
    private var isRefresh: Boolean = false
    private var isLoadMore: Boolean = false
    private var isNextPage: Boolean = false
    private var fuseOrgId = 0   // 使用组织
    private var foperatorType = "CGY"   // 业务员类型 （ 销售员：XSY，采购员：CGY，仓管员：WHY，计划员：JHY ）

    // 消息处理
    private val mHandler = MyHandler(this)

    private class MyHandler(activity: Operator_DialogActivity) : Handler() {
        private val mActivity: WeakReference<Operator_DialogActivity>

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
                        val list = JsonUtil.strToList2(msg.obj as String, Operator_K3::class.java)
                        m.listDatas.addAll(list!!)

                        val size = m.listDatas.size
                        m.mAdapter!!.notifyDataSetChanged()

                        if (m.isRefresh) {
                            m.xRecyclerView.refreshComplete(true)
                        } else if (m.isLoadMore) {
                            m.xRecyclerView.loadMoreComplete(true)
                        }

                        m.xRecyclerView.setLoadingMoreEnabled(m.isNextPage)
                    }
                    UNSUCC1 -> {// 数据加载失败！
                        m.mAdapter!!.notifyDataSetChanged()
                        m.toasts("抱歉，没有加载到数据！")
                    }
                }
            }
        }
    }

    override fun setLayoutResID(): Int {
        return R.layout.ab_operator_dialog
    }

    override fun initView() {
        xRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        xRecyclerView.setLayoutManager(LinearLayoutManager(context))
        mAdapter = Operator_DialogAdapter(context, listDatas)
        xRecyclerView.setAdapter(mAdapter)
        xRecyclerView.setLoadingListener(context)

        xRecyclerView.setPullRefreshEnabled(false) // 上啦刷新禁用
        xRecyclerView.setLoadingMoreEnabled(false) // 不显示下拉刷新的view
        // 行点击
        mAdapter!!.onItemClickListener = BaseRecyclerAdapter.OnItemClickListener { adapter, holder, view, pos ->
            val curPos = pos - 1
            val emp = listDatas[curPos]
            val bundle = Bundle()
            bundle.putSerializable("obj", emp)
            setResults(context, bundle)
            context.finish()
        }
    }

    override fun initData() {
        val bundle = context.intent.extras
        if (bundle != null) {
            fuseOrgId = bundle.getInt("fuseOrgId")
            foperatorType = bundle.getString("foperatorType")
            // 业务员类型 （ 销售员：XSY，采购员：CGY，仓管员：WHY，计划员：JHY ）
            when(foperatorType) {
                "XSY" -> tv_title.text  = "销售员列表"
                "CGY" -> tv_title.text  = "采购员列表"
                "WHY" -> tv_title.text  = "仓管员列表"
                "JHY" -> tv_title.text  = "计划员列表"
            }
        }

        initLoadDatas()
    }

    // 监听事件
    @OnClick(R.id.btn_close, R.id.btn_search)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_close -> {
                context.finish()
            }
            R.id.btn_search -> {
                initLoadDatas()
            }
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
        showLoadDialog("加载中...")
        val formBody = FormBody.Builder()
                .add("fnumberOrName", getValues(et_staff).trim())
                .add("fuseOrgId", fuseOrgId.toString())
                .add("foperatorType", foperatorType)
                .add("limit", limit.toString())
                .add("pageSize", "30")
                .build()
        val mUrl = getURL("operator/findListByPage")

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
                Log.e("Operator_DialogActivity --> onResponse", result)
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

}

package ykk.xc.com.zgwms.purchase

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import butterknife.OnClick
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import kotlinx.android.synthetic.main.pur_sel_poorder_list_dialog.*
import okhttp3.*
import ykk.xc.com.zgwms.R
import ykk.xc.com.zgwms.bean.pur.POOrder
import ykk.xc.com.zgwms.comm.BaseDialogActivity
import ykk.xc.com.zgwms.comm.BaseFragment
import ykk.xc.com.zgwms.comm.Comm
import ykk.xc.com.zgwms.comm.OnItemClickListener
import ykk.xc.com.zgwms.purchase.adapter.Pur_Sel_POOrder_DialogAdapter
import ykk.xc.com.zgwms.util.JsonUtil
import ykk.xc.com.zgwms.util.LogUtil
import ykk.xc.com.zgwms.util.basehelper.BaseRecyclerAdapter
import ykk.xc.com.zgwms.util.xrecyclerview.XRecyclerView
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

/**
 * 选择采购订单单号dialog
 */
class Pur_Sel_POOrder_DialogActivity : BaseDialogActivity(), XRecyclerView.LoadingListener {

    companion object {
        private val SUCC1 = 200
        private val UNSUCC1 = 500
        private val SUCC2 = 201
        private val UNSUCC2 = 501

        private val SETFOCUS = 1
        private val SAOMA = 2
        private val WRITE_CODE = 3
    }
    private val context = this
    private val listDatas = ArrayList<POOrder>()
    private var mAdapter: Pur_Sel_POOrder_DialogAdapter? = null
    private val okHttpClient = OkHttpClient()
    private var limit = 1
    private var isRefresh = false
    private var isLoadMore = false
    private var isNextPage = false
    private var frecOrgId = 0   // 收料组织
    private var fpurOrgId = 0   // 采购组织
    private var fsupplierId = 0 // 供应商id
    private var isTextChange: Boolean = false // 是否进入TextChange事件
    private var strFid :String? = null
    private var curSupplierId = 0 // 当前选择的供应商id

    // 消息处理
    private val mHandler = MyHandler(this)

    private class MyHandler(activity: Pur_Sel_POOrder_DialogActivity) : Handler() {
        private val mActivity: WeakReference<Pur_Sel_POOrder_DialogActivity>

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
                        m.strFid = JsonUtil.strToString(msgObj)
                        m.initLoadDatas()
                    }
                    UNSUCC1 -> { // 扫码失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "很抱歉，没有找到数据！"
                        Comm.showWarnDialog(m.context, errMsg)
                    }
                    SUCC2 -> { // 查询列表   成功
                        val list = JsonUtil.strToList(msg.obj as String, POOrder::class.java)
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
                    UNSUCC2 -> { // 查询列表    失败！
                        m.mAdapter!!.notifyDataSetChanged()
                        m.toasts("抱歉，没有加载到数据！")
                    }
                    SETFOCUS -> { // 当弹出其他窗口会抢夺焦点，需要跳转下，才能正常得到值
                        m.setFocusable(m.et_getFocus)
                        m.setFocusable(m.et_code)
                    }
                    SAOMA -> { // 扫码之后
                        // 执行查询方法
                        m.run_smDatas()
                    }
                }
            }
        }
    }

    override fun setLayoutResID(): Int {
        return R.layout.pur_sel_poorder_list_dialog
    }

    override fun initView() {
        xRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        xRecyclerView.layoutManager = LinearLayoutManager(context!!)
        mAdapter = Pur_Sel_POOrder_DialogAdapter(context, listDatas)
        xRecyclerView.adapter = mAdapter
        xRecyclerView.setLoadingListener(context)

        xRecyclerView.isPullRefreshEnabled = false // 上啦刷新禁用
        xRecyclerView.setLoadingMoreEnabled(false) // 不显示下拉刷新的view

        /*mAdapter!!.onItemClickListener = BaseRecyclerAdapter.OnItemClickListener { adapter, holder, view, pos ->
            val m = listDatas[pos - 1]
            val intent = Intent()
            intent.putExtra("obj", m)
            context.setResult(Activity.RESULT_OK, intent)
            context.finish()
        }*/
        mAdapter!!.onItemClickListener = BaseRecyclerAdapter.OnItemClickListener { adapter, holder, view, pos ->
            val m = listDatas[pos - 1]
            if(curSupplierId > 0 && curSupplierId != m.fsupplierId) {
                Comm.showWarnDialog(context,"请选择相同供应商！")

            } else {
                curSupplierId = m.fsupplierId
                if (m.isCheck) {
                    m.isCheck = false
                } else {
                    m.isCheck = true
                }
                mAdapter!!.notifyDataSetChanged()

                var isBool = false
                listDatas.forEach{
                    if(m.isCheck) {
                        isBool = true
                    }
                }
                if(!isBool) {
                    curSupplierId = 0
                }
            }

        }
    }

    override fun initData() {
        hideSoftInputMode(et_code)
        mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)

        val bundle = context.intent.extras
        if (bundle != null) {
            frecOrgId = bundle.getInt("frecOrgId")
            fpurOrgId = bundle.getInt("fpurOrgId")
            fsupplierId = bundle.getInt("fsupplierId")
        }

        initLoadDatas()
    }


    // 监听事件
    @OnClick(R.id.btn_close, R.id.btn_search, R.id.btn_scan, R.id.btn_confirm)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_close -> {
                closeHandler(mHandler)
                context.finish()
            }
            R.id.btn_search -> {
                et_code.setText("")
                strFid = null
                initLoadDatas()
            }
            R.id.btn_scan -> { // 调用摄像头扫描（物料）
                ScanUtil.startScan(context, BaseFragment.CAMERA_SCAN, HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create())
            }
            R.id.btn_confirm -> { // 确认
                val size = listDatas.size
                if (size == 0) {
                    Comm.showWarnDialog(context, "请查询数据！")
                    return
                }
                val strFid = StringBuffer()
                listDatas.forEach {
                    if (it.isCheck) {
                        strFid.append(( if(strFid.length>0) "," else "" )+it.fid)
                    }
                }
                if (strFid.length == 0) {
                    Comm.showWarnDialog(context, "请至少选择一行数据！")
                    return
                }
                val intent = Intent()
                intent.putExtra("obj", strFid.toString())
                context.setResult(Activity.RESULT_OK, intent)
                context.finish()
            }
        }
    }

    override fun setListener() {
        val click = View.OnClickListener { v ->
            setFocusable(et_getFocus)
            when (v.id) {
                R.id.et_code -> setFocusable(et_code)
            }
        }
        et_code!!.setOnClickListener(click)

        // 物料---数据变化
        et_code!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0) return
                if (!isTextChange) {
                    isTextChange = true
                    mHandler.sendEmptyMessageDelayed(SAOMA, 300)
                }
            }
        })
        /*// 物料---长按输入条码
        et_code!!.setOnLongClickListener {
            showInputDialog("输入条码号", getValues(et_code), "none", WRITE_CODE)
            true
        }*/
        // 物料---焦点改变
        et_code.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                lin_focusMtl.setBackgroundResource(R.drawable.back_style_red_focus)
            } else {
                if (lin_focusMtl != null) {
                    lin_focusMtl.setBackgroundResource(R.drawable.back_style_gray4)
                }
            }
        }

    }

    private fun initLoadDatas() {
        curSupplierId = 0
        limit = 1
        listDatas.clear()
        run_okhttpDatas()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                BaseFragment.CAMERA_SCAN -> {// 扫一扫成功  返回
                    val hmsScan = data!!.getParcelableExtra(ScanUtil.RESULT) as HmsScan
                    if (hmsScan != null) {
                        setTexts(et_code, hmsScan.originalValue)
                    }
                }
            }
            mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
        }
    }

    /**
     * 扫码查询对应的方法
     */
    private fun run_smDatas() {
        isTextChange = false
        showLoadDialog("加载中...", false)
        var mUrl = getURL("poOrder/findBarcodeGetFid")
        val formBody = FormBody.Builder()
                .add("barcode2", getValues(et_code)) //
                .add("fsupplierId", fsupplierId.toString())
//                .add("strFid", if(strFid != null) strFid else "")
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
                LogUtil.e("run_smDatas --> onResponse", result)
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

    /**
     * 通过okhttp加载数据
     */
    private fun run_okhttpDatas() {
        showLoadDialog("加载中...", false)
        val mUrl = getURL("poOrder/findPortionByParam_Parent")
        val formBody = FormBody.Builder()
                .add("frecOrgId", frecOrgId.toString())
                .add("fpurOrgId", fpurOrgId.toString())
                .add("fsupplierId", fsupplierId.toString())
                .add("strFid", isNULLS(strFid))
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
                mHandler.sendEmptyMessage(UNSUCC2)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                if (!JsonUtil.isSuccess(result)) {
                    mHandler.sendEmptyMessage(UNSUCC2)
                    return
                }
                isNextPage = JsonUtil.isNextPage(result)

                val msg = mHandler.obtainMessage(SUCC2, result)
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

}

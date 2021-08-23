package ykk.xc.com.zgwms.entrance.page5

import android.content.SharedPreferences
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText

import butterknife.BindView
import butterknife.OnClick
import kotlinx.android.synthetic.main.aa_main_item6_set_service.*
import ykk.xc.com.zgwms.R
import ykk.xc.com.zgwms.comm.BaseActivity
import ykk.xc.com.zgwms.comm.Consts

class ServiceSetActivity : BaseActivity() {

    private val context = this

    private var spfConfig: SharedPreferences? = null


    override fun setLayoutResID(): Int {
        return R.layout.aa_main_item6_set_service
    }

    override fun initView() {
        spfConfig = spf(getResStr(R.string.saveConfig))
        val http = spfConfig!!.getString("http", "http")
        val ip = spfConfig!!.getString("ip", "192.168.3.214")
        val port = spfConfig!!.getString("port", "8080")
        setTexts(et_http, http)
        setTexts(et_ip, ip)
        setTexts(et_port, port)
    }

    override fun initData() {

    }

    @OnClick(R.id.btn_close, R.id.btn_save)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_close -> {// 关闭
                hideKeyboard(currentFocus)
                context.finish()
            }
            R.id.btn_save -> {// 保存配置信息
                val http = getValues(et_http).trim()
                val ip = getValues(et_ip).trim()
                val port = getValues(et_port).trim()
                val editor = spfConfig!!.edit()
                editor.putString("http", isNULL2(http,"http"))
                editor.putString("ip", ip)
                editor.putString("port", port)
                editor.commit()

//                Consts.setIp(ip)
//                Consts.setIp(ip)
//                Consts.setPort(port)
                hideKeyboard(view)

                context.finish()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            context.finish()
        }
        return false
    }
}

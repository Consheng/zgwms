package ykk.xc.com.zgwms.entrance


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import ykk.xc.com.zgwms.R
import ykk.xc.com.zgwms.comm.BaseFragment
import ykk.xc.com.zgwms.sales.*
import ykk.xc.com.zgwms.warehouse.OutInStock_Search_MainActivity

/**
 * 销售
 */
class MainTabFragment3 : BaseFragment() {

    override fun setLayoutResID(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.aa_main_item3, container, false)
    }

    @OnClick(R.id.relative1, R.id.relative2, R.id.relative3, R.id.relative4, R.id.relative5, R.id.relative6)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.relative1 -> {// 待上传
                val bundle = Bundle()
                bundle.putInt("pageId", 9)
                bundle.putString("billType", "XSCK")
                show(OutInStock_Search_MainActivity::class.java, bundle)
            }
            R.id.relative2 -> {// 销售出库
                show(Sal_OutStock_SaoMaActivity::class.java, null)
            }
            R.id.relative3 -> {// 材料出库
                show(Sal_OutStock_SaoMa2Activity::class.java, null)
            }
            R.id.relative4 -> {// 销售退货
                show(Sal_OutStock_RED_MainActivity::class.java, null)
            }
            R.id.relative5 -> {// 快递打印
                show(Sal_OutStock_PrintActivity::class.java, null)
            }
            R.id.relative6 -> {// 快递解锁
                show(Sal_OutStock_PrintUnLockActivity::class.java, null)
            }
        }
    }
}

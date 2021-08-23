package ykk.xc.com.zgwms.entrance


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import ykk.xc.com.zgwms.R
import ykk.xc.com.zgwms.comm.BaseFragment
import ykk.xc.com.zgwms.warehouse.*


/**
 * 仓库
 */
class MainTabFragment4 : BaseFragment() {

    override fun setLayoutResID(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.aa_main_item4, container, false)
    }

    @OnClick(R.id.relative1, R.id.relative2, R.id.relative3, R.id.relative4, R.id.relative5, R.id.relative6, R.id.relative7, R.id.relative8)
    fun onViewClicked(view: View) {
        val bundle: Bundle? = null
        when (view.id) {
            R.id.relative1 -> { // 待上传
                val bundle = Bundle()
                bundle.putInt("pageId", 1)
                bundle.putString("billType", "QTRK")
                show(OutInStock_Search_MainActivity::class.java, bundle)
            }
            R.id.relative2 -> { // 其它入库
                show(Other_InStock_MainActivity::class.java, null)
            }
            R.id.relative3 -> { // 其它出库
                show(Other_OutStock_MainActivity::class.java, null)
            }
            R.id.relative4 -> { // 自由调拨
//                show(Ware_Transfer_MainActivity::class.java, null) // 直接调拨单
                show(Ware_ZY_Transfer_MainActivity::class.java, null)
            }
            R.id.relative5 -> { // 直接调拨
                show(Ware_Transfer_MainActivity::class.java, null) // 直接调拨单
//                show(Ware_TransferOut_MainActivity::class.java, null)
            }
            R.id.relative6 -> { // 物料盘点
                show(StkCountInput_SaoMaActivity::class.java, null)
//                show(Ware_TransferIn_MainActivity::class.java, null)
            }
            R.id.relative7 -> {// 待确认列表
                show(Ware_BillConfirmList_MainActivity::class.java, null)
            }
            R.id.relative8 -> {//
//                show(Ware_BillConfirmList_MainActivity::class.java, null)
            }
        }
    }
}

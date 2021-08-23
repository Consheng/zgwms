package ykk.xc.com.zgwms.set

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import butterknife.OnClick
import kotlinx.android.synthetic.main.aa_main_item6_bind_stock.*
import ykk.xc.com.zgwms.R
import ykk.xc.com.zgwms.basics.Organization_DialogActivity
import ykk.xc.com.zgwms.basics.Stock_DialogActivity
import ykk.xc.com.zgwms.bean.User
import ykk.xc.com.zgwms.bean.k3Bean.Organization_K3
import ykk.xc.com.zgwms.bean.k3Bean.Stock_K3
import ykk.xc.com.zgwms.comm.BaseActivity
import ykk.xc.com.zgwms.comm.Comm

/**
 * 绑定业务的默认仓库
 */
class BindDefaultStockActivity : BaseActivity() {

    companion object {
        private val SEL_ORG = 1
        private val SEL_PUR_STOCK = 10
        private val SEL_PUR_STOCK_RED = 11
        private val SEL_PROD_STOCK = 12
        private val SEL_SAL_OUT_STOCK = 13
        private val SEL_SAL_OUT_STOCK_RED = 14
        private val SEL_OTHER_IN_STOCK = 15
        private val SEL_OTHER_OUT_STOCK = 16
        private val SEL_ZYDB_IN_STOCK = 30
        private val SEL_ZYDB_OUT_STOCK = 31
        private val SEL_FBSDR_IN_STOCK = 40
        private val SEL_FBSDR_OUT_STOCK = 41
        private val SEL_FBSDC_IN_STOCK = 50
        private val SEL_FBSDC_OUT_STOCK = 51

        private val PUR_STOCK = "BIND_PUR_STOCK"
        private val PUR_STOCK_RED = "BIND_PUR_STOCK_RED"
        private val PROD_STOCK = "BIND_PROD_STOCK"
        private val SAL_OUT_STOCK = "BIND_SAL_OUT_STOCK"
        private val SAL_OUT_STOCK_RED = "BIND_SAL_OUT_STOCK_RED"
        private val OTHER_IN_STOCK = "BIND_OTHER_IN_STOCK"
        private val OTHER_OUT_STOCK = "BIND_OTHER_OUT_STOCK"
        private val ZYDB_IN_STOCK = "BIND_ZYDB_IN_STOCK"
        private val ZYDB_OUT_STOCK = "BIND_ZYDB_OUT_STOCK"
        private val FBSDR_IN_STOCK = "BIND_FBSDR_IN_STOCK"
        private val FBSDR_OUT_STOCK = "BIND_FBSDR_OUT_STOCK"
        private val FBSDC_IN_STOCK = "BIND_FBSDC_IN_STOCK"
        private val FBSDC_OUT_STOCK = "BIND_FBSDC_OUT_STOCK"
    }

    private val context = this
    private var organization :Organization_K3? = null
    private var user: User? = null

    override fun setLayoutResID(): Int {
        return R.layout.aa_main_item6_bind_stock
    }

    override fun initView() {
        getUserInfo()

        if(user!!.organization == null) {
            toasts("登陆的用户没有维护组织，请在WMS维护！3秒后自动关闭...")
            context!!.finish()
            return
        }
        tv_orgSel.text = user!!.organization.fname
        organization = user!!.organization

        // 显示仓库信息
        showLocalStockGroup()
    }

    override fun initData() {
    }

    // 监听事件
    @OnClick(R.id.btn_close, R.id.btn_save, R.id.btn_clear, R.id.tv_orgSel,
            R.id.tv_purStock, R.id.tv_purStock_red, R.id.tv_prodStock, R.id.tv_salOutStock, R.id.tv_salOutStock_red, R.id.tv_otherInStock, R.id.tv_otherOutStock,
            R.id.tv_zydbInStock, R.id.tv_zydbOutStock, R.id.tv_fbsdrInStock, R.id.tv_fbsdrOutStock, R.id.tv_fbsdcInStock, R.id.tv_fbsdcOutStock)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_close -> {
                context.finish()
            }
            R.id.btn_save -> {
            }
            R.id.tv_orgSel -> { // 选择组织
                showForResult(Organization_DialogActivity::class.java, SEL_ORG, null)
            }
            R.id.btn_clear -> {
                val saveDefaultStock = getResStr(R.string.saveDefaultStock)
                val editor = spf(saveDefaultStock).edit()
                editor.clear()
                editor.commit()
                /*spfRemove(PUR_STOCK, saveDefaultStock)
                spfRemove(PUR_STOCK_RED, saveDefaultStock)
                spfRemove(PROD_STOCK, saveDefaultStock)
                spfRemove(SAL_OUT_STOCK, saveDefaultStock)
                spfRemove(SAL_OUT_STOCK_RED, saveDefaultStock)
                spfRemove(OTHER_IN_STOCK, saveDefaultStock)
                spfRemove(OTHER_OUT_STOCK, saveDefaultStock)
                spfRemove(ZYDB_IN_STOCK, saveDefaultStock)
                spfRemove(ZYDB_OUT_STOCK, saveDefaultStock)
                spfRemove(FBSDR_IN_STOCK, saveDefaultStock)
                spfRemove(FBSDR_OUT_STOCK, saveDefaultStock)
                spfRemove(FBSDC_IN_STOCK, saveDefaultStock)
                spfRemove(FBSDC_OUT_STOCK, saveDefaultStock)*/

                tv_purStock.text = ""
                tv_purStock_red.text = ""
                tv_prodStock.text = ""
                tv_salOutStock.text = ""
                tv_salOutStock_red.text = ""
                tv_otherInStock.text = ""
                tv_otherOutStock.text = ""
                tv_zydbInStock.text = ""
                tv_zydbOutStock.text = ""
                tv_fbsdrInStock.text = ""
                tv_fbsdrOutStock.text = ""
                tv_fbsdcInStock.text = ""
                tv_fbsdcOutStock.text = ""
            }
            R.id.tv_purStock -> { // 采购入库
                val bool = checkOrgIsEmpty()
                if(!bool) return

                val bundle = Bundle()
                bundle.putInt("fuseOrgId", organization!!.forgId)
                showForResult(Stock_DialogActivity::class.java, SEL_PUR_STOCK, bundle)
            }
            R.id.tv_purStock_red -> { // 采购退料
                val bool = checkOrgIsEmpty()
                if(!bool) return

                val bundle = Bundle()
                bundle.putInt("fuseOrgId", organization!!.forgId)
                showForResult(Stock_DialogActivity::class.java, SEL_PUR_STOCK_RED, bundle)
            }
            R.id.tv_prodStock -> { // 生产入库
                val bool = checkOrgIsEmpty()
                if(!bool) return

                val bundle = Bundle()
                bundle.putInt("fuseOrgId", organization!!.forgId)
                showForResult(Stock_DialogActivity::class.java, SEL_PROD_STOCK, bundle)
            }
            R.id.tv_salOutStock -> { // 销售出库
                val bool = checkOrgIsEmpty()
                if(!bool) return

                val bundle = Bundle()
                bundle.putInt("fuseOrgId", organization!!.forgId)
                showForResult(Stock_DialogActivity::class.java, SEL_SAL_OUT_STOCK, bundle)
            }
            R.id.tv_salOutStock_red -> { // 销售退货
                val bool = checkOrgIsEmpty()
                if(!bool) return

                val bundle = Bundle()
                bundle.putInt("fuseOrgId", organization!!.forgId)
                showForResult(Stock_DialogActivity::class.java, SEL_SAL_OUT_STOCK_RED, bundle)
            }
            R.id.tv_otherInStock -> { // 其他入库
                val bool = checkOrgIsEmpty()
                if(!bool) return

                val bundle = Bundle()
                bundle.putInt("fuseOrgId", organization!!.forgId)
                showForResult(Stock_DialogActivity::class.java, SEL_OTHER_IN_STOCK, bundle)
            }
            R.id.tv_otherOutStock -> { // 其他出库
                val bool = checkOrgIsEmpty()
                if(!bool) return

                val bundle = Bundle()
                bundle.putInt("fuseOrgId", organization!!.forgId)
                showForResult(Stock_DialogActivity::class.java, SEL_OTHER_OUT_STOCK, bundle)
            }
            R.id.tv_zydbInStock -> { // 自由调拨（调入仓库）
                val bool = checkOrgIsEmpty()
                if(!bool) return

                val bundle = Bundle()
                bundle.putInt("fuseOrgId", organization!!.forgId)
                showForResult(Stock_DialogActivity::class.java, SEL_ZYDB_IN_STOCK, bundle)
            }
            R.id.tv_zydbOutStock -> { // 自由调拨（调出仓库）
                val bool = checkOrgIsEmpty()
                if(!bool) return

                val bundle = Bundle()
                bundle.putInt("fuseOrgId", organization!!.forgId)
                showForResult(Stock_DialogActivity::class.java, SEL_ZYDB_OUT_STOCK, bundle)
            }
            R.id.tv_fbsdrInStock -> { // 分步式调入（调入仓库）
                val bool = checkOrgIsEmpty()
                if(!bool) return

                val bundle = Bundle()
                bundle.putInt("fuseOrgId", organization!!.forgId)
                showForResult(Stock_DialogActivity::class.java, SEL_FBSDR_IN_STOCK, bundle)
            }
            R.id.tv_fbsdrOutStock -> { // 分步式调入（调出仓库）
                val bool = checkOrgIsEmpty()
                if(!bool) return

                val bundle = Bundle()
                bundle.putInt("fuseOrgId", organization!!.forgId)
                showForResult(Stock_DialogActivity::class.java, SEL_FBSDR_OUT_STOCK, bundle)
            }
            R.id.tv_fbsdcInStock -> { // 分步式调出（调出入库）
                val bool = checkOrgIsEmpty()
                if(!bool) return

                val bundle = Bundle()
                bundle.putInt("fuseOrgId", organization!!.forgId)
                showForResult(Stock_DialogActivity::class.java, SEL_FBSDC_IN_STOCK, bundle)
            }
            R.id.tv_fbsdcOutStock -> { // 分步式调出（调出仓库）
                val bool = checkOrgIsEmpty()
                if(!bool) return

                val bundle = Bundle()
                bundle.putInt("fuseOrgId", organization!!.forgId)
                showForResult(Stock_DialogActivity::class.java, SEL_FBSDC_OUT_STOCK, bundle)
            }
        }
    }

    /**
     * 检查是否选择组织
     */
    private fun checkOrgIsEmpty():Boolean {
        if(organization == null) {
            Comm.showWarnDialog(context,"请选择仓库的（使用组织）！")
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                SEL_ORG -> { // 发货组织
                    organization = data!!.getSerializableExtra("obj") as Organization_K3
                    tv_orgSel.text = organization!!.fname
                    // 显示仓库信息
                    showLocalStockGroup()
                }
                SEL_PUR_STOCK -> { // 采购入库（仓库）  返回
                    val stock = data!!.getSerializableExtra("obj") as Stock_K3
                    tv_purStock.text = stock.fname
                    saveLocalStockGroup(stock, PUR_STOCK)
                }
                SEL_PUR_STOCK_RED -> { // 采购退料（仓库）  返回
                    val stock = data!!.getSerializableExtra("obj") as Stock_K3
                    tv_purStock_red.text = stock.fname
                    saveLocalStockGroup(stock, PUR_STOCK_RED)
                }
                SEL_PROD_STOCK -> { // 生产入库（仓库）  返回
                    val stock = data!!.getSerializableExtra("obj") as Stock_K3
                    tv_prodStock.text = stock.fname
                    saveLocalStockGroup(stock, PROD_STOCK)
                }
                SEL_SAL_OUT_STOCK -> { // 销售出库（仓库）  返回
                    val stock = data!!.getSerializableExtra("obj") as Stock_K3
                    tv_salOutStock.text = stock.fname
                    saveLocalStockGroup(stock, SAL_OUT_STOCK)
                }
                SEL_SAL_OUT_STOCK_RED -> { // 销售出库（仓库）  返回
                    val stock = data!!.getSerializableExtra("obj") as Stock_K3
                    tv_salOutStock_red.text = stock.fname
                    saveLocalStockGroup(stock, SAL_OUT_STOCK_RED)
                }
                SEL_OTHER_IN_STOCK -> { // 其他入库（仓库）  返回
                    val stock = data!!.getSerializableExtra("obj") as Stock_K3
                    tv_otherInStock.text = stock.fname
                    saveLocalStockGroup(stock, OTHER_IN_STOCK)
                }
                SEL_OTHER_OUT_STOCK -> { // 其他出库（仓库）  返回
                    val stock = data!!.getSerializableExtra("obj") as Stock_K3
                    tv_otherOutStock.text = stock.fname
                    saveLocalStockGroup(stock, OTHER_OUT_STOCK)
                }
                SEL_ZYDB_IN_STOCK -> { // 自由调拨（调入仓库）  返回
                    val stock = data!!.getSerializableExtra("obj") as Stock_K3
                    if(stock.fname.equals(getValues(tv_zydbOutStock))) {
                        Comm.showWarnDialog(context,"自由调拨（调入仓库）和（调出仓库）不能一样！")
                        return
                    }
                    tv_zydbInStock.text = stock.fname
                    saveLocalStockGroup(stock, ZYDB_IN_STOCK)
                }
                SEL_ZYDB_OUT_STOCK -> { // 自由调拨（调出仓库）  返回
                    val stock = data!!.getSerializableExtra("obj") as Stock_K3
                    if(stock.fname.equals(getValues(tv_zydbInStock))) {
                        Comm.showWarnDialog(context,"自由调拨（调出仓库）和（调入仓库）不能一样！")
                        return
                    }
                    tv_zydbOutStock.text = stock.fname
                    saveLocalStockGroup(stock, ZYDB_OUT_STOCK)
                }
                SEL_FBSDR_IN_STOCK -> { // 分步式调入（调入仓库）  返回
                    val stock = data!!.getSerializableExtra("obj") as Stock_K3
                    if(stock.fname.equals(getValues(tv_fbsdrOutStock))) {
                        Comm.showWarnDialog(context,"分步式调入（调入仓库）和（调出仓库）不能一样！")
                        return
                    }
                    tv_fbsdrInStock.text = stock.fname
                    saveLocalStockGroup(stock, FBSDR_IN_STOCK)
                }
                SEL_FBSDR_OUT_STOCK -> { // 分步式调入（调出仓库）  返回
                    val stock = data!!.getSerializableExtra("obj") as Stock_K3
                    if(stock.fname.equals(getValues(tv_fbsdrInStock))) {
                        Comm.showWarnDialog(context,"分步式调入（调出仓库）和（调入仓库）不能一样！")
                        return
                    }
                    tv_fbsdrOutStock.text = stock.fname
                    saveLocalStockGroup(stock, FBSDR_OUT_STOCK)
                }
                SEL_FBSDC_IN_STOCK -> { // 分步式调出（调入仓库）  返回
                    val stock = data!!.getSerializableExtra("obj") as Stock_K3
                    if(stock.fname.equals(getValues(tv_fbsdcOutStock))) {
                        Comm.showWarnDialog(context,"分步式调出（调入仓库）和（调出仓库）不能一样！")
                        return
                    }
                    tv_fbsdcInStock.text = stock.fname
                    saveLocalStockGroup(stock, FBSDC_IN_STOCK)
                }
                SEL_FBSDC_OUT_STOCK -> {// 分步式调出（调出仓库）  返回
                    val stock = data!!.getSerializableExtra("obj") as Stock_K3
                    if(stock.fname.equals(getValues(tv_fbsdcInStock))) {
                        Comm.showWarnDialog(context,"分步式调出（调出仓库）和（调入仓库）不能一样！")
                        return
                    }
                    tv_fbsdcOutStock.text = stock.fname
                    saveLocalStockGroup(stock, FBSDC_OUT_STOCK)
                }
            }
        }
    }

    /**
     * 保存仓库信息到本地
     */
    private fun saveLocalStockGroup(stock: Stock_K3, key: String) {
        val saveDefaultStock = getResStr(R.string.saveDefaultStock)

        // 对象保存到xml
        saveObjectToXml(stock, organization!!.fnumber+"_"+key, saveDefaultStock)
    }

    /**
     * 显示保存本地的仓库信息
     */
    private fun showLocalStockGroup() {
        // 显示记录的本地仓库
        val saveDefaultStock = getResStr(R.string.saveDefaultStock)
        val spfStock = spf(saveDefaultStock)
        val orgNo = organization!!.fnumber

        // 先清空
        tv_purStock.text = ""
        tv_purStock_red.text = ""
        tv_prodStock.text = ""
        tv_salOutStock.text = ""
        tv_salOutStock_red.text = ""
        tv_otherInStock.text = ""
        tv_otherOutStock.text = ""
        tv_zydbInStock.text = ""
        tv_zydbOutStock.text = ""
        tv_fbsdrInStock.text = ""
        tv_fbsdrOutStock.text = ""
        tv_fbsdcInStock.text = ""
        tv_fbsdcOutStock.text = ""

        if (spfStock.contains(orgNo+"_"+PUR_STOCK)) {
            val stock = showObjectByXml(Stock_K3::class.java, orgNo+"_"+PUR_STOCK, saveDefaultStock)
            tv_purStock.text = stock!!.fname
        }
        if (spfStock.contains(orgNo+"_"+PUR_STOCK_RED)) {
            val stock = showObjectByXml(Stock_K3::class.java, orgNo+"_"+PUR_STOCK_RED, saveDefaultStock)
            tv_purStock_red.text = stock!!.fname
        }
        if (spfStock.contains(orgNo+"_"+PROD_STOCK)) {
            val stock = showObjectByXml(Stock_K3::class.java, orgNo+"_"+PROD_STOCK, saveDefaultStock)
            tv_prodStock.text = stock!!.fname
        }
        if (spfStock.contains(orgNo+"_"+SAL_OUT_STOCK)) {
            val stock = showObjectByXml(Stock_K3::class.java, orgNo+"_"+SAL_OUT_STOCK, saveDefaultStock)
            tv_salOutStock.text = stock!!.fname
        }
        if (spfStock.contains(orgNo+"_"+SAL_OUT_STOCK_RED)) {
            val stock = showObjectByXml(Stock_K3::class.java, orgNo+"_"+SAL_OUT_STOCK_RED, saveDefaultStock)
            tv_salOutStock_red.text = stock!!.fname
        }
        if (spfStock.contains(orgNo+"_"+OTHER_IN_STOCK)) {
            val stock = showObjectByXml(Stock_K3::class.java, orgNo+"_"+OTHER_IN_STOCK, saveDefaultStock)
            tv_otherInStock.text = stock!!.fname
        }
        if (spfStock.contains(orgNo+"_"+OTHER_OUT_STOCK)) {
            val stock = showObjectByXml(Stock_K3::class.java, orgNo+"_"+OTHER_OUT_STOCK, saveDefaultStock)
            tv_otherOutStock.text = stock!!.fname
        }
        if (spfStock.contains(orgNo+"_"+ZYDB_IN_STOCK)) {
            val stock = showObjectByXml(Stock_K3::class.java, orgNo+"_"+ZYDB_IN_STOCK, saveDefaultStock)
            tv_zydbInStock.text = stock!!.fname
        }
        if (spfStock.contains(orgNo+"_"+ZYDB_OUT_STOCK)) {
            val stock = showObjectByXml(Stock_K3::class.java, orgNo+"_"+ZYDB_OUT_STOCK, saveDefaultStock)
            tv_zydbOutStock.text = stock!!.fname
        }
        if (spfStock.contains(orgNo+"_"+FBSDR_IN_STOCK)) {
            val stock = showObjectByXml(Stock_K3::class.java, orgNo+"_"+FBSDR_IN_STOCK, saveDefaultStock)
            tv_fbsdrInStock.text = stock!!.fname
        }
        if (spfStock.contains(orgNo+"_"+FBSDR_OUT_STOCK)) {
            val stock = showObjectByXml(Stock_K3::class.java, orgNo+"_"+FBSDR_OUT_STOCK, saveDefaultStock)
            tv_fbsdrOutStock.text = stock!!.fname
        }
        if (spfStock.contains(orgNo+"_"+FBSDC_IN_STOCK)) {
            val stock = showObjectByXml(Stock_K3::class.java, orgNo+"_"+FBSDC_IN_STOCK, saveDefaultStock)
            tv_fbsdcInStock.text = stock!!.fname
        }
        if (spfStock.contains(orgNo+"_"+FBSDC_OUT_STOCK)) {
            val stock = showObjectByXml(Stock_K3::class.java, orgNo+"_"+FBSDC_OUT_STOCK, saveDefaultStock)
            tv_fbsdcOutStock.text = stock!!.fname
        }
    }


    /**
     * 得到用户对象
     */
    private fun getUserInfo() {
        if (user == null) user = showUserByXml()
    }
}

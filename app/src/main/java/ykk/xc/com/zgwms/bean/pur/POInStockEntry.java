package ykk.xc.com.zgwms.bean.pur;

import java.io.Serializable;

import ykk.xc.com.zgwms.bean.Unit;
import ykk.xc.com.zgwms.bean.k3Bean.ICItem;

/**
 * @Description:收料通知单单分录
 *
 */
public class POInStockEntry implements Serializable {
	/* 分录内码 */
	private int fdetailid;
	/* 采购订单内码 */
	private int finterid;
	/* 分录号 */
	private int fentryid;
	/* 物料内码 */
	private int fitemid;
	/* 订货数量 */
	private double fqty;
	/* 到货数量 */
	private double fcommitqty;
	/* 单价 */
	private double fprice;
	/* 金额 */
	private double famount;
	/* 备注 */
	private String fnote;
	/* 单位内码 */
	private int funitid;
	/* 源单内码id */
	private int fsourceid;
	/* 源单分录id */
	private int fsourceEntryid;
	/* 源单分录号 */
	private String fsourceBillNo;

	// 收料主表对象
	private POInStock poInStock;
	private ICItem icItem;
	private Unit unit;

	// 临时字段，不存表
	private double useableQty; // 可用数
	private double realQty; // 实际数
	private boolean check; // 是否选中

	public POInStockEntry() {
	}

	public int getFdetailid() {
		return fdetailid;
	}

	public void setFdetailid(int fdetailid) {
		this.fdetailid = fdetailid;
	}

	public int getFinterid() {
		return finterid;
	}

	public void setFinterid(int finterid) {
		this.finterid = finterid;
	}

	public int getFentryid() {
		return fentryid;
	}

	public void setFentryid(int fentryid) {
		this.fentryid = fentryid;
	}

	public int getFitemid() {
		return fitemid;
	}

	public void setFitemid(int fitemid) {
		this.fitemid = fitemid;
	}

	public double getFqty() {
		return fqty;
	}

	public void setFqty(double fqty) {
		this.fqty = fqty;
	}

	public double getFcommitqty() {
		return fcommitqty;
	}

	public void setFcommitqty(double fcommitqty) {
		this.fcommitqty = fcommitqty;
	}

	public double getFprice() {
		return fprice;
	}

	public void setFprice(double fprice) {
		this.fprice = fprice;
	}

	public double getFamount() {
		return famount;
	}

	public void setFamount(double famount) {
		this.famount = famount;
	}

	public String getFnote() {
		return fnote;
	}

	public void setFnote(String fnote) {
		this.fnote = fnote;
	}

	public int getFunitid() {
		return funitid;
	}

	public void setFunitid(int funitid) {
		this.funitid = funitid;
	}

	public int getFsourceEntryid() {
		return fsourceEntryid;
	}

	public void setFsourceEntryid(int fsourceEntryid) {
		this.fsourceEntryid = fsourceEntryid;
	}

	public String getFsourceBillNo() {
		return fsourceBillNo;
	}

	public void setFsourceBillNo(String fsourceBillNo) {
		this.fsourceBillNo = fsourceBillNo;
	}

	public int getFsourceid() {
		return fsourceid;
	}

	public void setFsourceid(int fsourceid) {
		this.fsourceid = fsourceid;
	}

	public POInStock getPoInStock() {
		return poInStock;
	}

	public void setPoInStock(POInStock poInStock) {
		this.poInStock = poInStock;
	}

	public ICItem getIcItem() {
		return icItem;
	}

	public void setIcItem(ICItem icItem) {
		this.icItem = icItem;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public double getUseableQty() {
		return useableQty;
	}

	public void setUseableQty(double useableQty) {
		this.useableQty = useableQty;
	}

	public double getRealQty() {
		return realQty;
	}

	public void setRealQty(double realQty) {
		this.realQty = realQty;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}


}
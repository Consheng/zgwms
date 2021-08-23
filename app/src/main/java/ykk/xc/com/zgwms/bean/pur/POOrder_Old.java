package ykk.xc.com.zgwms.bean.pur;

import java.io.Serializable;

import ykk.xc.com.zgwms.bean.Department;
import ykk.xc.com.zgwms.bean.Supplier;

/**
 * @Description:采购订单
 */
public class POOrder_Old implements Serializable {

	/*采购订单内码 */
	private int finterid;
	/*采购订单号 */
	private String fbillno;
	/* 单据类型 */
	private int ftrantype;
	/* 供应商内码 */
	private int fsupplyid;
	/* 部门 */
	private int fdeptid;
	/* 业务员 */
	private int fempid;
	/* 单据日期 */
	private String fdate;
	/* 审核人 */
	private int fcheckerid;
	/* 制单人 */
	private int fbillerid;
	/* 审核日期 */
	private String fcheckDate;
	/* 单据状态 */
	private int fstatus;
	/* 打印次数 */
	private int fprintcount;
	/* 关闭状态 */
	private int fclosed;

	/* 供应商 */
	private Supplier supplier;
	/* 部门 */
	private Department department;

	//临时字段，不存表
	private double sumQty;	//采购订单计算的总数
	private boolean wmsUploadStatus; // WMS上传状态

	public POOrder_Old() {
		super();
	}

	public int getFinterid() {
		return finterid;
	}

	public void setFinterid(int finterid) {
		this.finterid = finterid;
	}

	public String getFbillno() {
		return fbillno;
	}

	public void setFbillno(String fbillno) {
		this.fbillno = fbillno;
	}

	public int getFtrantype() {
		return ftrantype;
	}

	public void setFtrantype(int ftrantype) {
		this.ftrantype = ftrantype;
	}

	public int getFsupplyid() {
		return fsupplyid;
	}

	public void setFsupplyid(int fsupplyid) {
		this.fsupplyid = fsupplyid;
	}

	public int getFdeptid() {
		return fdeptid;
	}

	public void setFdeptid(int fdeptid) {
		this.fdeptid = fdeptid;
	}

	public int getFempid() {
		return fempid;
	}

	public void setFempid(int fempid) {
		this.fempid = fempid;
	}

	public String getFdate() {
		return fdate;
	}

	public void setFdate(String fdate) {
		this.fdate = fdate;
	}

	public int getFcheckerid() {
		return fcheckerid;
	}

	public void setFcheckerid(int fcheckerid) {
		this.fcheckerid = fcheckerid;
	}

	public int getFbillerid() {
		return fbillerid;
	}

	public void setFbillerid(int fbillerid) {
		this.fbillerid = fbillerid;
	}

	public String getFcheckDate() {
		return fcheckDate;
	}

	public void setFcheckDate(String fcheckDate) {
		this.fcheckDate = fcheckDate;
	}

	public int getFstatus() {
		return fstatus;
	}

	public void setFstatus(int fstatus) {
		this.fstatus = fstatus;
	}

	public int getFprintcount() {
		return fprintcount;
	}

	public void setFprintcount(int fprintcount) {
		this.fprintcount = fprintcount;
	}

	public int getFclosed() {
		return fclosed;
	}

	public void setFclosed(int fclosed) {
		this.fclosed = fclosed;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public double getSumQty() {
		return sumQty;
	}

	public void setSumQty(double sumQty) {
		this.sumQty = sumQty;
	}

	public boolean isWmsUploadStatus() {
		return wmsUploadStatus;
	}

	public void setWmsUploadStatus(boolean wmsUploadStatus) {
		this.wmsUploadStatus = wmsUploadStatus;
	}

}
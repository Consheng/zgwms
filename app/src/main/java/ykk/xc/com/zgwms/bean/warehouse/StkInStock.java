package ykk.xc.com.zgwms.bean.warehouse;

import java.io.Serializable;

import ykk.xc.com.zgwms.bean.k3Bean.Operator_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Organization_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Supplier_K3;

/**
 * 采购入库表
 * @author Administrator
 *
 */
public class StkInStock implements Serializable{
	private static final long serialVersionUID = 1L;

	private int fid;					// 单据内码id
	private String fbillTypeId;			// 单据类型id
	private String billType_fnumber;	// 单据类型代码
	private String billType_fname;		// 单据类型名称
	private String fbillNo;  			// 单据编号
	private String fdate;				// 单据日期
	private int fsupplierId;			// 供应商id
	private int fstockOrgId;			// 收料组织id
	private int fpurChaseOrgId;			// 采购组织id
	private int fstockerId;				// 仓管员id
	private String fdocumentStatus;		// 单据状态（A:新建， Z:暂存， B:审核中 ，C:已审核 ，D:重新审核 ）
	private String fcancelStatus;		// 作废标志：A：未作废 B：已作废
	private String fbusinessType;		// 业务类型：普通:CG	委外:WW

	private Supplier_K3 supplier;
	private Organization_K3 recOrg;
	private Organization_K3 purOrg;
	private Operator_K3 operator;
	private Operator_K3 stockManager;

	// 临时字段，不存表
	private double sumQty;	//采购入库计算的总数
	private boolean wmsUploadStatus; // WMS上传状态
	private boolean check;	// 是否选中

	public StkInStock() {
		super();
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public String getFbillTypeId() {
		return fbillTypeId;
	}

	public void setFbillTypeId(String fbillTypeId) {
		this.fbillTypeId = fbillTypeId;
	}

	public String getFbillNo() {
		return fbillNo;
	}

	public void setFbillNo(String fbillNo) {
		this.fbillNo = fbillNo;
	}

	public String getFdate() {
		return fdate;
	}

	public void setFdate(String fdate) {
		this.fdate = fdate;
	}

	public int getFsupplierId() {
		return fsupplierId;
	}

	public void setFsupplierId(int fsupplierId) {
		this.fsupplierId = fsupplierId;
	}

	public int getFstockOrgId() {
		return fstockOrgId;
	}

	public void setFstockOrgId(int fstockOrgId) {
		this.fstockOrgId = fstockOrgId;
	}

	public int getFpurChaseOrgId() {
		return fpurChaseOrgId;
	}

	public void setFpurChaseOrgId(int fpurChaseOrgId) {
		this.fpurChaseOrgId = fpurChaseOrgId;
	}

	public int getFstockerId() {
		return fstockerId;
	}

	public void setFstockerId(int fstockerId) {
		this.fstockerId = fstockerId;
	}

	public String getFdocumentStatus() {
		return fdocumentStatus;
	}

	public void setFdocumentStatus(String fdocumentStatus) {
		this.fdocumentStatus = fdocumentStatus;
	}

	public String getFcancelStatus() {
		return fcancelStatus;
	}

	public void setFcancelStatus(String fcancelStatus) {
		this.fcancelStatus = fcancelStatus;
	}

	public String getFbusinessType() {
		return fbusinessType;
	}

	public void setFbusinessType(String fbusinessType) {
		this.fbusinessType = fbusinessType;
	}

	public Supplier_K3 getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier_K3 supplier) {
		this.supplier = supplier;
	}

	public Organization_K3 getRecOrg() {
		return recOrg;
	}

	public void setRecOrg(Organization_K3 recOrg) {
		this.recOrg = recOrg;
	}

	public Organization_K3 getPurOrg() {
		return purOrg;
	}

	public void setPurOrg(Organization_K3 purOrg) {
		this.purOrg = purOrg;
	}

	public Operator_K3 getOperator() {
		return operator;
	}

	public void setOperator(Operator_K3 operator) {
		this.operator = operator;
	}

	public Operator_K3 getStockManager() {
		return stockManager;
	}

	public void setStockManager(Operator_K3 stockManager) {
		this.stockManager = stockManager;
	}

	public String getBillType_fnumber() {
		return billType_fnumber;
	}

	public void setBillType_fnumber(String billType_fnumber) {
		this.billType_fnumber = billType_fnumber;
	}

	public String getBillType_fname() {
		return billType_fname;
	}

	public void setBillType_fname(String billType_fname) {
		this.billType_fname = billType_fname;
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

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

}

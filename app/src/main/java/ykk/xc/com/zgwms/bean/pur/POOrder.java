package ykk.xc.com.zgwms.bean.pur;

import java.io.Serializable;

import ykk.xc.com.zgwms.bean.k3Bean.Department_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Operator_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Organization_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Supplier_K3;

/**
 * 采购订单
 * @author Administrator
 *
 */
public class POOrder implements Serializable {
	private static final long serialVersionUID = 1L;

	private int fid;					// 单据id
	private String fbillTypeId;			// 单据类型
	private String fbillNo;				// 单据编号
	private String fdate;				// 采购日期
	private int fsupplierId;			// 供应商id
	private int fpurChaseOrgId;			// 采购组织id
	private int fpurChaserGroupId;		// 采购组id
	private int fpurChaseDeptId;		// 采购部门id
	private int fpurChaserId;			// 采购员
	private char fdocumentStatus;		// 单据状态：A:新建 Z:暂存 B: 审核中 C: 已审核 D:重新审核
	private char fcloseStatus;			// 关闭状态：A：未关闭 B：已关闭
	private char fcancelStatus;			// 作废标志：A：未作废 B：已作废
	private String fbusinessType;		// 业务类型：普通:CG	委外:WW
	private String fobjectTypeId;		// 业务对象类型：PUR_PurchaseOrder

	private Supplier_K3 supplier;
	private Organization_K3 purOrg;
	private Department_K3 purDept;
	private Operator_K3 operator;

	// 临时字段
	private String fbillTypeNumber;
	private String fbillTypeName;
	private double sumQty;	//采购订单计算的总数
	private boolean wmsUploadStatus; // WMS上传状态
	private boolean check;	// 是否选中

	public POOrder() {
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

	public int getFpurChaseOrgId() {
		return fpurChaseOrgId;
	}

	public void setFpurChaseOrgId(int fpurChaseOrgId) {
		this.fpurChaseOrgId = fpurChaseOrgId;
	}

	public int getFpurChaserGroupId() {
		return fpurChaserGroupId;
	}

	public void setFpurChaserGroupId(int fpurChaserGroupId) {
		this.fpurChaserGroupId = fpurChaserGroupId;
	}

	public int getFpurChaseDeptId() {
		return fpurChaseDeptId;
	}

	public void setFpurChaseDeptId(int fpurChaseDeptId) {
		this.fpurChaseDeptId = fpurChaseDeptId;
	}

	public int getFpurChaserId() {
		return fpurChaserId;
	}

	public void setFpurChaserId(int fpurChaserId) {
		this.fpurChaserId = fpurChaserId;
	}

	public char getFdocumentStatus() {
		return fdocumentStatus;
	}

	public void setFdocumentStatus(char fdocumentStatus) {
		this.fdocumentStatus = fdocumentStatus;
	}

	public char getFcloseStatus() {
		return fcloseStatus;
	}

	public void setFcloseStatus(char fcloseStatus) {
		this.fcloseStatus = fcloseStatus;
	}

	public char getFcancelStatus() {
		return fcancelStatus;
	}

	public void setFcancelStatus(char fcancelStatus) {
		this.fcancelStatus = fcancelStatus;
	}

	public String getFbusinessType() {
		return fbusinessType;
	}

	public void setFbusinessType(String fbusinessType) {
		this.fbusinessType = fbusinessType;
	}

	public String getFobjectTypeId() {
		return fobjectTypeId;
	}

	public void setFobjectTypeId(String fobjectTypeId) {
		this.fobjectTypeId = fobjectTypeId;
	}

	public Supplier_K3 getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier_K3 supplier) {
		this.supplier = supplier;
	}

	public Organization_K3 getPurOrg() {
		return purOrg;
	}

	public void setPurOrg(Organization_K3 purOrg) {
		this.purOrg = purOrg;
	}

	public Department_K3 getPurDept() {
		return purDept;
	}

	public void setPurDept(Department_K3 purDept) {
		this.purDept = purDept;
	}

	public Operator_K3 getOperator() {
		return operator;
	}

	public void setOperator(Operator_K3 operator) {
		this.operator = operator;
	}

	public String getFbillTypeNumber() {
		return fbillTypeNumber;
	}

	public void setFbillTypeNumber(String fbillTypeNumber) {
		this.fbillTypeNumber = fbillTypeNumber;
	}

	public String getFbillTypeName() {
		return fbillTypeName;
	}

	public void setFbillTypeName(String fbillTypeName) {
		this.fbillTypeName = fbillTypeName;
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

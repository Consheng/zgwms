package ykk.xc.com.zgwms.bean.prod;

import java.io.Serializable;

import ykk.xc.com.zgwms.bean.k3Bean.Department_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Operator_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Organization_K3;

/**
 * 生产订单表头
 * @author Administrator
 *
 */
public class PrdMo implements Serializable {
	private static final long serialVersionUID = 1L;

	private int fid;
	private String fbillNo;				// 单据编号
	private String fbillType;			// 单据类型
	private String billType_fnumber;	// 单据类型代码
	private String billType_fname;		// 单据类型名称
	private String fdate;				// 单据日期
	private int fprdOrgId;				// 生产组织
	private int fplannerId;				// 计划员
	private String fdocumentStatus;		// 单据状态（A:新建， Z:暂存， B:审核中 ，C:已审核 ，D:重新审核 ）
	private String fcancelStatus;		// 作废标志：A：未作废 B：已作废

	private Organization_K3 prdOrg;
	private Operator_K3 operator;

	// 临时字段，不存表
	private double sumQty;	//采购入库计算的总数
	private boolean wmsUploadStatus; // WMS上传状态

	public PrdMo() {
		super();
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public String getFbillNo() {
		return fbillNo;
	}

	public void setFbillNo(String fbillNo) {
		this.fbillNo = fbillNo;
	}

	public String getFbillType() {
		return fbillType;
	}

	public void setFbillType(String fbillType) {
		this.fbillType = fbillType;
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

	public String getFdate() {
		return fdate;
	}

	public void setFdate(String fdate) {
		this.fdate = fdate;
	}

	public int getFprdOrgId() {
		return fprdOrgId;
	}

	public void setFprdOrgId(int fprdOrgId) {
		this.fprdOrgId = fprdOrgId;
	}

	public int getFplannerId() {
		return fplannerId;
	}

	public void setFplannerId(int fplannerId) {
		this.fplannerId = fplannerId;
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

	public Organization_K3 getPrdOrg() {
		return prdOrg;
	}

	public void setPrdOrg(Organization_K3 prdOrg) {
		this.prdOrg = prdOrg;
	}

	public Operator_K3 getOperator() {
		return operator;
	}

	public void setOperator(Operator_K3 operator) {
		this.operator = operator;
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

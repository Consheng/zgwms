package ykk.xc.com.zgwms.bean.warehouse;

import java.io.Serializable;

import ykk.xc.com.zgwms.bean.k3Bean.Organization_K3;

/**
 * 库存物料盘点作业单据头表
 * @author Administrator
 *
 */
public class StkCountInput implements Serializable {
	private static final long serialVersionUID = 1L;

	private int fid;					// 内码id
	private String fbillNo;				// 单据编号
	private String fdate;				// 日期
	private String fbackupDate;			// 盘点日期
	private int fstockOrgId;			// 调出库存组织id
	private int fschemeId;				// 盘点来源内码id
	private String fschemeNo;			// 盘点来源编码
	private String fschemeName;			// 盘点来源名称
	private int fsource;				// 来源（引入:'0'，盘点方案:'1'，周期盘点计划:'2'）
	private char fdocumentStatus;		// 单据状态：A:新建 Z:暂存 B: 审核中 C: 已审核 D:重新审核

	private Organization_K3 stockOrg;

	// 临时字段
	private double sumQty;					// 分录总数
	private boolean wmsUploadStatus; 		// WMS上传状态
	
	public StkCountInput() {
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

	public String getFdate() {
		return fdate;
	}

	public void setFdate(String fdate) {
		this.fdate = fdate;
	}

	public String getFbackupDate() {
		return fbackupDate;
	}

	public void setFbackupDate(String fbackupDate) {
		this.fbackupDate = fbackupDate;
	}

	public int getFstockOrgId() {
		return fstockOrgId;
	}

	public void setFstockOrgId(int fstockOrgId) {
		this.fstockOrgId = fstockOrgId;
	}

	public int getFschemeId() {
		return fschemeId;
	}

	public void setFschemeId(int fschemeId) {
		this.fschemeId = fschemeId;
	}

	public String getFschemeNo() {
		return fschemeNo;
	}

	public void setFschemeNo(String fschemeNo) {
		this.fschemeNo = fschemeNo;
	}

	public String getFschemeName() {
		return fschemeName;
	}

	public void setFschemeName(String fschemeName) {
		this.fschemeName = fschemeName;
	}

	public int getFsource() {
		return fsource;
	}

	public void setFsource(int fsource) {
		this.fsource = fsource;
	}

	public char getFdocumentStatus() {
		return fdocumentStatus;
	}

	public void setFdocumentStatus(char fdocumentStatus) {
		this.fdocumentStatus = fdocumentStatus;
	}

	public Organization_K3 getStockOrg() {
		return stockOrg;
	}

	public void setStockOrg(Organization_K3 stockOrg) {
		this.stockOrg = stockOrg;
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

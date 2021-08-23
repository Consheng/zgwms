package ykk.xc.com.zgwms.bean.warehouse;

import java.io.Serializable;

import ykk.xc.com.zgwms.bean.k3Bean.Organization_K3;

/**
 * 分步式调出单主表
 * @author Administrator
 *
 */
public class StkTransferOut implements Serializable {
	private static final long serialVersionUID = 1L;

	private int fid;						// 内码id
	private int fstockOrgId;				// 调出库存组织id
	private int fstockInOrgId;				// 调入库存组织
	private String fbillTypeId;				// 单据类型id
	private String billType_fnumber;		// 单据类型代码
	private String billType_fname;			// 单据类型名称
	private String fbillNo;					// 单据编号
	private String ftransferBizType;		// 调拨类型（InnerOrgTransfer:组织内调拨，OverOrgTransfer:跨组织调拨）
	private String ftransferDirect;			// 调拨方向（GENERAL:普通，RETURN:退货）
	private char fvestonWay;				// 在途归属（A:调出方，B:调入方）
	private String fownerTypeInId;			// 调入货主类型id
	private int fownerInId;					// 调入货主id
	private String ownerIn_fnumber;			// 调入货主代码
	private String ownerIn_fname;			// 调入货主名称
	private String fownerTypeOutId;			// 调出货主类型id
	private int fownerOutId;				// 调出货主id
	private String ownerOut_fnumber;		// 调出货主代码
	private String ownerOut_fname;			// 调出货主名称
	private String fdate;					// 日期
	private char fdocumentStatus;			// 单据状态：A:新建 Z:暂存 B: 审核中 C: 已审核 D:重新审核
	private String fcreateDate;				// 创建日期
	private String fcancelStatus;			// 作废标志：A：未作废 B：已作废
	private String fobjectTypeId;			// 业务对象类型：STK_TRANSFEROUT
	private String fbizType;				// 业务类型：（NORMAL）

	private Organization_K3 stockOutOrg;
	private Organization_K3 stockInOrg;

	// 临时字段
	private double sumQty;					// 分录总数
	private boolean wmsUploadStatus; 		// WMS上传状态

	public StkTransferOut() {
		super();
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public int getFstockOrgId() {
		return fstockOrgId;
	}

	public void setFstockOrgId(int fstockOrgId) {
		this.fstockOrgId = fstockOrgId;
	}

	public int getFstockInOrgId() {
		return fstockInOrgId;
	}

	public void setFstockInOrgId(int fstockInOrgId) {
		this.fstockInOrgId = fstockInOrgId;
	}

	public String getFbillTypeId() {
		return fbillTypeId;
	}

	public void setFbillTypeId(String fbillTypeId) {
		this.fbillTypeId = fbillTypeId;
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

	public String getFbillNo() {
		return fbillNo;
	}

	public void setFbillNo(String fbillNo) {
		this.fbillNo = fbillNo;
	}

	public String getFtransferBizType() {
		return ftransferBizType;
	}

	public void setFtransferBizType(String ftransferBizType) {
		this.ftransferBizType = ftransferBizType;
	}

	public String getFtransferDirect() {
		return ftransferDirect;
	}

	public void setFtransferDirect(String ftransferDirect) {
		this.ftransferDirect = ftransferDirect;
	}

	public char getFvestonWay() {
		return fvestonWay;
	}

	public void setFvestonWay(char fvestonWay) {
		this.fvestonWay = fvestonWay;
	}

	public String getFownerTypeInId() {
		return fownerTypeInId;
	}

	public void setFownerTypeInId(String fownerTypeInId) {
		this.fownerTypeInId = fownerTypeInId;
	}

	public int getFownerInId() {
		return fownerInId;
	}

	public void setFownerInId(int fownerInId) {
		this.fownerInId = fownerInId;
	}

	public String getOwnerIn_fnumber() {
		return ownerIn_fnumber;
	}

	public void setOwnerIn_fnumber(String ownerIn_fnumber) {
		this.ownerIn_fnumber = ownerIn_fnumber;
	}

	public String getOwnerIn_fname() {
		return ownerIn_fname;
	}

	public void setOwnerIn_fname(String ownerIn_fname) {
		this.ownerIn_fname = ownerIn_fname;
	}

	public String getFownerTypeOutId() {
		return fownerTypeOutId;
	}

	public void setFownerTypeOutId(String fownerTypeOutId) {
		this.fownerTypeOutId = fownerTypeOutId;
	}

	public int getFownerOutId() {
		return fownerOutId;
	}

	public void setFownerOutId(int fownerOutId) {
		this.fownerOutId = fownerOutId;
	}

	public String getOwnerOut_fnumber() {
		return ownerOut_fnumber;
	}

	public void setOwnerOut_fnumber(String ownerOut_fnumber) {
		this.ownerOut_fnumber = ownerOut_fnumber;
	}

	public String getOwnerOut_fname() {
		return ownerOut_fname;
	}

	public void setOwnerOut_fname(String ownerOut_fname) {
		this.ownerOut_fname = ownerOut_fname;
	}

	public String getFdate() {
		return fdate;
	}

	public void setFdate(String fdate) {
		this.fdate = fdate;
	}

	public char getFdocumentStatus() {
		return fdocumentStatus;
	}

	public void setFdocumentStatus(char fdocumentStatus) {
		this.fdocumentStatus = fdocumentStatus;
	}

	public String getFcreateDate() {
		return fcreateDate;
	}

	public void setFcreateDate(String fcreateDate) {
		this.fcreateDate = fcreateDate;
	}

	public String getFcancelStatus() {
		return fcancelStatus;
	}

	public void setFcancelStatus(String fcancelStatus) {
		this.fcancelStatus = fcancelStatus;
	}

	public String getFobjectTypeId() {
		return fobjectTypeId;
	}

	public void setFobjectTypeId(String fobjectTypeId) {
		this.fobjectTypeId = fobjectTypeId;
	}

	public String getFbizType() {
		return fbizType;
	}

	public void setFbizType(String fbizType) {
		this.fbizType = fbizType;
	}

	public Organization_K3 getStockOutOrg() {
		return stockOutOrg;
	}

	public void setStockOutOrg(Organization_K3 stockOutOrg) {
		this.stockOutOrg = stockOutOrg;
	}

	public Organization_K3 getStockInOrg() {
		return stockInOrg;
	}

	public void setStockInOrg(Organization_K3 stockInOrg) {
		this.stockInOrg = stockInOrg;
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

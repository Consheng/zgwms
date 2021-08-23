package ykk.xc.com.zgwms.bean.warehouse;

import java.io.Serializable;

import ykk.xc.com.zgwms.bean.k3Bean.Customer_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Department_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Operator_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Organization_K3;

/**
 * 销售出库单
 * @author Administrator
 *
 */
public class SalOutStock implements Serializable {
	private static final long serialVersionUID = 1L;

	private int fid; 						// 单据id
	private String fbillNo; 				// 单据编号
	private String fbillTypeId; 			// 单据类型
	private String billType_fnumber;		// 单据类型代码
	private String billType_fname;			// 单据类型名称
	private String fdate; 					// 日期
	private int fcustomerId; 				// 客户Id,
	private int fstockOrgId; 				// 销售组织id
	private int fsaleOrgId; 				// 销售组织id
	private int fsaleDeptId;				// 销售部门id
	private int fdeliveryDeptId;			// 发货部门id
	private int fstockerId;					// 仓管员id
	private int fsalesManId;				// 销售员id
	private String fcarriageNo;				// 快递单号
	private int fcreatorId;					// 创建人id
	private String fcreateDate;				// 创建日期
	private String fownerTypeId;			// 货主类型
	private String fheadlocAddress;			// 收件人地址
	private String fheAddeliveryWay;		// 发货方式
	private String fdocumentStatus;			// 单据状态（A:新建， Z:暂存， B:审核中 ，C:已审核 ，D:重新审核 ）
	private String fcancelStatus;			// 作废标志：A：未作废 B：已作废
	private String fbusinessType;			// 业务类型（NORMAL）
	private String fobjectTypeId;			// 业务对象类型：SAL_OUTSTOCK

	private Organization_K3 stockOrg;
	private Organization_K3 saleOrg;
	private Customer_K3 cust;
	private Department_K3 saleDept;
	private Department_K3 deliDept;
	private Operator_K3 saleMan;
	private Operator_K3 stockManager;

	// 临时字段，不存表
	private boolean check;	// 是否选中
	private double sumQty;	//采购入库计算的总数
	private boolean wmsUploadStatus; // WMS上传状态

	public SalOutStock() {
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

	public String getFdate() {
		return fdate;
	}

	public void setFdate(String fdate) {
		this.fdate = fdate;
	}

	public int getFcustomerId() {
		return fcustomerId;
	}

	public void setFcustomerId(int fcustomerId) {
		this.fcustomerId = fcustomerId;
	}

	public int getFstockOrgId() {
		return fstockOrgId;
	}

	public void setFstockOrgId(int fstockOrgId) {
		this.fstockOrgId = fstockOrgId;
	}

	public int getFsaleOrgId() {
		return fsaleOrgId;
	}

	public void setFsaleOrgId(int fsaleOrgId) {
		this.fsaleOrgId = fsaleOrgId;
	}

	public int getFsaleDeptId() {
		return fsaleDeptId;
	}

	public void setFsaleDeptId(int fsaleDeptId) {
		this.fsaleDeptId = fsaleDeptId;
	}

	public int getFdeliveryDeptId() {
		return fdeliveryDeptId;
	}

	public void setFdeliveryDeptId(int fdeliveryDeptId) {
		this.fdeliveryDeptId = fdeliveryDeptId;
	}

	public int getFstockerId() {
		return fstockerId;
	}

	public void setFstockerId(int fstockerId) {
		this.fstockerId = fstockerId;
	}

	public int getFsalesManId() {
		return fsalesManId;
	}

	public void setFsalesManId(int fsalesManId) {
		this.fsalesManId = fsalesManId;
	}

	public String getFcarriageNo() {
		return fcarriageNo;
	}

	public void setFcarriageNo(String fcarriageNo) {
		this.fcarriageNo = fcarriageNo;
	}

	public int getFcreatorId() {
		return fcreatorId;
	}

	public void setFcreatorId(int fcreatorId) {
		this.fcreatorId = fcreatorId;
	}

	public String getFcreateDate() {
		return fcreateDate;
	}

	public void setFcreateDate(String fcreateDate) {
		this.fcreateDate = fcreateDate;
	}

	public String getFownerTypeId() {
		return fownerTypeId;
	}

	public void setFownerTypeId(String fownerTypeId) {
		this.fownerTypeId = fownerTypeId;
	}

	public String getFheadlocAddress() {
		return fheadlocAddress;
	}

	public void setFheadlocAddress(String fheadlocAddress) {
		this.fheadlocAddress = fheadlocAddress;
	}

	public String getFheAddeliveryWay() {
		return fheAddeliveryWay;
	}

	public void setFheAddeliveryWay(String fheAddeliveryWay) {
		this.fheAddeliveryWay = fheAddeliveryWay;
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

	public String getFobjectTypeId() {
		return fobjectTypeId;
	}

	public void setFobjectTypeId(String fobjectTypeId) {
		this.fobjectTypeId = fobjectTypeId;
	}

	public Organization_K3 getStockOrg() {
		return stockOrg;
	}

	public void setStockOrg(Organization_K3 stockOrg) {
		this.stockOrg = stockOrg;
	}

	public Organization_K3 getSaleOrg() {
		return saleOrg;
	}

	public void setSaleOrg(Organization_K3 saleOrg) {
		this.saleOrg = saleOrg;
	}

	public Customer_K3 getCust() {
		return cust;
	}

	public void setCust(Customer_K3 cust) {
		this.cust = cust;
	}

	public Department_K3 getSaleDept() {
		return saleDept;
	}

	public void setSaleDept(Department_K3 saleDept) {
		this.saleDept = saleDept;
	}

	public Department_K3 getDeliDept() {
		return deliDept;
	}

	public void setDeliDept(Department_K3 deliDept) {
		this.deliDept = deliDept;
	}

	public Operator_K3 getSaleMan() {
		return saleMan;
	}

	public void setSaleMan(Operator_K3 saleMan) {
		this.saleMan = saleMan;
	}

	public Operator_K3 getStockManager() {
		return stockManager;
	}

	public void setStockManager(Operator_K3 stockManager) {
		this.stockManager = stockManager;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
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

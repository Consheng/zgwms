package ykk.xc.com.zgwms.bean;

import java.io.Serializable;

import ykk.xc.com.zgwms.bean.k3Bean.Customer;
import ykk.xc.com.zgwms.bean.k3Bean.Customer_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Department_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Operator_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Organization_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Supplier_K3;
import ykk.xc.com.zgwms.comm.Comm;

/**
 * Wms 本地的出入库	主表
 * @author Administrator
 */
public class ICStockBill implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private String pdaNo;				// 本地生产的流水号
	/** ------ 单据类型 ------
	 *   QTRK:其他入库，QTCK:其他出库，CGRK:采购入库，CGTL，采购退料，
	 * 	 ZJDBD:直接调拨单，FBSDCD:分步式调出单，FDSDRD:分步式调入单
	 * 	 SCRK:生产订单入库，
	 * 	 SCLL:生产领料，SQDBD:申请调拨单，
	 *	 XSCK:销售出库，XSTH:销售退货单，XSZX:销售装箱出库，
	 *	 WWCK:委外出库，WWRK:委外入库，STRK：受托入库,STLL:受托领料
	 *	 PYRK:盘盈入库，PKCK：盘亏出库
	 *	 DS_XSCK:电商销售出库，DS_XSCK_BTOR:电商销售出库_红字
	 *	 WLPD:物料盘点
	 */
	private String billType;			// 单据类型
	private char billStatus;			// 单据业务状态 (A：创建，B：审核，C：关闭)
	private String fdate;				// 操作日期
	private int ftranType;				// 单据类型（1:采购入库，2:采购退料，10:其他入库，11:其他出库，20:生产入库，30:销售出库，31:销售退货，40:直接调拨单，41:分步式调出单，42:分步式调入单，43:物料盘点）
	/** ------ 源单类型 ------
	 *   采购入库（0：无源单，1：采购订单，2：收料通知单）
	 *   采购退货（0：无源单，1：采购入库单）
	 *   生产入库（1：生产订单）
	 *   分步式调入单（1：分步式调出单）
	 *   销售出库（0：无源单，1：销售订单，2：发货通知单）
	 */
	private int fselTranType;			// 源单类型
	private String fstockDirect;		// 库存方向：（GENERAL:普通，RETURN:退货）
	private String ftransferType;		// 调拨类型：（InnerOrgTransfer:组织内调拨，OverOrgTransfer:跨组织调拨）
	private char fvestonWay;			// 分步式调出单--在途归属：（A:调出方，B:调入方）
	private char fmrMode;				// 采购退料单--退料方式（A:退料补料，B:退料并扣款）
	private int fsupplierId;			// 供应商id
	private int fcustId;				// 客户id
	private int fstockOrgId;			// 调入组织id
	private int fstockOutOrgId;			// 调出组织id
	private int purDeptId;				// 采购部门id
	private int recDeptId;				// 收料部门id
	private String empNumber;			// 采购员代码
	private String empName;				// 采购员名称
	private String stockManagerNumber;	// 仓管员代码
	private String stockManagerName;	// 仓管员名称
	private int childSize;				// 分录个数
	private int combineSalOrderId;		// 拼单id

	private int createUserId;			// 创建人id
	private String createUserName;		// 创建人
	private String createDate;			// 创建日期
	private int passUserId;				// 审核人id
	private String passUserName;		// 审核人
	private String passDate;			// 审核日期
	private int isToK3;					// 是否提交到K3
	private String k3Number;			// k3返回的单号
	private String fbillTypeNumber;		// 单据类型代码
	private String fbusinessTypeNumber;	// 业务类型代码
	private String fownerType;			// 调入货主类型
	private String fownerNumber;		// 调入货主代码
	private String fownerName;			// 调入货主名称
	private String fownerOutType;		// 调出货主类型
	private String fownerOutNumber;		// 调出货主代码
	private String fownerOutName;		// 调出货主名称
	private String deliveryWayNumber;	// 销售出库单--发货方式代码
	private String deliveryWayName;		// 销售出库单--发货方式名称
	private String expressNo;			// 销售出库单--快递单号
	private String receiveAddress;		// 销售出库单--收货地址
	private String receiveContact;		// 销售出库单--收货人
	private String receivePhone;		// 销售出库单--收货人电话
	private String returnReasonNumber;	// 销售退货单--退货原因代码
	private String returnReasonName;	// 销售退货单--退货原因名称

	private Organization_K3 stockOrg;		// 调入组织
	private Organization_K3 stockOutOrg;	// 调出组织
	private Supplier_K3 supplier;			// 供应商对象
	private Department_K3 purDept;			// 采购部门对象
	private Department_K3 recDept;			// 收料部门对象
	private Customer_K3 cust;				// 客户对象
	private User user;

	// 临时字段，不存表
	private boolean showButton; 		// 是否显示操作按钮
	private String summary; 			// 主表摘要
	private String strSourceNo; // 对应的源单单号
	private String outStockName; // 调出仓库
	private int isCommit;	// 是否提交到仓管确认

	public ICStockBill() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPdaNo() {
		return pdaNo;
	}

	public void setPdaNo(String pdaNo) {
		this.pdaNo = pdaNo;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public char getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(char billStatus) {
		this.billStatus = billStatus;
	}

	public String getFdate() {
		return fdate;
	}

	public void setFdate(String fdate) {
		this.fdate = fdate;
	}

	public int getFtranType() {
		return ftranType;
	}

	public void setFtranType(int ftranType) {
		this.ftranType = ftranType;
	}

	public int getFselTranType() {
		return fselTranType;
	}

	public void setFselTranType(int fselTranType) {
		this.fselTranType = fselTranType;
	}

	public String getFstockDirect() {
		return fstockDirect;
	}

	public void setFstockDirect(String fstockDirect) {
		this.fstockDirect = fstockDirect;
	}

	public String getFtransferType() {
		return ftransferType;
	}

	public void setFtransferType(String ftransferType) {
		this.ftransferType = ftransferType;
	}

	public char getFvestonWay() {
		return fvestonWay;
	}

	public void setFvestonWay(char fvestonWay) {
		this.fvestonWay = fvestonWay;
	}

	public char getFmrMode() {
		return fmrMode;
	}

	public void setFmrMode(char fmrMode) {
		this.fmrMode = fmrMode;
	}

	public int getFsupplierId() {
		return fsupplierId;
	}

	public void setFsupplierId(int fsupplierId) {
		this.fsupplierId = fsupplierId;
	}

	public int getFcustId() {
		return fcustId;
	}

	public void setFcustId(int fcustId) {
		this.fcustId = fcustId;
	}

	public int getFstockOrgId() {
		return fstockOrgId;
	}

	public void setFstockOrgId(int fstockOrgId) {
		this.fstockOrgId = fstockOrgId;
	}

	public int getFstockOutOrgId() {
		return fstockOutOrgId;
	}

	public void setFstockOutOrgId(int fstockOutOrgId) {
		this.fstockOutOrgId = fstockOutOrgId;
	}

	public int getPurDeptId() {
		return purDeptId;
	}

	public void setPurDeptId(int purDeptId) {
		this.purDeptId = purDeptId;
	}

	public int getRecDeptId() {
		return recDeptId;
	}

	public void setRecDeptId(int recDeptId) {
		this.recDeptId = recDeptId;
	}

	public String getEmpNumber() {
		return empNumber;
	}

	public void setEmpNumber(String empNumber) {
		this.empNumber = empNumber;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getStockManagerNumber() {
		return stockManagerNumber;
	}

	public void setStockManagerNumber(String stockManagerNumber) {
		this.stockManagerNumber = stockManagerNumber;
	}

	public String getStockManagerName() {
		return stockManagerName;
	}

	public void setStockManagerName(String stockManagerName) {
		this.stockManagerName = stockManagerName;
	}

	public int getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public int getPassUserId() {
		return passUserId;
	}

	public void setPassUserId(int passUserId) {
		this.passUserId = passUserId;
	}

	public String getPassUserName() {
		return passUserName;
	}

	public void setPassUserName(String passUserName) {
		this.passUserName = passUserName;
	}

	public String getPassDate() {
		return passDate;
	}

	public void setPassDate(String passDate) {
		this.passDate = passDate;
	}

	public int getIsToK3() {
		return isToK3;
	}

	public void setIsToK3(int isToK3) {
		this.isToK3 = isToK3;
	}

	public String getK3Number() {
		return k3Number;
	}

	public void setK3Number(String k3Number) {
		this.k3Number = k3Number;
	}

	public String getFbillTypeNumber() {
		return fbillTypeNumber;
	}

	public void setFbillTypeNumber(String fbillTypeNumber) {
		this.fbillTypeNumber = fbillTypeNumber;
	}

	public String getFbusinessTypeNumber() {
		return fbusinessTypeNumber;
	}

	public void setFbusinessTypeNumber(String fbusinessTypeNumber) {
		this.fbusinessTypeNumber = fbusinessTypeNumber;
	}

	public String getFownerType() {
		return fownerType;
	}

	public void setFownerType(String fownerType) {
		this.fownerType = fownerType;
	}

	public String getFownerNumber() {
		return fownerNumber;
	}

	public void setFownerNumber(String fownerNumber) {
		this.fownerNumber = fownerNumber;
	}

	public String getFownerName() {
		return fownerName;
	}

	public void setFownerName(String fownerName) {
		this.fownerName = fownerName;
	}

	public String getFownerOutType() {
		return fownerOutType;
	}

	public void setFownerOutType(String fownerOutType) {
		this.fownerOutType = fownerOutType;
	}

	public String getFownerOutNumber() {
		return fownerOutNumber;
	}

	public void setFownerOutNumber(String fownerOutNumber) {
		this.fownerOutNumber = fownerOutNumber;
	}

	public String getFownerOutName() {
		return fownerOutName;
	}

	public void setFownerOutName(String fownerOutName) {
		this.fownerOutName = fownerOutName;
	}

	public String getDeliveryWayNumber() {
		return deliveryWayNumber;
	}

	public void setDeliveryWayNumber(String deliveryWayNumber) {
		this.deliveryWayNumber = deliveryWayNumber;
	}

	public String getDeliveryWayName() {
		return deliveryWayName;
	}

	public void setDeliveryWayName(String deliveryWayName) {
		this.deliveryWayName = deliveryWayName;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public String getReceiveAddress() {
		return receiveAddress;
	}

	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}

	public String getReceiveContact() {
		return receiveContact;
	}

	public void setReceiveContact(String receiveContact) {
		this.receiveContact = receiveContact;
	}

	public String getReceivePhone() {
		return receivePhone;
	}

	public void setReceivePhone(String receivePhone) {
		this.receivePhone = receivePhone;
	}

	public String getReturnReasonNumber() {
		return returnReasonNumber;
	}

	public void setReturnReasonNumber(String returnReasonNumber) {
		this.returnReasonNumber = returnReasonNumber;
	}

	public String getReturnReasonName() {
		return returnReasonName;
	}

	public void setReturnReasonName(String returnReasonName) {
		this.returnReasonName = returnReasonName;
	}

	public Organization_K3 getStockOrg() {
		return stockOrg;
	}

	public void setStockOrg(Organization_K3 stockOrg) {
		this.stockOrg = stockOrg;
	}

	public Organization_K3 getStockOutOrg() {
		return stockOutOrg;
	}

	public void setStockOutOrg(Organization_K3 stockOutOrg) {
		this.stockOutOrg = stockOutOrg;
	}

	public Supplier_K3 getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier_K3 supplier) {
		this.supplier = supplier;
	}

	public Department_K3 getPurDept() {
		return purDept;
	}

	public void setPurDept(Department_K3 purDept) {
		this.purDept = purDept;
	}

	public Department_K3 getRecDept() {
		return recDept;
	}

	public void setRecDept(Department_K3 recDept) {
		this.recDept = recDept;
	}

	public Customer_K3 getCust() {
		return cust;
	}

	public void setCust(Customer_K3 cust) {
		this.cust = cust;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isShowButton() {
		return showButton;
	}

	public void setShowButton(boolean showButton) {
		this.showButton = showButton;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getStrSourceNo() {
		// 存在小写的逗号（,）,且大于1
		if(Comm.isNULLS(strSourceNo).indexOf(",") > -1 && Comm.isNULLS(strSourceNo).length() > 0) {
			return strSourceNo.substring(0, strSourceNo.length()-1);
		}
		return strSourceNo;
	}

	public void setStrSourceNo(String strSourceNo) {
		this.strSourceNo = strSourceNo;
	}

	public String getOutStockName() {
		return outStockName;
	}

	public void setOutStockName(String outStockName) {
		this.outStockName = outStockName;
	}

	public int getIsCommit() {
		return isCommit;
	}

	public void setIsCommit(int isCommit) {
		this.isCommit = isCommit;
	}

	public int getChildSize() {
		return childSize;
	}

	public void setChildSize(int childSize) {
		this.childSize = childSize;
	}

	public int getCombineSalOrderId() {
		return combineSalOrderId;
	}

	public void setCombineSalOrderId(int combineSalOrderId) {
		this.combineSalOrderId = combineSalOrderId;
	}

}

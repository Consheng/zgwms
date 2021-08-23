package ykk.xc.com.zgwms.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ykk.xc.com.zgwms.bean.k3Bean.Customer_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Department_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Material_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Unit_K3;
import ykk.xc.com.zgwms.bean.sales.SalOrderEntry;

/**
 * 物料装箱记录类
 *
 * @author Administrator
 *
 */
public class MaterialBinningRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private int status;						// 0：正常，1：开箱，2：封箱
	private char type; 						// 1:生产装箱，2:销售装箱
	private char packType;					// 包装类型（A:单装，B:混装）
	private int boxId;						// 包装箱id
	private int boxBarCodeId; 				// 包装条码id
	private int mtlId;						// 物料id
	private int funitId;					// 单位id
	private double fqty;					// 包装数
	private int fsourceInterId;				// 来源单表头id
	private int fsourceEntryId;				// 来源单分录id
	private String fsourceNo;				// 来源单号
	private double fsourceQty;				// 来源数量
	private int fsourceOrgId;				// 来源组织id
	private int forderInterId;				// 销售订单表头id
	private int forderEntryId;				// 销售订单分录id
	private String forderNo;				// 销售订单号
	private int forderOrgId;				// 销售订单组织id
	private String createUserName;			// 创建人id
	private String createDate;				// 创建日期
	private String modifyDate;				// 修改日期
	private int modifyUserId;				// 修改人id
	private int fdeptId;					// 部门id
	private int fcustId;					// 客户id
	private int icstockBillId;				// 销售装箱保存的 ( 出入库主表id )
	private String deliveryTypeNumber;		// 销售订单--交货类别代码
	private String deliveryTypeName;		// 销售订单--交货类别名称
	private String deliveryWayNumber;		// 销售订单--交货方式代码
	private String deliveryWayName;			// 销售订单--交货方式名称
	private String deliveryAddress;			// 销售订单--交货地点

	private BoxBarCode boxBarCode;
	private Box box;
	private Material_K3 material;
	private Unit_K3 unit;
	private User createUser;
	private User modifyUser;
	private Department_K3 dept;
	private Customer_K3 cust;
	private SalOrderEntry salOrderEntry;

	// 临时字段，不存表
	private List<MaterialBinningRecord_Barcode> mbrBarcodes;
	private double usableQty;		// 可用数
	private int rowNo;	// 前端行号
	private boolean showButton; 		// 是否显示操作按钮

	public MaterialBinningRecord() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public char getPackType() {
		return packType;
	}

	public void setPackType(char packType) {
		this.packType = packType;
	}

	public int getBoxId() {
		return boxId;
	}

	public void setBoxId(int boxId) {
		this.boxId = boxId;
	}

	public int getBoxBarCodeId() {
		return boxBarCodeId;
	}

	public void setBoxBarCodeId(int boxBarCodeId) {
		this.boxBarCodeId = boxBarCodeId;
	}

	public int getMtlId() {
		return mtlId;
	}

	public void setMtlId(int mtlId) {
		this.mtlId = mtlId;
	}

	public double getFqty() {
		return fqty;
	}

	public void setFqty(double fqty) {
		this.fqty = fqty;
	}

	public int getFsourceInterId() {
		return fsourceInterId;
	}

	public void setFsourceInterId(int fsourceInterId) {
		this.fsourceInterId = fsourceInterId;
	}

	public int getFsourceEntryId() {
		return fsourceEntryId;
	}

	public void setFsourceEntryId(int fsourceEntryId) {
		this.fsourceEntryId = fsourceEntryId;
	}

	public String getFsourceNo() {
		return fsourceNo;
	}

	public void setFsourceNo(String fsourceNo) {
		this.fsourceNo = fsourceNo;
	}

	public double getFsourceQty() {
		return fsourceQty;
	}

	public void setFsourceQty(double fsourceQty) {
		this.fsourceQty = fsourceQty;
	}

	public int getFsourceOrgId() {
		return fsourceOrgId;
	}

	public void setFsourceOrgId(int fsourceOrgId) {
		this.fsourceOrgId = fsourceOrgId;
	}

	public int getForderInterId() {
		return forderInterId;
	}

	public void setForderInterId(int forderInterId) {
		this.forderInterId = forderInterId;
	}

	public int getForderEntryId() {
		return forderEntryId;
	}

	public void setForderEntryId(int forderEntryId) {
		this.forderEntryId = forderEntryId;
	}

	public String getForderNo() {
		return forderNo;
	}

	public void setForderNo(String forderNo) {
		this.forderNo = forderNo;
	}

	public int getForderOrgId() {
		return forderOrgId;
	}

	public void setForderOrgId(int forderOrgId) {
		this.forderOrgId = forderOrgId;
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

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public int getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(int modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	public int getFdeptId() {
		return fdeptId;
	}

	public void setFdeptId(int fdeptId) {
		this.fdeptId = fdeptId;
	}

	public int getFcustId() {
		return fcustId;
	}

	public void setFcustId(int fcustId) {
		this.fcustId = fcustId;
	}

	public int getIcstockBillId() {
		return icstockBillId;
	}

	public void setIcstockBillId(int icstockBillId) {
		this.icstockBillId = icstockBillId;
	}

	public String getDeliveryTypeNumber() {
		return deliveryTypeNumber;
	}

	public void setDeliveryTypeNumber(String deliveryTypeNumber) {
		this.deliveryTypeNumber = deliveryTypeNumber;
	}

	public String getDeliveryTypeName() {
		return deliveryTypeName;
	}

	public void setDeliveryTypeName(String deliveryTypeName) {
		this.deliveryTypeName = deliveryTypeName;
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

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public BoxBarCode getBoxBarCode() {
		return boxBarCode;
	}

	public void setBoxBarCode(BoxBarCode boxBarCode) {
		this.boxBarCode = boxBarCode;
	}

	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
	}

	public Material_K3 getMaterial() {
		return material;
	}

	public void setMaterial(Material_K3 material) {
		this.material = material;
	}

	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	public User getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(User modifyUser) {
		this.modifyUser = modifyUser;
	}

	public Department_K3 getDept() {
		return dept;
	}

	public void setDept(Department_K3 dept) {
		this.dept = dept;
	}

	public Customer_K3 getCust() {
		return cust;
	}

	public void setCust(Customer_K3 cust) {
		this.cust = cust;
	}

	public List<MaterialBinningRecord_Barcode> getMbrBarcodes() {
		if(mbrBarcodes == null) {
			mbrBarcodes = new ArrayList<>();
		}
		return mbrBarcodes;
	}

	public void setMbrBarcodes(List<MaterialBinningRecord_Barcode> mbrBarcodes) {
		this.mbrBarcodes = mbrBarcodes;
	}

	public double getUsableQty() {
		return usableQty;
	}

	public void setUsableQty(double usableQty) {
		this.usableQty = usableQty;
	}

	public int getRowNo() {
		return rowNo;
	}

	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}

	public boolean isShowButton() {
		return showButton;
	}

	public void setShowButton(boolean showButton) {
		this.showButton = showButton;
	}

	public int getFunitId() {
		return funitId;
	}

	public void setFunitId(int funitId) {
		this.funitId = funitId;
	}

	public Unit_K3 getUnit() {
		return unit;
	}

	public void setUnit(Unit_K3 unit) {
		this.unit = unit;
	}

	public SalOrderEntry getSalOrderEntry() {
		return salOrderEntry;
	}

	public void setSalOrderEntry(SalOrderEntry salOrderEntry) {
		this.salOrderEntry = salOrderEntry;
	}

}

package ykk.xc.com.zgwms.bean.prod;

import java.io.Serializable;

import ykk.xc.com.zgwms.bean.k3Bean.Department_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Material_K3;
import ykk.xc.com.zgwms.bean.k3Bean.StockPosition_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Stock_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Unit_K3;

/**
 * 生产订单分录表
 * @author Administrator
 *
 */
public class PrdMoEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	private int fid;							// 主表id
	private int fentryId;						// 分录id
	private int fmaterialId;					// 物料id
	private int funitId;						// 单位id
	private double fqty;						// 数量
	private String fplanStartDate;				// 计划开工时间
	private String fplanFinishDate;				// 计划完工时间
	private String fmtoNo;						// 计划跟踪号
	private double fstockInLimith;				// 入库上限
	private double fstockInLimitl;				// 入库下限
	private int fworkshopId;					// 生产车间
	private int fstockId;						// 仓库id
	private int fstockLocId;					// 库位id
	private int fseq;							// 序号
	private int fsaleOrderId;					// 销售订单id
	private String fsaleOrderNo;				// 销售订单号
	private int fsaleOrderEntryId;				// 销售订单分录id
	private int fsaleOrderEntrySeq;				// 销售订单行号
	private String sal_billType_fnumber;		// 销售单据类型名称（BHXS:备货的，XSDD01_SYS:标准销售订单）
	private int sal_fcustId;					// 销售订单客户
	private int sal_fsaleOrgId;					// 销售订单--销售组织id
	private String salOrg_id;					// 销售组织id
	private String salOrg_fnumber;				// 销售组织代码
	private String salOrg_fname;				// 销售组织名称
	private String sal_deliveryWayNumber;		// 销售订单--交货方式代码
	private String sal_deliveryWayName;			// 销售订单--交货方式名称
	private String sal_receiveAddress;			// 销售订单--交货地点
	private String personalCarVersionNumber;	// 通用车型版号代码
	private String personalCarVersionLocation;	// 通用车型版号位置
	private String personalCarVersionName;		// 通用车型版号名称

	private String fauxpropid_103_number;		// 辅助属性（款式）代码
	private String fauxpropid_103_name;			// 辅助属性（款式）名称
	private String fauxpropid_104_number;		// 辅助属性（一层用料）代码
	private String fauxpropid_104_name;			// 辅助属性（一层用料）名称
	private String fauxpropid_105_number;		// 辅助属性（二层用料）代码
	private String fauxpropid_105_name;			// 辅助属性（二层用料）名称
	private String fauxpropid_106_number;		// 辅助属性（座位）代码
	private String fauxpropid_106_name;			// 辅助属性（座位）名称

	private PrdMo prdMo;
	private Department_K3 dept;
	private Material_K3 material;
	private Unit_K3 unit;
	private Stock_K3 stock;
	private StockPosition_K3 stockPos;

	// 临时字段，不存表
	private double usableQty; 			// 可用的数量
	private String smSnCode;			// 扫码的序列号
	private String smBatchCode;			// 扫码的批次号
	private double smQty;				// 扫码后计算出的数

	public PrdMoEntry() {
		super();
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public int getFentryId() {
		return fentryId;
	}

	public void setFentryId(int fentryId) {
		this.fentryId = fentryId;
	}

	public int getFmaterialId() {
		return fmaterialId;
	}

	public void setFmaterialId(int fmaterialId) {
		this.fmaterialId = fmaterialId;
	}

	public int getFunitId() {
		return funitId;
	}

	public void setFunitId(int funitId) {
		this.funitId = funitId;
	}

	public double getFqty() {
		return fqty;
	}

	public void setFqty(double fqty) {
		this.fqty = fqty;
	}

	public String getFplanStartDate() {
		return fplanStartDate;
	}

	public void setFplanStartDate(String fplanStartDate) {
		this.fplanStartDate = fplanStartDate;
	}

	public String getFplanFinishDate() {
		return fplanFinishDate;
	}

	public void setFplanFinishDate(String fplanFinishDate) {
		this.fplanFinishDate = fplanFinishDate;
	}

	public String getFmtoNo() {
		return fmtoNo;
	}

	public void setFmtoNo(String fmtoNo) {
		this.fmtoNo = fmtoNo;
	}

	public double getFstockInLimith() {
		return fstockInLimith;
	}

	public void setFstockInLimith(double fstockInLimith) {
		this.fstockInLimith = fstockInLimith;
	}

	public double getFstockInLimitl() {
		return fstockInLimitl;
	}

	public void setFstockInLimitl(double fstockInLimitl) {
		this.fstockInLimitl = fstockInLimitl;
	}

	public int getFworkshopId() {
		return fworkshopId;
	}

	public void setFworkshopId(int fworkshopId) {
		this.fworkshopId = fworkshopId;
	}

	public int getFstockId() {
		return fstockId;
	}

	public void setFstockId(int fstockId) {
		this.fstockId = fstockId;
	}

	public int getFstockLocId() {
		return fstockLocId;
	}

	public void setFstockLocId(int fstockLocId) {
		this.fstockLocId = fstockLocId;
	}

	public PrdMo getPrdMo() {
		return prdMo;
	}

	public void setPrdMo(PrdMo prdMo) {
		this.prdMo = prdMo;
	}

	public Department_K3 getDept() {
		return dept;
	}

	public void setDept(Department_K3 dept) {
		this.dept = dept;
	}

	public Material_K3 getMaterial() {
		return material;
	}

	public void setMaterial(Material_K3 material) {
		this.material = material;
	}

	public Unit_K3 getUnit() {
		return unit;
	}

	public void setUnit(Unit_K3 unit) {
		this.unit = unit;
	}

	public Stock_K3 getStock() {
		return stock;
	}

	public void setStock(Stock_K3 stock) {
		this.stock = stock;
	}

	public StockPosition_K3 getStockPos() {
		return stockPos;
	}

	public void setStockPos(StockPosition_K3 stockPos) {
		this.stockPos = stockPos;
	}

	public double getUsableQty() {
		return usableQty;
	}

	public void setUsableQty(double usableQty) {
		this.usableQty = usableQty;
	}

	public int getFseq() {
		return fseq;
	}

	public void setFseq(int fseq) {
		this.fseq = fseq;
	}

	public int getFsaleOrderId() {
		return fsaleOrderId;
	}

	public void setFsaleOrderId(int fsaleOrderId) {
		this.fsaleOrderId = fsaleOrderId;
	}

	public String getSalOrg_id() {
		return salOrg_id;
	}

	public void setSalOrg_id(String salOrg_id) {
		this.salOrg_id = salOrg_id;
	}

	public String getSalOrg_fnumber() {
		return salOrg_fnumber;
	}

	public void setSalOrg_fnumber(String salOrg_fnumber) {
		this.salOrg_fnumber = salOrg_fnumber;
	}

	public String getSalOrg_fname() {
		return salOrg_fname;
	}

	public void setSalOrg_fname(String salOrg_fname) {
		this.salOrg_fname = salOrg_fname;
	}

	public String getFsaleOrderNo() {
		return fsaleOrderNo;
	}

	public void setFsaleOrderNo(String fsaleOrderNo) {
		this.fsaleOrderNo = fsaleOrderNo;
	}

	public int getFsaleOrderEntryId() {
		return fsaleOrderEntryId;
	}

	public void setFsaleOrderEntryId(int fsaleOrderEntryId) {
		this.fsaleOrderEntryId = fsaleOrderEntryId;
	}

	public int getFsaleOrderEntrySeq() {
		return fsaleOrderEntrySeq;
	}

	public void setFsaleOrderEntrySeq(int fsaleOrderEntrySeq) {
		this.fsaleOrderEntrySeq = fsaleOrderEntrySeq;
	}

	public String getSal_billType_fnumber() {
		return sal_billType_fnumber;
	}

	public void setSal_billType_fnumber(String sal_billType_fnumber) {
		this.sal_billType_fnumber = sal_billType_fnumber;
	}

	public int getSal_fcustId() {
		return sal_fcustId;
	}

	public void setSal_fcustId(int sal_fcustId) {
		this.sal_fcustId = sal_fcustId;
	}

	public int getSal_fsaleOrgId() {
		return sal_fsaleOrgId;
	}

	public void setSal_fsaleOrgId(int sal_fsaleOrgId) {
		this.sal_fsaleOrgId = sal_fsaleOrgId;
	}

	public String getSal_deliveryWayNumber() {
		return sal_deliveryWayNumber;
	}

	public void setSal_deliveryWayNumber(String sal_deliveryWayNumber) {
		this.sal_deliveryWayNumber = sal_deliveryWayNumber;
	}

	public String getSal_deliveryWayName() {
		return sal_deliveryWayName;
	}

	public void setSal_deliveryWayName(String sal_deliveryWayName) {
		this.sal_deliveryWayName = sal_deliveryWayName;
	}

	public String getSal_receiveAddress() {
		return sal_receiveAddress;
	}

	public void setSal_receiveAddress(String sal_receiveAddress) {
		this.sal_receiveAddress = sal_receiveAddress;
	}

	public String getSmSnCode() {
		return smSnCode;
	}

	public void setSmSnCode(String smSnCode) {
		this.smSnCode = smSnCode;
	}

	public String getSmBatchCode() {
		return smBatchCode;
	}

	public void setSmBatchCode(String smBatchCode) {
		this.smBatchCode = smBatchCode;
	}

	public String getPersonalCarVersionNumber() {
		return personalCarVersionNumber;
	}

	public void setPersonalCarVersionNumber(String personalCarVersionNumber) {
		this.personalCarVersionNumber = personalCarVersionNumber;
	}

	public String getPersonalCarVersionLocation() {
		return personalCarVersionLocation;
	}

	public void setPersonalCarVersionLocation(String personalCarVersionLocation) {
		this.personalCarVersionLocation = personalCarVersionLocation;
	}

	public String getPersonalCarVersionName() {
		return personalCarVersionName;
	}

	public void setPersonalCarVersionName(String personalCarVersionName) {
		this.personalCarVersionName = personalCarVersionName;
	}

	public double getSmQty() {
		return smQty;
	}

	public void setSmQty(double smQty) {
		this.smQty = smQty;
	}

	public String getFauxpropid_103_number() {
		return fauxpropid_103_number;
	}

	public void setFauxpropid_103_number(String fauxpropid_103_number) {
		this.fauxpropid_103_number = fauxpropid_103_number;
	}

	public String getFauxpropid_103_name() {
		return fauxpropid_103_name;
	}

	public void setFauxpropid_103_name(String fauxpropid_103_name) {
		this.fauxpropid_103_name = fauxpropid_103_name;
	}

	public String getFauxpropid_104_number() {
		return fauxpropid_104_number;
	}

	public void setFauxpropid_104_number(String fauxpropid_104_number) {
		this.fauxpropid_104_number = fauxpropid_104_number;
	}

	public String getFauxpropid_104_name() {
		return fauxpropid_104_name;
	}

	public void setFauxpropid_104_name(String fauxpropid_104_name) {
		this.fauxpropid_104_name = fauxpropid_104_name;
	}

	public String getFauxpropid_105_number() {
		return fauxpropid_105_number;
	}

	public void setFauxpropid_105_number(String fauxpropid_105_number) {
		this.fauxpropid_105_number = fauxpropid_105_number;
	}

	public String getFauxpropid_105_name() {
		return fauxpropid_105_name;
	}

	public void setFauxpropid_105_name(String fauxpropid_105_name) {
		this.fauxpropid_105_name = fauxpropid_105_name;
	}

	public String getFauxpropid_106_number() {
		return fauxpropid_106_number;
	}

	public void setFauxpropid_106_number(String fauxpropid_106_number) {
		this.fauxpropid_106_number = fauxpropid_106_number;
	}

	public String getFauxpropid_106_name() {
		return fauxpropid_106_name;
	}

	public void setFauxpropid_106_name(String fauxpropid_106_name) {
		this.fauxpropid_106_name = fauxpropid_106_name;
	}

}

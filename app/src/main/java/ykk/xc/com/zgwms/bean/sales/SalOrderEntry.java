package ykk.xc.com.zgwms.bean.sales;

import java.io.Serializable;

import ykk.xc.com.zgwms.bean.k3Bean.Material_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Organization_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Stock_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Unit_K3;

/**
 * 销售订单分录
 * @author Administrator
 *
 */
public class SalOrderEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	private int fid; 						// 单据id,
	private int fentryId;					// 分录id
	private int fseq;								// 序号
	private int fmaterialId;				// 物料id
	private int funitId;					// 单位id
	private String frowType;						// 产品类型
	private double fqty;					// 数量
	private double fprice;					// 单价
	private double ftaxPrice;				// 含税单价
	private double ftaxRate;				// 税率
	private double famount;					// 金额
	private String fnote;					// 摘要
	private String fmrpFreezeStatus;		// 业务冻结（A:正常 B:业务冻结）
	private String fmrpTerminateStatus;		// 业务终止（A:正常 B:业务终止）
	private String fmrpCloseStatus;			// 业务关闭（A:正常 B:业务关闭）
	private int fstockOrgId;				// 库存组织id
	private int fstockId;					// 仓库id
	private String fownerTypeId;			// 货主类型id
	private int fownerId;					// 货主id
	private int fsupplyOrgId;				// 供应组织id
	private int fstockUnitId;				// 库存单位id
	private double fstockQty;				// 库存数
	private String fmapId;							// 客户物料id（100006&101943&100009）
	private int fauxPropId;							// 辅助属性id
	private int fisFree;							// 是否免费（0：不免费（赠品），1：免费（赠品））
	private String jtks;							// 具体款式
	private String personalCarVersionNumber;// 通用车型版号代码
	private String personalCarVersionLocation;		// 通用车型版号代码
	private String personalCarVersionName;	// 通用车型版号名称

	private String fauxpropid_103_id;			// 辅助属性（款式）id
	private String fauxpropid_103_number;		// 辅助属性（款式）代码
	private String fauxpropid_103_name;			// 辅助属性（款式）名称
	private int fauxpropid_103_eanble;			// 辅助属性（款式）是否启用
	private String fauxpropid_104_id;			// 辅助属性（一层用料）id
	private String fauxpropid_104_number;		// 辅助属性（一层用料）代码
	private String fauxpropid_104_name;			// 辅助属性（一层用料）名称
	private int fauxpropid_104_eanble;			// 辅助属性（一层用料）是否启用
	private String fauxpropid_105_id;			// 辅助属性（二层用料）id
	private String fauxpropid_105_number;		// 辅助属性（二层用料）代码
	private String fauxpropid_105_name;			// 辅助属性（二层用料）名称
	private int fauxpropid_105_eanble;			// 辅助属性（二层用料）是否启用
	private String fauxpropid_106_id;			// 辅助属性（座位）id
	private String fauxpropid_106_number;		// 辅助属性（座位）代码
	private String fauxpropid_106_name;			// 辅助属性（座位）名称
	private int fauxpropid_106_eanble;			// 辅助属性（座位）是否启用

	private SalOrder salOrder;
	private Material_K3 material;
	private Unit_K3 unit;
	private Organization_K3 stockOrg;
	private Stock_K3 stock;
	private Organization_K3 suppOrg;

	// 临时字段，不存表
	private double usableQty; 			// 可用的数量
	private String smSnCode;			// 扫码的序列号
	private String smBatchCode;			// 扫码的批次号
	private double smQty;				// 扫码后计算出的数
	private int prdMoId;				// 生产订单id
	private String prdMoNo;				// 生产订单号
	private int prdMoEntryId;			// 生产订单分录id
	private int combineSalOrderId;		// 拼单id
	private int combineSalOrderSumRow;	// 拼单总行数
	private boolean check;				// 是否选中

	public SalOrderEntry() {
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

	public int getFseq() {
		return fseq;
	}

	public void setFseq(int fseq) {
		this.fseq = fseq;
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

	public String getFrowType() {
		return frowType;
	}

	public void setFrowType(String frowType) {
		this.frowType = frowType;
	}

	public double getFqty() {
		return fqty;
	}

	public void setFqty(double fqty) {
		this.fqty = fqty;
	}

	public double getFprice() {
		return fprice;
	}

	public void setFprice(double fprice) {
		this.fprice = fprice;
	}

	public double getFtaxPrice() {
		return ftaxPrice;
	}

	public void setFtaxPrice(double ftaxPrice) {
		this.ftaxPrice = ftaxPrice;
	}

	public double getFtaxRate() {
		return ftaxRate;
	}

	public void setFtaxRate(double ftaxRate) {
		this.ftaxRate = ftaxRate;
	}

	public double getFamount() {
		return famount;
	}

	public void setFamount(double famount) {
		this.famount = famount;
	}

	public String getFnote() {
		return fnote;
	}

	public void setFnote(String fnote) {
		this.fnote = fnote;
	}

	public String getFmrpFreezeStatus() {
		return fmrpFreezeStatus;
	}

	public void setFmrpFreezeStatus(String fmrpFreezeStatus) {
		this.fmrpFreezeStatus = fmrpFreezeStatus;
	}

	public String getFmrpTerminateStatus() {
		return fmrpTerminateStatus;
	}

	public void setFmrpTerminateStatus(String fmrpTerminateStatus) {
		this.fmrpTerminateStatus = fmrpTerminateStatus;
	}

	public String getFmrpCloseStatus() {
		return fmrpCloseStatus;
	}

	public void setFmrpCloseStatus(String fmrpCloseStatus) {
		this.fmrpCloseStatus = fmrpCloseStatus;
	}

	public int getFstockOrgId() {
		return fstockOrgId;
	}

	public void setFstockOrgId(int fstockOrgId) {
		this.fstockOrgId = fstockOrgId;
	}

	public int getFstockId() {
		return fstockId;
	}

	public void setFstockId(int fstockId) {
		this.fstockId = fstockId;
	}

	public String getFownerTypeId() {
		return fownerTypeId;
	}

	public void setFownerTypeId(String fownerTypeId) {
		this.fownerTypeId = fownerTypeId;
	}

	public int getFownerId() {
		return fownerId;
	}

	public void setFownerId(int fownerId) {
		this.fownerId = fownerId;
	}

	public int getFsupplyOrgId() {
		return fsupplyOrgId;
	}

	public void setFsupplyOrgId(int fsupplyOrgId) {
		this.fsupplyOrgId = fsupplyOrgId;
	}

	public int getFstockUnitId() {
		return fstockUnitId;
	}

	public void setFstockUnitId(int fstockUnitId) {
		this.fstockUnitId = fstockUnitId;
	}

	public double getFstockQty() {
		return fstockQty;
	}

	public void setFstockQty(double fstockQty) {
		this.fstockQty = fstockQty;
	}

	public SalOrder getSalOrder() {
		return salOrder;
	}

	public void setSalOrder(SalOrder salOrder) {
		this.salOrder = salOrder;
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

	public Organization_K3 getStockOrg() {
		return stockOrg;
	}

	public void setStockOrg(Organization_K3 stockOrg) {
		this.stockOrg = stockOrg;
	}

	public Stock_K3 getStock() {
		return stock;
	}

	public void setStock(Stock_K3 stock) {
		this.stock = stock;
	}

	public Organization_K3 getSuppOrg() {
		return suppOrg;
	}

	public void setSuppOrg(Organization_K3 suppOrg) {
		this.suppOrg = suppOrg;
	}

	public double getUsableQty() {
		return usableQty;
	}

	public void setUsableQty(double usableQty) {
		this.usableQty = usableQty;
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

	public double getSmQty() {
		return smQty;
	}

	public void setSmQty(double smQty) {
		this.smQty = smQty;
	}

	public int getPrdMoId() {
		return prdMoId;
	}

	public void setPrdMoId(int prdMoId) {
		this.prdMoId = prdMoId;
	}

	public String getPrdMoNo() {
		return prdMoNo;
	}

	public void setPrdMoNo(String prdMoNo) {
		this.prdMoNo = prdMoNo;
	}

	public int getPrdMoEntryId() {
		return prdMoEntryId;
	}

	public void setPrdMoEntryId(int prdMoEntryId) {
		this.prdMoEntryId = prdMoEntryId;
	}

	public int getCombineSalOrderId() {
		return combineSalOrderId;
	}

	public void setCombineSalOrderId(int combineSalOrderId) {
		this.combineSalOrderId = combineSalOrderId;
	}

	public int getCombineSalOrderSumRow() {
		return combineSalOrderSumRow;
	}

	public void setCombineSalOrderSumRow(int combineSalOrderSumRow) {
		this.combineSalOrderSumRow = combineSalOrderSumRow;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public String getFmapId() {
		return fmapId;
	}

	public void setFmapId(String fmapId) {
		this.fmapId = fmapId;
	}

	public int getFauxPropId() {
		return fauxPropId;
	}

	public void setFauxPropId(int fauxPropId) {
		this.fauxPropId = fauxPropId;
	}

	public int getFisFree() {
		return fisFree;
	}

	public void setFisFree(int fisFree) {
		this.fisFree = fisFree;
	}

	public String getJtks() {
		return jtks;
	}

	public void setJtks(String jtks) {
		this.jtks = jtks;
	}

	public int getFauxpropid_103_eanble() {
		return fauxpropid_103_eanble;
	}

	public void setFauxpropid_103_eanble(int fauxpropid_103_eanble) {
		this.fauxpropid_103_eanble = fauxpropid_103_eanble;
	}

	public int getFauxpropid_104_eanble() {
		return fauxpropid_104_eanble;
	}

	public void setFauxpropid_104_eanble(int fauxpropid_104_eanble) {
		this.fauxpropid_104_eanble = fauxpropid_104_eanble;
	}

	public int getFauxpropid_105_eanble() {
		return fauxpropid_105_eanble;
	}

	public void setFauxpropid_105_eanble(int fauxpropid_105_eanble) {
		this.fauxpropid_105_eanble = fauxpropid_105_eanble;
	}

	public int getFauxpropid_106_eanble() {
		return fauxpropid_106_eanble;
	}

	public void setFauxpropid_106_eanble(int fauxpropid_106_eanble) {
		this.fauxpropid_106_eanble = fauxpropid_106_eanble;
	}

	public String getFauxpropid_103_id() {
		return fauxpropid_103_id;
	}

	public void setFauxpropid_103_id(String fauxpropid_103_id) {
		this.fauxpropid_103_id = fauxpropid_103_id;
	}

	public String getFauxpropid_104_id() {
		return fauxpropid_104_id;
	}

	public void setFauxpropid_104_id(String fauxpropid_104_id) {
		this.fauxpropid_104_id = fauxpropid_104_id;
	}

	public String getFauxpropid_105_id() {
		return fauxpropid_105_id;
	}

	public void setFauxpropid_105_id(String fauxpropid_105_id) {
		this.fauxpropid_105_id = fauxpropid_105_id;
	}

	public String getFauxpropid_106_id() {
		return fauxpropid_106_id;
	}

	public void setFauxpropid_106_id(String fauxpropid_106_id) {
		this.fauxpropid_106_id = fauxpropid_106_id;
	}



}

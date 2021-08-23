package ykk.xc.com.zgwms.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ykk.xc.com.zgwms.bean.k3Bean.Customer_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Material_K3;
import ykk.xc.com.zgwms.bean.k3Bean.StockPosition_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Stock_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Unit_K3;
import ykk.xc.com.zgwms.comm.Comm;

/**
 * Wms 本地的出入库	Entry表
 * @author Administrator
 *
 */
public class ICStockBillEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id; 						// 
	private int icstockBillId;				// 主表id
	private int mtlId;						// 物料id
	private int fentryId;					// 分录id
	private int fdcStockId;					// 调入仓库id
	private int fdcSPId;					// 调入库位id
	private int fscStockId;					// 调出仓库id
	private int fscSPId;					// 调出库位id
	private double fqty;					// 数量
	private double fprice;					// 单价
	private int funitId;					// 单位id
	private String frowType;					// 产品类型
	private int fsourceInterId;				// 来源内码id
	private int fsourceEntryId;				// 来源分录id
	private String fsourceBillNo;			// 来源单号
	private int fsourceSeq;					// 来源顺序号
	private double fsourceQty;				// 来源单数量
	private int forderInterId;				// 来源订单id
	private String forderBillNo;			// 来源订单号
	private int forderEntryId;				// 来源订单分录id
	private int forderSeq;					// 订单顺序号
	private String forderDate;					// 来源订单日期
	private int fisFree;						// 来源订单是否赠品
	private String jtks;						// 具体款式
	private String remark;						// 备注
	private String boxBarcode;				// 箱子条码
	private int mbrId;						// 物料装箱表id
	private String carVersionNumber;			// 车型版号代码
	private String carVersionLocation;		// 车型版号代码
	private String carVersionName;			// 车型版号名称
	private String fownerNumber;			// 货主代码
	private String fownerName;				// 货主名称
	private String fsrcBillTypeId;			// 源单类型id
	private String fentity_Link_FRuleId;	// 分录关联的类型名称
	private String fentity_Link_FSTableName;// 分录关联的表名
	private String fmtoNo;						// 计划跟踪号
	private String custMtlId;					// 客户物料id（100006&101943&100009）

	private String fauxpropid_103_number;		// 辅助属性（款式）代码
	private String fauxpropid_103_name;			// 辅助属性（款式）名称
	private String fauxpropid_104_number;		// 辅助属性（一层用料）代码
	private String fauxpropid_104_name;			// 辅助属性（一层用料）名称
	private String fauxpropid_105_number;		// 辅助属性（二层用料）代码
	private String fauxpropid_105_name;			// 辅助属性（二层用料）名称
	private String fauxpropid_106_number;		// 辅助属性（座位）代码
	private String fauxpropid_106_name;			// 辅助属性（座位）名称

	private ICStockBill icstockBill;
	private Stock_K3 stock;
	private StockPosition_K3 stockPos;
	private Stock_K3 stock2;
	private StockPosition_K3 stockPos2;
	private Material_K3 material;
	private Unit_K3 unit;

	// 临时字段，不存表
	private boolean showButton; 		// 是否显示操作按钮
	private String smBatchCode; // 扫码的批次号
	private String smSnCode; // 扫码的序列号
	private double smQty;	// 扫码后计算出的数
	private String strBatchCode; // 拼接的批次号
	private String strBarcode;	// 用于显示拼接的条码号
	private double inventoryNowQty; // 当前扫码的可用库存数
	private double inStockQty; // 调入仓库库存
	private double outStockQty; // 调出仓库库存

	private List<ICStockBillEntry_Barcode> icstockBillEntry_Barcodes; // 条码记录

	public ICStockBillEntry() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIcstockBillId() {
		return icstockBillId;
	}

	public void setIcstockBillId(int icstockBillId) {
		this.icstockBillId = icstockBillId;
	}

	public int getMtlId() {
		return mtlId;
	}

	public void setMtlId(int mtlId) {
		this.mtlId = mtlId;
	}

	public int getFentryId() {
		return fentryId;
	}

	public void setFentryId(int fentryId) {
		this.fentryId = fentryId;
	}

	public int getFdcStockId() {
		return fdcStockId;
	}

	public void setFdcStockId(int fdcStockId) {
		this.fdcStockId = fdcStockId;
	}

	public int getFdcSPId() {
		return fdcSPId;
	}

	public void setFdcSPId(int fdcSPId) {
		this.fdcSPId = fdcSPId;
	}

	public int getFscStockId() {
		return fscStockId;
	}

	public void setFscStockId(int fscStockId) {
		this.fscStockId = fscStockId;
	}

	public int getFscSPId() {
		return fscSPId;
	}

	public void setFscSPId(int fscSPId) {
		this.fscSPId = fscSPId;
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

	public String getFsourceBillNo() {
		return fsourceBillNo;
	}

	public void setFsourceBillNo(String fsourceBillNo) {
		this.fsourceBillNo = fsourceBillNo;
	}

	public double getFsourceQty() {
		return fsourceQty;
	}

	public void setFsourceQty(double fsourceQty) {
		this.fsourceQty = fsourceQty;
	}

	public int getForderInterId() {
		return forderInterId;
	}

	public void setForderInterId(int forderInterId) {
		this.forderInterId = forderInterId;
	}

	public String getForderBillNo() {
		return forderBillNo;
	}

	public void setForderBillNo(String forderBillNo) {
		this.forderBillNo = forderBillNo;
	}

	public int getForderEntryId() {
		return forderEntryId;
	}

	public void setForderEntryId(int forderEntryId) {
		this.forderEntryId = forderEntryId;
	}

	public String getFsrcBillTypeId() {
		return fsrcBillTypeId;
	}

	public void setFsrcBillTypeId(String fsrcBillTypeId) {
		this.fsrcBillTypeId = fsrcBillTypeId;
	}

	public String getFentity_Link_FRuleId() {
		return fentity_Link_FRuleId;
	}

	public void setFentity_Link_FRuleId(String fentity_Link_FRuleId) {
		this.fentity_Link_FRuleId = fentity_Link_FRuleId;
	}

	public String getFentity_Link_FSTableName() {
		return fentity_Link_FSTableName;
	}

	public void setFentity_Link_FSTableName(String fentity_Link_FSTableName) {
		this.fentity_Link_FSTableName = fentity_Link_FSTableName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBoxBarcode() {
		return boxBarcode;
	}

	public void setBoxBarcode(String boxBarcode) {
		this.boxBarcode = boxBarcode;
	}

	public int getMbrId() {
		return mbrId;
	}

	public void setMbrId(int mbrId) {
		this.mbrId = mbrId;
	}

	public String getCarVersionNumber() {
		return carVersionNumber;
	}

	public void setCarVersionNumber(String carVersionNumber) {
		this.carVersionNumber = carVersionNumber;
	}

	public String getCarVersionLocation() {
		return carVersionLocation;
	}

	public void setCarVersionLocation(String carVersionLocation) {
		this.carVersionLocation = carVersionLocation;
	}

	public String getCarVersionName() {
		return carVersionName;
	}

	public void setCarVersionName(String carVersionName) {
		this.carVersionName = carVersionName;
	}

	public ICStockBill getIcstockBill() {
		return icstockBill;
	}

	public void setIcstockBill(ICStockBill icstockBill) {
		this.icstockBill = icstockBill;
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

	public Stock_K3 getStock2() {
		return stock2;
	}

	public void setStock2(Stock_K3 stock2) {
		this.stock2 = stock2;
	}

	public StockPosition_K3 getStockPos2() {
		return stockPos2;
	}

	public void setStockPos2(StockPosition_K3 stockPos2) {
		this.stockPos2 = stockPos2;
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

	public boolean isShowButton() {
		return showButton;
	}

	public void setShowButton(boolean showButton) {
		this.showButton = showButton;
	}

	public String getSmBatchCode() {
		return smBatchCode;
	}

	public void setSmBatchCode(String smBatchCode) {
		this.smBatchCode = smBatchCode;
	}

	public String getSmSnCode() {
		return smSnCode;
	}

	public void setSmSnCode(String smSnCode) {
		this.smSnCode = smSnCode;
	}

	public double getSmQty() {
		return smQty;
	}

	public void setSmQty(double smQty) {
		this.smQty = smQty;
	}

	public String getStrBatchCode() {
		// 存在小写的逗号（,）,且大于1
		if(Comm.isNULLS(strBatchCode).indexOf(",") > -1 && Comm.isNULLS(strBatchCode).length() > 0) {
			return strBatchCode.substring(0, strBatchCode.length()-1);
		}
		return strBatchCode;
	}

	public void setStrBatchCode(String strBatchCode) {
		this.strBatchCode = strBatchCode;
	}

	public String getStrBarcode() {
		return strBarcode;
	}

	public void setStrBarcode(String strBarcode) {
		this.strBarcode = strBarcode;
	}

	public double getInventoryNowQty() {
		return inventoryNowQty;
	}

	public void setInventoryNowQty(double inventoryNowQty) {
		this.inventoryNowQty = inventoryNowQty;
	}

	public double getInStockQty() {
		return inStockQty;
	}

	public void setInStockQty(double inStockQty) {
		this.inStockQty = inStockQty;
	}

	public double getOutStockQty() {
		return outStockQty;
	}

	public void setOutStockQty(double outStockQty) {
		this.outStockQty = outStockQty;
	}

	public int getFsourceSeq() {
		return fsourceSeq;
	}

	public void setFsourceSeq(int fsourceSeq) {
		this.fsourceSeq = fsourceSeq;
	}

	public String getForderDate() {
		return forderDate;
	}

	public void setForderDate(String forderDate) {
		this.forderDate = forderDate;
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

	public int getForderSeq() {
		return forderSeq;
	}

	public void setForderSeq(int forderSeq) {
		this.forderSeq = forderSeq;
	}

	public List<ICStockBillEntry_Barcode> getIcstockBillEntry_Barcodes() {
		if(icstockBillEntry_Barcodes == null) {
			icstockBillEntry_Barcodes = new ArrayList<>();
		}
		return icstockBillEntry_Barcodes;
	}

	public void setIcstockBillEntry_Barcodes(List<ICStockBillEntry_Barcode> icstockBillEntry_Barcodes) {
		this.icstockBillEntry_Barcodes = icstockBillEntry_Barcodes;
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

	public String getFmtoNo() {
		return fmtoNo;
	}

	public void setFmtoNo(String fmtoNo) {
		this.fmtoNo = fmtoNo;
	}

	public String getCustMtlId() {
		return custMtlId;
	}

	public void setCustMtlId(String custMtlId) {
		this.custMtlId = custMtlId;
	}

}

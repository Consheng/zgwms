package ykk.xc.com.zgwms.bean.sales;

import java.io.Serializable;

import ykk.xc.com.zgwms.bean.k3Bean.Material_K3;
import ykk.xc.com.zgwms.bean.k3Bean.StockPosition_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Stock_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Unit_K3;

/**
 * 发货通知单分录
 * @author Administrator
 *
 */
public class DeliveryNoticeEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	private int fid; 						// 单据id,
	private int fentryId;					// 分录id
	private int fmaterialId;				// 物料id
	private int funitId;					// 单位id
	private double fqty;					// 数量
	private double fprice;					// 单价
	private double ftaxPrice;				// 含税单价
	private double ftaxRate;				// 税率
	private double famount;					// 金额
	private String fnote;					// 摘要
	private String fcloseStatus;			// 业务关闭（A:正常 B:业务关闭）
	private int fshipmentStockId;			// 仓库id
	private int fshipmentStockLocId;		// 仓库id
	private String fsrcType;				// 源单类型
	private String fsrcBillNo;				// 源单单号
	private String forderNo;				// 订单单号
	private String forderSeq;				// 订单行号
	private int salOrderId;					// 订单id
	private int salOrderEntryId;			// 订单分录id
	private String sal_gy_purNo;			// 销售分录--管易订单号
	private String personalCarSeriesNumber;	// 定制车系代码
	private String personalCarSeriesName;	// 定制车系名称
	private String personalCarTypeNumber;	// 定制车型代码
	private String personalCarTypeName;		// 定制车型名称
	private String personalCarVersionNumber;// 定制车型版号代码
	private String personalCarVersionName;	// 定制车型版号名称

	private DeliveryNotice deliveryNotice;
	private Material_K3 material;
	private Unit_K3 unit;
	private Stock_K3 stock;
	private StockPosition_K3 stockPos;

	// 临时字段，不存表
	private double usableQty; 			// 可用的数量
	
	public DeliveryNoticeEntry() {
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

	public String getFcloseStatus() {
		return fcloseStatus;
	}

	public void setFcloseStatus(String fcloseStatus) {
		this.fcloseStatus = fcloseStatus;
	}

	public int getFshipmentStockId() {
		return fshipmentStockId;
	}

	public void setFshipmentStockId(int fshipmentStockId) {
		this.fshipmentStockId = fshipmentStockId;
	}

	public int getFshipmentStockLocId() {
		return fshipmentStockLocId;
	}

	public void setFshipmentStockLocId(int fshipmentStockLocId) {
		this.fshipmentStockLocId = fshipmentStockLocId;
	}

	public String getFsrcType() {
		return fsrcType;
	}

	public void setFsrcType(String fsrcType) {
		this.fsrcType = fsrcType;
	}

	public String getFsrcBillNo() {
		return fsrcBillNo;
	}

	public void setFsrcBillNo(String fsrcBillNo) {
		this.fsrcBillNo = fsrcBillNo;
	}

	public String getForderNo() {
		return forderNo;
	}

	public void setForderNo(String forderNo) {
		this.forderNo = forderNo;
	}

	public String getForderSeq() {
		return forderSeq;
	}

	public void setForderSeq(String forderSeq) {
		this.forderSeq = forderSeq;
	}

	public int getSalOrderId() {
		return salOrderId;
	}

	public void setSalOrderId(int salOrderId) {
		this.salOrderId = salOrderId;
	}

	public int getSalOrderEntryId() {
		return salOrderEntryId;
	}

	public void setSalOrderEntryId(int salOrderEntryId) {
		this.salOrderEntryId = salOrderEntryId;
	}

	public String getSal_gy_purNo() {
		return sal_gy_purNo;
	}

	public void setSal_gy_purNo(String sal_gy_purNo) {
		this.sal_gy_purNo = sal_gy_purNo;
	}

	public DeliveryNotice getDeliveryNotice() {
		return deliveryNotice;
	}

	public void setDeliveryNotice(DeliveryNotice deliveryNotice) {
		this.deliveryNotice = deliveryNotice;
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

	public String getPersonalCarSeriesNumber() {
		return personalCarSeriesNumber;
	}

	public void setPersonalCarSeriesNumber(String personalCarSeriesNumber) {
		this.personalCarSeriesNumber = personalCarSeriesNumber;
	}

	public String getPersonalCarSeriesName() {
		return personalCarSeriesName;
	}

	public void setPersonalCarSeriesName(String personalCarSeriesName) {
		this.personalCarSeriesName = personalCarSeriesName;
	}

	public String getPersonalCarTypeNumber() {
		return personalCarTypeNumber;
	}

	public void setPersonalCarTypeNumber(String personalCarTypeNumber) {
		this.personalCarTypeNumber = personalCarTypeNumber;
	}

	public String getPersonalCarTypeName() {
		return personalCarTypeName;
	}

	public void setPersonalCarTypeName(String personalCarTypeName) {
		this.personalCarTypeName = personalCarTypeName;
	}

	public String getPersonalCarVersionNumber() {
		return personalCarVersionNumber;
	}

	public void setPersonalCarVersionNumber(String personalCarVersionNumber) {
		this.personalCarVersionNumber = personalCarVersionNumber;
	}

	public String getPersonalCarVersionName() {
		return personalCarVersionName;
	}

	public void setPersonalCarVersionName(String personalCarVersionName) {
		this.personalCarVersionName = personalCarVersionName;
	}

}

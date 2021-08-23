package ykk.xc.com.zgwms.bean.warehouse;

import java.io.Serializable;

import ykk.xc.com.zgwms.bean.k3Bean.Material_K3;
import ykk.xc.com.zgwms.bean.k3Bean.StockPosition_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Stock_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Unit_K3;

/**
 * 采购入库分录
 * @author Administrator
 *
 */
public class StkInStockEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	private int fid;							// 主表id
	private int fentryId;						// 分录id
	private int fmaterialId;					// 物料id
	private int funitId;						// 单位id
	private String frowType;					// 产品类型
	private double fmustQty;					// 应收数量
	private double frealQty;					// 实收数量
	private double fprice;						// 单价
	private double ftaxPrice;					// 含税单价
	private int fstockId;						// 仓库id
	private int fstockLocId;					// 库位id
	private int fstockStatusId;					// 库存状态id
	private String stockStatus_fnumber;			// 库存状态代码
	private String stockStatus_fname;			// 库存状态名称

	private StkInStock stkInStock;
	private Material_K3 material;
	private Unit_K3 unit;
	private Stock_K3 stock;
	private StockPosition_K3 stockPos;

	// 临时字段，不存表
	private double usableQty; 			// 可用的数量

	public StkInStockEntry() {
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

	public String getFrowType() {
		return frowType;
	}

	public void setFrowType(String frowType) {
		this.frowType = frowType;
	}

	public double getFmustQty() {
		return fmustQty;
	}

	public void setFmustQty(double fmustQty) {
		this.fmustQty = fmustQty;
	}

	public double getFrealQty() {
		return frealQty;
	}

	public void setFrealQty(double frealQty) {
		this.frealQty = frealQty;
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

	public int getFstockStatusId() {
		return fstockStatusId;
	}

	public void setFstockStatusId(int fstockStatusId) {
		this.fstockStatusId = fstockStatusId;
	}

	public String getStockStatus_fnumber() {
		return stockStatus_fnumber;
	}

	public void setStockStatus_fnumber(String stockStatus_fnumber) {
		this.stockStatus_fnumber = stockStatus_fnumber;
	}

	public String getStockStatus_fname() {
		return stockStatus_fname;
	}

	public void setStockStatus_fname(String stockStatus_fname) {
		this.stockStatus_fname = stockStatus_fname;
	}

	public StkInStock getStkInStock() {
		return stkInStock;
	}

	public void setStkInStock(StkInStock stkInStock) {
		this.stkInStock = stkInStock;
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


}

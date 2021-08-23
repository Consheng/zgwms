package ykk.xc.com.zgwms.bean.pur;

import java.io.Serializable;

import ykk.xc.com.zgwms.bean.k3Bean.Material_K3;
import ykk.xc.com.zgwms.bean.k3Bean.StockPosition_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Stock_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Unit_K3;

/**
 * 采购收料通知单分录
 * @author Administrator
 *
 */
public class PurReceiveEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	private int fid; 					// 单据id
	private int fentryId; 				// 分录id
	private int fmaterialId; 			// 物料id
	private int funitId;				// 单位id
	private double fdisburdenQty;		// 装卸数量
	private double factReceiveQty; 		// 送料数量
	private double finStockBaseQty; 	// 入库数量（审核后）
	private double finStockJoinBaseQty; // 累计入库的数量
	private double fprice;				// 单价
	private double famount;				// 金额
	private char fmrpcloseStatus;		// 业务关闭：A：正常 B：业务关闭
	private String fdescription;		// 描述
	private int fstockId;				// 仓库id
	private int fstockLocId;			// 库位id

	private PurReceive purReceive;
	private Material_K3 material;
	private Unit_K3 unit;
	private Stock_K3 stock;
	private StockPosition_K3 stockPos;

	// 临时字段，不存表
	private double usableQty; 			// 可用的数量

	public PurReceiveEntry() {
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

	public double getFdisburdenQty() {
		return fdisburdenQty;
	}

	public void setFdisburdenQty(double fdisburdenQty) {
		this.fdisburdenQty = fdisburdenQty;
	}

	public double getFactReceiveQty() {
		return factReceiveQty;
	}

	public void setFactReceiveQty(double factReceiveQty) {
		this.factReceiveQty = factReceiveQty;
	}

	public double getFinStockBaseQty() {
		return finStockBaseQty;
	}

	public void setFinStockBaseQty(double finStockBaseQty) {
		this.finStockBaseQty = finStockBaseQty;
	}

	public double getFinStockJoinBaseQty() {
		return finStockJoinBaseQty;
	}

	public void setFinStockJoinBaseQty(double finStockJoinBaseQty) {
		this.finStockJoinBaseQty = finStockJoinBaseQty;
	}

	public double getFprice() {
		return fprice;
	}

	public void setFprice(double fprice) {
		this.fprice = fprice;
	}

	public double getFamount() {
		return famount;
	}

	public void setFamount(double famount) {
		this.famount = famount;
	}

	public char getFmrpcloseStatus() {
		return fmrpcloseStatus;
	}

	public void setFmrpcloseStatus(char fmrpcloseStatus) {
		this.fmrpcloseStatus = fmrpcloseStatus;
	}

	public String getFdescription() {
		return fdescription;
	}

	public void setFdescription(String fdescription) {
		this.fdescription = fdescription;
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

	public PurReceive getPurReceive() {
		return purReceive;
	}

	public void setPurReceive(PurReceive purReceive) {
		this.purReceive = purReceive;
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

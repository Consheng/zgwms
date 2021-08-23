package ykk.xc.com.zgwms.bean.warehouse;

import java.io.Serializable;

import ykk.xc.com.zgwms.bean.k3Bean.Material_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Organization_K3;
import ykk.xc.com.zgwms.bean.k3Bean.StockPosition_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Stock_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Unit_K3;

/**
 * 库存物料盘点作业单表体
 * @author Administrator
 *
 */
public class StkCountInputEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	private int fid;					// 内码id
	private int fentryId;				// 分录id
	private int fseq;					// 分录号
	private char fentryType;			// 分录类型（A:备份而来，B：手工添加，C：引入添加）
	private int fmaterialId;			// 物料id
	private int funitId;				// 单位id
	private int fstockId;				// 仓库id
	private int fstockLocId;			// 库位id
	private double facctQty;			// 账存数量
	private double fcountQty;			// 盘点数量
	private double fgainQty;			// 盘盈数量
	private double flossQty;			// 盘亏数量
	private String fownerTypeId;		// 货主类型id
	private int fownerId;				// 货主id
	private String owner_fnumber;		// 货主代码
	private String owner_fname;			// 货主名称
	private String fkeeperTypeId;		// 保管者类型id
	private int fkeeperId;				// 保管者id
	private String keeper_fnumber;		// 保管者代码
	private String keeper_fname;		// 保管者代码

	private StkCountInput stkCountInput;
	private Material_K3 material;
	private Unit_K3 unit;
	private Stock_K3 stock;
	private StockPosition_K3 stockPos;
	private Organization_K3 stockOrg;

	// 临时字段
	private boolean wmsUploadStatus; 		// WMS上传状态
	
	public StkCountInputEntry() {
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

	public char getFentryType() {
		return fentryType;
	}

	public void setFentryType(char fentryType) {
		this.fentryType = fentryType;
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

	public double getFacctQty() {
		return facctQty;
	}

	public void setFacctQty(double facctQty) {
		this.facctQty = facctQty;
	}

	public double getFcountQty() {
		return fcountQty;
	}

	public void setFcountQty(double fcountQty) {
		this.fcountQty = fcountQty;
	}

	public double getFgainQty() {
		return fgainQty;
	}

	public void setFgainQty(double fgainQty) {
		this.fgainQty = fgainQty;
	}

	public double getFlossQty() {
		return flossQty;
	}

	public void setFlossQty(double flossQty) {
		this.flossQty = flossQty;
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

	public String getOwner_fnumber() {
		return owner_fnumber;
	}

	public void setOwner_fnumber(String owner_fnumber) {
		this.owner_fnumber = owner_fnumber;
	}

	public String getOwner_fname() {
		return owner_fname;
	}

	public void setOwner_fname(String owner_fname) {
		this.owner_fname = owner_fname;
	}

	public String getFkeeperTypeId() {
		return fkeeperTypeId;
	}

	public void setFkeeperTypeId(String fkeeperTypeId) {
		this.fkeeperTypeId = fkeeperTypeId;
	}

	public int getFkeeperId() {
		return fkeeperId;
	}

	public void setFkeeperId(int fkeeperId) {
		this.fkeeperId = fkeeperId;
	}

	public String getKeeper_fnumber() {
		return keeper_fnumber;
	}

	public void setKeeper_fnumber(String keeper_fnumber) {
		this.keeper_fnumber = keeper_fnumber;
	}

	public String getKeeper_fname() {
		return keeper_fname;
	}

	public void setKeeper_fname(String keeper_fname) {
		this.keeper_fname = keeper_fname;
	}

	public StkCountInput getStkCountInput() {
		return stkCountInput;
	}

	public void setStkCountInput(StkCountInput stkCountInput) {
		this.stkCountInput = stkCountInput;
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

	public Organization_K3 getStockOrg() {
		return stockOrg;
	}

	public void setStockOrg(Organization_K3 stockOrg) {
		this.stockOrg = stockOrg;
	}

	public boolean isWmsUploadStatus() {
		return wmsUploadStatus;
	}

	public void setWmsUploadStatus(boolean wmsUploadStatus) {
		this.wmsUploadStatus = wmsUploadStatus;
	}

	
}

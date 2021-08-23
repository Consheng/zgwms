package ykk.xc.com.zgwms.bean.warehouse;

import java.io.Serializable;

import ykk.xc.com.zgwms.bean.k3Bean.Material_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Organization_K3;
import ykk.xc.com.zgwms.bean.k3Bean.StockPosition_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Stock_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Unit_K3;

/**
 * 分步式调出单分录表
 * @author Administrator
 *
 */
public class StkTransferOutEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	private int fid;						// 主表id
	private int fentryId;					// 分录id
	private int fmaterialId;				// 物料id
	private int funitId;						// 单位id
	private int fsrcStockId;				// 调出仓库id
	private int fsrcStockLocId;				// 调出库位id
	private int fdestStockId;				// 调入仓库id
	private int fdestStockLocId;			// 调入库位id
	private double fqty;					// 数量
	private int fsrcStockStatusId;			// 调出库存状态
	private String outStockStatus_fnumber;	// 调出库存状态代码
	private String outStockStatus_fname;	// 调出库存状态名称
	private int fdestStockStatusId;			// 调入库存状态
	private String inStockStatus_fnumber;	// 调出库存状态代码
	private String inStockStatus_fname;		// 调出库存状态名称
	private String fownerTypeId;			// 调出货主类型（）
	private int fownerId;					// 调出货主id
	private String ownerOut_fnumber;		// 调出货主代码
	private String ownerOut_fname;			// 调出货主名称
	private String fkeeperTypeId;			// 调出保管者类型id
	private int fkeeperId;					// 调出保管者id
	private String fownerTypeInId;				// 调入货主类型id
	private int fownerInId;					// 调入货主id
	private String ownerIn_fnumber;			// 调入货主代码
	private String ownerIn_fname;			// 调入货主名称
	private String fkeeperTypeInId;			// 调入保管者类型id
	private int fkeeperInId;				// 调入保管者id

	private StkTransferOut stkTransferOut;
	private Material_K3 material;
	private Unit_K3 unit;
	private Stock_K3 srcStock;
	private StockPosition_K3 srcStockPos;
	private Stock_K3 destStock;
	private StockPosition_K3 destStockPos;
	private Organization_K3 keeper;
	private Organization_K3 keeperIn;

	// 临时字段，不存表
	private double usableQty; 			// 可用的数量

	public StkTransferOutEntry() {
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

	public int getFsrcStockId() {
		return fsrcStockId;
	}

	public void setFsrcStockId(int fsrcStockId) {
		this.fsrcStockId = fsrcStockId;
	}

	public int getFsrcStockLocId() {
		return fsrcStockLocId;
	}

	public void setFsrcStockLocId(int fsrcStockLocId) {
		this.fsrcStockLocId = fsrcStockLocId;
	}

	public int getFdestStockId() {
		return fdestStockId;
	}

	public void setFdestStockId(int fdestStockId) {
		this.fdestStockId = fdestStockId;
	}

	public int getFdestStockLocId() {
		return fdestStockLocId;
	}

	public void setFdestStockLocId(int fdestStockLocId) {
		this.fdestStockLocId = fdestStockLocId;
	}

	public double getFqty() {
		return fqty;
	}

	public void setFqty(double fqty) {
		this.fqty = fqty;
	}

	public int getFsrcStockStatusId() {
		return fsrcStockStatusId;
	}

	public void setFsrcStockStatusId(int fsrcStockStatusId) {
		this.fsrcStockStatusId = fsrcStockStatusId;
	}

	public String getOutStockStatus_fnumber() {
		return outStockStatus_fnumber;
	}

	public void setOutStockStatus_fnumber(String outStockStatus_fnumber) {
		this.outStockStatus_fnumber = outStockStatus_fnumber;
	}

	public String getOutStockStatus_fname() {
		return outStockStatus_fname;
	}

	public void setOutStockStatus_fname(String outStockStatus_fname) {
		this.outStockStatus_fname = outStockStatus_fname;
	}

	public int getFdestStockStatusId() {
		return fdestStockStatusId;
	}

	public void setFdestStockStatusId(int fdestStockStatusId) {
		this.fdestStockStatusId = fdestStockStatusId;
	}

	public String getInStockStatus_fnumber() {
		return inStockStatus_fnumber;
	}

	public void setInStockStatus_fnumber(String inStockStatus_fnumber) {
		this.inStockStatus_fnumber = inStockStatus_fnumber;
	}

	public String getInStockStatus_fname() {
		return inStockStatus_fname;
	}

	public void setInStockStatus_fname(String inStockStatus_fname) {
		this.inStockStatus_fname = inStockStatus_fname;
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

	public String getOwnerOut_fnumber() {
		return ownerOut_fnumber;
	}

	public void setOwnerOut_fnumber(String ownerOut_fnumber) {
		this.ownerOut_fnumber = ownerOut_fnumber;
	}

	public String getOwnerOut_fname() {
		return ownerOut_fname;
	}

	public void setOwnerOut_fname(String ownerOut_fname) {
		this.ownerOut_fname = ownerOut_fname;
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

	public String getFownerTypeInId() {
		return fownerTypeInId;
	}

	public void setFownerTypeInId(String fownerTypeInId) {
		this.fownerTypeInId = fownerTypeInId;
	}

	public int getFownerInId() {
		return fownerInId;
	}

	public void setFownerInId(int fownerInId) {
		this.fownerInId = fownerInId;
	}

	public String getOwnerIn_fnumber() {
		return ownerIn_fnumber;
	}

	public void setOwnerIn_fnumber(String ownerIn_fnumber) {
		this.ownerIn_fnumber = ownerIn_fnumber;
	}

	public String getOwnerIn_fname() {
		return ownerIn_fname;
	}

	public void setOwnerIn_fname(String ownerIn_fname) {
		this.ownerIn_fname = ownerIn_fname;
	}

	public String getFkeeperTypeInId() {
		return fkeeperTypeInId;
	}

	public void setFkeeperTypeInId(String fkeeperTypeInId) {
		this.fkeeperTypeInId = fkeeperTypeInId;
	}

	public int getFkeeperInId() {
		return fkeeperInId;
	}

	public void setFkeeperInId(int fkeeperInId) {
		this.fkeeperInId = fkeeperInId;
	}

	public StkTransferOut getStkTransferOut() {
		return stkTransferOut;
	}

	public void setStkTransferOut(StkTransferOut stkTransferOut) {
		this.stkTransferOut = stkTransferOut;
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

	public Stock_K3 getSrcStock() {
		return srcStock;
	}

	public void setSrcStock(Stock_K3 srcStock) {
		this.srcStock = srcStock;
	}

	public StockPosition_K3 getSrcStockPos() {
		return srcStockPos;
	}

	public void setSrcStockPos(StockPosition_K3 srcStockPos) {
		this.srcStockPos = srcStockPos;
	}

	public Stock_K3 getDestStock() {
		return destStock;
	}

	public void setDestStock(Stock_K3 destStock) {
		this.destStock = destStock;
	}

	public StockPosition_K3 getDestStockPos() {
		return destStockPos;
	}

	public void setDestStockPos(StockPosition_K3 destStockPos) {
		this.destStockPos = destStockPos;
	}

	public Organization_K3 getKeeper() {
		return keeper;
	}

	public void setKeeper(Organization_K3 keeper) {
		this.keeper = keeper;
	}

	public Organization_K3 getKeeperIn() {
		return keeperIn;
	}

	public void setKeeperIn(Organization_K3 keeperIn) {
		this.keeperIn = keeperIn;
	}

	public double getUsableQty() {
		return usableQty;
	}

	public void setUsableQty(double usableQty) {
		this.usableQty = usableQty;
	}
}

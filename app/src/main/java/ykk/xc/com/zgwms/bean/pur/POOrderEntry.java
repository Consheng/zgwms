package ykk.xc.com.zgwms.bean.pur;

import java.io.Serializable;

import ykk.xc.com.zgwms.bean.k3Bean.Material_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Organization_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Unit_K3;

/**
 * 采购订单分录
 * @author Administrator
 *
 */
public class POOrderEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	private int fid;					// 单据id
	private int fentryId; 				// 分录id
	private int fseq;					// 分录行号
	private int fmaterialId; 			// 物料id
	private int funitId;				// 单位id
	private String frowType;			// 产品类型
	private double fqty;				// 数量
	private double fstockInQty; 		// 累计入库数量
	private double fdisburdenQty;		// 装卸数量
	private double fprice;				// 单价
	private double famount;				// 金额
	private char fmrpfreezeStatus;		// 业务冻结：A：正常 B：业务冻结
	private char fterMinaterId;			// 业务终止：A：正常 B：业务终止
	private char fmrpcloseStatus;		// 业务关闭：A：正常 B：业务关闭
	private String fproductType;		// 产品类型：1:主产品 2:联产品 3:副产品(委外申请单下推时携带)
	private int freceiveOrgId;			// 收料组织id
	private String fsrcBillNo;			// 源单号
	private String fnote;				// 备注
	private int frequireOrgId;			// 需求组织id

	private POOrder poOrder;
	private Material_K3 material;
	private Unit_K3 unit;
	private Organization_K3 receiveOrg;
	private Organization_K3 requireOrg;

	// 临时字段，不存表
	private int createCodeQty;			//已生码数量
	private int createSatus;			//生码状态
	private double usableQty; 			// 可用的数量

	public POOrderEntry() {
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

	public double getFstockInQty() {
		return fstockInQty;
	}

	public void setFstockInQty(double fstockInQty) {
		this.fstockInQty = fstockInQty;
	}

	public double getFdisburdenQty() {
		return fdisburdenQty;
	}

	public void setFdisburdenQty(double fdisburdenQty) {
		this.fdisburdenQty = fdisburdenQty;
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

	public char getFmrpfreezeStatus() {
		return fmrpfreezeStatus;
	}

	public void setFmrpfreezeStatus(char fmrpfreezeStatus) {
		this.fmrpfreezeStatus = fmrpfreezeStatus;
	}

	public char getFterMinaterId() {
		return fterMinaterId;
	}

	public void setFterMinaterId(char fterMinaterId) {
		this.fterMinaterId = fterMinaterId;
	}

	public char getFmrpcloseStatus() {
		return fmrpcloseStatus;
	}

	public void setFmrpcloseStatus(char fmrpcloseStatus) {
		this.fmrpcloseStatus = fmrpcloseStatus;
	}

	public String getFproductType() {
		return fproductType;
	}

	public void setFproductType(String fproductType) {
		this.fproductType = fproductType;
	}

	public int getFreceiveOrgId() {
		return freceiveOrgId;
	}

	public void setFreceiveOrgId(int freceiveOrgId) {
		this.freceiveOrgId = freceiveOrgId;
	}

	public String getFsrcBillNo() {
		return fsrcBillNo;
	}

	public void setFsrcBillNo(String fsrcBillNo) {
		this.fsrcBillNo = fsrcBillNo;
	}

	public String getFnote() {
		return fnote;
	}

	public void setFnote(String fnote) {
		this.fnote = fnote;
	}

	public int getFrequireOrgId() {
		return frequireOrgId;
	}

	public void setFrequireOrgId(int frequireOrgId) {
		this.frequireOrgId = frequireOrgId;
	}

	public POOrder getPoOrder() {
		return poOrder;
	}

	public void setPoOrder(POOrder poOrder) {
		this.poOrder = poOrder;
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

	public Organization_K3 getReceiveOrg() {
		return receiveOrg;
	}

	public void setReceiveOrg(Organization_K3 receiveOrg) {
		this.receiveOrg = receiveOrg;
	}

	public Organization_K3 getRequireOrg() {
		return requireOrg;
	}

	public void setRequireOrg(Organization_K3 requireOrg) {
		this.requireOrg = requireOrg;
	}

	public int getCreateCodeQty() {
		return createCodeQty;
	}

	public void setCreateCodeQty(int createCodeQty) {
		this.createCodeQty = createCodeQty;
	}

	public int getCreateSatus() {
		return createSatus;
	}

	public void setCreateSatus(int createSatus) {
		this.createSatus = createSatus;
	}

	public double getUsableQty() {
		return usableQty;
	}

	public void setUsableQty(double usableQty) {
		this.usableQty = usableQty;
	}



}

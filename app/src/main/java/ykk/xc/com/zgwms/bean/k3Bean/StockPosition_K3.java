package ykk.xc.com.zgwms.bean.k3Bean;

import java.io.Serializable;

/**
 * 库位表
 */
public class StockPosition_K3 implements Serializable {
	private static final long serialVersionUID = 1L;

	/*k3组织多语言表fpkid*/
	private int fpkId;
	// K3库位id
	private int fid;
	// K3库位编码
	private String fnumber;
	// K3库位名称
	private String fname;
	// K3库位使用组织id
	private int fuseOrgId;

	// 使用组织实体类
	private Organization_K3 useOrg;
	private Stock_K3 stock;

	// 临时字段，不存表
	private boolean check;
	private String className; // 前段用到的，请勿删除

	public StockPosition_K3() {
		super();
	}

	public int getFpkId() {
		return fpkId;
	}

	public void setFpkId(int fpkId) {
		this.fpkId = fpkId;
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public String getFnumber() {
		return fnumber;
	}

	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public int getFuseOrgId() {
		return fuseOrgId;
	}

	public void setFuseOrgId(int fuseOrgId) {
		this.fuseOrgId = fuseOrgId;
	}

	public Organization_K3 getUseOrg() {
		return useOrg;
	}

	public void setUseOrg(Organization_K3 useOrg) {
		this.useOrg = useOrg;
	}

	public Stock_K3 getStock() {
		return stock;
	}

	public void setStock(Stock_K3 stock) {
		this.stock = stock;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}

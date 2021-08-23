package ykk.xc.com.zgwms.bean.k3Bean;

import java.io.Serializable;

/**
 * 仓库表
 */
public class Stock_K3 implements Serializable {
	private static final long serialVersionUID = 1L;

	/*k3组织多语言表fpkid*/
	private int fpkId;
	// K3部门id
	private int fstockId;
	// K3部门编码
	private String fnumber;
	// K3部门名称
	private String fname;
	// K3部门使用组织id
	private int fuseOrgId;
	// 是否启用仓位
	private int fisOpenLocation;

	// 使用组织实体类
	private Organization_K3 useOrg;

	// 临时字段，不存表
	private boolean check;
	private String className; // 前段用到的，请勿删除

	public Stock_K3() {
		super();
	}

	public int getFpkId() {
		return fpkId;
	}

	public void setFpkId(int fpkId) {
		this.fpkId = fpkId;
	}

	public int getFstockId() {
		return fstockId;
	}

	public void setFstockId(int fstockId) {
		this.fstockId = fstockId;
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

	public int getFisOpenLocation() {
		return fisOpenLocation;
	}

	public void setFisOpenLocation(int fisOpenLocation) {
		this.fisOpenLocation = fisOpenLocation;
	}

	public Organization_K3 getUseOrg() {
		return useOrg;
	}

	public void setUseOrg(Organization_K3 useOrg) {
		this.useOrg = useOrg;
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

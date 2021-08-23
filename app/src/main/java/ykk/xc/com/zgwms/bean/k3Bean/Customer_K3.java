package ykk.xc.com.zgwms.bean.k3Bean;

import java.io.Serializable;

/**
* 客户表
*/
public class Customer_K3 implements Serializable {
	private static final long serialVersionUID = 1L;

	private int fpkId;			/*k3组织多语言表fpkid*/
	private int fcustId;		// K3部门id
	private String fnumber;		// K3部门编码
	private String fname;		// K3部门名称
	private int fuseOrgId;		// 使用组织id
	private String contact;		// 联系人
	private String fmobile;		// 联系人手机
	private String faddress;	// 收货地址

	private Organization_K3 useOrg;

	// 临时字段，不存表
	private boolean check;

	public Customer_K3() {
		super();
	}

	public int getFpkId() {
		return fpkId;
	}

	public void setFpkId(int fpkId) {
		this.fpkId = fpkId;
	}

	public int getFcustId() {
		return fcustId;
	}

	public void setFcustId(int fcustId) {
		this.fcustId = fcustId;
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

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getFmobile() {
		return fmobile;
	}

	public void setFmobile(String fmobile) {
		this.fmobile = fmobile;
	}

	public String getFaddress() {
		return faddress;
	}

	public void setFaddress(String faddress) {
		this.faddress = faddress;
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


}

package ykk.xc.com.zgwms.bean.k3Bean;

import java.io.Serializable;

public class Staff_K3 implements Serializable {
	private static final long serialVersionUID = 1L;

	private int fpkId;						// k3组织多语言表fpkid
	private int fstaffId;					// 员工id
	private String fnumber;					// 员工编号
	private String fname;					// 员工名称
	private String fstaffNumber;			// 员工任岗编码
	private int fuseOrgId;					// 使用组织id
	private int fdeptId;					// 部门id
	private int fpostId;					// 岗位id

	private Organization_K3 useOrg;
	private Department_K3 department;
//	@Lazy
//	private Post post;

	public Staff_K3() {
		super();
	}

	public int getFpkId() {
		return fpkId;
	}

	public void setFpkId(int fpkId) {
		this.fpkId = fpkId;
	}

	public int getFstaffId() {
		return fstaffId;
	}

	public void setFstaffId(int fstaffId) {
		this.fstaffId = fstaffId;
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

	public String getFstaffNumber() {
		return fstaffNumber;
	}

	public void setFstaffNumber(String fstaffNumber) {
		this.fstaffNumber = fstaffNumber;
	}

	public int getFuseOrgId() {
		return fuseOrgId;
	}

	public void setFuseOrgId(int fuseOrgId) {
		this.fuseOrgId = fuseOrgId;
	}

	public int getFdeptId() {
		return fdeptId;
	}

	public void setFdeptId(int fdeptId) {
		this.fdeptId = fdeptId;
	}

	public int getFpostId() {
		return fpostId;
	}

	public void setFpostId(int fpostId) {
		this.fpostId = fpostId;
	}

	public Organization_K3 getUseOrg() {
		return useOrg;
	}

	public void setUseOrg(Organization_K3 useOrg) {
		this.useOrg = useOrg;
	}

	public Department_K3 getDepartment() {
		return department;
	}

	public void setDepartment(Department_K3 department) {
		this.department = department;
	}


}

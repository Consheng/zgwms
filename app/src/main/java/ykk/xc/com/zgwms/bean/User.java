package ykk.xc.com.zgwms.bean;

import java.io.Serializable;
import java.util.List;

import ykk.xc.com.zgwms.bean.k3Bean.Department_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Organization_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Staff_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Supplier_K3;

/**
 * 用户   (t_user)
 */
public class User implements Serializable {
    /* 用户id */
    private int id;

    /* 用户名 */
    private String username;

    /* 密码 */
    private String password;

    /* 性别 */
    private int sex;

    /* 真实姓名 */
    private String truename;

    /* 部门id */
    private int deptId;

    /* 创建时间 */
    private String createTime;

    /* 创建者id */
    private int createrId;

    /* 创建者名字 */
    private String createrName;

    /* 对应金蝶用户名 */
    private String kdUserName;

    /* 对应金蝶用户编码 */
    private String kdUserNumber;

    /* k3员工id */
    private int staffId;
    /* 状态：1.启用，2.禁用 */
    private int status;
    /* k3员工 */
     private Staff_K3 staff;
    private List<Role> roles;
    private List<SystemSet> sysSetList;

    /* 部门 */
    private Department_K3 department;

    /* by qxp 2018-10-15 仓库类 */
    private Stock stock;
    private StockPosition stockPos;
    /* k3账号id */
    private int kdAccountId;
    /* k3账号名称 */
    private String kdAccountName;
    /* k3账号 */
    private String kdAccount;
    /* k3账号密码 */
    private String kdAccountPassword;
    /* 用户类型。1内部用户，2供应商 */
    private int accountType;
    /* 供应商id */
    private int supplierId;
    private Supplier supplier;

    /*是否车间主管 A代表不是，B代表是*/
    private String workDirector;
    private int organizationId;		// 默认组织id
    private Organization_K3 organization;	// 默认组织对象

    public User() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getCreaterId() {
        return createrId;
    }

    public void setCreaterId(int createrId) {
        this.createrId = createrId;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public Department_K3 getDepartment() {
        return department;
    }

    public void setDepartment(Department_K3 department) {
        this.department = department;
    }

    public String getKdUserName() {
        return kdUserName;
    }

    public void setKdUserName(String kdUserName) {
        this.kdUserName = kdUserName;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public Staff_K3 getStaff() {
        return staff;
    }

    public void setStaff(Staff_K3 staff) {
        this.staff = staff;
    }

    public String getKdUserNumber() {
        return kdUserNumber;
    }

    public void setKdUserNumber(String kdUserNumber) {
        this.kdUserNumber = kdUserNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public int getKdAccountId() {
        return kdAccountId;
    }

    public void setKdAccountId(int kdAccountId) {
        this.kdAccountId = kdAccountId;
    }

    public String getKdAccountName() {
        return kdAccountName;
    }

    public void setKdAccountName(String kdAccountName) {
        this.kdAccountName = kdAccountName;
    }

    public String getKdAccount() {
        return kdAccount;
    }

    public void setKdAccount(String kdAccount) {
        this.kdAccount = kdAccount;
    }

    public String getKdAccountPassword() {
        return kdAccountPassword;
    }

    public void setKdAccountPassword(String kdAccountPassword) {
        this.kdAccountPassword = kdAccountPassword;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public StockPosition getStockPos() {
        return stockPos;
    }

    public void setStockPos(StockPosition stockPos) {
        this.stockPos = stockPos;
    }

    public List<SystemSet> getSysSetList() {
        return sysSetList;
    }

    public void setSysSetList(List<SystemSet> sysSetList) {
        this.sysSetList = sysSetList;
    }

    public String getWorkDirector() {
        return workDirector;
    }

    public void setWorkDirector(String workDirector) {
        this.workDirector = workDirector;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public Organization_K3 getOrganization() {
        return organization;
    }

    public void setOrganization(Organization_K3 organization) {
        this.organization = organization;
    }

}

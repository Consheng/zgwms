package ykk.xc.com.zgwms.bean.warehouse;

import java.io.Serializable;

import ykk.xc.com.zgwms.bean.k3Bean.Material_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Stock_K3;
import ykk.xc.com.zgwms.bean.k3Bean.Unit_K3;

/**
 * 销售出库单分录
 *
 * @author Administrator
 */
public class SalOutStockEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    private int fid;                            // 单据id,
    private int fentryId;                        // 分录id
    private int fseq;                            // 序号
    private int fmaterialId;                    // 物料id
    private int funitId;                        // 单位id
    private int fauxPropId;                        // 辅助属性id
    private double fmustQty;                    // 应发数量
    private double frealQty;                    // 实发数量
    private double fprice;                        // 单价
    private double ftaxPrice;                    // 含税单价
    private String fnote;                        // 摘要
    private int fstockId;                        // 仓库id
    private int fstockStatusId;                    // 库存状态id
    private String stockStatus_fnumber;            // 库存状态代码
    private String stockStatus_fname;            // 库存状态名称
    private int fbaseUnitId;                    // 基本单位id
    private double fbaseUnitQty;                // 基本单位数量
    private String fownerTypeId;                // 货主类型id
    private int fownerId;                        // 货主id
    private String fkeeperTypeId;                // 保管者类型
    private int fkeeperId;                        // 保管者id
    private String fmtoNo;                        // 计划跟踪号
    private String personalCarVersionNumber;    // 定制车型版号代码
    private String personalCarVersionLocation;    // 定制车型版号代码
    private String personalCarVersionName;        // 定制车型版号名称

    private String fauxpropid_103_number;        // 辅助属性（款式）代码
    private String fauxpropid_103_name;            // 辅助属性（款式）名称
    private String fauxpropid_104_number;        // 辅助属性（一层用料）代码
    private String fauxpropid_104_name;            // 辅助属性（一层用料）名称
    private String fauxpropid_105_number;        // 辅助属性（二层用料）代码
    private String fauxpropid_105_name;            // 辅助属性（二层用料）名称
    private String fauxpropid_106_number;        // 辅助属性（座位）代码
    private String fauxpropid_106_name;            // 辅助属性（座位）名称

    private SalOutStock salOutStock;
    private Material_K3 material;
    private Unit_K3 unit;
    private Stock_K3 stock;

    // 临时字段，不存表
    private double usableQty;            // 可用的数量
    private String smSnCode;            // 扫码的序列号
    private String smBatchCode;            // 扫码的批次号
    private double smQty;                // 扫码后计算出的数

    public SalOutStockEntry() {
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

    public int getFauxPropId() {
        return fauxPropId;
    }

    public void setFauxPropId(int fauxPropId) {
        this.fauxPropId = fauxPropId;
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

    public String getFnote() {
        return fnote;
    }

    public void setFnote(String fnote) {
        this.fnote = fnote;
    }

    public int getFstockId() {
        return fstockId;
    }

    public void setFstockId(int fstockId) {
        this.fstockId = fstockId;
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

    public int getFbaseUnitId() {
        return fbaseUnitId;
    }

    public void setFbaseUnitId(int fbaseUnitId) {
        this.fbaseUnitId = fbaseUnitId;
    }

    public double getFbaseUnitQty() {
        return fbaseUnitQty;
    }

    public void setFbaseUnitQty(double fbaseUnitQty) {
        this.fbaseUnitQty = fbaseUnitQty;
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

    public String getFmtoNo() {
        return fmtoNo;
    }

    public void setFmtoNo(String fmtoNo) {
        this.fmtoNo = fmtoNo;
    }

    public String getPersonalCarVersionNumber() {
        return personalCarVersionNumber;
    }

    public void setPersonalCarVersionNumber(String personalCarVersionNumber) {
        this.personalCarVersionNumber = personalCarVersionNumber;
    }

    public String getPersonalCarVersionLocation() {
        return personalCarVersionLocation;
    }

    public void setPersonalCarVersionLocation(String personalCarVersionLocation) {
        this.personalCarVersionLocation = personalCarVersionLocation;
    }

    public String getPersonalCarVersionName() {
        return personalCarVersionName;
    }

    public void setPersonalCarVersionName(String personalCarVersionName) {
        this.personalCarVersionName = personalCarVersionName;
    }

    public String getFauxpropid_103_number() {
        return fauxpropid_103_number;
    }

    public void setFauxpropid_103_number(String fauxpropid_103_number) {
        this.fauxpropid_103_number = fauxpropid_103_number;
    }

    public String getFauxpropid_103_name() {
        return fauxpropid_103_name;
    }

    public void setFauxpropid_103_name(String fauxpropid_103_name) {
        this.fauxpropid_103_name = fauxpropid_103_name;
    }

    public String getFauxpropid_104_number() {
        return fauxpropid_104_number;
    }

    public void setFauxpropid_104_number(String fauxpropid_104_number) {
        this.fauxpropid_104_number = fauxpropid_104_number;
    }

    public String getFauxpropid_104_name() {
        return fauxpropid_104_name;
    }

    public void setFauxpropid_104_name(String fauxpropid_104_name) {
        this.fauxpropid_104_name = fauxpropid_104_name;
    }

    public String getFauxpropid_105_number() {
        return fauxpropid_105_number;
    }

    public void setFauxpropid_105_number(String fauxpropid_105_number) {
        this.fauxpropid_105_number = fauxpropid_105_number;
    }

    public String getFauxpropid_105_name() {
        return fauxpropid_105_name;
    }

    public void setFauxpropid_105_name(String fauxpropid_105_name) {
        this.fauxpropid_105_name = fauxpropid_105_name;
    }

    public String getFauxpropid_106_number() {
        return fauxpropid_106_number;
    }

    public void setFauxpropid_106_number(String fauxpropid_106_number) {
        this.fauxpropid_106_number = fauxpropid_106_number;
    }

    public String getFauxpropid_106_name() {
        return fauxpropid_106_name;
    }

    public void setFauxpropid_106_name(String fauxpropid_106_name) {
        this.fauxpropid_106_name = fauxpropid_106_name;
    }

    public SalOutStock getSalOutStock() {
        return salOutStock;
    }

    public void setSalOutStock(SalOutStock salOutStock) {
        this.salOutStock = salOutStock;
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

    public double getUsableQty() {
        return usableQty;
    }

    public void setUsableQty(double usableQty) {
        this.usableQty = usableQty;
    }

    public String getSmSnCode() {
        return smSnCode;
    }

    public void setSmSnCode(String smSnCode) {
        this.smSnCode = smSnCode;
    }

    public String getSmBatchCode() {
        return smBatchCode;
    }

    public void setSmBatchCode(String smBatchCode) {
        this.smBatchCode = smBatchCode;
    }

    public double getSmQty() {
        return smQty;
    }

    public void setSmQty(double smQty) {
        this.smQty = smQty;
    }


}

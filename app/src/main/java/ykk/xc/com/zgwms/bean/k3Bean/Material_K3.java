package ykk.xc.com.zgwms.bean.k3Bean;

import java.io.Serializable;

import ykk.xc.com.zgwms.bean.BarCodeTable;
import ykk.xc.com.zgwms.bean.Stock;
import ykk.xc.com.zgwms.bean.StockPosition;

/**
 * 物料表
 */
public class Material_K3 implements Serializable {
	private static final long serialVersionUID = 1L;

	/* k3物料id */
	private int fmaterialId;
	/* k3物料编号 */
	private String fnumber;
	/* k3物料名称 */
	private String fname;
	/* 使用组织ID */
	private String fuseOrgId;
	/* 基本单位id */
	private int funitId;
	/* 是否启用批号管理，0代表不启用，1代表启用 */
	private int isBatchManager;
	/* 是否启用序列号管理，0代表不启用，1代表启用 */
	private int isSnManager;
	/* 系列id */
	private String seriesId;
	/* 系列名称 */
	private String seriesName;
	/* 系列编号 */
	private String seriesNumber;
	/* 车系id */
	private String carSeriesId;
	/* 车系名称 */
	private String carSeriesName;
	/* 车系编号 */
	private String carSeriesNumber;
	/* 车型id */
	private String carTypeId;
	/* 车型名称 */
	private String carTypeName;
	/* 车型编号 */
	private String carTypeNumber;
	/* 颜色id */
	private String colorId;
	/* 颜色名称 */
	private String colorName;
	/* 颜色编号 */
	private String colorNumber;
	/* 定价要素id */
	private String priceElementId;
	/* 定价要素名称 */
	private String priceElementName;
	/* 定价要素编号 */
	private String priceElementNumber;
	/* 类别id */
	private String categoryId;
	/* 类别名称 */
	private String categoryName;
	/* 类别编号 */
	private String categoryNumber;
	/* 款式id */
	private String styleId;
	/* 款式名称 */
	private String styleName;
	/* 款式编号 */
	private String styleNumber;
	/* 产品规格 */
	private String materialSize;
	/* 产品类别id */
	private int materialTypeId;
	/* k3是否允许采购超收 */
	private String isOvercharge;
	/* k3采购超收上限 */
	private double receiveMaxScale;
	/* k3采购超收下限 */
	private double receiveMinScale;
	/* k3旧物料编码 */
	private String oldNumber;
	/* k3旧物料名称 */
	private String oldName;
	/* 销售出库是否自动带出：默认0(不带出)，1带出 */
	private int isAotuBringOut;
	/* 备注 */
	private String remarks;
	/* 是否为定制类（0：否，1：是） */
	private int personal;

	private double finishReceiptOverRate;//生产入库超收比例
	private double finishReceiptShortRate;//生产入库欠收比例

	private Organization_K3 useOrg;
	private Unit_K3 unit;
	private MaterialType materialType;

	// 临时字段，不存表
	private boolean check; // 是否选中
	private double barcodeQty; // 条码数量


	public Material_K3() {
		super();
	}


	public int getFmaterialId() {
		return fmaterialId;
	}


	public void setFmaterialId(int fmaterialId) {
		this.fmaterialId = fmaterialId;
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


	public String getFuseOrgId() {
		return fuseOrgId;
	}


	public void setFuseOrgId(String fuseOrgId) {
		this.fuseOrgId = fuseOrgId;
	}


	public int getFunitId() {
		return funitId;
	}


	public void setFunitId(int funitId) {
		this.funitId = funitId;
	}


	public String getSeriesId() {
		return seriesId;
	}


	public void setSeriesId(String seriesId) {
		this.seriesId = seriesId;
	}


	public String getSeriesName() {
		return seriesName;
	}


	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}


	public String getSeriesNumber() {
		return seriesNumber;
	}


	public void setSeriesNumber(String seriesNumber) {
		this.seriesNumber = seriesNumber;
	}


	public String getCarSeriesId() {
		return carSeriesId;
	}


	public void setCarSeriesId(String carSeriesId) {
		this.carSeriesId = carSeriesId;
	}


	public String getCarSeriesName() {
		return carSeriesName;
	}


	public void setCarSeriesName(String carSeriesName) {
		this.carSeriesName = carSeriesName;
	}


	public String getCarSeriesNumber() {
		return carSeriesNumber;
	}


	public void setCarSeriesNumber(String carSeriesNumber) {
		this.carSeriesNumber = carSeriesNumber;
	}


	public String getCarTypeId() {
		return carTypeId;
	}


	public void setCarTypeId(String carTypeId) {
		this.carTypeId = carTypeId;
	}


	public String getCarTypeName() {
		return carTypeName;
	}


	public void setCarTypeName(String carTypeName) {
		this.carTypeName = carTypeName;
	}


	public String getCarTypeNumber() {
		return carTypeNumber;
	}


	public void setCarTypeNumber(String carTypeNumber) {
		this.carTypeNumber = carTypeNumber;
	}


	public String getColorId() {
		return colorId;
	}


	public void setColorId(String colorId) {
		this.colorId = colorId;
	}


	public String getColorName() {
		return colorName;
	}


	public void setColorName(String colorName) {
		this.colorName = colorName;
	}


	public String getColorNumber() {
		return colorNumber;
	}


	public void setColorNumber(String colorNumber) {
		this.colorNumber = colorNumber;
	}


	public String getPriceElementId() {
		return priceElementId;
	}


	public void setPriceElementId(String priceElementId) {
		this.priceElementId = priceElementId;
	}


	public String getPriceElementName() {
		return priceElementName;
	}


	public void setPriceElementName(String priceElementName) {
		this.priceElementName = priceElementName;
	}


	public String getPriceElementNumber() {
		return priceElementNumber;
	}


	public void setPriceElementNumber(String priceElementNumber) {
		this.priceElementNumber = priceElementNumber;
	}


	public String getCategoryId() {
		return categoryId;
	}


	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}


	public String getCategoryName() {
		return categoryName;
	}


	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}


	public String getCategoryNumber() {
		return categoryNumber;
	}


	public void setCategoryNumber(String categoryNumber) {
		this.categoryNumber = categoryNumber;
	}


	public String getStyleId() {
		return styleId;
	}


	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}


	public String getStyleName() {
		return styleName;
	}


	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}


	public String getStyleNumber() {
		return styleNumber;
	}


	public void setStyleNumber(String styleNumber) {
		this.styleNumber = styleNumber;
	}


	public String getMaterialSize() {
		return materialSize;
	}


	public void setMaterialSize(String materialSize) {
		this.materialSize = materialSize;
	}


	public int getMaterialTypeId() {
		return materialTypeId;
	}


	public void setMaterialTypeId(int materialTypeId) {
		this.materialTypeId = materialTypeId;
	}


	public MaterialType getMaterialType() {
		return materialType;
	}


	public void setMaterialType(MaterialType materialType) {
		this.materialType = materialType;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getPersonal() {
		return personal;
	}


	public void setPersonal(int personal) {
		this.personal = personal;
	}

	public int getIsBatchManager() {
		return isBatchManager;
	}

	public void setIsBatchManager(int isBatchManager) {
		this.isBatchManager = isBatchManager;
	}


	public int getIsSnManager() {
		return isSnManager;
	}


	public void setIsSnManager(int isSnManager) {
		this.isSnManager = isSnManager;
	}


	public String getIsOvercharge() {
		return isOvercharge;
	}


	public void setIsOvercharge(String isOvercharge) {
		this.isOvercharge = isOvercharge;
	}


	public double getReceiveMaxScale() {
		return receiveMaxScale;
	}


	public void setReceiveMaxScale(double receiveMaxScale) {
		this.receiveMaxScale = receiveMaxScale;
	}


	public double getReceiveMinScale() {
		return receiveMinScale;
	}


	public void setReceiveMinScale(double receiveMinScale) {
		this.receiveMinScale = receiveMinScale;
	}


	public String getOldNumber() {
		return oldNumber;
	}


	public void setOldNumber(String oldNumber) {
		this.oldNumber = oldNumber;
	}


	public String getOldName() {
		return oldName;
	}


	public void setOldName(String oldName) {
		this.oldName = oldName;
	}


	public int getIsAotuBringOut() {
		return isAotuBringOut;
	}


	public void setIsAotuBringOut(int isAotuBringOut) {
		this.isAotuBringOut = isAotuBringOut;
	}


	public double getFinishReceiptOverRate() {
		return finishReceiptOverRate;
	}


	public void setFinishReceiptOverRate(double finishReceiptOverRate) {
		this.finishReceiptOverRate = finishReceiptOverRate;
	}


	public double getFinishReceiptShortRate() {
		return finishReceiptShortRate;
	}


	public void setFinishReceiptShortRate(double finishReceiptShortRate) {
		this.finishReceiptShortRate = finishReceiptShortRate;
	}


	public Organization_K3 getUseOrg() {
		return useOrg;
	}


	public void setUseOrg(Organization_K3 useOrg) {
		this.useOrg = useOrg;
	}


	public Unit_K3 getUnit() {
		return unit;
	}


	public void setUnit(Unit_K3 unit) {
		this.unit = unit;
	}


	public boolean isCheck() {
		return check;
	}


	public void setCheck(boolean check) {
		this.check = check;
	}


	public double getBarcodeQty() {
		return barcodeQty;
	}


	public void setBarcodeQty(double barcodeQty) {
		this.barcodeQty = barcodeQty;
	}


}

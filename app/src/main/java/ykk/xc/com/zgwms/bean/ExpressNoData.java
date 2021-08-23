package ykk.xc.com.zgwms.bean;

import java.io.Serializable;

import ykk.xc.com.zgwms.comm.Comm;

/**
 * 快递面单数据
 */
public class ExpressNoData implements Serializable {
	private String t01; 		// 顺丰快递单
	private String t02; 		// 目的地+
	private String t03; 		// 顺丰标快 (陆运 )
	private String t04; 		// E
	private String t05; 		// 寄付月结
	private String t06; 		// 月结卡号
	private String t07; 		// 转寄协议客户
	private String t08; 		// 店铺名称
	private String t09; 		// 店铺id
	private String t10; 		// 收方人
	private String t11; 		// 收方电话
	private String t12; 		// 收方地址
	private String t13; 		// 卖家备注
	private String t14; 		// 支付时间
	private String t15; 		// 寄付人
	private String t16; 		// 寄付电话
	private String t17; 		// 寄方地址
	private String t18; 		// 商品类别名称
	private String t19;
	private String t20;
	private String destTeamCode; 		// 单元区域编码
	private String codingMapping;		// 进港映射码
	private String abFlag;				// A/B标
	private String codingMappingOut;	// 出港映射码
	private String printIcon;			// 贴纸信息
	private String twoDimensionCode;	// 二维码
	private String judge;				// 是否为丰蜜接口（Y:是）
	private String printCount;			// 打印次数
	private String  proCode;			// 时效类型图片
	private String salOrderNo;			// 销售订单号

	// 临时字段，不加表
	private boolean check;

	public ExpressNoData() {
		super();
	}

	public String getT01() {
		if(t01 == null) t01 = "";
		return t01;
	}

	public void setT01(String t01) {
		this.t01 = t01;
	}

	public String getT02() {
		if(t02 == null) t02 = "";
		return t02;
	}

	public void setT02(String t02) {
		this.t02 = t02;
	}

	public String getT03() {
		if(t03 == null) t03 = "";
		return t03;
	}

	public void setT03(String t03) {
		this.t03 = t03;
	}

	public String getT04() {
		if(t04 == null) t04 = "";
		return t04;
	}

	public void setT04(String t04) {
		this.t04 = t04;
	}

	public String getT05() {
		if(t05 == null) t05 = "";
		return t05;
	}

	public void setT05(String t05) {
		this.t05 = t05;
	}

	public String getT06() {
		if(t06 == null) t06 = "";
		return t06;
	}

	public void setT06(String t06) {
		this.t06 = t06;
	}

	public String getT07() {
		if(t07 == null) t07 = "";
		return t07;
	}

	public void setT07(String t07) {
		this.t07 = t07;
	}

	public String getT08() {
		if(t08 == null) t08 = "";
		return t08;
	}

	public void setT08(String t08) {
		this.t08 = t08;
	}

	public String getT09() {
		if(t09 == null) t09 = "";
		return t09;
	}

	public void setT09(String t09) {
		this.t09 = t09;
	}

	public String getT10() {
		if(t10 == null) t10 = "";
		return t10;
	}

	public void setT10(String t10) {
		this.t10 = t10;
	}

	public String getT11() {
		if(t11 == null) t11 = "";
		return t11;
	}

	public void setT11(String t11) {
		this.t11 = t11;
	}

	public String getT12() {
		if(t12 == null) t12 = "";
		return t12;
	}

	public void setT12(String t12) {
		this.t12 = t12;
	}

	public String getT13() {
		if(t13 == null) t13 = "";
		return t13;
	}

	public void setT13(String t13) {
		this.t13 = t13;
	}

	public String getT14() {
		if(t14 == null) t14 = "";
		return t14;
	}

	public void setT14(String t14) {
		this.t14 = t14;
	}

	public String getT15() {
		if(t15 == null) t15 = "";
		return t15;
	}

	public void setT15(String t15) {
		this.t15 = t15;
	}

	public String getT16() {
		if(t16 == null) t16 = "";
		return t16;
	}

	public void setT16(String t16) {
		this.t16 = t16;
	}

	public String getT17() {
		if(t17 == null) t17 = "";
		return t17;
	}

	public void setT17(String t17) {
		this.t17 = t17;
	}

	public String getT18() {
		if(t18 == null) t18 = "";
		return t18;
	}

	public void setT18(String t18) {
		this.t18 = t18;
	}

	public String getT19() {
		if(t19 == null) t19 = "";
		return t19;
	}

	public void setT19(String t19) {
		this.t19 = t19;
	}

	public String getT20() {
		if(t20 == null) t20 = "";
		return t20;
	}

	public void setT20(String t20) {
		this.t20 = t20;
	}

	public String getDestTeamCode() {
		if(destTeamCode == null) destTeamCode = "";
		return destTeamCode;
	}

	public void setDestTeamCode(String destTeamCode) {
		this.destTeamCode = destTeamCode;
	}

	public String getCodingMapping() {
		if(codingMapping == null) codingMapping = "";
		return codingMapping;
	}

	public void setCodingMapping(String codingMapping) {
		this.codingMapping = codingMapping;
	}

	public String getAbFlag() {
		if(abFlag == null) abFlag = "";
		return abFlag;
	}

	public void setAbFlag(String abFlag) {
		this.abFlag = abFlag;
	}

	public String getCodingMappingOut() {
		if(codingMappingOut == null) codingMappingOut = "";
		return codingMappingOut;
	}

	public void setCodingMappingOut(String codingMappingOut) {
		this.codingMappingOut = codingMappingOut;
	}

	public String getPrintIcon() {
		if(printIcon == null) printIcon = "";
		return printIcon;
	}

	public void setPrintIcon(String printIcon) {
		this.printIcon = printIcon;
	}

	public String getTwoDimensionCode() {
		if(twoDimensionCode == null) twoDimensionCode = "";
		return twoDimensionCode;
	}

	public void setTwoDimensionCode(String twoDimensionCode) {
		this.twoDimensionCode = twoDimensionCode;
	}

	public String getJudge() {
		if(judge == null) judge = "";
		return judge;
	}

	public void setJudge(String judge) {
		this.judge = judge;
	}

	public String getPrintCount() {
		if(printCount == null) printCount = "";
		return printCount;
	}

	public void setPrintCount(String printCount) {
		this.printCount = printCount;
	}

	public String getProCode() {
		if(proCode == null) proCode = "";
		return proCode;
	}

	public void setProCode(String proCode) {
		this.proCode = proCode;
	}

	public String getSalOrderNo() {
		// 存在小写的逗号（,）,且大于1
		if(Comm.isNULLS(salOrderNo).indexOf(",") > -1 && Comm.isNULLS(salOrderNo).length() > 0) {
			return salOrderNo.substring(0, salOrderNo.length()-1);
		}
		return salOrderNo;
	}

	public void setSalOrderNo(String salOrderNo) {
		this.salOrderNo = salOrderNo;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

}

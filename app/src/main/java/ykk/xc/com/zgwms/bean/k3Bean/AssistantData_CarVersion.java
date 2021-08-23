package ykk.xc.com.zgwms.bean.k3Bean;

import java.io.Serializable;
/**
 * 车型版号列表
 * @author Administrator
 *
 */
public class AssistantData_CarVersion implements Serializable {
	private static final long serialVersionUID = 1L;

	private String fnumber; 		// 编号
	private String location;		// 位置
	private String fname; 			// 名称

	public AssistantData_CarVersion() {
		super();
	}

	public String getFnumber() {
		return fnumber;
	}

	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}


	
}

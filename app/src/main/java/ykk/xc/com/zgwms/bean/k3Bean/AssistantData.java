package ykk.xc.com.zgwms.bean.k3Bean;

import java.io.Serializable;
/**
 * 辅助资料
 * @author Administrator
 *
 */
public class AssistantData implements Serializable {
	private static final long serialVersionUID = 1L;

	private String fid; 			// 单据id
	private String fentryId; 		// 分录id
	private String fparentId; 		// 上级id
	private String fnumber; 		// 编号
	private String fname; 			// 名称
	
	public AssistantData() {
		super();
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getFentryId() {
		return fentryId;
	}

	public void setFentryId(String fentryId) {
		this.fentryId = fentryId;
	}

	public String getFparentId() {
		return fparentId;
	}

	public void setFparentId(String fparentId) {
		this.fparentId = fparentId;
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
	
	
}

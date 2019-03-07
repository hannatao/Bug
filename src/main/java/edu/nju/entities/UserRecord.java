package edu.nju.entities;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class UserRecord implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6002106650098622328L;
	
	private String user_id;
	
	private String target_id;
	
	private String action;
	
	private String create_time_millis;
	
	private String remarks;

	public UserRecord(String user_id, String target_id, String action, String create_time_millis,
			String remarks) {
		this.user_id = user_id;
		this.target_id = target_id;
		this.action = action;
		this.create_time_millis = create_time_millis;
		this.remarks = remarks;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getTarget_id() {
		return target_id;
	}

	public void setTarget_id(String target_id) {
		this.target_id = target_id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getCreate_time_millis() {
		return create_time_millis;
	}

	public void setCreate_time_millis(String create_time_millis) {
		this.create_time_millis = create_time_millis;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
}

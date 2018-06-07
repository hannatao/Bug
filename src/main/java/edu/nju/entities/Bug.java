package edu.nju.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Bug implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8980368107739914394L;

	@Id
    private String id;

	private String case_take_id;
	
	private String bug_category;
	
	private int severity;
	
	private int recurrent;
	
	private String title;
	
	@Indexed
	private String report_id;

	private String create_time_millis;
	
	private String description;
	
	private String img_url;
	
	private String bug_page;
	
	private String case_id;
	
	@PersistenceConstructor
	public Bug(String id, String case_take_id, String create_time_millis, String bug_category, String description, String img_url, int severity, int recurrent, String title, String report_id, String bug_page, String case_id) {
		this.id = id;
		this.case_take_id = case_take_id;
		this.create_time_millis = create_time_millis;
		this.bug_category = bug_category;
		this.description = description;
		this.img_url = img_url;
		this.severity = severity;
		this.recurrent = recurrent;
		this.title = title;
		this.report_id = report_id;
		this.bug_page = bug_page;
		this.case_id = case_id;
	}
	
	public String getBug_page() {
		return bug_page;
	}

	public void setBug_page(String bug_page) {
		this.bug_page = bug_page;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCase_take_id() {
		return case_take_id;
	}

	public void setCase_take_id(String case_take_id) {
		this.case_take_id = case_take_id;
	}

	public String getCreate_time_millis() {
		return create_time_millis;
	}

	public void setCreate_time_millis(String create_time_millis) {
		this.create_time_millis = create_time_millis;
	}

	public String getBug_category() {
		return bug_category;
	}

	public void setBug_category(String bug_category) {
		this.bug_category = bug_category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public int getSeverity() {
		return severity;
	}

	public void setSeverity(int severity) {
		this.severity = severity;
	}

	public int getRecurrent() {
		return recurrent;
	}

	public void setRecurrent(int recurrent) {
		this.recurrent = recurrent;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	

	public String getReport_id() {
		return report_id;
	}

	public void setReport_id(String report_id) {
		this.report_id = report_id;
	}

	public String getCase_id() {
		return case_id;
	}

	public void setCase_id(String case_id) {
		this.case_id = case_id;
	}
}

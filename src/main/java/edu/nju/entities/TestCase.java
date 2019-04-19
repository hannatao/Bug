package edu.nju.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class TestCase implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4137909293358720275L;

	@Id
	private String id;
	
	@Indexed
	private String report_id;
	
	private String name;
	
	private String front;
	
	private String behind;
	
	private String description;
	
	private String create_time_millis;

	@PersistenceConstructor
	public TestCase(String name, String front, String behind, String description, String report_id, String create_time_millis) {
		this.name = name;
		this.report_id = report_id;
		this.front = front;
		this.behind = behind;
		this.description = description;
		this.create_time_millis = create_time_millis;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReport_id() {
		return report_id;
	}

	public void setReport_id(String report_id) {
		this.report_id = report_id;
	}

	public String getFront() {
		return front;
	}

	public void setFront(String front) {
		this.front = front;
	}

	public String getBehind() {
		return behind;
	}

	public void setBehind(String behind) {
		this.behind = behind;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreate_time_millis() {
		return create_time_millis;
	}

	public void setCreate_time_millis(String create_time_millis) {
		this.create_time_millis = create_time_millis;
	}
	
}

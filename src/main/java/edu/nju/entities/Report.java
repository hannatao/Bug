package edu.nju.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Report implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2711919701200675535L;

	@Id
	private String id;
	
	private String case_id;

	private String task_id;
	
	@Indexed
	private String case_take_id;
	
	private String woker_id;
	
	private String create_time_millis;
	
	private String device_model;
	
	private String device_brand;
	
	private String device_os;
	
	private String script_location;
	
	private String report_location;
	
	private String log_location;

	@PersistenceConstructor
	public Report(String case_id, String task_id, String case_take_id, String woker_id, String create_time_millis,
			String device_model, String device_brand, String device_os, String script_location, String report_location,
			String log_location) {
		super();
		this.case_id = case_id;
		this.task_id = task_id;
		this.case_take_id = case_take_id;
		this.woker_id = woker_id;
		this.create_time_millis = create_time_millis;
		this.device_model = device_model;
		this.device_brand = device_brand;
		this.device_os = device_os;
		this.script_location = script_location;
		this.report_location = report_location;
		this.log_location = log_location;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCase_id() {
		return case_id;
	}

	public void setCase_id(String case_id) {
		this.case_id = case_id;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getCase_take_id() {
		return case_take_id;
	}

	public void setCase_take_id(String case_take_id) {
		this.case_take_id = case_take_id;
	}

	public String getWoker_id() {
		return woker_id;
	}

	public void setWoker_id(String woker_id) {
		this.woker_id = woker_id;
	}

	public String getCreate_time_millis() {
		return create_time_millis;
	}

	public void setCreate_time_millis(String create_time_millis) {
		this.create_time_millis = create_time_millis;
	}

	public String getDevice_model() {
		return device_model;
	}

	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}

	public String getDevice_brand() {
		return device_brand;
	}

	public void setDevice_brand(String device_brand) {
		this.device_brand = device_brand;
	}

	public String getDevice_os() {
		return device_os;
	}

	public void setDevice_os(String device_os) {
		this.device_os = device_os;
	}

	public String getScript_location() {
		return script_location;
	}

	public void setScript_location(String script_location) {
		this.script_location = script_location;
	}

	public String getReport_location() {
		return report_location;
	}

	public void setReport_location(String report_location) {
		this.report_location = report_location;
	}

	public String getLog_location() {
		return log_location;
	}

	public void setLog_location(String log_location) {
		this.log_location = log_location;
	}
	
	
}

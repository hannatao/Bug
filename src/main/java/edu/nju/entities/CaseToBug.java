package edu.nju.entities;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CaseToBug implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1969002363605622255L;

	@Id
	private String id;
	
	private String case_take_id;
	
	private String report_id;
	
	private List<String> bug_id;
	
	@PersistenceConstructor
	public CaseToBug(String id, List<String> bug_id, String case_take_id, String report_id) {
		this.id = id;
		this.bug_id = bug_id;
		this.case_take_id = case_take_id;
		this.report_id = report_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getBug_id() {
		return bug_id;
	}

	public void setBug_id(List<String> bug_id) {
		this.bug_id = bug_id;
	}

	public String getCase_take_id() {
		return case_take_id;
	}

	public void setCase_take_id(String case_take_id) {
		this.case_take_id = case_take_id;
	}

	public String getReport_id() {
		return report_id;
	}

	public void setReport_id(String report_id) {
		this.report_id = report_id;
	}
	
}

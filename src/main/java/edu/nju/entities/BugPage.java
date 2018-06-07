package edu.nju.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BugPage implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5647215351692151191L;

	@Id
    private String id;
	
	@Indexed
	private String case_take_id;
	
	@Indexed
	private String page1;
	
	@Indexed
	private String page2;
	
	@Indexed
	private String page3;

	@PersistenceConstructor
	public BugPage(String id, String page1, String page2, String page3, String case_take_id) {
		this.id = id;
		this.page1 = page1;
		this.page2 = page2;
		this.page3 = page3;
		this.case_take_id = case_take_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPage1() {
		return page1;
	}

	public void setPage1(String page1) {
		this.page1 = page1;
	}

	public String getPage2() {
		return page2;
	}

	public void setPage2(String page2) {
		this.page2 = page2;
	}

	public String getPage3() {
		return page3;
	}

	public void setPage3(String page3) {
		this.page3 = page3;
	}

	public String getCase_take_id() {
		return case_take_id;
	}

	public void setCase_take_id(String case_take_id) {
		this.case_take_id = case_take_id;
	}
	
	
}

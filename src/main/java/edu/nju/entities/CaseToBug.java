package edu.nju.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CaseToBug {
	
	@Id
	private String id;
	
	private String bug_id;

	public CaseToBug(String id, String bug_id) {
		this.id = id;
		this.bug_id = bug_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBug_id() {
		return bug_id;
	}

	public void setBug_id(String bug_id) {
		this.bug_id = bug_id;
	}
	
	
}

package edu.nju.entities;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CaseToBug {
	
	@Id
	private String id;
	
	private List<String> bug_id;
	
	@PersistenceConstructor
	public CaseToBug(String id, List<String> bug_id) {
		this.id = id;
		this.bug_id = bug_id;
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
	
}

package edu.nju.entities;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BugHistory {
	@Id
    private String id;
	
	private String parent;
	
	private List<String> children;

	@PersistenceConstructor
	public BugHistory(String id, String parent, List<String> children) {
		this.id = id;
		this.parent = parent;
		this.children = children;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}
	
	public List<String> getChildren() {
		return children;
	}

	public void setChildren(List<String> children) {
		this.children = children;
	}
}

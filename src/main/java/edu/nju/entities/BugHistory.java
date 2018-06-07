package edu.nju.entities;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BugHistory implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8852808978961197553L;

	@Id
    private String id;
	
	private String parent;
	
	private List<String> children;
	
	private String root;

	@PersistenceConstructor
	public BugHistory(String id, String parent, List<String> children, String root) {
		this.id = id;
		this.parent = parent;
		this.children = children;
		this.root = root;
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

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}
}

package edu.nju.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Exam implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7248743411159553345L;

	@Id
	private String id;
	
	private String json;
	
	private String name;

	@PersistenceConstructor
	public Exam(String json, String name) {
		this.json = json;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}

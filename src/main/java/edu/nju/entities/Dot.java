package edu.nju.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Dot implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1721784427824000141L;

	@Id
    private String id;
	
	private String type1;
	
	@PersistenceConstructor
	public Dot(String id, String type1) {
		this.id = id;
		this.type1 = type1;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType1() {
		return type1;
	}

	public void setType1(String type1) {
		this.type1 = type1;
	}
}

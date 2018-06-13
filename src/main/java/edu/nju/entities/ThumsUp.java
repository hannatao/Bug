package edu.nju.entities;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ThumsUp implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -17882697895537936L;
	
	@Id
    private String id;
	
	Set<String> thums;
	
	Set<String> diss;

	@PersistenceConstructor
	public ThumsUp(String id, Set<String> thums, Set<String> diss) {
		this.id = id;
		this.thums = thums;
		this.diss = diss;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<String> getThums() {
		return thums;
	}

	public void setThums(Set<String> thums) {
		this.thums = thums;
	}

	public Set<String> getDiss() {
		return diss;
	}

	public void setDiss(Set<String> diss) {
		this.diss = diss;
	}
	
}

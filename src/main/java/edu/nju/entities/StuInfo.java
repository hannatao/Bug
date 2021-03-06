package edu.nju.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class StuInfo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7968494783792967307L;
	
	@Id
    private String id;  //report_id
	
	private String worker_id;
	
	private String name;

	@PersistenceConstructor
	public StuInfo(String id, String worker_id, String name) {
		this.id = id;
		this.worker_id = worker_id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWorker_id() {
		return worker_id;
	}

	public void setWorker_id(String worker_id) {
		this.worker_id = worker_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}

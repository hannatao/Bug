package edu.nju.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BugScore implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2993945374555651123L;

	@Id
    private String id;
	
	private int grade;
	
	private int score;

	@PersistenceConstructor
	public BugScore(String id, int grade, int score) {
		this.id = id;
		this.grade = grade;
		this.score = score;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}

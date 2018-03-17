package edu.nju.entities;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BugMirror {
	@Id
    private String id;

	@Indexed
	private String case_take_id;
	
	@Indexed
	private String bug_category;
	
	@Indexed
	private int severity;
	
	@Indexed
	private int recurrent;

	private String title;
	
	private Set<String> good;

	private Set<String> bad;
	
	private String img_url;
	
	@PersistenceConstructor
	public BugMirror(String id, String case_take_id, String bug_category, int severity, int recurrent, String title, String img_url, Set<String> good, Set<String> bad) {
		this.id = id;
		this.case_take_id = case_take_id;
		this.bug_category = bug_category;
		this.severity = severity;
		this.recurrent = recurrent;
		this.title = title;
		this.img_url = img_url;
		this.bad = bad;
		this.good = good;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCase_take_id() {
		return case_take_id;
	}

	public void setCase_take_id(String case_take_id) {
		this.case_take_id = case_take_id;
	}

	public String getBug_category() {
		return bug_category;
	}

	public void setBug_category(String bug_category) {
		this.bug_category = bug_category;
	}

	public int getSeverity() {
		return severity;
	}

	public void setSeverity(int severity) {
		this.severity = severity;
	}

	public int getRecurrent() {
		return recurrent;
	}

	public void setRecurrent(int recurrent) {
		this.recurrent = recurrent;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	
	public Set<String> getGood() {
		return good;
	}

	public void setGood(Set<String> good) {
		this.good = good;
	}

	public Set<String> getBad() {
		return bad;
	}

	public void setBad(Set<String> bad) {
		this.bad = bad;
	}
}

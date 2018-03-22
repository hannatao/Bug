package edu.nju.model;

import java.util.Set;

public class BugMirrorModel {
	private String id;
	private String case_take_id;
	private String bug_category;
	private int severity;
	private int recurrent;
	private String title;
	private Set<String> good;
	private Set<String> bad;
	private String img_url;
	private String page1;
	private String page2;
	private String page3;
	public BugMirrorModel(String id, String case_take_id, String bug_category, int severity, int recurrent,
			String title, Set<String> good, Set<String> bad, String img_url, String page1, String page2, String page3) {
		this.id = id;
		this.case_take_id = case_take_id;
		this.bug_category = bug_category;
		this.severity = severity;
		this.recurrent = recurrent;
		this.title = title;
		this.good = good;
		this.bad = bad;
		this.img_url = img_url;
		this.page1 = page1;
		this.page2 = page2;
		this.page3 = page3;
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
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	public String getPage1() {
		return page1;
	}
	public void setPage1(String page1) {
		this.page1 = page1;
	}
	public String getPage2() {
		return page2;
	}
	public void setPage2(String page2) {
		this.page2 = page2;
	}
	public String getPage3() {
		return page3;
	}
	public void setPage3(String page3) {
		this.page3 = page3;
	}
	
}

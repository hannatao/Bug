package edu.nju.entities;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BugMirror implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7484726588592610736L;

	@Id
    private String id;

	@Indexed
	private String case_take_id;
	
	@Indexed
	private String bug_category;
	
	private String report_id;
	
	@Indexed
	private int severity;
	
	@Indexed
	private int recurrent;
	
	private String title;
	
	private Set<String> good;

	private Set<String> bad;
	
	private String img_url;
	
	private boolean flag;
	
	private String useCase;
	
	@PersistenceConstructor
	public BugMirror(String id, String case_take_id, String bug_category, int severity, int recurrent, String title, String img_url, Set<String> good, Set<String> bad, String report_id, String useCase, boolean flag) {
		this.id = id;
		this.case_take_id = case_take_id;
		this.bug_category = bug_category;
		this.severity = severity;
		this.recurrent = recurrent;
		this.title = title;
		this.img_url = img_url;
		this.bad = bad;
		this.good = good;
		this.report_id = report_id;
		this.useCase = useCase;
		this.flag = flag;
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

	public String getReport_id() {
		return report_id;
	}

	public void setReport_id(String report_id) {
		this.report_id = report_id;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getUseCase() {
		return useCase;
	}

	public void setUseCase(String useCase) {
		this.useCase = useCase;
	}
	
	@Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
	
	@Override
    public boolean equals(Object o) {
        // 比较引用
        if(this == o) return true;
        // 比较类型
        if(o == null || getClass() != o.getClass()) return false;
        // 比较内容，这里比较的是 id 值
        BugMirror other = (BugMirror) o;
        // 判断逻辑就是：三元运算符 的结果如果等于null 那么就返回 false
        // 首先确定当前 id 不等于 null，然后确定当前 id 不等于被比较的 id
        // 最后条件都满足了之后还是不等null，说明肯定不一致就返回 false 就可以了
        if (id != null ? !id.equals(other.id) : other.id != null) return false;
        return true;
    }
}

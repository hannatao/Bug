package edu.nju.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import edu.nju.entities.BugPage;

@Repository
public class BugPageDao {
	
	@Autowired
	private MongoOperations mongoOperations;
	
	//save存在则更新，不存在则插入
	public void save(BugPage page){
	    mongoOperations.save(page);
	}
	
	//根据id删除文档
	public void remove(String id){
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		mongoOperations.remove(query,BugPage.class);
	}
	
	//根据ids删除文档
	public void remove(List<String> ids){
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").in(ids));
		mongoOperations.remove(query,BugPage.class);
	}
	
	public BugPage findByid(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		return mongoOperations.find(query,BugPage.class).get(0);
	}
	
	public List<BugPage> findByIds(List<String> ids) {
		if(ids == null || ids.size() == 0) {return new ArrayList<BugPage>();}
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").in(ids));
		return mongoOperations.find(query, BugPage.class);
	}
	
	public List<BugPage> findByPage1(String case_take_id, String page1) {
		Query query = new Query();
		query.addCriteria(Criteria.where("case_take_id").is(case_take_id).and("page1").is(page1));
		return mongoOperations.find(query,BugPage.class);
	}
	
	public List<BugPage> findByPage2(String case_take_id, String page2) {
		Query query = new Query();
		query.addCriteria(Criteria.where("case_take_id").is(case_take_id).and("page2").is(page2));
		return mongoOperations.find(query,BugPage.class);
	}
	
	public List<BugPage> findByPage3(String case_take_id, String page3) {
		Query query = new Query();
		query.addCriteria(Criteria.where("case_take_id").is(case_take_id).and("page3").is(page3));
		return mongoOperations.find(query,BugPage.class);
	}
}

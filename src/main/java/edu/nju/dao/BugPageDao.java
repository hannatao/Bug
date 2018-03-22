package edu.nju.dao;

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
	
	public BugPage findByid(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		return mongoOperations.find(query,BugPage.class).get(0);
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

package edu.nju.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import edu.nju.entities.BugMirror;

@Repository
public class BugMirrorDao {
	
	@Autowired
	private MongoOperations mongoOperations;
	
	//save存在则更新，不存在则插入
	public void save(BugMirror mirror){
	    mongoOperations.save(mirror);
	}
	
	//根据id删除文档
	public void remove(String id){
	    Query query = new Query();
	    query.addCriteria(Criteria.where("_id").is(id));
	    mongoOperations.remove(query,BugMirror.class);
	}
	
	public List<BugMirror> findByCase(String case_take_id){
		Query query = new Query();
		query.addCriteria(Criteria.where("case_take_id").is(case_take_id));
		return mongoOperations.find(query, BugMirror.class);
	}
	
	public List<BugMirror> findByCategory(String case_take_id, String bug_category){
		Query query = new Query();
		query.addCriteria(Criteria.where("case_take_id").is(case_take_id).and("bug_category").is(bug_category));
		return mongoOperations.find(query, BugMirror.class);
	}
	
	public List<BugMirror> findBySeverity(String case_take_id, int severity){
		Query query = new Query();
		query.addCriteria(Criteria.where("case_take_id").is(case_take_id).and("severity").is(severity));
		return mongoOperations.find(query, BugMirror.class);
	}
	
	public List<BugMirror> findByRecurrent(String case_take_id, int recurrent){
		Query query = new Query();
		query.addCriteria(Criteria.where("case_take_id").is(case_take_id).and("recurrent").is(recurrent));
		return mongoOperations.find(query, BugMirror.class);
	}
	
	public boolean haveJudged(String id, String report_id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		BugMirror mirror = mongoOperations.find(query, BugMirror.class).get(0);
		if(mirror.getGood().contains(report_id) || mirror.getBad().contains(report_id)) {
			return false;
		} else {
			return true;
		}
	}
	
	public void good(String id, String report_id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		Update update = new Update();
		update.set("good", mongoOperations.find(query, BugMirror.class).get(0).getGood().add(report_id));
		mongoOperations.updateFirst(query,update,BugMirror.class);
	}
	
	public void bad(String id, String report_id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		Update update = new Update();
		update.set("bad", mongoOperations.find(query, BugMirror.class).get(0).getBad().add(report_id));
		mongoOperations.updateFirst(query,update,BugMirror.class);
	}
}

package edu.nju.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
	
	//根据ids删除文档
	public void remove(List<String> ids){
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").in(ids));
		mongoOperations.remove(query,BugMirror.class);
	}
	
	public List<BugMirror> findValid(String case_take_id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("case_take_id").is(case_take_id).and("flag").is(true));
		return mongoOperations.find(query, BugMirror.class);
	}
	
	public List<String> findByReport(String report_id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("report_id").is(report_id).and("flag").is(true));
		List<BugMirror> lists = mongoOperations.find(query, BugMirror.class);
		List<String> result = new ArrayList<String>();
		for(BugMirror mirror : lists) {
			result.add(mirror.getId());
		}
		return result;
	}
	
	public List<BugMirror> findByCase(String case_take_id){
		Query query = new Query();
		query.addCriteria(Criteria.where("case_take_id").is(case_take_id));
		return mongoOperations.find(query, BugMirror.class);
	}
	
	public List<BugMirror> findByCase(String case_take_id, String report_id){
		Query query = new Query();
		query.addCriteria(Criteria.where("case_take_id").is(case_take_id).and("report_id").nin(report_id));
		return mongoOperations.find(query, BugMirror.class);
	}
	
	public List<BugMirror> findByCategory(String case_take_id, String bug_category, String report_id){
		Query query = new Query();
		query.addCriteria(Criteria.where("case_take_id").is(case_take_id).and("bug_category").is(bug_category).and("report_id").nin(report_id));
		return mongoOperations.find(query, BugMirror.class);
	}
	
	public List<BugMirror> findBySeverity(String case_take_id, int severity, String report_id){
		Query query = new Query();
		query.addCriteria(Criteria.where("case_take_id").is(case_take_id).and("severity").is(severity).and("report_id").nin(report_id));
		return mongoOperations.find(query, BugMirror.class);
	}
	
	public List<BugMirror> findByRecurrent(String case_take_id, int recurrent, String report_id){
		Query query = new Query();
		query.addCriteria(Criteria.where("case_take_id").is(case_take_id).and("recurrent").is(recurrent).and("report_id").nin(report_id));
		return mongoOperations.find(query, BugMirror.class);
	}
	
	public List<BugMirror> findByIds(List<String> ids){
		if(ids == null || ids.size() == 0) {return new ArrayList<BugMirror>();}
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").in(ids));
		return mongoOperations.find(query, BugMirror.class);
	}
	
	public List<BugMirror> findByIds(List<String> ids, String report_id){
		if(ids == null || ids.size() == 0) {return new ArrayList<BugMirror>();}
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").in(ids).and("report_id").nin(report_id));
		return mongoOperations.find(query, BugMirror.class);
	}
	
	public BugMirror findById(String id){
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		return mongoOperations.find(query, BugMirror.class).get(0);
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
		Set<String> good = mongoOperations.find(query, BugMirror.class).get(0).getGood();
		good.add(report_id);
		update.set("good", good);
		mongoOperations.updateFirst(query,update,BugMirror.class);
	}
	
	public void bad(String id, String report_id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		Update update = new Update();
		Set<String> bad = mongoOperations.find(query, BugMirror.class).get(0).getBad();
		bad.add(report_id);
		update.set("bad", bad);
		mongoOperations.updateFirst(query,update,BugMirror.class);
	}
}

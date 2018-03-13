package edu.nju.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import edu.nju.entities.BugHistory;

@Repository
public class BugHistoryDao {
	
	@Autowired
	MongoOperations mongoOps;
	
	//save存在则更新，不存在则插入
	public void save(BugHistory history){
		mongoOps.save(history);
	}
	
	//根据id删除文档
	public void remove(String bug_id){
	    Query query = new Query();
	    query.addCriteria(Criteria.where("bug_id").is(bug_id));
	    mongoOps.remove(query,BugHistory.class);
	}
	
	//根据bug_id查找
	public List<BugHistory> findParent(String bug_id){
		Query query = new Query();
		query.addCriteria(Criteria.where("bug_id").is(bug_id));
		return mongoOps.find(query, BugHistory.class);
	}
}

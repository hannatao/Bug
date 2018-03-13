package edu.nju.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
	public void remove(String id){
	    Query query = new Query();
	    query.addCriteria(Criteria.where("_id").is(id));
	    mongoOps.remove(query,BugHistory.class);
	}
	
	//根据id查找
	public BugHistory findParent(String id){
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		return mongoOps.find(query, BugHistory.class).get(0);
	}
	
	//增加child
	public void addChild(String id, String child) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		BugHistory temp_hisroty = (BugHistory) mongoOps.find(query, BugHistory.class);
		Update update = new Update();
		update.set("children", temp_hisroty.getChildren().add(child));
		mongoOps.updateFirst(query,update,BugHistory.class);
	}
}

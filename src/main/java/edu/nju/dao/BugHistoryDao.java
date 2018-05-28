package edu.nju.dao;

import java.util.ArrayList;
import java.util.List;

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
	
	//id查询，find查询所有
	public BugHistory findByid(String id){
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		return mongoOps.find(query,BugHistory.class).get(0);
	}
	
	//查找所有指定的根
	public List<String> findRoots(List<String> lists) {
		if(lists == null || lists.size() == 0) {return new ArrayList<String>();}
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").in(lists).and("parent").is("null"));
		List<BugHistory> roots = mongoOps.find(query,BugHistory.class);
		List<String> ids = new ArrayList<String>();
		for(BugHistory root: roots) {
			ids.add(root.getRoot());
		}
		return ids;
	}
	
	//查找所有的BugRoot
	public List<String> findRoots() {
		Query query = new Query();
		query.addCriteria(Criteria.where("parent").is("null"));
		List<BugHistory> roots = mongoOps.find(query,BugHistory.class);
		List<String> ids = new ArrayList<String>();
		for(BugHistory root : roots) {
			ids.add(root.getId());
		}
		return ids;
	}
	
	//根据id删除文档
	public void remove(String id){
	    Query query = new Query();
	    query.addCriteria(Criteria.where("_id").is(id));
	    mongoOps.remove(query,BugHistory.class);
	}
	
	//根据ids删除文档
	public void remove(List<String> ids){
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").in(ids));
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
		BugHistory temp_hisroty = (BugHistory) mongoOps.find(query, BugHistory.class).get(0);
		Update update = new Update();
		List<String> children = temp_hisroty.getChildren();
		if(!children.contains(child)) {children.add(child);}
		update.set("children", children);
		mongoOps.updateFirst(query,update,BugHistory.class);
	}
}

package edu.nju.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import edu.nju.entities.Bug;

@Repository
public class BugDao {
	
	@Autowired
	private MongoOperations mongoOperations;
	
	//save存在则更新，不存在则插入
	public void save(Bug bug){
	    mongoOperations.save(bug);
	}
	
	//insert存在则不做处理，不存在则插入
	public void insert(Bug bug){
	    mongoOperations.insert(bug);
	}
	
	//根据id删除文档
	public void remove(String id){
	    Query query = new Query();
	    query.addCriteria(Criteria.where("_id").is(id));
	    mongoOperations.remove(query,Bug.class);
	}
	
	//根据ids删除文档
	public void remove(List<String> ids){
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").in(ids));
		mongoOperations.remove(query,Bug.class);
	}
	
	//根据report_id查找所有的Bug
	public List<String> findByReport(String report_id) {
		List<String> result = new ArrayList<String>();
		Query query = new Query();
	    query.addCriteria(Criteria.where("report_id").is(report_id));
	    List<Bug> lists = mongoOperations.find(query,Bug.class);
	    for(Bug list : lists) {
	    	result.add(list.getId());
	    }
	    return result;
	}
	
	//根据id更新文档
	public int update(Bug bug){
	    Query query = new Query();
	    query.addCriteria(Criteria.where("_id").is(bug.getId()));
	    Update update = new Update();
	    update.set("bug_category",bug.getBug_category());
	    return mongoOperations.updateFirst(query,update,Bug.class).getN();
	}

	//id查询，find查询所有
	public Bug findByid(String id){
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		List<Bug> list = mongoOperations.find(query,Bug.class);
		if(list.size() == 0 || list == null) {return null;}
		return list.get(0);
	}
	
	//case条件查询，find查询所有
	public List<Bug> findByCaseid(String case_take_id){
	    Query query = new Query();
	    query.addCriteria(Criteria.where("case_take_id").is(case_take_id));
	    return mongoOperations.find(query,Bug.class);
	}

}

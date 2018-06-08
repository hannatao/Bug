package edu.nju.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import edu.nju.entities.BugScore;

@Repository
public class BugScoreDao {
	
	@Autowired
	private MongoOperations mongoOperations;
	
	public void save(BugScore score) {
		mongoOperations.save(score);
	}
	
	public BugScore findById(String id) {
		Query query = new Query();
	    query.addCriteria(Criteria.where("_id").is(id));
	    List<BugScore> list = mongoOperations.find(query, BugScore.class);
	    if(list != null && list.size() != 0) {return list.get(0);}
	    return null;
	}
	
	public List<BugScore> findByIds(List<String> list) {
		Query query = new Query();
	    query.addCriteria(Criteria.where("_id").in(list));
	    return mongoOperations.find(query, BugScore.class);
	}
	
	public void remove(List<String> ids) {
		Query query = new Query();
	    query.addCriteria(Criteria.where("_id").in(ids));
	    mongoOperations.remove(query, BugScore.class);
	}
}

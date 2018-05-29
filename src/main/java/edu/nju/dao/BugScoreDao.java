package edu.nju.dao;

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
	    return mongoOperations.find(query, BugScore.class).get(0);
	}
	
	
}

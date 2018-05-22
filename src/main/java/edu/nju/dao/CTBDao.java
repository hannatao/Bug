package edu.nju.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import edu.nju.entities.CaseToBug;

@Repository
public class CTBDao {
	
	@Autowired
	private MongoOperations mongoOperations;
	
	public void save(CaseToBug ctb) {
		mongoOperations.save(ctb);
	}
	
	public List<CaseToBug> findById(String id) {
		Query query = new Query();
	    query.addCriteria(Criteria.where("_id").is(id));
	    return mongoOperations.find(query, CaseToBug.class);
	}
	
	public void remove(String bug_id) {
		Query query = new Query();
	    query.addCriteria(Criteria.where("bug_id").is(bug_id));
	    mongoOperations.remove(query, CaseToBug.class);
	}
}

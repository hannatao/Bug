package edu.nju.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import edu.nju.entities.KeyWords;

@Repository
public class KWDao {

	@Autowired
	private MongoOperations mongoOperations;
	
	public void save(KeyWords kws) {
		mongoOperations.save(kws);
	}
	
	public KeyWords findById(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		return mongoOperations.find(query, KeyWords.class).get(0);
	}
	
	public void remove(String id){
	    Query query = new Query();
	    query.addCriteria(Criteria.where("_id").is(id));
	    mongoOperations.remove(query, KeyWords.class);
	}
	
	public void remove(List<String> ids) {
		Query query = new Query();
	    query.addCriteria(Criteria.where("_id").in(ids));
	    mongoOperations.remove(query, KeyWords.class);
	}
}

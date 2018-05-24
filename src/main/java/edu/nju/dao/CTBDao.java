package edu.nju.dao;

import java.util.ArrayList;
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
	
	public void save(String useCase, String bug_id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(useCase));
		List<CaseToBug> result = mongoOperations.find(query, CaseToBug.class);
		if(result.size() != 0) {
			CaseToBug ctb = result.get(0);
			if(!ctb.getBug_id().contains(bug_id)) {
				ctb.getBug_id().add(bug_id);
				mongoOperations.save(ctb);
			}
		} else {
			List<String> list = new ArrayList<String>();
			list.add(bug_id);
			CaseToBug ctb = new CaseToBug(useCase, list);
			mongoOperations.save(ctb);
		}
		
	}
	
	public List<String> findById(String id) {
		Query query = new Query();
	    query.addCriteria(Criteria.where("_id").is(id));
	    List<CaseToBug> result = mongoOperations.find(query, CaseToBug.class);
	    if(result.size() == 0 || result == null) {return null;}
	    return result.get(0).getBug_id();
	    
	}
	
	public void remove(String useCase, String bug_id) {
		Query query = new Query();
	    query.addCriteria(Criteria.where("_id").is(useCase));
	    CaseToBug result = mongoOperations.find(query, CaseToBug.class).get(0);
	    result.getBug_id().remove(bug_id);
	    mongoOperations.save(result);
	}
	
	public void removeAll(String useCase) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(useCase));
		mongoOperations.remove(query, CaseToBug.class);
	}
}

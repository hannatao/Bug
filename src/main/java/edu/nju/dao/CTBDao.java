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
	
	public void save(String useCase, String bug_id, String case_take_id, String report_id) {
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
			CaseToBug ctb = new CaseToBug(useCase, list, case_take_id, report_id);
			mongoOperations.save(ctb);
		}
		
	}
	
	public CaseToBug find(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		return mongoOperations.find(query, CaseToBug.class).get(0);
	}
	
	public List<String> findById(String id) {
		Query query = new Query();
	    query.addCriteria(Criteria.where("_id").is(id));
	    List<CaseToBug> result = mongoOperations.find(query, CaseToBug.class);
	    if(result.size() == 0 || result == null) {return new ArrayList<String>();}
	    return result.get(0).getBug_id();
	    
	}
	
	public List<CaseToBug> findByCase(String case_take_id) {
		Query query = new Query();
	    query.addCriteria(Criteria.where("case_take_id").is(case_take_id));
	    return mongoOperations.find(query, CaseToBug.class);
	}
	
	public List<CaseToBug> findByReport(String report_id) {
		Query query = new Query();
	    query.addCriteria(Criteria.where("report_id").is(report_id));
	    return mongoOperations.find(query, CaseToBug.class);
	}
	
	public void remove(String useCase, String bug_id) {
		Query query = new Query();
	    query.addCriteria(Criteria.where("_id").is(useCase));
	    CaseToBug result = mongoOperations.find(query, CaseToBug.class).get(0);
	    result.getBug_id().remove(bug_id);
	    mongoOperations.save(result);
	}
	
	public void removeAll(String case_take_id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("case_take_id").is(case_take_id));
		mongoOperations.remove(query, CaseToBug.class);
	}
	
	public void remove(String useCase) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(useCase));
		mongoOperations.remove(query, CaseToBug.class);
	}
}

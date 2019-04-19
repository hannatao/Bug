package edu.nju.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import edu.nju.entities.Report;

@Repository
public class ReportDao {

	@Autowired
	private MongoOperations mongoOperations;
	
	public String save(Report report) { 
		mongoOperations.save(report);
		return report.getId(); 
	}
	
	public Report findById(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		List<Report> list = mongoOperations.find(query, Report.class);
		if(list == null || list.size() == 0) { return null;}
		else { return list.get(0); }
	}
	
	public Report findByWoker(String case_take_id, String worker_id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("case_take_id").is(case_take_id).and("woker_id").is(worker_id));
		List<Report> list = mongoOperations.find(query, Report.class);
		if(list == null || list.size() == 0) { return null;}
		else { return list.get(0); }
	}
	
}

package edu.nju.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import edu.nju.entities.StuInfo;

@Repository
public class StuInfoDao {
	
	@Autowired
	private MongoOperations mongoOperations;
	
	public void save(String report_id, String stu_id, String name) {
		mongoOperations.save(new StuInfo(report_id, stu_id, name));
	}
	
	public String findById(String report_id) {
		Query query = new Query();
	    query.addCriteria(Criteria.where("_id").is(report_id));
	    List<StuInfo> stu_info = mongoOperations.find(query, StuInfo.class);
	    if(stu_info == null || stu_info.size() == 0) { return "null"; }
	    else { return stu_info.get(0).getName(); }
	}
	
	public String findWorkerId(String report_id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(report_id));
		List<StuInfo> stu_info = mongoOperations.find(query, StuInfo.class);
		if(stu_info == null || stu_info.size() == 0) { return "null"; }
		else { return stu_info.get(0).getWorker_id(); }
	}
}

package edu.nju.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import edu.nju.entities.Exam;

@Repository
public class ExamDao {

	@Autowired
	private MongoOperations mongoOperations;
	
	public String save(Exam exam) { 
		mongoOperations.save(exam); 
		return exam.getId();
	}
	
	public Exam findById(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		List<Exam> list = mongoOperations.find(query, Exam.class);
		if(list == null || list.size() == 0) { return null;}
		else { return list.get(0); }
	}
}

package edu.nju.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import edu.nju.entities.TestCase;

@Repository
public class TestCaseDao {

	@Autowired
	private MongoOperations mongoOperations;
	
	public String save(TestCase testCase) { 
		mongoOperations.save(testCase); 
		return testCase.getId();
	}
	
	public TestCase findById(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		List<TestCase> list = mongoOperations.find(query, TestCase.class);
		if(list == null || list.size() == 0) { return null; }
		else { return list.get(0); }
	}
	
	public List<TestCase> findByReport(String report_id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("report_id").is(report_id));
		List<TestCase> list = mongoOperations.find(query, TestCase.class);
		if(list == null || list.size() == 0) { return null; }
		else { return list; }
	}
	
	public void updateTestCase(String id, String report_id, String name, String front, String behind, String description) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		List<TestCase> list = mongoOperations.find(query, TestCase.class);
		if(list != null && list.size() != 0) {
			TestCase testCase = list.get(0);
			testCase.setFront(front);
			testCase.setName(name);
			testCase.setBehind(behind);
			testCase.setDescription(description);
			mongoOperations.save(testCase);
		}
	}
}

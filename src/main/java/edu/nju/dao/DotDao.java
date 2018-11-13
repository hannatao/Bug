package edu.nju.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import edu.nju.entities.Dot;

@Repository
public class DotDao {

	@Autowired
	private MongoOperations mongoOperations;
	
	public void save(Dot dot) {
		mongoOperations.save(dot);
	}
	
	public String findByid(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		List<Dot> list = mongoOperations.find(query, Dot.class);
		if(list.size() == 0 || list == null) {return null;}
		return list.get(0).getType1();
	}
}

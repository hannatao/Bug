package edu.nju.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import edu.nju.entities.ImageAnnotation;

@Repository
public class AnnotationDao {
	
	@Autowired
	private MongoOperations mongoOperations;
	
	public void save(ImageAnnotation a) {
		mongoOperations.save(a);
	}
	
	//根据id删除文档
	public void remove(String id){
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		mongoOperations.remove(query,ImageAnnotation.class);
	}
	
	//根据id删除文档
	public ImageAnnotation findById(String id){
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		List<ImageAnnotation> lists = mongoOperations.find(query, ImageAnnotation.class);
		if(lists == null || lists.size() == 0) {return null;}
		return lists.get(0);
	}
}

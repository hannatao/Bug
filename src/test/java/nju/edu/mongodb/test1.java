package nju.edu.mongodb;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

public class test1 {
	
	@Autowired
	private MongoOperations mongoOperations;
	
	@Test
	public void save(){
		mongoOperations.getCollection("Bug");
	}
}

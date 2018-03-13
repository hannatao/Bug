package nju.edu.mongodb;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

import edu.nju.entities.BugHistory;

public class test1 {
	
	@Autowired
	private MongoOperations mongoOperations;
	
	@Test
	public void save(){
		BugHistory history = new BugHistory("1", "1", "1");
		System.out.println(mongoOperations);
	    mongoOperations.save(history);
	}
}

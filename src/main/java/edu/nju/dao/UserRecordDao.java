package edu.nju.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import edu.nju.entities.UserRecord;

@Repository
public class UserRecordDao {
	
	@Autowired
	private MongoOperations mongoOperations;
	
	public void save(UserRecord userRecord) {
		mongoOperations.save(userRecord);
	}
	
	
}

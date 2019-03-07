package edu.nju.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nju.dao.DotDao;
import edu.nju.dao.UserRecordDao;
import edu.nju.entities.Dot;
import edu.nju.entities.UserRecord;

@Service
public class DotService {
	
	@Autowired
	DotDao dotDao;
	
	@Autowired
	UserRecordDao recordDao;
	
	public boolean saveType1(String id, String type1) {
		try {
			dotDao.save(new Dot(id, type1));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean saveRecord(String user_id, String target_id, String action, String remarks) {
		try {
			recordDao.save(new UserRecord(user_id, target_id, action, Long.toString(System.currentTimeMillis()), remarks));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}

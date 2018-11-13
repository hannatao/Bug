package edu.nju.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nju.dao.DotDao;
import edu.nju.entities.Dot;

@Service
public class DotService {
	
	@Autowired
	DotDao dotDao;
	
	public boolean saveType1(String id, String type1) {
		try {
			dotDao.save(new Dot(id, type1));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}

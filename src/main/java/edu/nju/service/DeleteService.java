package edu.nju.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nju.dao.BugDao;
import edu.nju.dao.BugHistoryDao;
import edu.nju.dao.BugMirrorDao;
import edu.nju.dao.BugPageDao;

@Service
public class DeleteService {
	@Autowired
	BugHistoryDao historydao;
	
	@Autowired
	BugMirrorDao mirrordao;
	
	@Autowired
	BugPageDao pagedao;
	
	@Autowired
	BugDao bugdao;
	
	@Autowired
	RecommendService recservice;
	
	public boolean deleteOne(String id) {
		try {
			historydao.remove(id);
			mirrordao.remove(id);
			bugdao.remove(id);
			pagedao.remove(id);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean deleteCase(String case_take_id) {
		try {
			List<String> ids = recservice.getListIds(case_take_id);
			bugdao.remove(ids);
			historydao.remove(ids);
			pagedao.remove(ids);
			mirrordao.remove(ids);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}

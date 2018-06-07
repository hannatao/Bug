package edu.nju.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nju.dao.CTBDao;

@Service
public class CTBService {
	
	@Autowired
	CTBDao ctbdao;
	
	public boolean save(String useCase, String bug_id, String case_take_id, String report_id) {
		try {
			ctbdao.save(useCase, bug_id, case_take_id, report_id);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public List<String> findById(String useCase) {
		List<String> lists = ctbdao.findById(useCase);
		if(lists == null) {return null;}
		return lists;
	}
	
	public boolean remove(String useCase, String bug_id) {
		try {
			ctbdao.remove(useCase, bug_id);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean removeAll(String useCase) {
		try {
			ctbdao.remove(useCase);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}

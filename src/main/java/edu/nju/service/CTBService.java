package edu.nju.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nju.dao.CTBDao;
import edu.nju.entities.CaseToBug;

@Service
public class CTBService {
	
	@Autowired
	CTBDao ctbdao;
	
	public boolean save(String useCase, String bug_id) {
		try {
			ctbdao.save(new CaseToBug(useCase, bug_id));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public List<String> findById(String useCase) {
		List<CaseToBug> lists = ctbdao.findById(useCase);
		if(lists == null) {return null;}
		List<String> result = new ArrayList<String>();
		for(CaseToBug ctb : lists) {
			result.add(ctb.getBug_id());
		}
		return result;
	}
	
	public boolean remove(String bug_id) {
		try {
			ctbdao.remove(bug_id);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}

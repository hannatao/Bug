package edu.nju.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nju.dao.CTBDao;
import edu.nju.entities.CaseToBug;

@Service
public class AnalyzeService {
	
	@Autowired
	CTBDao ctbdao;
	
	public List<String> getValid(String case_take_id) {
		List<String> result = new ArrayList<String>();
		List<CaseToBug> lists = ctbdao.findByCase(case_take_id);
		for(CaseToBug ctb : lists) {
			for(String str: ctb.getBug_id()) {
				if(!result.contains(str)) {result.add(str);}
			}
		}
		return result;
	}
}

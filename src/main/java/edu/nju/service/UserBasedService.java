package edu.nju.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nju.dao.BugDao;
import edu.nju.dao.BugHistoryDao;
import edu.nju.dao.BugMirrorDao;
import edu.nju.entities.BugMirror;
import edu.nju.util.UserCF;

@Service
public class UserBasedService {
	
	@Autowired
	BugMirrorDao mirrordao;
	
	@Autowired
	BugDao bugdao;
	
	@Autowired
	BugHistoryDao historydao;
		
	public List<BugMirror> UserBased(String[] report_ids) {
		UserCF cf = new UserCF();
		String[][] input = new String[report_ids.length][];
		for(int i = 0; i < input.length; i ++) {
			List<String> list = getBugs(report_ids[i]);
			list.add(0, report_ids[i]);
			input[i] = list.toArray(new String[list.size()]);
		}
		return mirrordao.findByIds(cf.calculate(input));
	}
	
	public List<String> getBugs(String report_id) {
		List<String> result = historydao.findRoots(bugdao.findByReport(report_id));
		if(result == null) {
			return new ArrayList<String>();
		} else {
			return result;
		}
	}
	
}

package edu.nju.service;

import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nju.dao.BugDao;
import edu.nju.dao.BugHistoryDao;
import edu.nju.dao.BugMirrorDao;
import edu.nju.entities.Bug;
import edu.nju.entities.BugHistory;
import edu.nju.entities.BugMirror;
import edu.nju.util.UUid;

@Service
public class SaveService {
	
	@Autowired
	BugMirrorDao mirrordao;
	
	@Autowired
	BugDao bugdao;
	
	@Autowired
	BugHistoryDao historydao;
	
	public boolean save(String case_take_id, String bug_category, String description, String img_url, int severity, int recurrent, String title, String report_id, String parent) {
		try {
			String id = new UUid().getUUID32();
			bugdao.save(new Bug(id, case_take_id, Long.toString(System.currentTimeMillis()), bug_category, description, img_url, severity, recurrent, title, report_id));
			mirrordao.save(new BugMirror(id, case_take_id, bug_category, severity, recurrent, title, img_url, new HashSet<String>(), new HashSet<String>()));
			historydao.save(new BugHistory(id, parent, new ArrayList<String>()));
			if(parent != "null") {
				historydao.addChild(parent, id);
			}
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public boolean confirm(String id, String report_id) {
		try {
			if(mirrordao.haveJudged(id, report_id)) {
				mirrordao.good(id, report_id);
				return true;
			}
			return false;
		} catch(Exception e) {
			return false;
		}
	}
	
	public boolean diss(String id, String report_id) {
		try {
			if(mirrordao.haveJudged(id, report_id)) {
				mirrordao.bad(id, report_id);
				return true;
			}
			return false;
		} catch(Exception e) {
			return false;
		}
	}
}

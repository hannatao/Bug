package edu.nju.service;

import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nju.dao.BugDao;
import edu.nju.dao.BugHistoryDao;
import edu.nju.dao.BugMirrorDao;
import edu.nju.dao.BugPageDao;
import edu.nju.dao.KWDao;
import edu.nju.dao.ThumsUpDao;
import edu.nju.entities.Bug;
import edu.nju.entities.BugHistory;
import edu.nju.entities.BugMirror;
import edu.nju.entities.BugPage;
import edu.nju.entities.KeyWords;
import edu.nju.util.StringMatch;

@Service
public class SaveService {
	
	@Autowired
	BugMirrorDao mirrordao;
	
	@Autowired
	BugDao bugdao;
	
	@Autowired
	BugHistoryDao historydao;
	
	@Autowired
	BugPageDao pagedao;
	
	@Autowired
	HistoryService hisservice;
	
	@Autowired
	KWDao kwdao;
	
	@Autowired
	ThumsUpDao tdao;
	
	public boolean save(String id, String case_take_id, String bug_category, String description, String img_url, String severity, String recurrent, String title, String report_id, String parent, String page, String useCase, String case_id) {
		try {
			StringMatch match = new StringMatch();
			bugdao.save(new Bug(id, case_take_id, Long.toString(System.currentTimeMillis()), bug_category, description, img_url, severityTranse(severity), recurrentTranse(recurrent), title, report_id, page, case_id));
			mirrordao.save(new BugMirror(id, case_take_id, bug_category, severityTranse(severity), recurrentTranse(recurrent), title, img_url, new HashSet<String>(), new HashSet<String>(), report_id, useCase, true));
			kwdao.save(new KeyWords(id, match.Ansj(title), match.Ansj(description)));
			if(!parent.equals("null")) {
				historydao.addChild(parent, id);
				String p = hisservice.parents(parent).get(0);
				historydao.save(new BugHistory(id, parent, new ArrayList<String>(), p));
			} else {
				historydao.save(new BugHistory(id, parent, new ArrayList<String>(), id));
			}
			
			if(!page.equals("")) {
				savePage(id, case_take_id, page);
			}
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
//	public boolean update(String id, String case_take_id, String bug_category, String description, String img_url, int severity, int recurrent, String title, String report_id, String parent,String page, String useCase, String case_id) {
//		try {
//			bugdao.save(new Bug(id, case_take_id, Long.toString(System.currentTimeMillis()), bug_category, description, img_url, severityTranse(severity), recurrentTranse(recurrent), title, report_id, page, case_id));
//			mirrordao.save(new BugMirror(id, case_take_id, bug_category, severityTranse(severity), recurrentTranse(recurrent), title, img_url, new HashSet<String>(), new HashSet<String>(), report_id, useCase, true));
//			if(!page.equals("")) {
//				savePage(id, case_take_id, page);
//			}
//			return true;
//		} catch(Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
	
	public boolean confirm(String id, String report_id) {
		try {
			if(mirrordao.haveJudged(id, report_id)) {
				mirrordao.good(id, report_id);
				tdao.saveGood(id, report_id);
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
				tdao.saveDiss(id, report_id);
				return true;
			}
			return false;
		} catch(Exception e) {
			return false;
		}
	}
	
	private void savePage(String id, String case_take_id, String page) {
		String[] pages = page.split("-");
		int length = pages.length;
		String page2 = "";
		String page3 = "";
		if(length == 2) {
			page2 = pages[1];
		} else if(length == 3) {
			page2 = pages[1];
			page3 = pages[2];
		}
		BugPage save = new BugPage(id, pages[0], page2, page3, case_take_id);
		pagedao.save(save);
	}
	
	private int severityTranse(String str) {
		if(str.equals("待定")) {
			return 1;
		}
		if(str.equals("较轻")) {
			return 2;
		}
		if(str.equals("一般")) {
			return 3;
		}
		if(str.equals("严重")) {
			return 4;
		}
		if(str.equals("紧急")) {
			return 5;
		}
		return 0;
	}
	
	private int recurrentTranse(String str) {
		if(str.equals("其他")) {
			return 1;
		}
		if(str.equals("无规律复现")) {
			return 2;
		}
		if(str.equals("小概率复现")) {
			return 3;
		}
		if(str.equals("大概率复现")) {
			return 4;
		}
		if(str.equals("必现")) {
			return 5;
		}
		return 0;
	}
}

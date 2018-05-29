package edu.nju.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nju.dao.BugHistoryDao;
import edu.nju.dao.BugMirrorDao;
import edu.nju.dao.BugScoreDao;
import edu.nju.dao.CTBDao;
import edu.nju.entities.BugHistory;
import edu.nju.entities.BugMirror;
import edu.nju.entities.BugScore;
import edu.nju.entities.CaseToBug;

@Service
public class AnalyzeService {
	
	@Autowired
	CTBDao ctbdao;
	
	@Autowired
	BugScoreDao bsdao;
	
	@Autowired
	BugHistoryDao hdao;
	
	@Autowired
	BugMirrorDao mdao;
	
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
	
	public List<String> getReports(String case_take_id) {
		List<String> result = new ArrayList<String>();
		List<CaseToBug> lists = ctbdao.findByCase(case_take_id);
		for(CaseToBug ctb : lists) {
			if(!result.contains(ctb.getReport_id())) {result.add(ctb.getReport_id());}
		}
		return result;
	}
	
	public int getGrade(String id) {
		return bsdao.findById(id).getGrade();
	}
	
	public boolean saveGrade(String id, int grade) {
		try {
			bsdao.save(new BugScore(id, grade, 0));
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public int mark(String id, Map<String, Integer> grades, BugMirror mirror) {
		int mark = 0;
		int grade = grades.get(id);
		BugHistory history = hdao.findByid(id);
		
		int parent = 0;
		if(!history.getParent().equals("null")) {
			parent = grades.get(history.getParent());
		}
		int count = 0;
		for(String child : history.getChildren()) {
			if(grades.get(child) <= 2) {
				count ++;
			}
		}
		
		switch(grade) {
			case 1:
				if(parent == 1) {mark += 40;}
				else {mark += 100;}
				mark += count * 2;
				break;
			case 2:
				if(parent == 1) {break;}
				else if(parent == 2) {mark += 40;}
				else {mark += 80;}
				mark += count * 2;
				break;
			case 3:
				mark += count;
				break;
		}
		
		if(grade <= 2) {
			mark += 2 * (mirror.getGood().size() - mirror.getBad().size());
		}
		
		return mark;
	}
	
	public Map<String, Integer> getScores(String case_take_id) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		Map<String, Integer> grades = new HashMap<String, Integer>();
		List<String> bugs = getValid(case_take_id);
		for(String bug: bugs) {
			grades.put(bug, bsdao.findById(bug).getGrade());
		}
		for(String bug: bugs) {
			BugMirror mirror = mdao.findById(bug);
			int grade = grades.get(bug);
			if(grade == 1) {ThumsUp(5, result, mirror);}
			else if(grade == 2) {ThumsUp(3, result, mirror);}
			else {ThumsUp(-3, result, mirror);}
			result.put(mirror.getReport_id(), result.getOrDefault(mirror.getReport_id(), 0) + mark(bug, grades, mirror));
		}
		return result;
	}
	
	private void ThumsUp(int grade, Map<String, Integer> result, BugMirror mirror) {
		for(String report : mirror.getGood()) {
			result.put(report, result.getOrDefault(report, 0) + grade);
		}
		for(String report : mirror.getBad()) {
			result.put(report, result.getOrDefault(report, 0) - grade);
		}
	}
}

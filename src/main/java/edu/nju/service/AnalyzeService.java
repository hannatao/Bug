package edu.nju.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nju.dao.BugDao;
import edu.nju.dao.BugHistoryDao;
import edu.nju.dao.BugMirrorDao;
import edu.nju.dao.BugScoreDao;
import edu.nju.dao.CTBDao;
import edu.nju.entities.Bug;
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
	
	@Autowired
	BugDao bdao;
	
	@Autowired
	HistoryService hservice;
	
	//获取所有bug
	public List<String> getValid(String case_take_id) {
		List<String> result = new ArrayList<String>();
		List<BugMirror> mirrors = mdao.findValid(case_take_id);
		for(BugMirror ctb : mirrors) {
			result.add(ctb.getId());
		}
		return result;
	}
	
	//获取所有有测试用例的bug
	public List<String> getValidTwo(String case_take_id) {
		List<String> result = new ArrayList<String>();
		List<CaseToBug> lists = ctbdao.findByCase(case_take_id);
		for(CaseToBug ctb : lists) {
			for(String str: ctb.getBug_id()) {
				result.add(str);
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
		BugScore bs = bsdao.findById(id);
		if(bs != null) {return bs.getGrade();}
		return -1;
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
			parent = grades.getOrDefault(history.getParent(), 0);
		}
		int count = 0;
		for(String child : history.getChildren()) {
			if(grades.getOrDefault(child, 3) <= 2) {count ++;}
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
		Map<String, Integer> result = new HashMap<String, Integer>(); //用户得分
		Map<String, Integer> grades = new HashMap<String, Integer>(); //专家评价分
		Map<String, Integer> scores = new HashMap<String, Integer>(); //计算bug得分
		List<String> bugs = getValid(case_take_id);
		for(String bug: bugs) {
			BugScore temp = bsdao.findById(bug);
			if(temp != null) {grades.put(bug, temp.getGrade());}
			else {grades.put(bug, 0);}
		}
//		for(String bug: bugs) {
//			BugMirror mirror = mdao.findById(bug);
//			int grade = grades.get(bug);
//			if(grade == 0) {continue;}
//			if(grade == 1) {ThumsUp(5, result, mirror);}
//			else if(grade == 2) {ThumsUp(3, result, mirror);}
//			else {ThumsUp(-3, result, mirror);}
//			result.put(mirror.getReport_id(), result.getOrDefault(mirror.getReport_id(), 0) + mark(bug, grades, mirror));
//		}
		countScore(case_take_id, scores, grades);
		for(String bug: bugs) {
			BugMirror mirror = mdao.findById(bug);
			if(mirror == null) { continue; }
			int grade = grades.getOrDefault(bug, 0);
			if(grade >= 8) { ThumsUp(1, result, mirror); }
			if(grade <= 2) { ThumsUp(-1, result, mirror); }
		}
		for(Map.Entry<String, Integer> entry : result.entrySet()) {
			if(entry.getValue() > 10) { result.put(entry.getKey(), 10); }
			if(entry.getValue() < 0) { result.put(entry.getKey(), 0); }
		}
		for(String bug: bugs) {
			BugMirror mirror = mdao.findById(bug);
			if(mirror == null) { continue; }
			result.put(mirror.getReport_id(), result.getOrDefault(mirror.getReport_id(), 0) + scores.getOrDefault(bug, 0));
		}
		return result;
	}
	
	//计算点赞得分
	private void ThumsUp(int grade, Map<String, Integer> result, BugMirror mirror) {
		for(String report : mirror.getGood()) {
			result.put(report, result.getOrDefault(report, 0) + grade);
		}
		for(String report : mirror.getBad()) {
			result.put(report, result.getOrDefault(report, 0) - grade);
		}
	}
	
	public Map<String, String> getThums(String case_take_id) {
		Map<String, String> result = new HashMap<String, String>();
		List<String> bugs = getValid(case_take_id);
		for(String bug: bugs) {
			BugMirror mirror = mdao.findById(bug);
			if(mirror.getGood().size() > 0 || mirror.getBad().size() > 0) {
				result.put(bug, mirror.getGood().size() + "," + mirror.getBad().size());
			}
		}
		return result;
	}
	
	public Map<String, Integer> getBugDetail(String case_take_id) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		List<String> bugs = getValid(case_take_id);
		for(String id : bugs) {
			Bug bug = bdao.findByid(id);
			result.put(bug.getBug_page(), result.getOrDefault(bug.getBug_page(), 0) + 1);
		}
		return result;
	}
	
	public Map<String, Integer> getAllGrades(String case_take_id) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		List<BugMirror> mlist = mdao.findByCase(case_take_id);
		List<String> idlist = new ArrayList<String>();
		for(BugMirror mirror : mlist) {
			idlist.add(mirror.getId());
		}
		List<BugScore> slist = bsdao.findByIds(idlist);
		for(BugScore bugscore: slist) {
			result.put(bugscore.getId(), bugscore.getGrade());
		}
		return result;
	}
	
	public List<String> getDiff(String case_take_id) {
		List<String> bugs = getValid(case_take_id);
		bugs.add("split");
		for(Map.Entry<String, Integer> entry: getAllGrades(case_take_id).entrySet()) {
			if(bugs.contains(entry.getKey())) {bugs.remove(entry.getKey());}
			else {bugs.add(entry.getKey());}
		}
		return bugs;
	}
	
	//评价页面获取评分
	public List<List<String>> getScores(List<String> ids) {
		List<List<String>> result = new ArrayList<List<String>>();
		List<BugScore> list = bsdao.findByIds(ids);
		for(BugScore bs: list) {
			List<String> temp = new ArrayList<String>();
			temp.add(bs.getId());
			temp.add(Integer.toString(bs.getGrade()));
			result.add(temp);
		}
		return result;
	}
	
	//根据树状结构计算分数
	public void countScore(String case_take_id, Map<String, Integer> result, Map<String, Integer> grades) {
		List<String> roots = hservice.getRoots(case_take_id);
		for(String root : roots) {
			List<List<String>> lists = hservice.getDepth(root);
			for(List<String> path : lists) {
				int max = 0;
				for(String id : path) {
					int grade = grades.getOrDefault(id, 0);
					result.put(id, Math.max(grade - max, 0));
					max = Math.max(max, grade);
				}
			}
		}
	}
}

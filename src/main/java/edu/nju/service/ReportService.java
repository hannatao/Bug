package edu.nju.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nju.dao.BugDao;
import edu.nju.dao.BugHistoryDao;
import edu.nju.dao.BugMirrorDao;
import edu.nju.dao.BugScoreDao;
import edu.nju.dao.CTBDao;
import edu.nju.dao.ThumsUpDao;
import edu.nju.entities.Bug;
import edu.nju.entities.BugHistory;
import edu.nju.entities.BugScore;
import edu.nju.entities.ThumsUp;

@Service
public class ReportService {
	
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
	ThumsUpDao tdao;
	
	@Autowired
	AnalyzeService aservice;
	
	@Autowired
	HistoryService hservice;
	
	public List<String> getUserBugs(String report_id) {
		return mdao.findByReport(report_id);
	}
	
	public Map<String, Integer> getUserPath(String report_id) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		List<String> ids = getUserBugs(report_id);
		for(String id: ids) {
			Bug bug = bdao.findByid(id);
			result.put(bug.getBug_page(), result.getOrDefault(bug.getBug_page(), 0) + 1);
		}
		return result;
	}
	
	public int getValid(String report_id) {
		List<String> ids = getUserBugs(report_id);
		List<BugScore> bugscore = bsdao.findByIds(ids);
		int count = 0;
		for(BugScore bs: bugscore) {
			if(bs.getGrade() >= 8) {count ++;}
		}
		return count;
	}
	
	public double getTimeGap(String report_id) {
		List<String> ids = getUserBugs(report_id);
		if(ids.size() <= 1) {return 0;}
		Bug start = bdao.findByid(ids.get(0));
		Bug end = bdao.findByid(ids.get(ids.size() - 1));
		return Double.parseDouble(end.getCreate_time_millis()) - Double.parseDouble(start.getCreate_time_millis());
	}
	
	public Set<String> getAllThums(String report_id) {
		ThumsUp thumsup = tdao.findByReport(report_id);
		if(thumsup != null) {return thumsup.getThums();}
		return null;
	}
	
	public Set<String> getAllDiss(String report_id) {
		ThumsUp thumsup = tdao.findByReport(report_id);
		if(thumsup != null) {return thumsup.getDiss();}
		return null;
	}
	
	public int getValidThums(String report_id) {
		Set<String> thums = getAllThums(report_id);
		if(thums == null) {return 0;}
		int count = 0;
		for(String id: thums) {
			BugScore bs = bsdao.findById(id);
			if(bs.getGrade() >= 8) {count ++;}
		}
		return count;
	}
	
	public int getValidDiss(String report_id) {
		Set<String> diss = getAllDiss(report_id);
		if(diss == null) {return 0;}
		int count = 0;
		for(String id: diss) {
			BugScore bs = bsdao.findById(id);
			if(bs.getGrade() <= 2) {count ++;}
		}
		return count;
	}
	
	public List<Entry<String, Integer>> getThumsRank(String case_take_id) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		List<String> reports = aservice.getReports(case_take_id);
		for(String report : reports) {
			Set<String> thums = getAllThums(report);
			if(thums != null && thums.size() != 0) { result.put(report, thums.size()); }
		}
		List<Entry<String, Integer>> map_list = new ArrayList<>(result.entrySet());
		Collections.sort(map_list, (a, b) -> (b.getValue() - a.getValue()));
		return map_list;
	}
	
	public List<Entry<String, Integer>> getForkRank(String case_take_id) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		List<String> list = aservice.getValid(case_take_id);
		for(String id : list) {
			BugHistory history = hdao.findByid(id);
			if(history == null) { continue; }
			if(!history.getParent().equals("null")) { 
				String key = bdao.findByid(id).getReport_id();
				result.put(key, result.getOrDefault(key, 0) + 1);
			}
		}
		List<Entry<String, Integer>> map_list = new ArrayList<>(result.entrySet());
		Collections.sort(map_list, (a, b) -> (b.getValue() - a.getValue()));
		return map_list;
	}
	
	public List<JSONObject> relations(String case_take_id) {
		List<JSONObject> result = new ArrayList<JSONObject>();
		List<String> trees = hservice.getTreeRoots(case_take_id);
		Set<String> reports = new HashSet<String>();
		List<List<String>> links = new ArrayList<List<String>>();
		
		JSONObject tree_nodes = new JSONObject();
		JSONObject report_nodes = new JSONObject();
		JSONObject link = new JSONObject();
		
		
		for(String tree: trees) {
			for(List<String> path : hservice.getDepth(tree)) {
				for(String id: path) {
					String report = bdao.findByid(id).getReport_id();
					reports.add(report);
					List<String> temp = new ArrayList<String>();
					temp.add(report);
					temp.add(tree);
					links.add(temp);
				}
			}
		}
		
		tree_nodes.put("TreeNode", trees);
		report_nodes.put("PersonNode", reports);
		link.put("Link", links);
		result.add(tree_nodes);
		result.add(report_nodes);
		result.add(link);
		return result;
	}
	
}
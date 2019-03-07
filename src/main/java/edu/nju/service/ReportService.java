package edu.nju.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nju.dao.BugDao;
import edu.nju.dao.BugHistoryDao;
import edu.nju.dao.BugMirrorDao;
import edu.nju.dao.BugScoreDao;
import edu.nju.dao.CTBDao;
import edu.nju.dao.KWDao;
import edu.nju.dao.StuInfoDao;
import edu.nju.dao.ThumsUpDao;
import edu.nju.entities.Bug;
import edu.nju.entities.BugHistory;
import edu.nju.entities.BugMirror;
import edu.nju.entities.BugScore;
import edu.nju.entities.KeyWords;
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
	KWDao kdao;
	
	@Autowired
	StuInfoDao studao;
	
	@Autowired
	AnalyzeService aservice;
	
	@Autowired
	HistoryService hservice;
	
	public List<String> getUserBugs(String report_id, String case_take_id) {
		return mdao.findIdsByReport(report_id, case_take_id);
	}
	
	public Map<String, Integer> getUserPath(String report_id, String case_take_id) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		List<String> ids = getUserBugs(report_id, case_take_id);
		for(String id: ids) {
			Bug bug = bdao.findByid(id);
			result.put(bug.getBug_page(), result.getOrDefault(bug.getBug_page(), 0) + 1);
		}
		return result;
	}
	
	public int getValid(String report_id, String case_take_id) {
		List<String> ids = getUserBugs(report_id, case_take_id);
		List<BugScore> bugscore = bsdao.findByIds(ids);
		int count = 0;
		for(BugScore bs: bugscore) {
			if(bs.getGrade() >= 8) {count ++;}
		}
		return count;
	}
	
	public double getTimeGap(String report_id, String case_take_id) {
		List<String> ids = getUserBugs(report_id, case_take_id);
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
	
//	public JSONObject getThumsRank(String case_take_id) {
//		Map<String, Integer> result = new HashMap<String, Integer>();
//		List<String> reports = aservice.getReports(case_take_id);
//		for(String report : reports) {
//			Set<String> thums = getAllThums(report);
//			if(thums != null && thums.size() != 0) { result.put(report, thums.size()); }
//		}
//		return rank_sort(result);
//	}
	
	public JSONObject getThumsRank(String case_take_id) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		List<String> bugs = aservice.getValid(case_take_id);
		for(String bug: bugs) {
			BugMirror bugmirror = mdao.findById(bug);
			if(bugmirror == null) { continue; }
			String report = bugmirror.getReport_id();
			int thums = bugmirror.getGood().size();
			result.put(report, result.getOrDefault(report, 0) + thums);
		}
		return rank_sort(result);
	}
	
	public JSONObject getForkRank(String case_take_id) {
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
		return rank_sort(result);
	}
	
	public JSONObject relations(String case_take_id) {
		JSONObject result = new JSONObject();
		List<String> trees = hservice.getTreeRoots(case_take_id);
		Set<String> reports = new HashSet<String>();
		List<List<String>> links = new ArrayList<List<String>>();
		
		for(String tree: trees) {
			for(List<String> path : hservice.getDepth(tree)) {
				for(String id: path) {
					String report = report_trans(bdao.findByid(id).getReport_id());
					reports.add(report);
					List<String> temp = new ArrayList<String>();
					temp.add(report);
					temp.add(tree);
					links.add(temp);
				}
			}
		}
		
		result.put("TreeNode", trees);
		result.put("PersonNode", reports);
		result.put("Link", links);
		return result;
	}
	
	public JSONArray charm(String case_take_id) {
		JSONArray array = new JSONArray();
		Map<String, int[]> thumsup = new HashMap<String, int[]>();
		Map<String, int[]> bugTotal = new HashMap<String, int[]>();
		List<String> bugs = aservice.getValid(case_take_id);
		List<String> reports = aservice.getReports(case_take_id);
		for(String id: bugs) {
			Bug bug = bdao.findByid(id);
			BugScore bugscore = bsdao.findById(id);
			if(bug == null || bugscore == null || bug.getId().equals("10010000034853")) { continue; }
			String report_id = bug.getReport_id();
			int[] temp_score = bugTotal.getOrDefault(report_id, new int[2]);
			if(bugscore.getGrade() > 0) { temp_score[0] += 1; }
			temp_score[1] += 1;
			bugTotal.put(report_id, temp_score);
		}
		for(String report: reports) {
			int[] temp_score = new int[2];
			ThumsUp thums = tdao.findByReport(report);
			if(thums == null) { continue; }
			for(String id: thums.getThums()) {
				Bug bug = bdao.findByid(id);
				if(bug == null || bug.getId().equals("10010000034853")) { continue; }
				BugScore bugscore = bsdao.findById(id);
				if(bugscore == null) { continue; }
				if(bugscore.getGrade() > 0) { temp_score[0] += 1; }
				temp_score[1] += 1;
			}
			thumsup.put(report, temp_score);
		}
		for(Map.Entry<String, int[]> entry: bugTotal.entrySet()) {
			JSONObject json = new JSONObject();
			String key = entry.getKey();
			int[] bug_score = entry.getValue();
			int[] thums_score = thumsup.getOrDefault(key, new int[2]);
			String value = "bug-" + bug_score[0] + "/" + bug_score[1] 
					+ ";thumsup-" + thums_score[0] + "/" + thums_score[1];
			json.put(report_trans(key), value);
			array.put(json);
		}
		return array;
	}
	
	public JSONObject keyWords(String id) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		if(id != null || id != "") {
			for(List<String> paths : hservice.getDepth(id)) {
				for(String path : paths) {
					KeyWords keywords = kdao.findById(path);
					if(keywords == null) { continue; }
					String[] words = keywords.getDescription().split(",");
					for(String word : words) {
						map.put(word, map.getOrDefault(word, 0) + 1);
					}
				}
			}
		}
		return map_sort(map);
	}
	
	private JSONObject rank_sort(Map<String, Integer> result) {
		JSONObject json = new JSONObject(true);
//		List<Entry<String, Integer>> map_list = new ArrayList<>(result.entrySet());
//		Collections.sort(map_list, (a, b) -> (b.getValue() - a.getValue()));
		for(Entry<String, Integer> entry : result.entrySet()) {
			json.put(report_trans(entry.getKey()), entry.getValue());
		}
		return json;
	}
	
	private JSONObject map_sort(Map<String, Integer> result) {
		JSONObject json = new JSONObject(true);
		for(Entry<String, Integer> entry : result.entrySet()) {
			json.put(entry.getKey(), entry.getValue());
		}
		return json;
	}
	
	private String report_trans(String report_id) {
		String name = studao.findById(report_id);
		if(name == null || name.equals("null")) { return report_id;}
		return name;
	}
	
}
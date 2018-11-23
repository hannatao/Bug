package edu.nju.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import edu.nju.dao.DotDao;
import edu.nju.dao.StuInfoDao;
import edu.nju.dao.ThumsUpDao;
import edu.nju.entities.Bug;
import edu.nju.entities.BugHistory;
import edu.nju.entities.BugMirror;
import edu.nju.entities.BugScore;
import edu.nju.entities.CaseToBug;
import edu.nju.entities.ThumsUp;

@Service
public class GraphService {
	
	@Autowired
	BugMirrorDao mdao;
	
	@Autowired
	BugDao bdao;
	
	@Autowired
	BugScoreDao bsdao;
	
	@Autowired
	ThumsUpDao tdao;
	
	@Autowired
	BugHistoryDao hdao;
	
	@Autowired
	CTBDao ctbdao;
	
	@Autowired
	DotDao dotdao;
	
	@Autowired
	StuInfoDao studao;
	
	@Autowired
	HistoryService hservice;
	
	//获取所有bug
	public List<BugMirror> getBugs(String case_take_id) {
		return mdao.findByCase(case_take_id);
	}
	
	//获取所有report_id
	public Set<String> getReports(String case_take_id) {
		Set<String> result = new HashSet<String>();
		for(BugMirror mirror: getBugs(case_take_id)) {
			result.add(mirror.getReport_id());
		}
		return result;
	}
	
	//获取用户被点赞数
	public Map<String, Integer> getUserThums(String case_take_id) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		List<BugMirror> mirrors = getBugs(case_take_id);
		for(BugMirror mirror: mirrors) {
			String report_id = mirror.getReport_id();
			result.put(report_id, result.getOrDefault(report_id, 0) + mirror.getGood().size());
		}
		return result;
	}
	
	//获取用户所有bug
	public Map<String, Integer> getUserBugs(String case_take_id) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		List<BugMirror> mirrors = getBugs(case_take_id);
		for(BugMirror mirror: mirrors) {
			String report_id = mirror.getReport_id();
			result.put(report_id, result.getOrDefault(report_id, 0) + 1);
		}
		return result;
	}
	
	//获取用户有效bug
	public Map<String, Integer> getUserValid(String case_take_id) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		List<BugMirror> mirrors = getBugs(case_take_id);
		for(BugMirror mirror: mirrors) {
			String report_id = mirror.getReport_id();
			BugScore score = bsdao.findById(mirror.getId());
			if(score == null || score.getGrade() <= 0) { continue; }
			result.put(report_id, result.getOrDefault(report_id, 0) + 1);
		}
		return result;
	}
	
	//一（1）用例数目、与Bug数目、有效bug数目关系
	public JSONArray get_case_bug_valid(String case_take_id) {
		JSONArray array = new JSONArray();
		Set<String> reports = getReports(case_take_id);
		for(String report_id : reports) {
			List<CaseToBug> ctbs = ctbdao.findByReport(report_id);
			if(ctbs == null || ctbs.size() <= 0) { continue; }
			int valid = 0;
			int total = 0;
			for(CaseToBug ctb : ctbs) {
				List<String> bugs = ctb.getBug_id();
				total += bugs.size();
				for(String id : bugs) {
					BugScore score= bsdao.findById(id);
					if(score == null || score.getGrade() <= 0) { continue ;}
					valid += 1;
				}
			}
			JSONObject json = new JSONObject();
			json.put("report_id", report_id);
			json.put("use_case", ctbs.size());
			json.put("total_bugs", total);
			json.put("valid_bugs", valid);
			array.put(json);
		}
		
		return array;
	}
	
	//一（2）被点赞数、bug数目、有效bug数目
	public JSONArray get_thums_total_valid(String case_take_id) {
		JSONArray array = new JSONArray();
		Map<String, Integer> userBugs = getUserBugs(case_take_id);
		Map<String, Integer> userValid = getUserValid(case_take_id);
		Map<String, Integer> userThums = getUserThums(case_take_id);
		for(Map.Entry<String, Integer> entry : userBugs.entrySet()) {
			JSONObject json = new JSONObject();
			json.put("report_id", entry.getKey());
			json.put("total_bugs", entry.getValue());
			json.put("valid_bugs", userValid.getOrDefault(entry.getKey(), 0));
			json.put("thumsUp", userThums.getOrDefault(entry.getKey(), 0));
			array.put(json);
		}
		return array;
	}
	
	//一（3）有效Bug数目、点赞关系
	public JSONObject get_valid_thums(String case_take_id) {
		JSONObject json = new JSONObject();
		JSONArray nodes = new JSONArray();
		JSONArray links = new JSONArray();
		Map<String, Integer> userValid = getUserValid(case_take_id);
		Map<String, Integer> link_map = new HashMap<String, Integer>();
		for(Map.Entry<String, Integer> entry : userValid.entrySet()) {
			JSONObject node = new JSONObject();
			String key = entry.getKey();
			node.put("name", key);
			node.put("value", entry.getValue());
			nodes.put(node);
			ThumsUp thums = tdao.findByReport(key);
			if(thums == null) { continue; }
			for(String id : thums.getThums()) {
				BugMirror mirror = mdao.findById(id);
				if(mirror == null) { continue; }
				String new_key = key + "-" + mirror.getReport_id();
				link_map.put(new_key, link_map.getOrDefault(new_key, 0) + 1);
			}
		}
		for(Map.Entry<String, Integer> entry: link_map.entrySet()) {
			JSONObject link = new JSONObject();
			String[] key = entry.getKey().split("-");
			link.put("source", key[0]);
			link.put("target", key[1]);
			link.put("value", entry.getValue());
			links.put(link);
		}
		json.put("nodes", nodes);
		json.put("links", links);
		return json;
	}
	
	//一（4）有效Bug数目、fork数目
	public JSONObject get_valid_fork(String case_take_id, String end) {
		JSONObject json = new JSONObject();
		JSONArray nodes = new JSONArray();
		JSONArray links = new JSONArray();
		try {
			Map<String, String> map = new HashMap<String, String>();
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long deadLine = dateformat.parse(end).getTime();
			List<Bug> bugs = bdao.findByCaseid(case_take_id);
			for(Bug bug : bugs) {
				if(Long.parseLong(bug.getCreate_time_millis()) <= deadLine) {
					map.put(bug.getId(), bug.getReport_id());
				}
			}
			Map<String, Integer> node_value = new HashMap<String, Integer>();
			Map<String, Integer> link_value = new HashMap<String, Integer>();
			for(Map.Entry<String, String> entry : map.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				BugHistory history = hdao.findByid(key);
				if(history == null) { continue; }
				if(!history.getParent().equals("null")) {
					node_value.put(value, node_value.getOrDefault(value, 0) + 1);
					String link = value + "-" + map.get(history.getParent());
					link_value.put(link, link_value.getOrDefault(link, 0) + 1);
				} else if(history.getChildren().size() > 0) {
					node_value.put(value, node_value.getOrDefault(value, 0) + 1);
				}
			}
			for(Map.Entry<String, Integer> entry : node_value.entrySet()) {
				JSONObject node = new JSONObject();
				node.put("name", entry.getKey());
				node.put("value", entry.getValue());
				node.put("id", report_trans(entry.getKey()));
				nodes.put(node);
			}
			for(Map.Entry<String, Integer> entry : link_value.entrySet()) {
				JSONObject link = new JSONObject();
				String[] key = entry.getKey().split("-");
				link.put("source", key[0]);
				link.put("target", key[1]);
				link.put("value", entry.getValue());
				links.put(link);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		json.put("nodes", nodes);
		json.put("links", links);
		return json;
	}
	
	//二（1）被点赞数与bug分数
	public JSONArray ThumsToScores(String case_take_id) {
		JSONArray array = new JSONArray();
		for(BugMirror mirror : getBugs(case_take_id)) {
			JSONObject json = new JSONObject();
			json.put("id", mirror.getId());
			json.put("thums", mirror.getGood().size());
			BugScore score = bsdao.findById(mirror.getId());
			if(score != null) { json.put("score", score.getGrade()); }
			else { json.put("score", 0); }
			array.put(json);
		}
		return array;
	}
	
//	//二（2）Bug报告、fork关系
//	public JSONObject get_bug_fork(String case_take_id, String end) {
//		JSONObject json = new JSONObject();
//		JSONArray nodes = new JSONArray();
//		JSONArray links = new JSONArray();
//		try {
//			Set<String> set = new HashSet<String>();
//			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			long deadLine = dateformat.parse(end).getTime();
//			List<Bug> bugs = bdao.findByCaseid(case_take_id);
//			for(Bug bug : bugs) {
//				if(Long.parseLong(bug.getCreate_time_millis()) <= deadLine) {
//					set.add(bug.getId());
//				}
//			}
//			for(String id: set) {
//				BugHistory history = hdao.findByid(id);
//				if(history == null) { continue; }
//				if(!history.getParent().equals("null") || history.getChildren().size() > 0) {
//					JSONObject node = new JSONObject();
//					node.put("name", id);
//					nodes.put(node);
//				}
//				if(!history.getParent().equals("null")) {
//					JSONObject link = new JSONObject();
//					link.put("source", id);
//					link.put("target", history.getParent());
//					links.put(link);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		json.put("nodes", nodes);
//		json.put("links", links);
//		return json;
//	}
	
	//二（2）Bug报告、fork关系
		public JSONArray get_bug_fork(String case_take_id, String end) {
			JSONArray result = new JSONArray();
			try {
				List<String> list = new ArrayList<String>();
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				long deadLine = dateformat.parse(end).getTime();
				List<Bug> bugs = bdao.findByCaseid(case_take_id);
				for(Bug bug : bugs) {
					if(Long.parseLong(bug.getCreate_time_millis()) <= deadLine) {
						list.add(bug.getId());
					}
				}
				for(String id: list) {
					BugHistory history = hdao.findByid(id);
					if(history == null) { continue; }
					if(history.getParent().equals("null") && history.getChildren().size() > 0) {
						List<List<String>> tree = hservice.getDepth(id);
						Set<String> set = new HashSet<String>();
						List<List<String>> new_tree = new ArrayList<List<String>>();
						for(List<String> path : tree) {
							String new_path = "";
							for(int i = 0; i < path.size(); i ++) {
								if(!list.contains(path.get(i))) {
									path.remove(path.get(i));
									i --;
								} else {
									new_path = new_path + "-" + path.get(i);
								}
							}
							if(!new_path.equals("")) {
								if(set.isEmpty()) {
									new_tree.add(path);
									set.add(new_path);
								} else if(new_path.split("-").length > 2 && !set.contains(new_path)) {
									new_tree.add(path);
									set.add(new_path);
								}
							}
						}
						result.put(new JSONArray(new_tree));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}	
	
	//三（1）推荐有效性对比，fork比率，有效比率，点赞比率
	public JSONObject get_rec_fork_valid_thums(String case_take_id) {
		JSONObject json = new JSONObject();
		int fork_1 = 0, fork_2 = 0, valid_1 = 0, valid_2 = 0, thums_1 = 0, thums_2 = 0;
		List<BugMirror> mirrors = getBugs(case_take_id);
		for(BugMirror mirror : mirrors) {
			String id = mirror.getId();
			String dot = dotdao.findByid(id);
			if(dot == null) { continue; }
			if(dot.equals("1")) {
				fork_1 ++;
				BugScore score = bsdao.findById(id);
				if(score != null && score.getGrade() > 0) { valid_1 ++; }
				if(mirror.getGood().size() > 0) { thums_1 ++;}
			} else {
				fork_2 ++;
				BugScore score = bsdao.findById(id);
				if(score != null && score.getGrade() > 0) { valid_2 ++; }
				if(mirror.getGood().size() > 0) { thums_2 ++;}
			}
		}
		json.put("fork_by_rec", fork_1);
		json.put("fork_by_self", fork_2);
		json.put("valid_by_rec", valid_1);
		json.put("valid_by_self", valid_2);
		json.put("thums_by_rec", thums_1);
		json.put("thums_by_self", thums_2);
		return json;
	}
	
	private String report_trans(String report_id) {
		String name = studao.findById(report_id);
		if(name == null || name.equals("null")) { return report_id;}
		return name;
	}
}

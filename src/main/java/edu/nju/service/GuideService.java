package edu.nju.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nju.entities.BugMirror;

@Service
public class GuideService {
	
	@Autowired
	DivRecService drService;
	
	@Autowired
	HttpService httpservice;
	
	@Autowired
	AnalyzeService aservice;
	
	@Autowired
	ReportService rservice;
	
	//用户二分类，0为审查，1为提交
	public int UserClassify(String report_id, String case_take_id) {
		List<List<String>> relations = drService.getAllRelation(case_take_id, report_id);
		int self = relations.get(0).size() - relations.get(3).size();
		int thumsUp = relations.get(1).size();
//		int diss = relations.get(2).size();
		int fork = relations.get(3).size();
		if(self <= 3 || self >= 8) { return 1; }
		if(self >= (thumsUp + fork) / 2) { return 1; }
		return 0;
	}
	
	//根据pmf预测用户的审查目标
	public List<BugMirror> PMFRecomend(String report_id, String case_take_id) {
		return drService.randomRec(report_id, case_take_id);
	}
	
	//根据用户历史推荐工作计划
	public String workPlan(String report_id, String case_take_id, HttpSession session) {
		String[] case_id = case_take_id.split("-");
		String QS = case_id[0];
		String structure = "";
		if(session.getAttribute(QS) == null) {
			structure = httpservice.sendGet("http://mooctest-site.oss-cn-shanghai.aliyuncs.com/json/" + QS + ".json" , "");
		} else {
			structure = session.getAttribute(QS).toString();
		}
		return jsonGet(structure, getPages(rservice.getUserPath(report_id, case_take_id)), getPages(aservice.getBugDetail(case_take_id)));
	}
	
	public List<String> getPages(Map<String, Integer> map) {
		Set<String> result = new HashSet<String>();
		for(Map.Entry<String, Integer> entry : map.entrySet()) {
			String[] pages = entry.getKey().split("-");
			if(pages.length > 0) {
				String key = pages[pages.length - 1];
				result.add(key);
			}
		}
		return new ArrayList<String>(result);
	}
	
	public String jsonGet(String structure, List<String> my, List<String> total) {
		JSONArray array = new JSONArray(structure);
		List<String> map = new ArrayList<String>();  // 页面按序存放
		for(int i = 0; i < array.length(); i ++) {
			JSONObject object = new JSONObject(array.get(i).toString());
			map.add(object.get("item").toString());
			JSONArray second = new JSONArray(object.get("children").toString());
			if(second.length() <= 0) { continue; }
			
			for(int j = 0; j < second.length(); j ++) {
				JSONObject object2 = new JSONObject(second.get(j).toString());
				map.add(object2.get("item").toString());
				JSONArray third = new JSONArray(object2.get("children").toString());
				if(third.length() <= 0) { continue; }
				
				for(int z = 0; z < third.length(); z ++) {
					JSONObject object3 = new JSONObject(third.get(z).toString());
					map.add(object3.get("item").toString());
				}
			}
		}
		int size = map.size();
		Map<String, Integer> hash = new HashMap<String, Integer>(); // 记录页面与序号
		for(int i = 0; i < size; i ++) { hash.put(map.get(i), i); }
		int[][] relation = new int[size][size];  // 关系数组
		for(int i = 0; i < array.length(); i ++) {
			JSONObject object = new JSONObject(array.get(i).toString());
			int n = hash.get(object.get("item").toString());
			relation[n][n] = -1;
			JSONArray second = new JSONArray(object.get("children").toString()); // 二级页面
			if(second.length() <= 0) { continue; }
			
			for(int j = 0; j < second.length(); j ++) {
				JSONObject object2 = new JSONObject(second.get(j).toString());  // 单个二级页面
				int m = hash.get(object2.get("item").toString());
				relation[m][m] = -1;
				relation[n][m] = relation[m][n] = 2;
				for(int j1 = j + 1; j1 < second.length(); j1 ++) {
					JSONObject temp = new JSONObject(second.get(j1).toString());
					int m1 = hash.get(temp.get("item").toString());
					relation[m][m1] = relation[m1][m] = 1;
				}
				JSONArray third = new JSONArray(object2.get("children").toString());
				if(third.length() <= 0) { continue; }
				
				for(int z = 0; z < third.length(); z ++) {
					JSONObject object3 = new JSONObject(third.get(z).toString());
					int l = hash.get(object3.get("item").toString());
					relation[l][l] = -1;
					relation[m][l] = relation[l][m] = 2;
					relation[n][l] = relation[l][n] = 3;
					for(int z1 = z + 1; z1 < third.length(); z1 ++) {
						JSONObject temp2 = new JSONObject(third.get(z1).toString());
						int l1 = hash.get(temp2.get("item").toString());
						relation[l][l1] = relation[l1][l] = 1;
					}
				}
			}
		}
		
		String advise = "";
		if(my.size() == map.size()) { return "页面已全部覆盖"; }  // 自己已经全部覆盖
		List<String> page_remain = new ArrayList<String>(map);  // 未全覆盖
		Map<String, Integer> scores = new HashMap<String, Integer>();
		if(total.size() == map.size()) {  // 整个测试环境已全覆盖
			advise = "当前所有页面均有人提交，您还可以在以下页面进行测试:";
		} else {
			advise = "推荐您去以下页面进行测试:";
			for(String page : total) {
				page_remain.remove(page);
			}
		}
		for(String page : my) {
			page_remain.remove(page);
		}
		for(String page : page_remain) {
			if(page.equals("")) { continue; }
			for(String my_page: my) {
				if(my_page.equals("")) { continue; }
				int score = relation[hash.get(page)][hash.get(my_page)];
				if(score > 0) { scores.put(page, score); }
			}
		}
		List<Entry<String, Integer>> entries = new ArrayList<Entry<String, Integer>>(scores.entrySet());
		Collections.sort(entries, (a, b) -> (a.getValue() - b.getValue()));
//		for(Entry<String, Integer> entry : entries) {
//			System.out.println(entry.getKey() + ":" + entry.getValue());
//		}
		if(entries.size() == 0) {
			for(int i = 0; i < page_remain.size() && i < 3; i ++) {
				advise += page_remain.get(i) + ";";
			}
		} else {
			for(int i = 0; i < entries.size() && i < 3; i ++) {
				advise += entries.get(i).getKey() + ";";
			}
		}
		return advise;
	}
	
}

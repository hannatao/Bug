package edu.nju.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.nju.entities.BugMirror;
import edu.nju.service.HistoryService;
import edu.nju.service.RecommendService;
import edu.nju.service.UserBasedService;

@Controller
@RequestMapping(value = "/rec")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RecommendController {
	
	@Autowired
	RecommendService recservice;
	
	@Autowired
	HistoryService historyservice;
	
	@Autowired
	UserBasedService ubservice;
	
	/**
	 * 每次刷新或进入填写页面，都应该调用一次该方法，因为其中包含了一些初始化操作
	 * @param case_take_id
	 * @return 该题目下排完序的BugMirror类的列表
	 */
	@RequestMapping(value = "/getList")
	@ResponseBody
	public void getList(String case_take_id, String report_id, HttpSession session, HttpServletResponse response) {
		try {
			if(session.getAttribute("rec") != null) {
				session.removeAttribute("rec");
			}
			if(session.getAttribute("page") != null) {
				session.removeAttribute("page");
			}
			if(session.getAttribute("path") != null) {
				session.removeAttribute("path");
			}
			if(session.getAttribute("title") != null) {
				session.removeAttribute("title");
			}
			if(session.getAttribute("des") != null) {
				session.removeAttribute("des");
			}
			session.setAttribute("case", case_take_id);
			session.setAttribute("report", report_id);
			PrintWriter out = response.getWriter();
			List<BugMirror> mirrors = recservice.getList(case_take_id, report_id);
			filter(historyservice.getNew(case_take_id, report_id), mirrors);
			out.print(new JSONArray(mirrors));
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 用户点击查看一个Bug的详细信息
	 * @param id
	 * @return Bug类
	 */
	@RequestMapping(value = "/getDetail")
	@ResponseBody
	public void getDetail(String id, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			result.put("detail", new JSONObject(recservice.getDetail(id)));
			result.put("history", new JSONObject(historyservice.getHistory(id)));
			result.put("mirror", new JSONObject(recservice.getMirror(id)));
			PrintWriter out = response.getWriter();
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 用户点击六个类别之后，都使用该接口
	 * @param type("category", "severity", "recurrent", "page1", "page2", "page3"), content
	 * @return List<BugMirror>
	 */
	@RequestMapping(value = "/recommend")
	@ResponseBody
	public void recommend(String case_take_id, String type, String content, HttpSession session, HttpServletResponse response) {
		try {
			JSONObject result = new JSONObject();
			PrintWriter out = response.getWriter();
			List<BugMirror> mirror1 = new ArrayList<BugMirror>();
			List<Float> scores = new ArrayList<Float>();
			Set<BugMirror> mirror2 = new HashSet<BugMirror>();
			Map<BugMirror, Float> map;
			if(type.equals("title") || type.equals("description")) {
				if(type.equals("title")) {map = recservice.recommandByTitle(content, session);}
				else {map = recservice.recommandByDes(content, session);}
				if(mirror1 != null) {
					mirror1.addAll(map.keySet());
					scores.addAll(map.values());
				}
			} else {
				if(type.contains("page")) {map = recservice.recommndByPage(case_take_id, type, content, true, session);}
				else {map = recservice.recommend(case_take_id, type, content, true, session);}
				List<String> reports = new ArrayList<String>();
				reports.add((String)session.getAttribute("report"));
				if(mirror1 != null) {
					mirror1.addAll(map.keySet());
					scores.addAll(map.values());
				}
//				for(int i = 1; i <= mirror1.size(); i ++) {
//					String temp = recservice.getReport(mirror1.get(i - 1).getId());
//					if(!reports.contains(temp)) {reports.add(temp);}
//				}
//				mirror2.addAll(ubservice.UserBased(reports.toArray(new String[reports.size()])));
			}
			mirror2.addAll(historyservice.getNew(case_take_id, (String)session.getAttribute("report")));
			filter(mirror2, mirror1);
			result.put("same", new JSONArray(mirror1));
			result.put("scores", new JSONArray(scores));
			result.put("new", new JSONArray(mirror2));
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//获取标题
	@RequestMapping(value = "/title")
	@ResponseBody
	public void getTitle(String id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.print(recservice.getTitle(id));
			out.flush();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//fork时，传回所有的选择参数
	@RequestMapping(value = "/fork")
	@ResponseBody
	public void fork(String page1, String page2, String page3, String bug_category, String severity, String recurrent, HttpSession session, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			JSONObject result = new JSONObject();
			Map<String, String> map = new LinkedHashMap<String, String>();
			if(!page1.equals("null")) {map.put("page1", page1);}
			if(!page2.equals("null")) {map.put("page2", page2);}
			if(!page3.equals("null")) {map.put("page3", page3);}
			map.put("bug_category", bug_category);
			map.put("severity", severity);
			map.put("recurrent", recurrent);
			session.setAttribute("path", map);
			result.put("status", 200);
			out.print(result);
			out.flush();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void filter(List<BugMirror> a, List<BugMirror> b) {
		if(a == null) {return;}
		for(BugMirror a1: a) {
			boolean flag = true;
			for(BugMirror b1 : b) {
				if(a1.getId().equals(b1.getId())) {flag = false;}
			}
			if(flag) {b.add(a1);}
		}
	}
	
	public static void filter(Set<BugMirror> a, List<BugMirror> b) {
		if(a == null || b == null) {return;}
		for(BugMirror mirror : b) {
			if(a.contains(mirror)) {a.remove(mirror);}
		}
	}
}

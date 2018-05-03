package edu.nju.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
			session.setAttribute("case", case_take_id);
			session.setAttribute("report", report_id);
			PrintWriter out = response.getWriter();
			List<BugMirror> mirrors = recservice.getList(case_take_id);
			filter(historyservice.getNew(), mirrors, case_take_id);
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
			PrintWriter out = response.getWriter();
			List<BugMirror> mirrors = new ArrayList<BugMirror>();
			if(type.equals("title")) {
				mirrors.addAll(recservice.recommandByTitle(content, session));
			} else {
				if(type.contains("page")) {
					mirrors.addAll(recservice.recommndByPage(case_take_id, type, content, session));
				} else {
					mirrors.addAll(recservice.recommend(case_take_id, type, content, session));
				}
				List<String> reports = new ArrayList<String>();
				reports.add((String)session.getAttribute("report"));
				for(int i = 1; i <= mirrors.size(); i ++) {
					String temp = recservice.getReport(mirrors.get(i - 1).getId());
					if(!reports.contains(temp)) {reports.add(temp);}
				}
				filter(ubservice.UserBased(reports.toArray(new String[reports.size()])), mirrors, case_take_id);
				System.out.println(mirrors.size());
				filter(historyservice.getNew(), mirrors, case_take_id);
				System.out.println(mirrors.size());
			}
			out.print(new JSONArray(mirrors));
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
	
	public static void filter(List<BugMirror> a, List<BugMirror> b, String case_take_id) {
		if(a == null) {return;}
		for(BugMirror a1: a) {
			boolean flag = true;
			for(BugMirror b1 : b) {
				if(a1.getId().equals(b1.getId())) {flag = false;}
				if(!a1.getCase_take_id().equals(case_take_id)) {flag = false;}
			}
			if(flag) {b.add(a1);}
		}
	}
}

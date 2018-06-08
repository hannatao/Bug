package edu.nju.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
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

import edu.nju.service.AnalyzeService;
import edu.nju.service.HistoryService;

@Controller
@RequestMapping(value = "/history")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HistoryController {
	
	@Autowired
	HistoryService hisservice;
	
	@Autowired
	AnalyzeService aservice;
	
	//获取指定节点的历史信息
	@RequestMapping(value = "/getHistory")
	@ResponseBody
	public void getHistory(String id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.print(new JSONObject(hisservice.getHistory(id)));
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//获取所有根节点
	@RequestMapping(value = "/getRoots")
	@ResponseBody
	public void getRoots(String case_take_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			JSONObject result = new JSONObject();
			List<String> list = hisservice.getRoots(case_take_id);
			result.put("Count", list.size());
			result.put("TreeRoot", new JSONArray(list));
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//获取所有形成树状结构的bug根节点
	@RequestMapping(value = "/getTrees")
	@ResponseBody
	public void getTrees(String case_take_id, String start, String count, HttpSession session, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			JSONObject result = new JSONObject();
			List<String> all = new ArrayList<String>();
			for(String id : hisservice.getRoots(case_take_id)) {
				if(hisservice.getHistory(id).getChildren().size() > 0) {all.add(id);}
			}
			
			List<String> ids = all.subList(Integer.parseInt(start), Math.max(all.size(), Integer.parseInt(start) + Integer.parseInt(count)));
			List<List<String>> list = new ArrayList<List<String>>();
			for(String id: ids) {
				list.add(hisservice.getDetail(id));
			}
			result.put("Count", list.size());
			result.put("TreeRoot", new JSONArray(list));
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//获取所有单个节点的数据
	@RequestMapping(value = "/getSingle")
	@ResponseBody
	public void getSingle(String case_take_id, String start, String count, HttpSession session, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			JSONObject result = new JSONObject();
			List<String> all = new ArrayList<String>();
			for(String id : hisservice.getRoots(case_take_id)) {
				if(hisservice.getHistory(id).getChildren().size() == 0) {all.add(id);}
			}
			
			List<String> ids = all.subList(Integer.parseInt(start), Math.max(all.size(), Integer.parseInt(start) + Integer.parseInt(count)));
			List<String> invalid = hisservice.getInvalid(ids);
			for(String id: invalid) {
				if(ids.contains(id)) {ids.remove(id);}
			}
			
			List<List<String>> list = new ArrayList<List<String>>();
			for(String id : ids) {
				List<String> temp = new ArrayList<String>();
				temp.add(id);
				int score = aservice.getGrade(id);
				if(score != -1) {temp.add("true");}
				else {temp.add("false");}
				list.add(temp);
			}
			result.put("Count", list.size());
			result.put("TreeRoot", new JSONArray(list));
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//获取指定bug数的所有路径
	@RequestMapping(value = "/getPath")
	@ResponseBody
	public void getPath(String id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			JSONObject result = new JSONObject();
			List<List<String>> lists = hisservice.getDepth(id);
			Set<String> filter = hisservice.filter(lists);
			result.put("path", new JSONArray(lists));
			result.put("invalid", new JSONArray(hisservice.getInvalid(filter)));
			List<String> ids = new ArrayList<String>(filter);
			result.put("score", new JSONArray(aservice.getScores(ids)));
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/fresh")
	@ResponseBody
	public void fresh(HttpSession session, HttpServletResponse response) {
		if(session.getAttribute("trees") != null) {session.removeAttribute("trees");}
		if(session.getAttribute("single") != null) {session.removeAttribute("single");}
	}
}

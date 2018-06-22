package edu.nju.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.nju.service.AnalyzeService;
import edu.nju.service.ReportService;

@Controller
@RequestMapping(value = "/analyze")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AnalyzeController {
	
	@Autowired
	AnalyzeService aservice;
	
	@Autowired
	ReportService rservice;
	
	//根据用例获取所有有效bug
	@RequestMapping(value = "/valid")
	@ResponseBody
	public void getValid(String case_take_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			JSONObject result = new JSONObject();
			List<String> list = aservice.getValid(case_take_id);
			result.put("Count", list.size());
			result.put("Detail", new JSONArray(list));
			out.print(result);
			out.flush();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//获取所有有点赞记录的bug
	@RequestMapping(value = "/thums")
	@ResponseBody
	public void getThums(String case_take_id, HttpServletResponse response) {
		try {
			JSONObject result = new JSONObject();
			PrintWriter out = response.getWriter();
			Map<String, String> map = aservice.getThums(case_take_id);
			result.put("Count", map.size());
			result.put("Detail", new JSONObject(map));
			out.print(result);
			out.flush();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//算出每个人的分数
	@RequestMapping(value = "/scores")
	@ResponseBody
	public void getScores(String case_take_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.print(new JSONObject(aservice.getScores(case_take_id)));
			out.flush();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//获取所有的参与用户
	@RequestMapping(value = "/users")
	@ResponseBody
	public void getUsers(String case_take_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.print(new JSONArray(aservice.getReports(case_take_id)));
			out.flush();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//获取指定bug的打分等级
	@RequestMapping(value = "/grade")
	@ResponseBody
	public void getGrade(String id, HttpServletResponse response) {
		try {
			JSONObject result = new JSONObject();
			result.put("grade", aservice.getGrade(id));
			PrintWriter out = response.getWriter();
			out.print(result);
			out.flush();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//存储单个bug的打分等级
	@RequestMapping(value = "/save")
	@ResponseBody
	public void saveGrade(String id, String grade, HttpServletResponse response) {
		try {
			JSONObject result = new JSONObject();
			if(aservice.saveGrade(id, Integer.parseInt(grade))) {result.put("status", "200");}
			else {result.put("status", "500");}
			PrintWriter out = response.getWriter();
			out.print(result);
			out.flush();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//获取页面和种类的分布情况
	@RequestMapping(value = "/bugDetail")
	@ResponseBody
	public void getDetail(String case_take_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.print(new JSONObject(aservice.getBugDetail(case_take_id)));
			out.flush();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//获取所有打分等级
	@RequestMapping(value = "/allGrades")
	@ResponseBody
	public void getAllGrades(String case_take_id, HttpServletResponse response) {
		try {
			JSONObject result = new JSONObject();
			Map<String, Integer> map = aservice.getAllGrades(case_take_id);
			result.put("Count", map.size());
			result.put("Detail", new JSONObject(map));
			PrintWriter out = response.getWriter();
			out.print(result);
			out.flush();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//判断哪些还没有打分，split后面是用例表中不存在的bug
	@RequestMapping(value = "/diff")
	@ResponseBody
	public void getDiff(String case_take_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			JSONObject result = new JSONObject();
			List<String> list = aservice.getDiff(case_take_id);
			result.put("Count", list.size());
			result.put("Detail", new JSONArray(list));
			out.print(result);
			out.flush();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//获取用户的有效信息
	@RequestMapping(value = "/report")
	@ResponseBody
	public void getReport(String report_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			JSONObject result = new JSONObject();
			result.put("Valid Thumsup", rservice.getValidThums(report_id) + "/" + rservice.getAllThums(report_id).size());
			result.put("Valid Diss", rservice.getValidDiss(report_id) + "/" + rservice.getAllDiss(report_id).size());
			result.put("Path Cover", rservice.getUserPath(report_id));
			result.put("Valid Bug", rservice.getValid(report_id) + "/" + rservice.getUserBugs(report_id).size());
			result.put("Time Gap", rservice.getTimeGap(report_id));
			out.print(result);
			out.flush();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//获取路径信息
	@RequestMapping(value = "/path")
	@ResponseBody
	public void getUserPath(String case_take_id, String report_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			JSONObject result = new JSONObject();
			result.put("all", filter(aservice.getBugDetail(case_take_id)));
			result.put("self", filter(rservice.getUserPath(report_id)));
			out.print(result);
			out.flush();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Map<String, Integer> filter(Map<String, Integer> maps) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		for(Map.Entry<String, Integer> entry : maps.entrySet()) {
			String[] pages = entry.getKey().split("-");
			if(pages.length > 0) {
				String key = pages[pages.length - 1];
				result.put(key, result.getOrDefault(key, 0) + entry.getValue());
			}
		}
		return result;
	}
}

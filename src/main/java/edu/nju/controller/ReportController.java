package edu.nju.controller;

import java.io.PrintWriter;
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
import edu.nju.service.DotService;
import edu.nju.service.GuideService;
import edu.nju.service.ReportService;

@Controller
@RequestMapping(value = "/report")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReportController {
	
	@Autowired
	ReportService rservice;
	
	@Autowired
	GuideService gservice;
	
	@Autowired
	DotService dservice;
	
	//获取用户的有效信息
	@RequestMapping(value = "/report")
	@ResponseBody
	public void getReport(String report_id, String case_take_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			JSONObject result = new JSONObject();
			result.put("Valid Thumsup", rservice.getValidThums(report_id) + "/" + rservice.getAllThums(report_id).size());
			result.put("Valid Diss", rservice.getValidDiss(report_id) + "/" + rservice.getAllDiss(report_id).size());
			result.put("Path Cover", rservice.getUserPath(report_id, case_take_id));
			result.put("Valid Bug", rservice.getValid(report_id, case_take_id) + "/" + rservice.getUserBugs(report_id, case_take_id).size());
			result.put("Time Gap", rservice.getTimeGap(report_id, case_take_id));
			out.print(result);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/ThumsRank")
	@ResponseBody
	public void ThumsRank(String case_take_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			response.setContentType("text/html; charset=utf-8");
			out.print(rservice.getThumsRank(case_take_id));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/ForkRank")
	@ResponseBody
	public void ForkRank(String case_take_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.print(rservice.getForkRank(case_take_id));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/relations")
	@ResponseBody
	public void relations(String case_take_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.print(rservice.relations(case_take_id));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/keyWords")
	@ResponseBody
	public void keyWords(String id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.print(rservice.keyWords(id));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/charm")
	@ResponseBody
	public void charm(String case_take_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.print(rservice.charm(case_take_id));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//我是分隔符////////////
	@RequestMapping(value = "/record")
	@ResponseBody
	public void userRecord(String user_id, String target_id, String action, String remarks, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.print(dservice.saveRecord(user_id, target_id, action, remarks));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/pageRec")
	@ResponseBody
	public void pageRec(String case_take_id, String report_id, HttpSession session, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			String result = gservice.workPlan(report_id, case_take_id, session);
			JSONObject object = new JSONObject();
			String[] temp1 = result.split(":");
			object.put("info", result);
			if(temp1.length == 1) { object.put("page", ""); }
			else {
				String[] pages = temp1[1].split(";");
				object.put("page", pages);
			}
			out.print(object);
			dservice.saveRecord(report_id, case_take_id, "pageRec", result);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/bugRec")
	@ResponseBody
	public void bugRec(String case_take_id, String report_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			List<BugMirror> result = gservice.PMFRecomend(report_id, case_take_id);
			out.print(new JSONArray(result));
			String remarks = "";
			for(BugMirror mirror: result) {
				remarks += mirror.getId() + ",";
			}
			dservice.saveRecord(report_id, case_take_id, "bugRec", remarks);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

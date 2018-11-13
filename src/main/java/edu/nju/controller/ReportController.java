package edu.nju.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.nju.service.ReportService;

@Controller
@RequestMapping(value = "/report")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReportController {
	
	@Autowired
	ReportService rservice;
	
	
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
	
	@RequestMapping(value = "/ThumsRank")
	@ResponseBody
	public void ThumsRank(String case_take_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.print(rservice.getThumsRank(case_take_id));
			out.flush();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

package edu.nju.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.nju.service.AnalyzeService;

@Controller
@RequestMapping(value = "/analyze")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AnalyzeController {
	
	@Autowired
	AnalyzeService aservice;
	
	@RequestMapping(value = "/valid")
	@ResponseBody
	public void getValid(String case_take_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.print(new JSONArray(aservice.getValid(case_take_id)));
			out.flush();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
}

package edu.nju.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.nju.service.SaveService;

@Controller
@RequestMapping(value = "/upload")
public class UploadController {
	
	@Autowired
	SaveService saveservice;
	
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public void submit(String case_take_id, String bug_category, String description, String img_url, String severity, String recurrent, String title, String report_id, String parent, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		if(saveservice.save(case_take_id, bug_category, description, img_url, Integer.parseInt(severity), Integer.parseInt(recurrent), title, report_id, parent)) {
			result.put("status", "200");
		} else {
			result.put("status", "500");
		}
		try {
			PrintWriter out = response.getWriter();
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/good")
	public void good(String id, String report_id, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		if(saveservice.confirm(id, report_id)) {
			result.put("status", "200");
		} else {
			result.put("status", "500");
		}
		try {
			PrintWriter out = response.getWriter();
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/bad")
	public void bad(String id, String report_id, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		if(saveservice.diss(id, report_id)) {
			result.put("status", "200");
		} else {
			result.put("status", "500");
		}
		try {
			PrintWriter out = response.getWriter();
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

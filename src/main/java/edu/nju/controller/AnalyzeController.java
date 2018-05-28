package edu.nju.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
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
}

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

import edu.nju.service.CTBService;

@Controller
@RequestMapping(value = "/relation")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RelationController {
	
	@Autowired
	CTBService ctbservice;
	
	//获取用例下的所有bug
	@RequestMapping(value = "/CTB")
	@ResponseBody
	public void getCTB(String useCase, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.print(new JSONArray(ctbservice.findById(useCase)));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//删除单条bug
	@RequestMapping(value = "/remove")
	@ResponseBody
	public void removeCTB(String useCase, String bug_id, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		if(ctbservice.remove(useCase, bug_id)) {result.put("status", "200");}
		else {result.put("status", "500");}
		try {
			PrintWriter out = response.getWriter();
			out.print(result);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//删除一个用例下的所有bug
	@RequestMapping(value = "/removeAll")
	@ResponseBody
	public void removeCase(String useCase, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		if(ctbservice.removeAll(useCase)) {result.put("status", "200");}
		else {result.put("status", "500");}
		try {
			PrintWriter out = response.getWriter();
			out.print(result);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

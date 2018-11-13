package edu.nju.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.nju.service.DeleteService;

@Controller
@RequestMapping(value = "/delete")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DeleteController {
	
	@Autowired
	DeleteService deleteservice;
	
	//删除指定的case
	@RequestMapping(value = "/case")
	@ResponseBody
	public void deleteCase(String case_take_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.print(deleteservice.deleteCase(case_take_id));
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//数据库删除单条bug
	@RequestMapping(value = "/one")
	@ResponseBody
	public void deleteOne(String id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.print(deleteservice.deleteOne(id));
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//指定bug无效
	@RequestMapping(value = "/bug")
	@ResponseBody
	public void deleteBug(String id, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		if(deleteservice.deleteBug(id)) {result.put("status", "200");}
		else {result.put("status", "500");}
		try {
			PrintWriter out = response.getWriter();
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

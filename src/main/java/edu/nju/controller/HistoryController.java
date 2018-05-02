package edu.nju.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.nju.service.HistoryService;

@Controller
@RequestMapping(value = "/history")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HistoryController {
	
	@Autowired
	HistoryService hisservice;
	
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
	
	@RequestMapping(value = "/getPath")
	@ResponseBody
	public void getPath(String id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.print(new JSONArray(hisservice.getDepth(id)));
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

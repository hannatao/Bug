package edu.nju.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.nju.service.HistoryService;

@Controller
@RequestMapping(value = "/history")
public class HistoryController {
	
	@Autowired
	HistoryService hisservice;
	
	//获取指定节点的历史信息
	@RequestMapping(value = "/getHistory")
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
	
}

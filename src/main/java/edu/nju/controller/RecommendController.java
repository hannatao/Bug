package edu.nju.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.nju.service.HistoryService;
import edu.nju.service.RecommendService;

@Controller
@RequestMapping(value = "/rec")
public class RecommendController {
	
	@Autowired
	RecommendService recservice;
	
	@Autowired
	HistoryService historyservice;
	
	@RequestMapping(value = "/getList")
	public void getList(String case_take_id, HttpSession session, HttpServletResponse response) {
		try {
			if(session.getAttribute("rec") != null) {
				session.removeAttribute("rec");
			}
			session.setAttribute("case", case_take_id);
			PrintWriter out = response.getWriter();
			out.print(recservice.getList(case_take_id));
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/getDetail")
	public void getDetail(String id, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			result.put("detail", recservice.getDetail(id));
			result.put("history", historyservice.getHistory(id));
			PrintWriter out = response.getWriter();
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/recommend")
	public void recommend(String type, String content, HttpSession session, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.print(recservice.recommend(type, content, session));
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

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
	
	/**
	 * 每次刷新或进入填写页面，都应该调用一次该方法，因为其中包含了一些初始化操作
	 * @param case_take_id
	 * @return 该题目下排完序的BugMirror类的列表
	 */
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
	
	/**
	 * 用户点击查看一个Bug的详细信息
	 * @param id
	 * @return Bug类
	 */
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
	
	/**
	 * 用户点击三个类别之后，都使用该接口
	 * @param type("category", "severity", "recurrent"), content
	 * @return List<BugMirror>
	 */
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

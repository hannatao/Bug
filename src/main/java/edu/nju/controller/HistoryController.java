package edu.nju.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
	
	//获取指定bug数的所有路径
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
	
	//获取所有根节点
	@RequestMapping(value = "/getRoots")
	@ResponseBody
	public void getRoots(String case_take_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			JSONObject result = new JSONObject();
			List<String> list = hisservice.getRoots(case_take_id);
			result.put("Count", list.size());
			result.put("TreeRoot", new JSONArray(list));
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//获取所有形成树状结构的bug根节点
	@RequestMapping(value = "/getTrees")
	@ResponseBody
	public void getTrees(String case_take_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			JSONObject result = new JSONObject();
			List<String> list = new ArrayList<String>();
			for(String id : hisservice.getRoots(case_take_id)) {
				if(hisservice.getHistory(id).getChildren().size() > 0) {list.add(id);}
			}
			result.put("Count", list.size());
			result.put("TreeRoot", new JSONArray(list));
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/getSingle")
	@ResponseBody
	public void getSingle(String case_take_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			JSONObject result = new JSONObject();
			List<String> list = new ArrayList<String>();
			for(String id : hisservice.getRoots(case_take_id)) {
				if(hisservice.getHistory(id).getChildren().size() == 0) {list.add(id);}
			}
			result.put("Count", list.size());
			result.put("TreeRoot", new JSONArray(list));
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

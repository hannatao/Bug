package edu.nju.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.nju.entities.BugHistory;
import edu.nju.service.HistoryService;

@Controller
@RequestMapping(value = "/his")
public class HistoryController {
	
	@Autowired
	HistoryService hisservice;
	
	@RequestMapping(value = "/getList")
	public void getList(HttpSession session, HttpServletResponse response) {
		System.out.println(hisservice.insert(new BugHistory("1","1","1")));
	}
	
	@RequestMapping(value = "/getDetail")
	public void getDetail(HttpSession session, HttpServletResponse response) {
		
	}
	
}

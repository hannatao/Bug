package edu.nju.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.nju.service.RecommendService;

@Controller
@RequestMapping(value = "/rec")
public class RecommendController {
	
	@Autowired
	RecommendService recservice;
	
	@RequestMapping(value = "/getList")
	public void getList(HttpSession session, HttpServletResponse response) {
		
	}
	
	@RequestMapping(value = "/getDetail")
	public void getDetail(HttpSession session, HttpServletResponse response) {
		
	}
	
	@RequestMapping(value = "/submit")
	public void submit(HttpSession session, HttpServletResponse response) {
		
	}
	
}

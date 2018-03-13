package edu.nju.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

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
	public void getList(String case_take_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.print(recservice.recommend(case_take_id));
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/getDetail")
	public void getDetail(String id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.print(recservice.getDetail(id));
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

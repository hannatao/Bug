package edu.nju.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.nju.service.GraphService;

@Controller
@RequestMapping(value = "/graph")
@CrossOrigin(origins = "*", maxAge = 3600)
public class GraphController {

	@Autowired
	GraphService gservice;
	
	@RequestMapping(value = "/11")
	@ResponseBody
	public void get_case_bug_valid(String case_take_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.print(gservice.get_case_bug_valid(case_take_id));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/12")
	@ResponseBody
	public void get_thums_total_valid(String case_take_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.print(gservice.get_thums_total_valid(case_take_id));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/13")
	@ResponseBody
	public void get_valid_thums(String case_take_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.print(gservice.get_valid_thums(case_take_id));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/14")
	@ResponseBody
	public void get_valid_fork(String case_take_id, String time, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.print(gservice.get_valid_fork(case_take_id, time));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/21")
	@ResponseBody
	public void ThumsToScores(String case_take_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.print(gservice.ThumsToScores(case_take_id));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/22")
	@ResponseBody
	public void get_bug_fork(String case_take_id, String time, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.print(gservice.get_bug_fork(case_take_id, time));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/31")
	@ResponseBody
	public void get_rec_fork_valid_thums(String case_take_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.print(gservice.get_rec_fork_valid_thums(case_take_id));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package edu.nju.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.nju.entities.Bug;
import edu.nju.entities.Exam;
import edu.nju.entities.Report;
import edu.nju.entities.TestCase;
import edu.nju.service.ExtraService;

@Controller
@RequestMapping(value = "/extra")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ExtraController {
	@Autowired
	ExtraService extraService;
	
	//上传测试报告
	@RequestMapping(value = "/uploadReport", method = RequestMethod.POST)
	@ResponseBody
	public void uploadReport(String case_id, String task_id, String case_take_id, String woker_id, String device_model, 
			String device_brand, String device_os, String script_location, String report_location, 
			String log_location, HttpServletResponse response) {
		try {
			String id = extraService.saveReport(case_id, task_id, case_take_id, woker_id, device_model, device_brand, device_os, script_location, report_location, log_location);
			PrintWriter out = response.getWriter();
			JSONObject result = new JSONObject();
			result.put("id", id);
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//获取测试报告
	@RequestMapping(value = "/getReport")
	@ResponseBody
	public void getReport(String report_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			JSONObject result = new JSONObject();
			Report report = extraService.getReport(report_id);
			if(report != null) { 
				result.put("status", 200);
				result.put("result", new JSONObject(report));
			}
			else { result.put("status", 500); }
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//使用worker_id获取测试报告
	@RequestMapping(value = "/findByWorker")
	@ResponseBody
	public void findByWorker(String case_take_id, String worker_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			JSONObject result = new JSONObject();
			Report report = extraService.findByWorker(case_take_id, worker_id);
			if(report != null) { 
				result.put("status", 200);
				result.put("result", new JSONObject(report));
			}
			else { result.put("status", 500); }
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//获取测试用例列表
	@RequestMapping(value = "/getCaseList")
	@ResponseBody
	public void getCaseList(String report_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			JSONObject result = new JSONObject();
			List<TestCase> caseList = extraService.getCaseList(report_id);
			if(caseList != null) { 
				result.put("status", 200);
				result.put("result", new JSONArray(caseList));
			}
			else { result.put("status", 500); }
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//获取测试Bug列表
	@RequestMapping(value = "/getBugList")
	@ResponseBody
	public void getBugList(String report_id, String case_take_id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			JSONObject result = new JSONObject();
			List<Bug> bugList = extraService.getBugList(report_id, case_take_id);
			if(bugList != null && bugList.size() > 0) { 
				result.put("status", 200);
				result.put("result", new JSONArray(bugList));
			}
			else { result.put("status", 500); }
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//上传题目信息
	@RequestMapping(value = "/uploadExam", method = RequestMethod.POST)
	@ResponseBody
	public void uploadExam(String path, String name, HttpServletResponse response) {
		try {
			String id = extraService.saveExam(path, name);
			PrintWriter out = response.getWriter();
			JSONObject result = new JSONObject();
			result.put("id", id);
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//获取题目信息
	@RequestMapping(value = "/getExam")
	@ResponseBody
	public void getExam(String id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			JSONObject result = new JSONObject();
			Exam exam = extraService.getExam(id);
			if(exam != null) { 
				result.put("status", 200);
				result.put("result", new JSONObject(exam));
			}
			else { result.put("status", 500); }
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//上传测试用例
	@RequestMapping(value = "/uploadTestCase", method = RequestMethod.POST)
	@ResponseBody
	public void uploadTestCase(String report_id, String name, String front, String behind, 
			String description, HttpServletResponse response) {
		try {
			String id = extraService.saveTestCase(report_id, name, front, behind, description);
			PrintWriter out = response.getWriter();
			JSONObject result = new JSONObject();
			result.put("id", id);
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//获取测试用例
	@RequestMapping(value = "/getTestCase")
	@ResponseBody
	public void getTestCase(String id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			JSONObject result = new JSONObject();
			TestCase testCase = extraService.getTestCase(id);
			if(testCase != null) { 
				result.put("status", 200);
				result.put("result", new JSONObject(testCase));
			}
			else { result.put("status", 500); }
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//更新测试用例
	@RequestMapping(value = "/updateTestCase", method = RequestMethod.POST)
	@ResponseBody
	public void updateTestCase(String id, String report_id, String name, String front, String behind, 
			String description, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			JSONObject result = new JSONObject();
			if(extraService.updateTestCase(id, report_id, name, front, behind, description)) {
				result.put("status", 200);
			} else { result.put("status", 500); }
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//获取页面json
	@RequestMapping(value = "/getJson")
	@ResponseBody
	public void getJson(String id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			JSONObject result = new JSONObject();
			Exam exam = extraService.getExam(id);
			if(exam != null) { 
				result.put("status", 200);
				result.put("result", exam.getJson());
			}
			else { result.put("status", 500); }
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

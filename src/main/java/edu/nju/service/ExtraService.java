package edu.nju.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nju.dao.ReportDao;
import edu.nju.dao.BugDao;
import edu.nju.dao.ExamDao;
import edu.nju.dao.TestCaseDao;
import edu.nju.entities.Bug;
import edu.nju.entities.Exam;
import edu.nju.entities.Report;
import edu.nju.entities.TestCase;
import edu.nju.util.ExcelToJson;

@Service
public class ExtraService {

	@Autowired
	ReportDao reportDao;
	
	@Autowired
	ExamDao examDao;
	
	@Autowired
	TestCaseDao testDao;
	
	@Autowired
	BugDao bugDao;
	
	//测试用例相关
	public String saveTestCase(String report_id, String name, String front, String behind, String description) {
		TestCase testCase = new TestCase(name, front, behind, description, report_id, Long.toString(System.currentTimeMillis()));
		return testDao.save(testCase);
	}
	
	public boolean updateTestCase(String id, String report_id, String name, String front, String behind, String description) {
		try {
			testDao.updateTestCase(id, report_id, name, front, behind, description);
			return true;
		} catch (Exception e) { return false; }
	}
	
	public List<TestCase> getCaseList(String report_id) {
		return testDao.findByReport(report_id);
	}
	
	public TestCase getTestCase(String id) {
		return testDao.findById(id);
	}
	
	
	//测试报告相关
	public String saveReport(String case_id, String task_id, String case_take_id, String woker_id, String name, String device_model, 
			String device_brand, String device_os, String script_location, String report_location, String log_location) {
		Report report = new Report(case_id, task_id, case_take_id, woker_id, name, Long.toString(System.currentTimeMillis()),
				device_model, device_brand, device_os, script_location, report_location, log_location);
		return reportDao.save(report);
	}
	
	public Report getReport(String report_id) {
		return reportDao.findById(report_id);
	}
	
	public Report findByWorker(String case_take_id, String worker_id) {
		return reportDao.findByWoker(case_take_id, worker_id);
	}
	
	public List<Bug> getBugList(String report_id, String case_take_id) {
		return bugDao.findByReport(report_id, case_take_id);
	}
	
	
	//测试题目相关
	public String saveExam(String path, String name) {
		String json = ExcelToJson.ExcelTranse(path).toString();
		Exam exam = new Exam(json, name);
		return examDao.save(exam);
	}
	
	public Exam getExam(String id) {
		return examDao.findById(id);
	}
}

package edu.nju.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nju.dao.BugDao;
import edu.nju.dao.BugMirrorDao;
import edu.nju.entities.Bug;
import edu.nju.entities.BugMirror;
import edu.nju.util.Algorithm;
import edu.nju.util.Algorithm_1;

@Service
public class RecommendService {
	
	@Autowired
	BugMirrorDao mirrordao;
	
	@Autowired
	BugDao bugdao;
	
	public List<BugMirror> getList (String case_take_id){
		Algorithm algorithm = new Algorithm_1();
		List<BugMirror> results = mirrordao.findByCase(case_take_id);
		return algorithm.sort(results);
	}
	
	public Bug getDetail (String id) {
		return bugdao.findByid(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<BugMirror> recommend (String type, String content, HttpSession session){
		Algorithm algorithm = new Algorithm_1();
		List<BugMirror> results = new ArrayList<BugMirror>();
		
		if(session.getAttribute("rec") == null) {
			results = findByNothing((String)session.getAttribute("case"), type, content);
		} else {
			results = findByNow(type, content, (List<BugMirror>) session.getAttribute("rec"));
		}
		
		session.setAttribute("rec", results);
		if(results != null) {return algorithm.sort(results);}
		return results;
	}
	
	private List<BugMirror> findByNow(String type, String content, List<BugMirror> lists){
		List<BugMirror> results = new ArrayList<BugMirror>();
		switch(type) {
			case "category":
				for(BugMirror mirror : lists) {
					if(mirror.getBug_category() == content) {results.add(mirror);}
				}
				break;
			case "severity":
				for(BugMirror mirror : lists) {
					if(mirror.getSeverity() == Integer.parseInt(content)) {results.add(mirror);}
				}
				break;
			case "recurrent":
				for(BugMirror mirror : lists) {
					if(mirror.getRecurrent() == Integer.parseInt(content)) {results.add(mirror);}
				}
				break;
		}
		return results;
	}
	
	private List<BugMirror> findByNothing(String case_take_id, String type, String content){
		switch(type) {
			case "category":
				return mirrordao.findByCategory(case_take_id, content);
			case "severity":
				return mirrordao.findBySeverity(case_take_id, Integer.parseInt(content));
			case "recurrent":
				return mirrordao.findByRecurrent(case_take_id, Integer.parseInt(content));
		}
		return null;
	}
	
	
}

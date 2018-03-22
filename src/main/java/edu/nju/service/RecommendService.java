package edu.nju.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nju.dao.BugDao;
import edu.nju.dao.BugMirrorDao;
import edu.nju.dao.BugPageDao;
import edu.nju.entities.Bug;
import edu.nju.entities.BugMirror;
import edu.nju.entities.BugPage;
import edu.nju.util.Algorithm;
import edu.nju.util.Algorithm_1;

@Service
public class RecommendService {
	
	@Autowired
	BugMirrorDao mirrordao;
	
	@Autowired
	BugDao bugdao;
	
	@Autowired
	BugPageDao pagedao;
	
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
	
	@SuppressWarnings("unchecked")
	public List<BugMirror> recommndByPage(String type, String content, HttpSession session){
		
		Algorithm algorithm = new Algorithm_1();
		List<BugPage> results = new ArrayList<BugPage>();
		List<BugMirror> mirrors = new ArrayList<BugMirror>();
		
		if(session.getAttribute("page") == null) {
			results = findPages((String)session.getAttribute("case"), type, content);
			mirrors = findMirror(getIds(results));
		} else {
			results = findByPages(type, content, (List<BugPage>) session.getAttribute("page"));
			mirrors = findByMirror(getIds(results), (List<BugMirror>) session.getAttribute("rec"));
		}
		
		session.setAttribute("page", results);
		session.setAttribute("rec", mirrors);
		
		if(mirrors != null) {return algorithm.sort(mirrors);}
		return mirrors;
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
	
	private List<BugPage> findPages(String case_take_id, String type, String content){
		switch(type) {
			case "page1":
				return pagedao.findByPage1(case_take_id, content);
			case "page2":
				return pagedao.findByPage2(case_take_id, content);
			case "page3":
				return pagedao.findByPage3(case_take_id, content);
		}
		return null;
	}
	
	private List<BugPage> findByPages(String type, String content, List<BugPage> lists){
		List<BugPage> results = new ArrayList<BugPage>();
		switch(type) {
			case "category":
				for(BugPage page : lists) {
					if(page.getPage1() == content) {results.add(page);}
				}
				break;
			case "severity":
				for(BugPage page : lists) {
					if(page.getPage2() == content) {results.add(page);}
				}
				break;
			case "recurrent":
				for(BugPage page : lists) {
					if(page.getPage3() == content) {results.add(page);}
				}
				break;
		}
		return results;
	}
	
	private List<BugMirror> findMirror(List<String> ids){
		List<BugMirror> results = new ArrayList<BugMirror>();
		results = mirrordao.findByIds(ids);
		return results;
	}
	
	private List<BugMirror> findByMirror(List<String> ids, List<BugMirror> lists){
		List<BugMirror> results = new ArrayList<BugMirror>();
		for(BugMirror mirror : lists) {
			if(ids.contains(mirror.getId())) {results.add(mirror);}
		}
		return results;
	}
	
	private List<String> getIds(List<BugPage> pages) {
		List<String> ids = new ArrayList<String>();
		for(int i = 0; i < pages.size(); i ++) {
			ids.add(pages.get(i).getId());
		}
		return ids;
	}
}

package edu.nju.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nju.dao.BugDao;
import edu.nju.dao.BugMirrorDao;
import edu.nju.dao.BugPageDao;
import edu.nju.dao.KWDao;
import edu.nju.entities.Bug;
import edu.nju.entities.BugMirror;
import edu.nju.entities.BugPage;
import edu.nju.util.Algorithm;
import edu.nju.util.Algorithm_1;
import edu.nju.util.StringMatch;

@Service
public class RecommendService {
	
	@Autowired
	BugMirrorDao mirrordao;
	
	@Autowired
	BugDao bugdao;
	
	@Autowired
	BugPageDao pagedao;
	
	@Autowired
	KWDao kwdao;
	
	public List<BugMirror> getList (String case_take_id, String report_id) {
		Algorithm algorithm = new Algorithm_1();
		List<BugMirror> results = mirrordao.findByCase(case_take_id, report_id);
		return algorithm.sort(results);
	}
	
	public List<String> getListIds (String case_take_id) {
		List<BugMirror> results = mirrordao.findByCase(case_take_id);
		List<String> lists = new ArrayList<String>();
		for(BugMirror mirror: results) {
			lists.add(mirror.getId());
		}
		return lists;
	}
	
	public Bug getDetail (String id) {
		return bugdao.findByid(id);
	}
	
	public BugMirror getMirror (String id) {
		return mirrordao.findById(id);
	}
	
	public String getTitle (String id) {
		return bugdao.findByid(id).getTitle();
	}
	
	public String getReport(String id) {
		return bugdao.findByid(id).getReport_id();
	}
	
	//根据bug的本质三属性进行推荐
	@SuppressWarnings("unchecked")
	public Map<BugMirror, Float> recommend (String case_take_id, String type, String content, boolean flag, HttpSession session) {
		
		List<BugMirror> results = new ArrayList<BugMirror>();
		
		if(session.getAttribute("rec") == null) {
			results = findByNothing(case_take_id, type, content, (String)session.getAttribute("report"));
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put(type, content);
			session.setAttribute("path", map);
		} else {
			Map<String, String> map = (Map<String, String>)session.getAttribute("path");
			if(map.containsKey(type)) {
				return pathExist(case_take_id, type, content, session, map);
			}
			map.put(type, content);
			session.setAttribute("path", map);
			results = findByNow(type, content, (List<BugMirror>) session.getAttribute("rec"));
		}
		
		session.setAttribute("rec", results);
		if(flag) {
			Algorithm algorithm = new Algorithm_1();
			if(results != null) {results = algorithm.sort(results);}
			if(session.getAttribute("title") != null) {return recommandByTitle((String)session.getAttribute("title"), session);}
			if(session.getAttribute("des") != null) {return recommandByDes((String)session.getAttribute("des"), session);}
		}
		
		Map<String, String> map = (Map<String, String>)session.getAttribute("path");
		return finalScore(results, map.size());
	}
	
	//根据页面进行推荐
	@SuppressWarnings("unchecked")
	public Map<BugMirror, Float> recommndByPage(String case_take_id, String type, String content, boolean flag, HttpSession session){
		
		List<BugPage> results = new ArrayList<BugPage>();
		List<BugMirror> mirrors = new ArrayList<BugMirror>();
		
		if(session.getAttribute("page") == null) {
			results = findPages(case_take_id, type, content);
			mirrors = findMirror(getIds(results), (String)session.getAttribute("report"));
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put(type, content);
			session.setAttribute("path", map);
		} else {
			Map<String, String> map = (Map<String, String>)session.getAttribute("path");
			if(map.containsKey(type)) {
				return pathExist(case_take_id, type, content, session, map);
			} else {
				map.put(type, content);
				session.setAttribute("path", map);
				results = findByPages(type, content, (List<BugPage>) session.getAttribute("page"));
				mirrors = findByMirror(getIds(results), (List<BugMirror>) session.getAttribute("rec"));
			}
		}
		
		session.setAttribute("page", results);
		session.setAttribute("rec", mirrors);
		
		if(flag) {
			Algorithm algorithm = new Algorithm_1();
			if(mirrors != null) {mirrors = algorithm.sort(mirrors);}
			if(session.getAttribute("title") != null) {return recommandByTitle((String)session.getAttribute("title"), session);}
			if(session.getAttribute("des") != null) {return recommandByDes((String)session.getAttribute("des"), session);}
		}
		
		Map<String, String> map = (Map<String, String>)session.getAttribute("path");
		return finalScore(mirrors, map.size());
	}
	
	//根据Title进行文本匹配
	@SuppressWarnings("unchecked")
	public Map<BugMirror, Float> recommandByTitle(String content, HttpSession session){
		session.setAttribute("title", content);
		if(session.getAttribute("rec") == null) {
			List<BugMirror> temp = mirrordao.findByCase((String)session.getAttribute("case"), (String)session.getAttribute("report"));
			if(temp != null) {session.setAttribute("rec", temp);}
		}
		if(session.getAttribute("rec") != null) {return count(content, (List<BugMirror>)session.getAttribute("rec"), true, session);}
		return null;
	}
	
	//根据description进行文本匹配
	@SuppressWarnings("unchecked")
	public Map<BugMirror, Float> recommandByDes(String content, HttpSession session){
		session.setAttribute("des", content);
		if(session.getAttribute("rec") == null) {
			List<BugMirror> temp = mirrordao.findByCase((String)session.getAttribute("case"), (String)session.getAttribute("report"));
			if(temp != null) {session.setAttribute("rec", temp);}
		}
		if(session.getAttribute("rec") != null) {return count(content, (List<BugMirror>)session.getAttribute("rec"), false, session);}
		return null;
	}
	
	//从缓存中获取
	private List<BugMirror> findByNow(String type, String content, List<BugMirror> lists){
		List<BugMirror> results = new ArrayList<BugMirror>();
		switch(type) {
			case "bug_category":
				for(BugMirror mirror : lists) {
					if(mirror.getBug_category().equals(content)) {results.add(mirror);}
				}
				break;
			case "severity":
				for(BugMirror mirror : lists) {
					if(mirror.getSeverity() == severityTranse(content)) {results.add(mirror);}
				}
				break;
			case "recurrent":
				for(BugMirror mirror : lists) {
					if(mirror.getRecurrent() == recurrentTranse(content)) {results.add(mirror);}
				}
				break;
		}
		return results;
	}

	//从数据库中获取
	private List<BugMirror> findByNothing(String case_take_id, String type, String content, String report_id){
		switch(type) {
			case "bug_category":
				return mirrordao.findByCategory(case_take_id, content, report_id);
			case "severity":
				return mirrordao.findBySeverity(case_take_id, severityTranse(content), report_id);
			case "recurrent":
				return mirrordao.findByRecurrent(case_take_id, recurrentTranse(content), report_id);
		}
		return null;
	}
	
	//从数据库中查找页面
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
	
	//从缓存中查找页面
	private List<BugPage> findByPages(String type, String content, List<BugPage> lists){
		List<BugPage> results = new ArrayList<BugPage>();
		switch(type) {
			case "page1":
				for(BugPage page : lists) {
					if(page.getPage1().equals(content)) {results.add(page);}
				}
				break;
			case "page2":
				for(BugPage page : lists) {
					if(page.getPage2().equals(content)) {results.add(page);}
				}
				break;
			case "page3":
				for(BugPage page : lists) {
					if(page.getPage3().equals(content)) {results.add(page);}
				}
				break;
		}
		return results;
	}
	
	//从数据库中获取指定id的bug
	private List<BugMirror> findMirror(List<String> ids, String report_id){
		List<BugMirror> results = new ArrayList<BugMirror>();
		results = mirrordao.findByIds(ids, report_id);
		return results;
	}
	
	//从缓存中进行筛选符合条件的
	private List<BugMirror> findByMirror(List<String> ids, List<BugMirror> lists){
		List<BugMirror> results = new ArrayList<BugMirror>();
		for(BugMirror mirror : lists) {
			if(ids.contains(mirror.getId())) {results.add(mirror);}
		}
		return results;
	}
	
	//根据页面获取bug_id
	private List<String> getIds(List<BugPage> pages) {
		List<String> ids = new ArrayList<String>();
		for(int i = 0; i < pages.size(); i ++) {
			ids.add(pages.get(i).getId());
		}
		return ids;
	}
	
	//该选项已经选择过
	private Map<BugMirror, Float> pathExist(String case_take_id, String type, String content, HttpSession session, Map<String, String> map){
		session.removeAttribute("rec");
		session.removeAttribute("path");
		session.removeAttribute("page");
		Map<BugMirror, Float> results = new LinkedHashMap<BugMirror, Float>();
		for(Entry<String, String> entry: map.entrySet()) {
			if(entry.getKey().equals(type)) {break;}
			if(!entry.getKey().contains("page")) {
				results = recommend(case_take_id, entry.getKey(), entry.getValue(), false, session);
			} else {
				results = recommndByPage(case_take_id, entry.getKey(), entry.getValue(), false, session);
			}
		}
		if(!type.contains("page")) {results = recommend(case_take_id, type, content, true, session);}
		else {results = recommndByPage(case_take_id, type, content, true, session);}
		return results;
	}
	
	@SuppressWarnings("unchecked")
	private Map<BugMirror, Float> count(String content, List<BugMirror> lists, boolean type, HttpSession session) {
		StringMatch match = new StringMatch();
		Map<BugMirror, Float> tmap = new HashMap<BugMirror, Float>();
		Map<BugMirror, Float> result = new LinkedHashMap<BugMirror, Float>();
		Map<String, BugMirror> bmap = new HashMap<String, BugMirror>();
		Map<String, String> titleMap = new HashMap<String, String>();
		Map<String, String> desMap = new HashMap<String, String>();
		int size = 0;
		
		for(BugMirror mirror: lists) {
			String id = mirror.getId();
			bmap.put(id, mirror);
			titleMap.put(id, kwdao.findById(id).getTitle());
			desMap.put(id, kwdao.findById(id).getDescription());
		}
		for(BugMirror mirror: lists) {
			String id = mirror.getId();
			float score = 0;
			if(type) {
				score += match.score(match.Ansj(content), match.Ansj(titleMap.get(id))) * 30;
				if(session.getAttribute("des") != null) {
					score += match.score(match.Ansj((String)session.getAttribute("des")), match.Ansj(desMap.get(id))) * 40;
				}
			} else {
				score += match.score(match.Ansj(content), match.Ansj(desMap.get(id))) * 40;
				if(session.getAttribute("title") != null) {
					score += match.score(match.Ansj((String)session.getAttribute("title")), match.Ansj(titleMap.get(id))) * 30;
				}
			}
			tmap.put(mirror, score);
		}
		List<Entry<BugMirror, Float>> tlist = new ArrayList<Entry<BugMirror, Float>>();
		tlist.addAll(tmap.entrySet());
		Collections.sort(tlist, (a, b) -> (Float.compare(b.getValue(), a.getValue())));
		if(session.getAttribute("path") != null) {
			Map<String, String> pmap = (Map<String, String>)session.getAttribute("path");
			size = pmap.size();
		}
		
		for(int i = 0; i < tlist.size() && i < 6; i ++) {
			float score = (float) (tlist.get(i).getValue());
			if(score <= 1) {continue;}
			score += size * 5;
			result.put(tlist.get(i).getKey(), score);
		}
		return result;
	}
	
	private Map<BugMirror, Float> finalScore(List<BugMirror> lists, int size) {
		Map<BugMirror, Float> rmap = new LinkedHashMap<BugMirror, Float>();
		for(BugMirror mirror : lists) {
			rmap.put(mirror, (float) (size * 5));
		}
		return rmap;
	}
	
	private int severityTranse(String str) {
		if(str.equals("待定")) {
			return 1;
		}
		if(str.equals("较轻")) {
			return 2;
		}
		if(str.equals("一般")) {
			return 3;
		}
		if(str.equals("严重")) {
			return 4;
		}
		if(str.equals("紧急")) {
			return 5;
		}
		return 0;
	}
	
	private int recurrentTranse(String str) {
		if(str.equals("其他")) {
			return 1;
		}
		if(str.equals("无规律复现")) {
			return 2;
		}
		if(str.equals("小概率复现")) {
			return 3;
		}
		if(str.equals("大概率复现")) {
			return 4;
		}
		if(str.equals("必现")) {
			return 5;
		}
		return 0;
	}
	
}
package edu.nju.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nju.dao.BugHistoryDao;
import edu.nju.dao.BugMirrorDao;
import edu.nju.dao.BugPageDao;
import edu.nju.entities.BugHistory;
import edu.nju.entities.BugMirror;
import edu.nju.entities.BugPage;

@Service
public class HistoryService {
	
	@Autowired
	BugHistoryDao historydao;
	
	@Autowired
	BugMirrorDao mirrordao;
	
	@Autowired
	BugPageDao pagedao;
	
	@Autowired
	RecommendService recservice;
	
	@Autowired
	AnalyzeService aservice;
	
	public BugHistory getHistory(String id) {
		return historydao.findByid(id);
	}
	
	public List<String> parents(String id) {
		Stack<String> stack = new Stack<String>();
		List<String> result = new ArrayList<String>();
		String parent = getHistory(id).getParent();
		while(!parent.equals("null")) {
			stack.push(parent);
			parent = getHistory(parent).getParent();
		}
		while(!stack.isEmpty()) {
			result.add(stack.pop());
		}
		result.add(id);
		return result;
	}
	
	public List<BugMirror> getNew(String case_take_id, String report_id) {
		List<String> ids = historydao.findRoots(recservice.getListIds(case_take_id));
		List<String> filter = new ArrayList<String>();
		if(ids.size() > 2) {
			int n = ids.size();
			filter.add(ids.get(n - 1));
			filter.add(ids.get(n - 2));
		} else {
			for(String id : ids) {
				filter.add(id);
			}
		}
		return mirrordao.findByIds(filter, report_id);
	}
	
	public List<String> getRoots(String case_take_id) {
		return historydao.findRoots(aservice.getValid(case_take_id));
	}
	
	public List<String> getTreeRoots(String case_take_id) {
		List<String> all = new ArrayList<String>();
		for(String id : getRoots(case_take_id)) {
			if(getHistory(id).getChildren().size() > 0) { all.add(id); }
		}
		return all;
	}
	
	public List<String> getInvalid(Set<String> ids) {
		List<String> result = new ArrayList<String>();
		for(String id: ids) {
			if(!mirrordao.findById(id).isFlag()) {result.add(id);}
		}
		return result;
	}
	
	public List<String> getInvalid(List<String> ids) {
		List<String> result = new ArrayList<String>();
		for(String id: ids) {
			if(!mirrordao.findById(id).isFlag()) {result.add(id);}
		}
		return result;
	}
	
	public Set<String> filter(List<List<String>> lists) {
		Set<String> set = new HashSet<String>();
		for(List<String> list : lists) {
			for(String id: list) {
				set.add(id);
			}
		}
		return set;
	}
	
	//获取评分时树的信息
	public List<String> getDetail(String id) {
		List<String> result = new ArrayList<String>();
		List<List<String>> paths = getDepth(id);
		List<Set<String>> widths = new ArrayList<Set<String>>();
		Set<String> ids = new HashSet<String>();
		int max_height = 0;
		int max_width = 0;
		int count = 0;
		String flag = "true";
		for(List<String> path: paths) {
			max_height = Math.max(max_height, path.size());
			for(int i = 0; i < path.size(); i ++) {
				String temp = path.get(i);
				ids.add(temp);
				if(widths.size() <= i) {
					Set<String> set = new HashSet<String>();
					set.add(temp);
					widths.add(set);
				}
				else {widths.get(i).add(temp);}
			}
		}
		for(Set<String> width: widths) {
			max_width = Math.max(max_width, width.size());
		}
		for(String str: ids) {
			if(aservice.getGrade(str) == -1) {
				flag = "false";
				break;
			}
		}
		count = ids.size();
		result.add(id);
		result.add(Integer.toString(max_width));
		result.add(Integer.toString(max_height));
		result.add(Integer.toString(count));
		result.add(flag);
		result.add(recservice.getTitle(id));
		return result;
	}
	
	public void pageFilter(List<String> all, String page) {
		if(page.equals("null")) { return; }
		List<BugPage> mirrors = pagedao.findByIds(all);
		String[] pages = page.split("-");
		for(BugPage mirror : mirrors) {
			if(pages.length > 0 && !mirror.getPage1().equals(pages[0])) {
				all.remove(mirror.getId());
				continue;
			}
			if(pages.length > 1 && !mirror.getPage2().equals(pages[1])) {
				all.remove(mirror.getId());
				continue;
			}
			if(pages.length > 2 && !mirror.getPage3().equals(pages[2])) {
				all.remove(mirror.getId());
				continue;
			}
		}
	}
	
	public List<List<String>> getDepth(String id) {
		BugHistory root = historydao.findByid(id);
		List<List<String>> result = new ArrayList<List<String>>();
		List<String> list = new ArrayList<String>();
		list.add(root.getId());
		dfs(root, result, list);
		return result;
	}
	
	private void dfs(BugHistory root, List<List<String>> result, List<String> list) {
		List<String> children = root.getChildren();
		if(children.size() != 0) {
			for(String child : children) {
				list.add(child);
				dfs(historydao.findByid(child), result, list);
				list.remove(child);
			}
		} else {
			result.add(new ArrayList<String>(list));
		}
	}
}
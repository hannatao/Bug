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
import edu.nju.entities.BugHistory;
import edu.nju.entities.BugMirror;

@Service
public class HistoryService {
	
	@Autowired
	BugHistoryDao historydao;
	
	@Autowired
	BugMirrorDao mirrordao;
	
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
	
	public List<String> getInvalid(Set<String> ids) {
		List<String> result = new ArrayList<String>();
		for(String id: ids) {
			if(!mirrordao.findById(id).isFlag()) {result.add(id);}
		}
		return result;
	}
	
	public List<String> filter(List<List<String>> lists) {
		Set<String> set = new HashSet<String>();
		for(List<String> list : lists) {
			for(String id: list) {
				set.add(id);
			}
		}
		return getInvalid(set);
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

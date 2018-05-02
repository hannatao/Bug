package edu.nju.service;

import java.util.ArrayList;
import java.util.List;
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
	
	public BugHistory getHistory(String id) {
		return historydao.findByid(id);
	}
	
	public List<String> parents(String id) {
		Stack<String> stack = new Stack<String>();
		List<String> result = new ArrayList<String>();
		String parent = getHistory(id).getParent();
		while(parent.equals("null")) {
			stack.push(parent);
			parent = getHistory(parent).getParent();
		}
		while(!stack.isEmpty()) {
			result.add(stack.pop());
		}
		return result;
	}
	
	public List<BugMirror> getNew() {
		List<String> ids = historydao.findRoots();
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
		return mirrordao.findByIds(filter);
	}
}

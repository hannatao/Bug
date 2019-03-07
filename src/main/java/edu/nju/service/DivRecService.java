package edu.nju.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
//import java.util.HashMap;
//import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nju.dao.BugDao;
import edu.nju.dao.BugHistoryDao;
import edu.nju.dao.BugMirrorDao;
import edu.nju.dao.BugPageDao;
import edu.nju.dao.ThumsUpDao;
import edu.nju.entities.Bug;
import edu.nju.entities.BugHistory;
import edu.nju.entities.BugMirror;
import edu.nju.entities.ThumsUp;
import edu.nju.util.PMF;

@Service
public class DivRecService {
	
	@Autowired
	BugPageDao pagedao;
	
	@Autowired
	BugMirrorDao mirrordao;
	
	@Autowired
	BugDao bugdao;
	
	@Autowired
	ThumsUpDao tdao;
	
	@Autowired
	BugHistoryDao hdao;
	
	public List<BugMirror> diverseRec(String report_id, String case_take_id) {
		List<BugMirror> result = new ArrayList<BugMirror>();
		List<Bug> all = bugdao.findByCaseid(case_take_id);
		Set<String> my = new HashSet<String>();
		List<String> allIds = bugdao.findByCase(case_take_id);
		
		int M = all.size(); //产品的数目
		int K = 3; //特征的数目
		List<String> users = new ArrayList<String>();
		for(Bug bug: all) {
			String user = bug.getReport_id();
			if(users.contains(user)) { continue; }
			users.add(user);
		}
		int N = users.size(); //用户的数目
		double[][] R = new double[N][M];
        double[][] P = new double[N][K];
        double[][] Q = new double[K][M];
		for(int i = 0; i < N; i ++) {
//			List<Map<String, Integer>> user_detail = personDetail(all, user);
			String user = users.get(i);
			List<List<String>> relations = getAllRelation(case_take_id, user);
			for(int j = 0; j < 4; j ++) {
				for(String id : relations.get(j)) {
					my.add(id);
					int index = allIds.indexOf(id);
					if(index < 0) { continue; }
					switch (j) {
						case 0: //自己提交
							R[i][index] = 10;
							break;
						case 1: //点过赞
							R[i][index] = 8;
							break;
						case 2: //点过踩
							R[i][index] = 1;
							break;
						case 3: //fork过
							R[i][index] = 5;
							break;
					}
				}
			}
		}
//		System.out.println("R矩阵");
//        for(int i = 0; i < N; ++i) { 
//        	for(int j = 0; j < M; ++j){ 
//        		System.out.print((int)R[i][j] + ",");
//        	}
//        	System.out.println();
//        }
		for(int i = 0; i < N; ++i) 
        {
            for(int j = 0; j < K; ++j)
            {
                P[i][j] = Math.random() % 9;
            }
        }
        for(int i = 0; i < K; ++i) 
        {
            for(int j = 0;j < M; ++j)
            {
                Q[i][j] = Math.random()%9;
            }
        }
        PMF.matrix_factorization(R, P, Q, N, M, K);
        double[][] fin_array = new double[N][M];
        for(int i = 0; i < N; ++i) { 
        	for(int j = 0; j < M; ++j) { 
        		double temp = 0; 
        		for (int k = 0; k < K; ++k){
        			temp += P[i][k] * Q[k][j]; 
        		}
        		fin_array[i][j] = temp;
//        		System.out.print(df.format(temp)+","); 
        	}
        }
        int this_user = users.indexOf(report_id);
        double[] line = fin_array[this_user];
        for(int i = 0; i < M; i ++) {
        	if(line[i] >= 5 && !my.contains(allIds.get(i))) {
        		String id = allIds.get(i);
        		result.add(mirrordao.findById(id));
        	}
        }
		return result;
	}
	
	public List<List<String>> getAllRelation(String case_take_id, String report_id) {
		List<List<String>> result = new ArrayList<List<String>>();
		result.add(new ArrayList<String>());  //获取所有自己提交
		result.add(new ArrayList<String>());  //获取所有点赞记录
		result.add(new ArrayList<String>());  //获取所有点踩记录
		result.add(new ArrayList<String>());  //获取所有父记录
		ThumsUp thum = tdao.findByReport(report_id);
		List<String> ids = mirrordao.findIdsByReport(report_id, case_take_id);
		result.get(0).addAll(ids);
		if(thum != null) {
			result.get(1).addAll(thum.getThums());
			result.get(2).addAll(thum.getDiss());
		}
		List<String> parents = result.get(3);
		for(String id : ids) {
			BugHistory his = hdao.findByid(id);
			if(his == null) { continue; }
			String parent = his.getParent();
			if(parent.equals("null") || parents.contains(parent)) { continue; }
			parents.add(parent);
		}
		return result;
	}
	
	public List<BugMirror> randomRec(String report_id, String case_take_id) {
		List<BugMirror> result = new ArrayList<BugMirror>();
		Set<String> my = new HashSet<String>();
		List<String> allIds = bugdao.findByCase(case_take_id);
		Map<String, Integer> map = new HashMap<String, Integer>();
		for(List<String> list: getAllRelation(case_take_id, report_id)) {
			for(String id: list) {
				my.add(id);
			}
		}
		for(String id: allIds) {
			if(my.contains(id)) { continue; }
			BugMirror mirror = mirrordao.findById(id);
			map.put(id, mirror.getGood().size() + mirror.getBad().size());
		}
		List<Map.Entry<String, Integer>> entries = new ArrayList<>(map.entrySet());
		Collections.sort(entries, (a, b) -> (a.getValue() - b.getValue()));
		for(int i = 0; i < entries.size() && i < 8; i ++) {
			result.add(mirrordao.findById(entries.get(i).getKey()));
		}
		return result;
	}
//	private List<Map<String, Integer>> personDetail(List<Bug> all, String report_id) {
//		List<Map<String, Integer>> result = new ArrayList<Map<String, Integer>>();
//		result.add(new HashMap<String, Integer>());
//		result.add(new HashMap<String, Integer>());
//		for(Bug bug : all) {
//			if(bug.getReport_id().equals("report_id")) {
//				Map<String, Integer> pages = result.get(0);
//				Map<String, Integer> categories = result.get(1);
//				String page = bug.getBug_page();
//				String category = bug.getBug_category();
//				pages.put(page, pages.getOrDefault(page, 0) + 1);
//				categories.put(category, categories.getOrDefault(category, 0) + 1);
//			}
//		}
//		return result;
//	}
	
}

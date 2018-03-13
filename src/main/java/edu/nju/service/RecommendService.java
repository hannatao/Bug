package edu.nju.service;

import java.util.List;

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
	
	public List<BugMirror> recommend (String case_take_id){
		Algorithm algorithm = new Algorithm_1();
		return algorithm.sort(mirrordao.findByCase(case_take_id));
	}
	
	public Bug getDetail (String id) {
		return (Bug) bugdao.findByid(id);
	}
}

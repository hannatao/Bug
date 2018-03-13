package edu.nju.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nju.dao.BugMirrorDao;

@Service
public class RecommendService {
	
	@Autowired
	BugMirrorDao mirror;
}

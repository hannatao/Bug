package edu.nju.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.nju.entities.BugMirror;

public class Algorithm_1 implements Algorithm{
	
	public List<BugMirror> sort(BugMirror sample, List<BugMirror> lists){
		if(lists == null || lists.size() == 0) {return lists;}
		List<BugMirror> results = new ArrayList<BugMirror>();
		
		lists.sort(new Comparator<BugMirror>() {
			public int compare(BugMirror m1, BugMirror m2) {
				return m2.getGood().size() - m1.getGood().size();
			}
		});
		for(int i = 0; i < lists.size() && i < 4; i ++) {
			results.add(lists.get(i));
		}
		return results;
	}
	
	public List<BugMirror> sort(List<BugMirror> lists){
		if(lists == null || lists.size() == 0) {return lists;}
		List<BugMirror> results = new ArrayList<BugMirror>();
		
		lists.sort(new Comparator<BugMirror>() {
			public int compare(BugMirror m1, BugMirror m2) {
				return m1.getGood().size() - m2.getGood().size();
			}
		});
		for(int i = 0; i < lists.size() && i < 4; i ++) {
			results.add(lists.get(i));
		}
		return results;
	}
}

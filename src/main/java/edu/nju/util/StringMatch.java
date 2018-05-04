package edu.nju.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import edu.nju.entities.BugMirror;

public class StringMatch {
	public List<BugMirror> match(String content, List<BugMirror> mirrors) {
		String[] strs = Ansj(content).split(",");
		List<BugMirror> result = new ArrayList<BugMirror>();
		Map<BugMirror, Integer> map = new HashMap<BugMirror, Integer>();
		for(BugMirror mirror : mirrors) {
			int sum = 0;
			for(int i = 0; i < strs.length && i < 10; i ++) {
				if(mirror.getTitle().contains(strs[i])) {
					sum ++;
				}
			}
			if(sum > 0) {
				map.put(mirror, sum);
			}
		}
		List<Entry<BugMirror,Integer>> list = new ArrayList<Entry<BugMirror,Integer>>();
		list.addAll(map.entrySet());
		Collections.sort(list, (a, b) -> (b.getValue() - a.getValue()));
		if(list.size() > 0) {
			for(int i = 0; i < list.size() && i < 8; i ++) {
				result.add(list.get(i).getKey());
			}
		}
		return result;
	}
	
	public String Ansj(String str) {
		Set<String> expectedNature = new HashSet<String> ();
        expectedNature.add("n");
        expectedNature.add("v");
        expectedNature.add("f");
        expectedNature.add("en");
        StringBuilder result = new StringBuilder();
        List<Term> terms = ToAnalysis.parse(str).getTerms();
        for(Term term : terms) {
        	String word = term.getName();
        	String nature = term.getNatureStr();
        	if(expectedNature.contains(nature)) {
        		result.append(word+",");
        	}
        }
        return result.toString();
	}
}

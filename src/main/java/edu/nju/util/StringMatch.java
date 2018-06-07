package edu.nju.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
			for(int i = 0; i < list.size() && i < 6; i ++) {
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
	
	public float score(String str1, String str2) {
		int[][] vector = getStringFrequency(Arrays.asList(str1.split(",")), Arrays.asList(str2.split(",")));
		return getDoubleStrForCosValue(vector);
	}
	
	public int[][] getStringFrequency(List<String> str1List, List<String> str2List){
        Set<String> cnSet = new HashSet<String>();
        cnSet.addAll(str1List);
        cnSet.addAll(str2List);
        int[][] res = new int[2][cnSet.size()];
        Iterator<String> it = cnSet.iterator();
        int i = 0;

        while(it.hasNext()){
            String word = it.next().toString();
            int s1 = 0;
            int s2 = 0;

            for(String str : str1List){
                if(word.equals(str)){
                    s1++;
                }
            }

            res[0][i] = s1;
            for(String str : str2List){
                if(word.equals(str)){
                    s2 ++;
                }
            }

            res[1][i] = s2;
            i ++;
        }

        return res;
    }
	
	public float getDoubleStrForCosValue(int [][] ints){
        BigDecimal fzSum = new BigDecimal(0);
        BigDecimal fmSum = new BigDecimal(0);
        int num = ints[0].length;

        for(int i = 0; i < num; i ++){
            BigDecimal adb = new BigDecimal(ints[0][i]).multiply(new BigDecimal(ints[1][i]));
            fzSum = fzSum.add(adb);

        }

        BigDecimal seq1SumBigDecimal = new BigDecimal(0);
        BigDecimal seq2SumBigDecimal = new BigDecimal(0);

        for(int i = 0; i < num; i ++){
            seq1SumBigDecimal = seq1SumBigDecimal.add(new BigDecimal(Math.pow(ints[0][i],2)));
            seq2SumBigDecimal = seq2SumBigDecimal.add(new BigDecimal(Math.pow(ints[1][i],2)));
        }
        double sqrt1 = Math.sqrt(seq1SumBigDecimal.doubleValue());
        double sqrt2 = Math.sqrt(seq2SumBigDecimal.doubleValue());
        fmSum = new BigDecimal(sqrt1).multiply(new BigDecimal(sqrt2));
        
        return fzSum.divide(fmSum,10,RoundingMode.HALF_UP).floatValue();
    }

}
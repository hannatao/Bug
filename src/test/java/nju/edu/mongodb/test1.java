package nju.edu.mongodb;

import org.junit.Test;

import edu.nju.util.StringMatch;

public class test1 {
	
	@Test
	public void match(){
		StringMatch match = new StringMatch();
		String str1 = "专四语法词汇单项精讲";
		String str2 = "单项精讲专四语法";
		System.out.println(match.score(match.Ansj(str1), match.Ansj(str2)));
	}
}

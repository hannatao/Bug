package nju.edu.mongodb;

import org.junit.Test;

import edu.nju.util.StringMatch;

public class test1 {
	
	@Test
	public void match(){
		StringMatch match = new StringMatch();
		String[] str = match.Ansj("点击app按钮时退出").split(",");
		System.out.println(str.length);
	}
}

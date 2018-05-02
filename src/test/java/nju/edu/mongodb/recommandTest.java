package nju.edu.mongodb;

import java.util.List;

import org.junit.Test;

import edu.nju.util.UserCF;

public class recommandTest {
	
	@Test
	public void recommand() {
		UserCF cf = new UserCF();
		List<String> lists = cf.calculate(new String[][] {{"A","a","b","d"},{"B","a","c"},{"C","b","e"},{"D","c","d","e"}});
		for(String str: lists) {
			System.out.println(str);
		}
	}
}

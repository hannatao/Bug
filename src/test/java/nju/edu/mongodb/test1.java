package nju.edu.mongodb;

import org.json.JSONObject;
import org.junit.Test;

import edu.nju.service.HttpService;
//import edu.nju.util.StringMatch;

public class test1 {
	
	HttpService hservice = new HttpService();
	
	@Test
	public void match(){
//		StringMatch match = new StringMatch();
//		String str1 = "专四语法词汇单项精讲";
//		String str2 = "单项精讲专四语法";
//		System.out.println(match.score(match.Ansj(str1), match.Ansj(str2)));
		String url = "http://114.55.91.83:8191/api/user/34720";
		JSONObject json = new JSONObject(hservice.sendGet(url, ""));
		System.out.println(json.getString("name"));
	}
}

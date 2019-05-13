package nju.edu.mongodb;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import edu.nju.service.HttpService;

public class PutTest {

	HttpService httpservice = new HttpService();
	
	@Test
	public void put() {
		String host = "http://www.mooctest.net";
		String url = "/api/common/uploadCaseScore";
		String case_take_id = "1632-2927";
		String[] ids = case_take_id.split("-");
		
		JSONArray array = new JSONArray();
		JSONObject json_temp = new JSONObject();
		json_temp.put("report_id", "10010000035902");
		json_temp.put("worker_id", "49512");
		json_temp.put("名字", "祁博文");
		json_temp.put("报告得分", 8);
		json_temp.put("审查得分", 0);
		array.put(json_temp);
		
		String param1 = "caseId=" + ids[0] + "&examId=" + ids[1];
		for(int i = 0; i < array.length(); i ++) {
			JSONObject json = (JSONObject)array.get(i);
			String worker_id = json.get("worker_id").toString();
			int score = Integer.parseInt(json.get("报告得分").toString()) + Integer.parseInt(json.get("审查得分").toString());
			
			if(score <= 0 || worker_id.equals("")) { continue; }
			if(score > 100) { score = 100; }
			String param2 = "&userId=" + worker_id + "&score=" + score;
			System.out.println(host + url + "?" + param1 + param2);
			System.out.println(httpservice.sendPut(host, url, param1 + param2));
		}
	}
}
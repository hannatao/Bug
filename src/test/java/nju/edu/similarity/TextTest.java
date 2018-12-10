package nju.edu.similarity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import edu.nju.util.StringMatch;
public class TextTest {
	
	@Test
	public void match_2018() {
		try {
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader("./data/2016移动应用决赛.json"));
			StringBuilder sb = new StringBuilder();
			StringMatch match = new StringMatch();
			Map<String, Integer> category = new HashMap<String, Integer>();
			String s = null;
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			JSONObject data = new JSONObject(sb.toString());
			JSONArray array = data.getJSONArray("RECORDS");
			JSONArray case2 = new JSONArray();
			JSONArray case1 = new JSONArray();
			
			for(int i = 0; i < array.length(); i ++) {
				JSONObject temp = array.getJSONObject(i);
				if(temp.getString("case_id").equals("216")) { case1.put(temp); }
				else if(temp.getString("case_id").equals("217")) { case2.put(temp); }
			}
			
			JSONArray use = case2;
			String[] splits = new String[use.length()];
			System.out.println(use.length());
			
			for(int i = 0; i < use.length(); i ++) {
				JSONObject temp = use.getJSONObject(i);
				category.put(temp.getString("bug_category"), category.getOrDefault(temp.getString("bug_category"), 0) + 1);
				splits[i] = match.Ansj(temp.getString("description"));
//				String temp_str = temp.get("description").toString();
//				JSONObject temp2 = new JSONObject(temp_str);
//				splits[i] = match.Ansj(temp2.get("description").toString());
			}
			int total = 0;
			for(int i = 0; i < splits.length; i ++) {
				for(int j = i + 1; j < splits.length; j ++) {
					float result = match.score(splits[i], splits[j]);
					if(result > 0.85 && result != 1) {
//					if(result == 1) {
						JSONObject temp_i = use.getJSONObject(i);
						JSONObject temp_j = use.getJSONObject(j);
						System.out.println(result + "/" + temp_i.get("id") + "/" + temp_j.get("id"));
						System.out.println(temp_i.get("report_id") + "/" + temp_j.get("report_id"));
						System.out.println(temp_i.get("description"));
						System.out.println(temp_j.get("description"));
//						System.out.println(splits[i]);
//						System.out.println(splits[j]);
						total ++;
					}
				}
			}
			System.out.println(total);
			for(Map.Entry<String, Integer> entry: category.entrySet()) {
				System.out.println(entry.getKey() + ":" + entry.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

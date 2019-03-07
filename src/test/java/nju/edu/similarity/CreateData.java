package nju.edu.similarity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import edu.nju.util.StringMatch;

public class CreateData {

	@Test
	public void addData() {
		try {
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader("./data/2016移动应用决赛.json"));
			StringBuilder sb = new StringBuilder();
			StringMatch match = new StringMatch();
			String s = null;
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			JSONObject data = new JSONObject(sb.toString());
			JSONArray array = data.getJSONArray("RECORDS");
			
			File file = new File("./data/搜狗文本分类语料库已分词.txt");
			FileWriter fw = new FileWriter(file, true);
			for(int i = 0; i < array.length(); i ++) {
				JSONObject temp = array.getJSONObject(i);
				String ansj = match.Ansj(temp.getString("description"));
				fw.write(ansj.replaceAll(",", " ") + "\r\n");
			}
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

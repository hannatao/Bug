package nju.edu.mongodb;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;

import edu.nju.service.GuideService;
import edu.nju.service.HttpService;

public class HttpTest {
	
	GuideService gservice = new GuideService();
	HttpService httpservice = new HttpService();
	
//	@Test
//	public void match(){
//		StringMatch match = new StringMatch();
//		String str1 = "专四语法词汇单项精讲";
//		String str2 = "单项精讲专四语法";
//		System.out.println(match.score(match.Ansj(str1), match.Ansj(str2)));
//		String url = "http://114.55.91.83:8191/api/user/34720";
//		JSONObject json = new JSONObject(hservice.sendGet(url, ""));
//		System.out.println(json.getString("name"));
//	}
	
	@Test
	public void recommend() {
		String QS = "1489";
		String[] my = new String[] {};
//		String[] total = new String[] {"GnuCash", 
//				"账簿","管理账簿", 
//				"打开", 
//				"收藏", "编辑收藏", 
//				"报表", "饼图", "柱状图", "折线图","汇总表", "时间选择",
//				"定时任务", "交易", "定时备份", "添加定时任务",
//				"导出", "导出交易",
//				"设置", "通用",
//				"帮助与反馈", "帮助详细",
//				"其他路径",
//				"科目","最近的", "最近的1",
//				"所有", "收藏1", "创建科目", "其他路径1",
//				"搜索", "引导页"};
		String[] total = new String[] {"搜索", "推荐目的地", "更多"};
		String result = gservice.jsonGet(httpservice.sendGet("http://mooctest-site.oss-cn-shanghai.aliyuncs.com/json/" + QS + ".json" , ""), 
				new ArrayList<String>(Arrays.asList(my)), new ArrayList<String>(Arrays.asList(total)));
		String[] temp1 = result.split(":");
		System.out.println(temp1[0]);
		String[] pages = temp1[1].split(";");
		for(String page: pages) {
			System.out.println(page);
		}
	}
}

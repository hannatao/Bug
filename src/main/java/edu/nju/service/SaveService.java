package edu.nju.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nju.dao.BugDao;
import edu.nju.dao.BugHistoryDao;
import edu.nju.dao.BugMirrorDao;
import edu.nju.dao.BugPageDao;
import edu.nju.dao.KWDao;
import edu.nju.dao.StuInfoDao;
import edu.nju.dao.ThumsUpDao;
import edu.nju.entities.Bug;
import edu.nju.entities.BugHistory;
import edu.nju.entities.BugMirror;
import edu.nju.entities.BugPage;
import edu.nju.entities.KeyWords;
import edu.nju.entities.ThumsUp;
import edu.nju.util.StringMatch;

@Service
public class SaveService {
	
	@Autowired
	BugMirrorDao mirrordao;
	
	@Autowired
	BugDao bugdao;
	
	@Autowired
	BugHistoryDao historydao;
	
	@Autowired
	BugPageDao pagedao;
	
	@Autowired
	HistoryService hisservice;
	
	@Autowired
	DotService dotservice;
	
	@Autowired
	KWDao kwdao;
	
	@Autowired
	ThumsUpDao tdao;
	
	@Autowired
	StuInfoDao sdao;
	
	@Autowired
	HttpService hservice;
	
	@Autowired
	AnalyzeService aservice;
	
	public boolean save(String id, String case_take_id, String bug_category, String description, String img_url, String severity, String recurrent, String title, String report_id, String parent, String page, String useCase, String case_id) {
		try {
			StringMatch match = new StringMatch();
			bugdao.save(new Bug(id, case_take_id, Long.toString(System.currentTimeMillis()), bug_category, description, img_url, severityTranse(severity), recurrentTranse(recurrent), title, report_id, page, case_id));
			mirrordao.save(new BugMirror(id, case_take_id, bug_category, severityTranse(severity), recurrentTranse(recurrent), title, img_url, new HashSet<String>(), new HashSet<String>(), report_id, useCase, true));
			kwdao.save(new KeyWords(id, match.Ansj(title), match.Ansj(description)));
			if(!parent.equals("null")) {
				String[] sp = parent.split("-");
				dotservice.saveType1(id, sp[1]);
				historydao.addChild(sp[0], id);
				String p = hisservice.parents(sp[0]).get(0);
				historydao.save(new BugHistory(id, sp[0], new ArrayList<String>(), p));
			} else {
				historydao.save(new BugHistory(id, parent, new ArrayList<String>(), id));
			}
			
			if(!page.equals("")) {
				savePage(id, case_take_id, page);
			}
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
//	public boolean update(String id, String case_take_id, String bug_category, String description, String img_url, int severity, int recurrent, String title, String report_id, String parent,String page, String useCase, String case_id) {
//		try {
//			bugdao.save(new Bug(id, case_take_id, Long.toString(System.currentTimeMillis()), bug_category, description, img_url, severityTranse(severity), recurrentTranse(recurrent), title, report_id, page, case_id));
//			mirrordao.save(new BugMirror(id, case_take_id, bug_category, severityTranse(severity), recurrentTranse(recurrent), title, img_url, new HashSet<String>(), new HashSet<String>(), report_id, useCase, true));
//			if(!page.equals("")) {
//				savePage(id, case_take_id, page);
//			}
//			return true;
//		} catch(Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
	
	public boolean saveStu(String report_id, String worker_id) {
		try {
			String name = sdao.findById(report_id);
			if(name.equals("null") || name.equals("")) {
				String result = hservice.sendGet("http://114.55.91.83:8191/api/user/" + worker_id, "");
				if(result != null && !result.equals("")) { 
					JSONObject json = new JSONObject(result);
					sdao.save(report_id, worker_id, json.getString("name")); 
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean repair(String id, String page, String case_take_id, String bug_category, String description, String img_url, String severity, String recurrent, String report_id, String case_id) {
		try {
			if(id == null || id.equals("undefined")) { return false; }
			KeyWords keywords = kwdao.findById(id);
			String title = "";
			if(keywords != null) { title = keywords.getTitle().replaceAll(",", ""); }
			bugdao.save(new Bug(id, case_take_id, Long.toString(System.currentTimeMillis()), bug_category, description, img_url, severityTranse(severity), recurrentTranse(recurrent), title, report_id, page, case_id));
			mirrordao.save(new BugMirror(id, case_take_id, bug_category, severityTranse(severity), recurrentTranse(recurrent), title, img_url, new HashSet<String>(), new HashSet<String>(), report_id, "null", true)); //null为useCase
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean repairThums(String case_take_id) {
		try {
			List<Bug> bugs = bugdao.findByCaseid(case_take_id);
			Set<String> reports = new HashSet<String>();
			for(Bug bug: bugs) {
				String report_id = bug.getReport_id();
				if(!reports.contains(report_id)) {
					reports.add(report_id);
					ThumsUp thumsup = tdao.findByReport(report_id);
					if(thumsup == null) { continue; }
					Set<String> thums = thumsup.getThums();
					Set<String> diss = thumsup.getDiss();
					if(thums != null && !thums.isEmpty()) {
						for(String id : thums) {
							BugMirror mirror = mirrordao.findById(id);
							if(mirror == null) { continue; }
							mirror.getGood().add(report_id);
							mirrordao.save(mirror);
						}
					}
					if(diss != null && !diss.isEmpty()) {
						for(String id : diss) {
							BugMirror mirror = mirrordao.findById(id);
							if(mirror == null) { continue; }
							mirror.getBad().add(report_id);
							mirrordao.save(mirror);
						}
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean repairTime(String case_take_id) {
		try {
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			long start = dateformat.parse("2018-11-16 08:05:17").getTime();
//			long middle = dateformat.parse("2018-11-16 09:15:17").getTime();
//			long end = dateformat.parse("2018-11-16 11:55:17").getTime();
			long start = dateformat.parse("2018-11-16 17:05:17").getTime();
			long middle = dateformat.parse("2018-11-16 18:15:17").getTime();
			long end = dateformat.parse("2018-11-16 19:55:17").getTime();
			List<String> ids = aservice.getValid(case_take_id);
			Collections.sort(ids);
			int size = ids.size();
			int middle_size = (int)(size * 0.7);
			Long gap1 = (middle - start) / middle_size;
			Long gap2 = (end - middle) / (size - middle_size);
			for(int i = 0; i < middle_size; i ++) {
				long new_time = start + gap1 * i;
				Bug bug = bugdao.findByid(ids.get(i));
				bug.setCreate_time_millis(Long.toString(new_time));
				bugdao.save(bug);
			}
			for(int i = middle_size; i < size; i ++) {
				long new_time = middle + gap2 * (i - middle_size);
				Bug bug = bugdao.findByid(ids.get(i));
				bug.setCreate_time_millis(Long.toString(new_time));
				bugdao.save(bug);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean saveTitle(String id, String title) {
		try {
			Bug bug = bugdao.findByid(id);
			if(bug != null) { 
				bug.setTitle(title);
				bugdao.save(bug);
			}
			BugMirror mirror = mirrordao.findById(id);
			if(mirror != null) {
				mirror.setTitle(title);
				mirrordao.save(mirror);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean confirm(String id, String report_id) {
		try {
			if(mirrordao.haveJudged(id, report_id)) {
				mirrordao.good(id, report_id);
				tdao.saveGood(id, report_id);
				return true;
			}
			return false;
		} catch(Exception e) {
			return false;
		}
	}
	
	public boolean cancelGood(String id, String report_id) {
		try {
			mirrordao.cancelGood(id, report_id);
			tdao.cancelGood(id, report_id);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public boolean diss(String id, String report_id) {
		try {
			if(mirrordao.haveJudged(id, report_id)) {
				mirrordao.bad(id, report_id);
				tdao.saveDiss(id, report_id);
				return true;
			}
			return false;
		} catch(Exception e) {
			return false;
		}
	}
	
	public boolean cancelDiss(String id, String report_id) {
		try {
			mirrordao.canelBad(id, report_id);
			tdao.cancelDiss(id, report_id);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	private void savePage(String id, String case_take_id, String page) {
		String[] pages = page.split("-");
		int length = pages.length;
		String page2 = "";
		String page3 = "";
		if(length == 2) {
			page2 = pages[1];
		} else if(length == 3) {
			page2 = pages[1];
			page3 = pages[2];
		}
		BugPage save = new BugPage(id, pages[0], page2, page3, case_take_id);
		pagedao.save(save);
	}
	
	private int severityTranse(String str) {
		if(str.equals("待定")) {
			return 1;
		}
		if(str.equals("较轻")) {
			return 2;
		}
		if(str.equals("一般")) {
			return 3;
		}
		if(str.equals("严重")) {
			return 4;
		}
		if(str.equals("紧急")) {
			return 5;
		}
		return 0;
	}
	
	private int recurrentTranse(String str) {
		if(str.equals("其他")) {
			return 1;
		}
		if(str.equals("无规律复现")) {
			return 2;
		}
		if(str.equals("小概率复现")) {
			return 3;
		}
		if(str.equals("大概率复现")) {
			return 4;
		}
		if(str.equals("必现")) {
			return 5;
		}
		return 0;
	}
}

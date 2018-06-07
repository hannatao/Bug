package edu.nju.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nju.dao.BugDao;
import edu.nju.dao.BugHistoryDao;
import edu.nju.dao.BugMirrorDao;
import edu.nju.dao.BugPageDao;
import edu.nju.dao.BugScoreDao;
import edu.nju.dao.CTBDao;
import edu.nju.dao.KWDao;
import edu.nju.entities.BugHistory;
import edu.nju.entities.BugMirror;
import edu.nju.entities.CaseToBug;

@Service
public class DeleteService {
	@Autowired
	BugHistoryDao historydao;
	
	@Autowired
	BugMirrorDao mirrordao;
	
	@Autowired
	BugPageDao pagedao;
	
	@Autowired
	BugDao bugdao;
	
	@Autowired
	RecommendService recservice;
	
	@Autowired
	CTBDao ctbdao;
	
	@Autowired
	KWDao kwdao;
	
	@Autowired
	BugScoreDao bsdao;
	
	public boolean deleteOne(String id) {
		try {
			String useCase = mirrordao.findById(id).getUseCase();
			String parent = historydao.findByid(id).getParent();
			if(!parent.equals("null")) {
				BugHistory history = historydao.findByid(parent);
				history.getChildren().remove(id);
				historydao.save(history);
			}
			historydao.remove(id);
			mirrordao.remove(id);
			bugdao.remove(id);
			pagedao.remove(id);
			kwdao.remove(id);
			if(!useCase.equals("null")) {
				CaseToBug ctb = ctbdao.find(useCase);
				ctb.getBug_id().remove(id);
				ctbdao.save(ctb);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean deleteCase(String case_take_id) {
		try {
			List<String> ids = recservice.getListIds(case_take_id);
			bugdao.remove(ids);
			historydao.remove(ids);
			pagedao.remove(ids);
			mirrordao.remove(ids);
			ctbdao.removeAll(case_take_id);
			kwdao.remove(ids);
			bsdao.remove(ids);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean deleteBug(String id) {
		try {
			BugMirror mirror = mirrordao.findById(id);
			mirror.setFlag(false);
			if(!mirror.getUseCase().equals("null")) {
				CaseToBug ctb = ctbdao.find(mirror.getUseCase());
				ctb.getBug_id().remove(id);
				ctbdao.save(ctb);
			}
			mirrordao.save(mirror);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}

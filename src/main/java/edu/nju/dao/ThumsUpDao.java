package edu.nju.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import edu.nju.entities.ThumsUp;

@Repository
public class ThumsUpDao {

	@Autowired
	private MongoOperations mongoOperations;
	
	public ThumsUp findByReport(String report_id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(report_id));
		List<ThumsUp> list = mongoOperations.find(query, ThumsUp.class);
		if(list == null || list.size() == 0) {return null;}
		return list.get(0);
	}
	
	public void saveGood(String id, String report_id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(report_id));
		List<ThumsUp> list = mongoOperations.find(query, ThumsUp.class);
		if(list == null || list.size() == 0) {
			Set<String> good = new HashSet<String>();
			good.add(id);
			ThumsUp thumsup = new ThumsUp(report_id, good, new HashSet<String>());
			mongoOperations.save(thumsup);
		} else {
			Update update = new Update();
			Set<String> good = list.get(0).getThums();
			good.add(id);
			update.set("thums", good);
			mongoOperations.updateFirst(query, update, ThumsUp.class);
		}
	}
	
	public void saveDiss(String id, String report_id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(report_id));
		List<ThumsUp> list = mongoOperations.find(query, ThumsUp.class);
		if(list == null || list.size() == 0) {
			Set<String> bad = new HashSet<String>();
			bad.add(id);
			ThumsUp thumsup = new ThumsUp(report_id, new HashSet<String>(), bad);
			mongoOperations.save(thumsup);
		} else {
			Update update = new Update();
			Set<String> bad = list.get(0).getDiss();
			bad.add(id);
			update.set("diss", bad);
			mongoOperations.updateFirst(query, update, ThumsUp.class);
		}
	}
}

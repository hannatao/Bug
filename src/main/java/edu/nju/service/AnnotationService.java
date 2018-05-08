package edu.nju.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nju.dao.AnnotationDao;
import edu.nju.entities.ImageAnnotation;

@Service
public class AnnotationService {
	
	@Autowired
	AnnotationDao andao;
	
	public boolean save(String id, String width, String height, String[] xs, String[] ys) {
		try {
			andao.save(new ImageAnnotation(id, width, height, xs, ys));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean delete(String id) {
		try {
			andao.remove(id);
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
	
	public ImageAnnotation get(String id) {
		return andao.findById(id);
	}
}

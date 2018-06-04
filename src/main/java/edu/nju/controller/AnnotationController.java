package edu.nju.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.nju.entities.ImageAnnotation;
import edu.nju.service.AnnotationService;

@Controller
@RequestMapping(value = "/annotation")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AnnotationController {
	
	@Autowired
	AnnotationService anservice;
	
	//存储标注信息
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public void saveAnnotation(String id, String width, String height, @RequestParam("xs") String[] xs, @RequestParam("ys") String[] ys,HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			JSONObject result = new JSONObject();
			if(anservice.save(id, width, height, xs, ys)) {
				result.put("status", "200");
			} else {
				result.put("status", "500");
			}
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//获取标注信息
	@RequestMapping(value = "/get")
	@ResponseBody
	public void getAnnotation(String id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			ImageAnnotation result = anservice.get(id);
			if(result != null) {
				out.print(new JSONObject(result));
			} else {
				JSONObject temp = new JSONObject();
				temp.put("status", "500");
				out.print(temp);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//删除标注数据
	@RequestMapping(value = "/delete")
	@ResponseBody
	public void deleteAnnotation(String id, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			JSONObject result = new JSONObject();
			if(anservice.delete(id)) {
				result.put("status", "200");
			} else {
				result.put("status", "500");
			}
			out.print(result);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

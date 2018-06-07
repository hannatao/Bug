package edu.nju.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ImageAnnotation implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4960970930384537028L;

	@Id
    private String id;
	
	private String width;
	
	private String height;
	
	private String[] xs;
	
	private String[] ys;
	
	@PersistenceConstructor
	public ImageAnnotation(String id, String width, String height, String[] xs, String[] ys) {
		this.id = id;
		this.width = width;
		this.height = height;
		this.xs = xs;
		this.ys = ys;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String[] getXs() {
		return xs;
	}

	public void setXs(String[] xs) {
		this.xs = xs;
	}

	public String[] getYs() {
		return ys;
	}

	public void setYs(String[] ys) {
		this.ys = ys;
	}
}

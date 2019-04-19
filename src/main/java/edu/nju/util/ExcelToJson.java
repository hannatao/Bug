package edu.nju.util;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

public class ExcelToJson {
	
	@SuppressWarnings("resource")
	public static JSONArray ExcelTranse(String path) {
		JSONArray dataArray = new JSONArray();
		try {
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(path));
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
			int xssfLastRowNum = xssfSheet.getLastRowNum();
			JSONObject object = new JSONObject();
			JSONObject second = new JSONObject();
			for(int rowNum = 1; rowNum <= xssfLastRowNum; rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if(xssfRow == null) { continue; }
				
				XSSFCell cell = xssfRow.getCell(0);
				String cellValue0 = cell.getStringCellValue();
				if(cellValue0 != "") {
					if(object.length() != 0) {
						dataArray.put(object);
						object = new JSONObject();
					}
					object.put("item", cellValue0);
					object.put("children", new JSONArray());
				}
				
				cell = xssfRow.getCell(1);
				String cellValue1 = cell.getStringCellValue();
				if(cellValue1 != "") {
					if(second.length() != 0) {
						JSONArray array = (JSONArray)object.get("children");
						array.put(second);
						object.put("children", array);
						second = new JSONObject();
					}
					second.put("item", cellValue1);
					second.put("children", new JSONArray());
				}

				cell = xssfRow.getCell(2);
				JSONArray arr = (JSONArray)second.get("children");
				JSONObject third = new JSONObject();
				third.put("item", cell.getStringCellValue());
				arr.put(third);
				second.put("children", arr);
				
			}
			dataArray.put(object);
			
			return dataArray;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void writeToFile(String path, JSONArray dataArray) {
		try {
			FileWriter writer = new FileWriter(path.replaceAll("xlsx", "json"));
			PrintWriter out = new PrintWriter(writer);
	        out.write(dataArray.toString());
	        out.println();
	        writer.close();
	        out.close();
		} catch (Exception e) { e.printStackTrace(); }
	}
	
}

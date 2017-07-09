package com.hourse.web.util;


import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.sql.ResultSet;


public class ExcelUtil {
	ResultSet rs;
	public static void fillExcelData(ResultSet rs,Workbook wb,String[] headers) throws Exception{
		int rowIndex=0;
		Sheet sheet=wb.createSheet();
		Row row=sheet.createRow(rowIndex++);
		for(int i=0;i<headers.length;i++){
			row.createCell(i).setCellValue(headers[i]);
		}
		while(rs.next()){
			row=sheet.createRow(rowIndex++);
			for(int i=0;i<headers.length;i++){
				row.createCell(i).setCellValue(rs.getObject(i+1).toString());
			}
		}
	}
	//poi导出数据
	//导出销售数据库
	public static Workbook fillExcelDataWithTemplateofdataofsale(ResultSet rs,String templateFileName) throws Exception{
		InputStream inp=ExcelUtil.class.getResourceAsStream("/com/java/template/"+templateFileName);
		XSSFWorkbook wb=new XSSFWorkbook(inp);
		Sheet sheet=wb.getSheetAt(0);
		int cellNums=sheet.getRow(0).getLastCellNum();
		int rowIndex=1;
		while(rs.next()){
			Row row=sheet.createRow(rowIndex++);
			for(int i=0;i<cellNums;i++){
				if(rs.getObject(i+1)==null){
					row.createCell(i).setCellValue("");
				}else{
					if(i==1){
						String a=rs.getObject(i+1).toString();

						a=a.replace("-", "/");
						row.createCell(i).setCellValue(a);
					}else{
						row.createCell(i).setCellValue(rs.getObject(i+1).toString());
					}

				}
			}
		}

		return wb;
	}

	//处理Excel里面的数据
	public static String formateCell(XSSFCell xssfcell){
		if(xssfcell==null){
			return null;
		}else {
			if(xssfcell.getCellType()==XSSFCell.CELL_TYPE_BOOLEAN){
				return String.valueOf(xssfcell.getBooleanCellValue());
			}else if(xssfcell.getCellType()==XSSFCell.CELL_TYPE_STRING){
				return String.valueOf(xssfcell.getStringCellValue());
			}else if(XSSFCell.CELL_TYPE_NUMERIC==xssfcell.getCellType()){

				return String.valueOf(xssfcell.getNumericCellValue());

			}
			else if(xssfcell.getCellType()==XSSFCell.CELL_TYPE_FORMULA){
				if(xssfcell.getCachedFormulaResultType()== XSSFCell.CELL_TYPE_NUMERIC){
					return String.valueOf(xssfcell.getNumericCellValue());
				}else if(xssfcell.getCachedFormulaResultType()== XSSFCell.CELL_TYPE_STRING){
					return String.valueOf(xssfcell.getStringCellValue());
				}
			}
		}
		return null;
	}
	public static Workbook fillExcelDataWithTemplateofCount(ResultSet rscount,
															String templateFileName) throws Exception {
		// TODO Auto-generated method stub
		InputStream inp=ExcelUtil.class.getResourceAsStream("/com/java/template/"+templateFileName);
		XSSFWorkbook wb=new XSSFWorkbook(inp);
		Sheet sheet=wb.getSheetAt(0);
		int cellNums=sheet.getRow(0).getLastCellNum();
		int rowIndex=1;

		while(rscount.next()){
			Row row=sheet.createRow(rowIndex++);
			for(int i=0;i<cellNums;i++){
				if(rscount.getObject(i+1)==null){
					row.createCell(i).setCellValue("");
				}else{
					row.createCell(i).setCellValue(rscount.getObject(i+1).toString());
				}
			}
		}

		return wb;
	}

}

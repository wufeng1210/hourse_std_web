package com.hourse.web.util;


import com.hourse.web.model.Hourse;
import com.hourse.web.service.IUserService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;


public class ExcelUtil {
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

}

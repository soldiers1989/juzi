package com.jzfq.retail.common.util;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.DocumentFactoryHelper;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * POI帮助类
 *
 * @author Garen Gosling
 * @create 2018-04-22 16:46
 * @since v1.0
 */

public class ExcelUtil {

    /**
     * 读取Excel
     * @param multipartFile
     * @return
     */
    public static List<Map<Integer, String>> readExcel(MultipartFile multipartFile, Integer sheetIndex){
        BufferedInputStream bf=null;
        try {
            bf=new BufferedInputStream(multipartFile.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Workbook workbook=null;
        try {
            if (POIFSFileSystem.hasPOIFSHeader(bf)) {
                workbook = new HSSFWorkbook(bf);
            } else if (DocumentFactoryHelper.hasOOXMLHeader(bf)) {
                workbook = new XSSFWorkbook(OPCPackage.open(bf));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Sheet sheet = null;
        if(sheetIndex == null) {
            sheet = workbook.getSheetAt(0);
        } else {
            sheet = workbook.getSheetAt(sheetIndex);
        }
        List<Map<Integer, String>> list = new ArrayList<>();
        for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            if(rowIndex == 0){
                continue;
            }
            Map<Integer, String> map = new HashedMap();
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }
            Cell cell = null;
            for (int columnIndex = 0; columnIndex <= row.getLastCellNum(); columnIndex++) {
                String value = "";
                cell = row.getCell(columnIndex);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    value = cell.getStringCellValue();
                }
                map.put(columnIndex, value);
            }
            list.add(map);
        }
        return list;
    }


    /**
     * 导出Excel
     *
     * @param fileName
     *                      文件名
     * @param columnNames
     *                      列名数组
     * @param data
     *                      数据集合
     * @param response
     *                      HttpServletResponse对象
     * @param <T>
     */
    public static <T> void export(String fileName, String[] columnNames, List<T> data, HttpServletResponse response){
        try {
            if(StringUtils.isBlank(fileName)){
                fileName = "default";
            }
            response.setHeader("Content-Type","application/force-download");
            response.setHeader("Content-Type","application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename="+ new String(fileName.getBytes("UTF-8"), "ISO-8859-1") +".xlsx");// 设定输出文件头
            doExport(columnNames, data, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static <T> void doExport(String[] columnNames, List<T> data, HttpServletResponse response) throws Exception {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("0");
        XSSFRow row = sheet.createRow(0);

        CellStyle styleHead = wb.createCellStyle();
        XSSFFont font = wb.createFont();
        font.setFontName("宋体");
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);//设置字体大小
        styleHead.setFont(font);

        XSSFCellStyle styleBody = wb.createCellStyle();
        XSSFFont font2 = wb.createFont();
        font2.setFontName("宋体");
        font2.setFontHeightInPoints((short) 11);
        styleBody.setFont(font2);//选择需要用到的字体格式

        for (short i = 0; i < columnNames.length; i++) {
            XSSFCell cell = row.createCell((short) i);
            String columnName = columnNames[i];
            cell.setCellValue(columnName);
            cell.setCellStyle(styleHead);
            sheet.setColumnWidth(i,columnName.getBytes().length*256);
        }

        OutputStream out = response.getOutputStream();
        Iterator<T> it = data.iterator();
        int index = 1;
        try {
            while (it.hasNext()) {
                row = sheet.createRow(index++);
                T t = (T) it.next();
                Field[] fields = t.getClass().getDeclaredFields();
                for (short i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    if (field.toString().contains("static")) {
                        continue;
                    }
                    XSSFCell cell = row.createCell((short) i);
                    Method getMethod = t.getClass().getMethod(getMethodName(field.getName()),new Class[] {});
                    Object obj = getMethod.invoke(t, new Object[] {});
                    String value = valueToString(obj);
                    cell.setCellValue(value);
                    cell.setCellStyle(styleBody);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            wb.write(out);
            out.close();
        }
    }

    private static String getMethodName(String methodName){
        Assert.hasText(methodName);
        return "get" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
    }

    private static String valueToString(Object obj){
        if(obj == null){
            return "";
        }
        if (obj instanceof Date){
            Date date = (Date) obj;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date);
        }
        return obj.toString();
    }

}

package com.wemall.core.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * Excel文件导入处理的工具类(暂不支持Excel 2007格式的文件).
 *
 * @author Kanine
 */
public class ExcelReader {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 解析处理指定路径的excel文件
     *
     * @param excelFilePath
     *            - excel文件所在路径
     * @param firstLineIncluded
     *            - 解析数据时是否包含首行
     */
    @SuppressWarnings("unchecked")
    public List<List<Entry>> parse(String excelFilePath, boolean firstLineIncluded) throws FileNotFoundException, IOException {
        final List<String> keys = new ArrayList<String>();
        List<List<Entry>> dataList = new ArrayList<List<Entry>>();

        POIFSFileSystem pss = new POIFSFileSystem(new FileInputStream(excelFilePath));
        HSSFWorkbook workbook = new HSSFWorkbook(pss);
        HSSFSheet sheet = workbook.getSheetAt(0);
        int indexOfFieldName = 0;
        int physicalNumberOfCells = 0;
        /** 首行作为标题时，读取列名需跳过该行 */
        if (!firstLineIncluded)
            indexOfFieldName = 1;

        HSSFRow rowOfFieldName = sheet.getRow(indexOfFieldName);
        Iterator cit = rowOfFieldName.cellIterator();
        // 读取列名
        while (cit.hasNext()){
            HSSFCell cell = (HSSFCell) cit.next();
            keys.add(cell.getRichStringCellValue().toString());
            physicalNumberOfCells++;
        }

        int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
        // 遍历每行数据
        for (int i = indexOfFieldName + 1; i < physicalNumberOfRows; i++){
            HSSFRow row = sheet.getRow(i);
            Map<String, String> map = new HashMap<String, String>();
            for (int m = 0; m < physicalNumberOfCells; m++){
                HSSFCell cell = row.getCell(m);
                map.put(keys.get(m), match(row, cell, m));
            }
            /**
             * 由于Map存放的对象是无序的，需重新将结果集排序
             */
            List<Entry> data = new ArrayList<Entry>(map.entrySet());
            Collections.sort(data, new Comparator<Entry>(){
                public int compare(Entry o1, Entry o2){
                    return keys.indexOf(o1.getKey()) - keys.indexOf(o2.getKey());
                }
            });
            dataList.add(data);
        }

        return dataList;
    }

    /**
     * 根据单元格格式返回正确的结果
     *
     * @param row
     *            - 单元格所在行
     * @param cell
     *            - 单元格
     * @param columnNo
     *            - 单元格所在列
     */
    private String match(HSSFRow row, HSSFCell cell, int columnNo){
        String value = "";
        if (cell != null)
            switch (cell.getCellType()){
            case HSSFCell.CELL_TYPE_BLANK:
                value = "";
                break;
            case HSSFCell.CELL_TYPE_ERROR:
                value = "";
                break;
            case HSSFCell.CELL_TYPE_STRING:
                value = cell.getRichStringCellValue().toString();
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                value = cell.getCellFormula().toString();
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                value = Boolean.toString(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)){ // 是否为日期型
                    value = sdf.format(cell.getDateCellValue());
                }else{ // 是否为数值型
                    double d = cell.getNumericCellValue();
                    if (d - (int) d < Double.MIN_VALUE){ // 是否为int型
                        value = Integer.toString((int) d);
                    }else{ // 是否为double型
                        value = Double.toString(cell.getNumericCellValue());
                    }
                }
                break;
            }

        return value;
    }
}
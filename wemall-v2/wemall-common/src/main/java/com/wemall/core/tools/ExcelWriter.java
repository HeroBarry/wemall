package com.wemall.core.tools;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Excel文件导出处理的工具类(暂不支持Excel 2007格式的文件).
 *
 * @author Kanine
 */
public class ExcelWriter {
    private HSSFWorkbook workbook;

    private HSSFSheet sheet;

    private ArrayList<String[]> dataList;

    private String[] columns;

    private int lineX = 0;

    private String sheetName = "Sheet1";

    private String headline = "";

    public void export(File exportfile) throws IOException {
        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet(sheetName);
        createHead();
        createBody();
        OutputStream os = new FileOutputStream(exportfile);
        workbook.write(os);
        os.flush();
        os.close();
    }

    /**
     * 创建第一行,也就是显示的标题,可以设置高度,单元格的格式,颜色,字体等设置,同时可以合并单元格.
     */
    private void createHead(){
        /**
         * 指定合并区域,前二个参数为开始处X,Y坐标.后二个为结束的坐标.
         */
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, (columns.length - 1)));
        HSSFRow headRow0 = sheet.createRow(lineX++);
        // headRow0.setHeight((short) 0x200);

        HSSFFont font = workbook.createFont();
        font.setColor(HSSFFont.SS_NONE);
        font.setFontHeight((short) 280);
        font.setFontName("Courier New");
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 等级居中
        createCell(style, headRow0.createCell(0), headline);
    }

    private void createBody(){
        HSSFCellStyle style = workbook.createCellStyle();
        style.setWrapText(true);// 文本区域随内容多少自动调整

        // 填充列名
        HSSFRow bodyTile = sheet.createRow(lineX++);
        for (int i = 0; i < columns.length; i++)
            createCell(style, bodyTile.createCell(i), "" + columns[i]);

        HSSFRow bodyRow;
        // 填充数据
        for (int i = 0; i < dataList.size(); i++){
            String[] datas = dataList.get(i);
            bodyRow = sheet.createRow(lineX++);
            for (int j = 0; j < columns.length; j++)
                createCell(style, bodyRow.createCell(j), datas[j]);
        }
    }

    private void createCell(HSSFCellStyle cellStyle, HSSFCell cell, String value){
        if (cellStyle != null)
            cell.setCellStyle(cellStyle);
        cell.setCellValue(new HSSFRichTextString(value));
    }

    public void setDataList(ArrayList<String[]> dataList){
        this.dataList = dataList;
    }

    public void setColumns(String[] columns){
        this.columns = columns;
    }

    public void setSheetName(String sheetName){
        this.sheetName = sheetName;
    }

    public void setHeadline(String headline){
        this.headline = headline;
    }
}
package com.yyj.framework.common.util.file;

import com.yyj.framework.common.util.util.ReflectUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyijun on 2018/5/24.
 */
public final class ExcelHandler {
    private static final String XLS_FILE = ".xls";
    private static final String XLSX_FILE = ".xlsx";

    public static <T> List<T> readExcel(Class<T> clazz, String filePath, String[] fieldNames, int sheetNo, boolean hasTitle) throws Exception {

        List<T> datas = new LinkedList<>();
        InputStream is = null;
        Workbook workbook = null;
        try {
            int fieldCnt = fieldNames.length;
            is = new FileInputStream(filePath);
            workbook = getWorkbook(is, filePath);
            Sheet sheet = workbook.getSheetAt(sheetNo);
            int start = sheet.getFirstRowNum() + (hasTitle ? 1 : 0);
            for (int i = start; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                T target = ExcelRowBuilder.build(clazz, fieldNames, row, fieldCnt);
                datas.add(target);
            }
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return datas;
    }

    public static <T> List<T> readExcel(Class<T> clazz, String filePath, String[] fieldNames, boolean hasTitle) throws Exception {
        return readExcel(clazz, filePath, fieldNames, 0, hasTitle);
    }

    public static <T> List<T> readExcel(Class<T> clazz, String filePath, String[] fieldNames) throws Exception {
        return readExcel(clazz, filePath, fieldNames, 0, true);
    }

    public static List<Map<Integer, Object>> readExcel(String filePath, int sheetNo, int rowNo, Class<?>... columnTypes) throws Exception {
        List<Map<Integer, Object>> resultList = new LinkedList<>();
        InputStream is = null;
        Workbook workbook = null;
        try {
            is = new FileInputStream(filePath);
            workbook = getWorkbook(is, filePath);
            Sheet sheet = workbook.getSheetAt(sheetNo);
            int start = sheet.getFirstRowNum() + rowNo;
            for (int i = start; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                resultList.add(ExcelRowBuilder.build(row, columnTypes));
            }
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }

    public static <T> void writeExcel(List<T> dataModels, String[] fieldNames, String[] titles, String filePath) throws Exception {
        Workbook workbook = getWorkbook(null, filePath);
        String sheetName = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSS");
        Sheet sheet = workbook.createSheet(sheetName);
        buildTitle(titles, workbook, sheet);
        buildRow(dataModels, fieldNames, sheet);
        FileOutputStream fos = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(filePath);
            workbook.write(fos);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static <T> void buildRow(List<T> dataModels, String[] fieldNames, Sheet sheet) throws NoSuchMethodException, IllegalAccessException, java.lang.reflect.InvocationTargetException {
        for (int i = 0; i < dataModels.size(); i++) {
            T target = dataModels.get(i);
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < fieldNames.length; j++) {
                String fieldName = fieldNames[j];
                Object result = ReflectUtils.invokeGetter(target, fieldName);
                Cell cell = row.createCell(j);
                if (result instanceof Date) {
                    cell.setCellValue(DateFormatUtils.format((Date) result, "yyyyMMddHHmmssSS"));
                } else if (result instanceof Long) {
                    cell.setCellValue(Long.parseLong(result.toString()));
                } else if (result instanceof Integer) {
                    cell.setCellValue(Long.parseLong(result.toString()));
                } else {
                    cell.setCellValue(result.toString());
                }
            }
        }
    }

    private static void buildTitle(String[] titles, Workbook workbook, Sheet sheet) {
        Row headRow = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = headRow.createCell(i);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(titles[i]);
            CellStyle cellStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            cellStyle.setFont(font);
            cellStyle.setWrapText(true);
            cell.setCellStyle(cellStyle);
            sheet.setColumnWidth(i, titles[i].length() * 1000);
        }
    }

    private static Workbook getWorkbook(InputStream is, String filePath) throws IOException {
        Workbook workbook = null;
        if (filePath.endsWith(XLS_FILE)) {
            if (null == is) {
                workbook = new HSSFWorkbook();
            } else {
                workbook = new HSSFWorkbook(is);
            }
        } else if (filePath.endsWith(XLSX_FILE)) {
            if (null == is) {
                workbook = new XSSFWorkbook();
            } else {
                workbook = new XSSFWorkbook(is);
            }
        } else {
            throw new RuntimeException("Invalid file format");
        }
        return workbook;
    }
}

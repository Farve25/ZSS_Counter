package model;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class ReportWriter {
    private XSSFWorkbook workbook = null;
    private XSSFSheet sheet = null;
    private String file;
    private static Map<String, CellStyle> styles;

    private static String readCell(int i, Row row) {
        try {
            return row.getCell(i).getStringCellValue();
        } catch (Exception e) {
            return ((Double) row.getCell(i).getNumericCellValue()).toString();
        }
    }

    void openFile(String group) throws IOException {
        file = ReportProcess.file + "/" + group + ".xlsx";
        File nFile = new File(file);
        if (nFile.exists()) {
            workbook = new XSSFWorkbook(new FileInputStream(file));
        } else {
            createFile(group);
        }
    }

    private void createFile(String group) {
        String subject = ReportProcess.subject;
        String prepod = ReportProcess.nameOfPrepod;
        String vid = ReportProcess.form;

            workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("14");

        //Map<String, CellStyle> styles;
        styles = createStyles(workbook);

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Ведомость учета посещаемости");
        cell.setCellStyle(styles.get("title"));

        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 8);
        sheet.addMergedRegion(region);

        row = sheet.createRow(2);
        cell = row.createCell(1);
        cell.setCellValue("Предмет");
        cell.setCellStyle(styles.get("header"));
        cell = row.createCell(2);
        cell.setCellValue(subject);
        cell.setCellStyle(styles.get("header"));
        for (int i = 3; i<7; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(styles.get("header"));
        }

        row = sheet.createRow(3);
        cell = row.createCell(1);
        cell.setCellValue("Вид занятия");
        cell.setCellStyle(styles.get("header"));
        cell = row.createCell(2);
        cell.setCellValue(vid);
        cell.setCellStyle(styles.get("header"));
        for (int i = 3; i<7; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(styles.get("header"));
        }

        row = sheet.createRow(4);
        cell = row.createCell(1);
        cell.setCellValue("Преподователь");
        cell.setCellStyle(styles.get("header"));
        cell = row.createCell(2);
        cell.setCellValue(prepod);
        cell.setCellStyle(styles.get("header"));
        for (int i = 3; i<7; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(styles.get("header"));
        }

        row = sheet.createRow(5);
        cell = row.createCell(1);
        cell.setCellValue("Группа");
        cell.setCellStyle(styles.get("header"));
        cell = row.createCell(2);
        cell.setCellValue(group);
        cell.setCellStyle(styles.get("header"));
        for (int i = 3; i<7; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(styles.get("header"));
        }

        for (int i = 2; i<6; i++) {
            region = new CellRangeAddress(i, i, 2, 6);
            sheet.addMergedRegion(region);
        }

        row = sheet.createRow(7);
        cell = row.createCell(0);
        cell.setCellValue("№");
        cell.setCellStyle(styles.get("topTable"));
        cell = row.createCell(1);
        cell.setCellValue("ФИО");
        cell.setCellStyle(styles.get("topTable"));
    }

    void addDate(){
        Row row = sheet.getRow(7);
        int lastCell = row.getLastCellNum();

        Cell cell = row.createCell(lastCell);
        cell.setCellValue(new Date());
        cell.setCellStyle(styles.get("dateTable"));

        sheet.autoSizeColumn(lastCell);

        for (int i =8; i<=sheet.getLastRowNum(); i++){
            row = sheet.createRow(i);
            cell = row.createCell(lastCell);
            cell.setCellValue("-");
            cell.setCellStyle(styles.get("table"));
        }
    }

    void updateStudent(String name){
        int lastRow = sheet.getLastRowNum();
        boolean f = false;
        for (int i = 8; i<=lastRow; i++) {
            Row currentRow = sheet.getRow(i);
            String currentName = readCell(1, currentRow);
            if (name.equals(currentName)) {
                Cell currentCell = currentRow.getCell(currentRow.getLastCellNum() - 1);
                currentCell.setCellValue("+");
                f = true;
            }
        }
        if (!f) {
            addStudent(name, lastRow);
        }
    }

    private void addStudent(String name, int lastRow){
        Row row = sheet.getRow(7);
        int lastCell = row.getLastCellNum() - 1;

        row = sheet.createRow(lastRow+1);
        Cell cell = row.createCell(0);
        cell.setCellValue(lastRow-6);
        cell.setCellStyle(styles.get("table"));

        cell = row.createCell(1);
        cell.setCellValue(name);
        cell.setCellStyle(styles.get("table"));

        for (int i = 2; i<lastCell; i++) {
            cell = row.createCell(i);
            cell.setCellValue("-");
            cell.setCellStyle(styles.get("table"));
        }

        cell = row.createCell(lastCell);
        cell.setCellValue("+");
        cell.setCellStyle(styles.get("table"));
    }

    void close() {
        if (workbook != null)
            try {
                sheet.autoSizeColumn(0);
                sheet.autoSizeColumn(1);

                FileOutputStream out = new FileOutputStream(new File(file));
                workbook.write(out);

                workbook.close();
                out.close();
            } catch (IOException e) {
                System.out.println("Программа завершена не корректно");
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("Программа завершена не корректно: " + e.getMessage());
            }
    }


    private static Map<String, CellStyle> createStyles(Workbook wb){
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();

        short borderColor = IndexedColors.GREY_50_PERCENT.getIndex();

        CellStyle style;
        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short)22);
        titleFont.setFontName("Constantia");
        titleFont.setItalic(true);
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(titleFont);
        styles.put("title", style);

        Font headerFont = wb.createFont();
        headerFont.setFontHeightInPoints((short)11);
        headerFont.setColor(IndexedColors.BLACK.getIndex());
        headerFont.setBold(true);
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setFont(headerFont);
        styles.put("header", style);

        Font topTableFont = wb.createFont();
        topTableFont.setFontHeightInPoints((short)11);
        topTableFont.setColor(IndexedColors.BLACK.getIndex());
        topTableFont.setBold(true);
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setFont(topTableFont);
        styles.put("table", style);

        Font tableFont = wb.createFont();
        tableFont.setFontHeightInPoints((short)11);
        tableFont.setColor(IndexedColors.BLACK.getIndex());
        tableFont.setBold(true);
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(borderColor);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setFont(tableFont);
        styles.put("topTable", style);

        Font dateFont = wb.createFont();
        dateFont.setFontHeightInPoints((short)11);
        dateFont.setColor(IndexedColors.BLACK.getIndex());
        dateFont.setBold(true);
        DataFormat format = wb.createDataFormat();
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setRotation((short)90);
        style.setFillForegroundColor(borderColor);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setFont(dateFont);
        style.setDataFormat(format.getFormat("dd.mm.yy"));
        styles.put("dateTable", style);



        return styles;
    }

}

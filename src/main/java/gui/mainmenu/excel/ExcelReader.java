package gui.mainmenu.excel;

import gui.account.Account;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import questions.question.Question;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.stream.Stream;

public class ExcelReader {

    private File file;
    private Sheet sheet;
    private JSONObject json;
    private String quizName;

    public ExcelReader(File file) {
        this.file = file;
    }

    public ExcelReader(JSONObject json) {
        this.json = json;
    }

    /**
     * Read the File. If valid, turn into Excel sheet.
     *
     * @throws ExcelValidateException if not properly formatted for toJSON().
     */
    public void validateFile() throws IOException {

        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        if (validateExcel(sheet)) {
            this.sheet = sheet;
        }

        workbook.close();
        fis.close();

    }

    /**
     * Get an excel sheet from the JSONObject.
     */
    public XSSFWorkbook jsonToExcel() throws JSONException, IOException {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Your Quiz");

        //Set guidelines
        Row row = sheet.createRow(0);
        for (int i = 0; i < JSONKey.values().length; i++) {
            Cell key = row.createCell(i);
            key.setCellValue(JSONKey.getKeyAtIndex(i).toString());
        }

        int rowCount = 1;
        for (int i = 0; i < this.json.length(); i++) {

            JSONObject internalJson = (JSONObject) this.json.get("obj" + i);
            row = sheet.createRow(rowCount++);

            Cell subject = row.createCell(0);
            subject.setCellValue(internalJson.get(JSONKey.SUBJECT.toString().toLowerCase()).toString());

            Cell question = row.createCell(1);
            question.setCellValue(internalJson.get(JSONKey.QUESTION.toString().toLowerCase()).toString());

            Cell options = row.createCell(2);
            options.setCellValue(internalJson.get(JSONKey.OPTIONS.toString().toLowerCase()).toString());

            Cell answer = row.createCell(3);
            answer.setCellValue(internalJson.get(JSONKey.ANSWER.toString().toLowerCase()).toString());

            Cell directions = row.createCell(4);
            directions.setCellValue(internalJson.get(JSONKey.DIRECTIONS.toString().toLowerCase()).toString());

            Cell type = row.createCell(5);
            type.setCellValue(internalJson.get(JSONKey.TYPE.toString().toLowerCase()).toString());

            if (i == 0) {
                Cell quizName = row.createCell(6);
                quizName.setCellValue(internalJson.get(JSONKey.QUIZNAME.toString().toLowerCase()).toString());
            }
        }

        return workbook;

    }

    /**
     * Get the JSONArray translation of Excel sheet.
     */
    public JSONArray sheetToJSONArray() throws JSONException {

        JSONArray jsonArray = new JSONArray();

        DataFormatter dataFormatter = new DataFormatter();

        for (Row row : sheet) {

            //If the first cell is Subject then skip the entire row
            if (row.getCell(0).toString().toUpperCase().equals(JSONKey.SUBJECT.toString())) {
                continue;
            }

            JSONObject internalJSON = new JSONObject();
            jsonArray.put(internalJSON);

            //Only use the first 6 cells, if there is more ignore it
            for (int cellIndex = 0; cellIndex < 5; cellIndex++) {

                Cell cell = row.getCell(cellIndex);
                internalJSON.put(JSONKey.getKeyAtIndex(cellIndex).toString().toLowerCase(), dataFormatter.formatCellValue(cell));

            }

            //Format to uppercase for SQL
            internalJSON.put(JSONKey.getKeyAtIndex(5).toString().toLowerCase(), dataFormatter.formatCellValue(row.getCell(5)).toUpperCase());

            //Attach  quiz name to every question
            internalJSON.put(JSONKey.getKeyAtIndex(6).toString().toLowerCase(), quizName);

            internalJSON.put("quizowner", Account.getUser().getUsername());
        }

        return jsonArray;

    }


    //use logic to make sure all required values are there and properly formatted
    private boolean validateExcel(Sheet sheet) {

        DataFormatter dataFormatter = new DataFormatter();

        for (Row row : sheet) {

            if (row.getRowNum() == 0) {
                try {

                    //must confine to this exact order
                    boolean valid = row.getCell(0).getStringCellValue().equalsIgnoreCase(JSONKey.SUBJECT.toString())
                            && row.getCell(1).getStringCellValue().equalsIgnoreCase(JSONKey.QUESTION.toString())
                            && row.getCell(2).getStringCellValue().equalsIgnoreCase(JSONKey.OPTIONS.toString())
                            && row.getCell(3).getStringCellValue().equalsIgnoreCase(JSONKey.ANSWER.toString())
                            && row.getCell(4).getStringCellValue().equalsIgnoreCase(JSONKey.DIRECTIONS.toString())
                            && row.getCell(5).getStringCellValue().equalsIgnoreCase(JSONKey.TYPE.toString())
                            && row.getCell(6).getStringCellValue().equals(JSONKey.QUIZNAME.toString());

                    if (!valid) {
                        throw new ExcelValidateException("First row not correctly formatted.");
                    }

                    continue;

                } catch (Exception e) {

                    throw new ExcelValidateException("First row not correctly formatted.");

                }


            } else if (row.getRowNum() == 1) {

                //row 1 must have a value.
                try {
                    quizName = row.getCell(6).getStringCellValue();
                } catch (Exception e) {
                    throw new ExcelValidateException("Second row must specify Quiz Name", row.getRowNum(), 6);
                }

            } else if (row.getRowNum() > 50) {

                throw new ExcelValidateException("File exceeds question limit.", row.getRowNum());

            }

            //required not to be null/empty
            Stream.of(row.getCell(0), row.getCell(1), row.getCell(3), row.getCell(4), row.getCell(5))
                    .forEach(cell -> {
                        if (dataFormatter.formatCellValue(cell) == null) {
                            throw new ExcelValidateException("Required value missing", row.getRowNum(), cell.getColumnIndex());
                        }
                    });

            //if not in possible types
            try {

                Question.Type.valueOf(dataFormatter.formatCellValue(row.getCell(5)).toUpperCase());

            } catch (IllegalArgumentException e) {

                throw new ExcelValidateException("Unsupported question type", row.getRowNum(), 5);

            }


            //if options is empty it must be true or false / written
            if (dataFormatter.formatCellValue(row.getCell(2)).isEmpty()) {

                switch (Question.Type.valueOf(dataFormatter.formatCellValue(row.getCell(5)).toUpperCase())) {

                    case MULTIPLECHOICE -> throw new ExcelValidateException("Options must not be empty for multiple choice", row.getRowNum(), 2);

                    case CHECKBOX -> throw new ExcelValidateException("Options must not be empty for checkbox", row.getRowNum(), 2);

                }
            }

        }
        return true;
    }

    private enum JSONKey {
        SUBJECT,
        QUESTION,
        OPTIONS,
        ANSWER,
        DIRECTIONS,
        TYPE,
        QUIZNAME;

        public static JSONKey getKeyAtIndex(int index) {
            return JSONKey.values()[index];
        }
    }

}

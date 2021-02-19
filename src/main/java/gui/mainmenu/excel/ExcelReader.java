package gui.mainmenu.excel;

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

/**
 * Read JSON to Excel or Excel to JSON.
 */
public class ExcelReader {

    private File file;
    private Sheet sheet;
    private JSONObject json;

    public ExcelReader(File file) {
        this.file = file;
    }

    public ExcelReader(JSONObject json) {
        this.json = json;
    }

    /**
     * Read the File. If valid, turn into Excel sheet.
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
     * @return Excel workbook from a JSONObject.
     */
    public XSSFWorkbook jsonToExcel() throws JSONException {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Your Quiz");

        //Set guideline upper row with JSONKeys
        Row row = sheet.createRow(0);
        for (int i = 0; i < JSONKey.values().length; i++) {
            Cell key = row.createCell(i);
            key.setCellValue(JSONKey.getKeyAtIndex(i).toString());
        }

        //Since the JSON comes from the rest server it will always be formatted in this way
        //Set all JSON to Excel in the order of the JSONKey guideline row.
        int rowCount = 1;
        for (int i = 0; i < this.json.length(); i++) {

            //Get "obj0" to "obj json.length", because that is how the server will format each question.
            JSONObject internalJson = (JSONObject) this.json.get("obj" + i);

            //Create new row below existing.
            row = sheet.createRow(rowCount++);

            Cell question = row.createCell(0);
            question.setCellValue(internalJson.get(JSONKey.QUESTION.toString().toLowerCase()).toString());

            Cell options = row.createCell(1);
            options.setCellValue(internalJson.get(JSONKey.OPTIONS.toString().toLowerCase()).toString());

            Cell answer = row.createCell(2);
            answer.setCellValue(internalJson.get(JSONKey.ANSWER.toString().toLowerCase()).toString());

            Cell directions = row.createCell(3);
            directions.setCellValue(internalJson.get(JSONKey.DIRECTIONS.toString().toLowerCase()).toString());

            Cell type = row.createCell(4);
            type.setCellValue(internalJson.get(JSONKey.TYPE.toString().toLowerCase()).toString());
        }

        //auto size all columns to make it look good
        for (int i = 0; i < this.json.length(); i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;

    }

    /**
     * @return JSONArray of an Excel sheet
     */
    public JSONObject sheetToJSON() throws JSONException {

        //Initialize jsonArray to write too
        JSONArray jsonArray = new JSONArray();

        //Initialize dataFormatter for formatting all excel information to a string.
        DataFormatter dataFormatter = new DataFormatter();

        for (Row row : sheet) {

            //If the first cell is Question then skip guideline row.
            if (row.getCell(0).toString().toUpperCase().equals(JSONKey.QUESTION.toString())) {
                continue;
            }

            //Initialize internal JSONObject that represents an individual question
            JSONObject internalJSON = new JSONObject();

            for (int cellIndex = 0; cellIndex < 4; cellIndex++) {

                Cell cell = row.getCell(cellIndex);
                internalJSON.put(JSONKey.getKeyAtIndex(cellIndex).toString().toLowerCase(), dataFormatter.formatCellValue(cell));

            }

            //Format Type to uppercase for SQL
            internalJSON.put(JSONKey.getKeyAtIndex(4).toString().toLowerCase(), dataFormatter.formatCellValue(row.getCell(4)).toUpperCase());

            //Write to jsonArray
            jsonArray.put(internalJSON);
        }

        return new JSONObject().put("questions", jsonArray);

    }


    //use logic to make sure all required values are there and properly formatted
    private boolean validateExcel(Sheet sheet) {

        //Format excel data to proper java object.
        DataFormatter dataFormatter = new DataFormatter();

        for (Row row : sheet) {

            //If the first cell is Question then skip guideline row.
            if (row.getCell(0).toString().toUpperCase().equals(JSONKey.QUESTION.toString())) {
                continue;
            }

            //50 Questions maximum
            if (row.getRowNum() > 50) {

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

                Question.Type.valueOf(dataFormatter.formatCellValue(row.getCell(4)).toUpperCase());

            } catch (IllegalArgumentException e) {

                throw new ExcelValidateException("Unsupported question type", row.getRowNum(), 4);

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

    public String getFilePath() {
        return file.getPath();
    }

    private enum JSONKey {
        QUESTION,
        OPTIONS,
        ANSWER,
        DIRECTIONS,
        TYPE;

        public static JSONKey getKeyAtIndex(int index) {
            return JSONKey.values()[index];
        }
    }

}

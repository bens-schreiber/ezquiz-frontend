package classes.gui.mainmenu.excel;

/**
 * Exception that is thrown when attempting to validate an Excel file for reading to server.
 * Contains errorMsg to display to user.
 */
public class ExcelValidateException extends RuntimeException {

    private final String errorMsg;
    private int row;
    private int cell;

    ExcelValidateException(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    ExcelValidateException(String errorMsg, int row) {
        this.errorMsg = errorMsg;
        this.row = row;
    }

    ExcelValidateException(String errorMsg, int row, int cell) {
        this.errorMsg = errorMsg;
        this.row = row + 1;
        this.cell = cell + 1;
    }

    public String getErrorMsg() {

        if (row == 0 && cell == 0) return errorMsg;

        return errorMsg + " [Row: " + row + " Cell: " + cell + "]";
    }
}

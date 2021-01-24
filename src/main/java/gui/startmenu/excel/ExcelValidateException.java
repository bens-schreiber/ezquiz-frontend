package gui.startmenu.excel;

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

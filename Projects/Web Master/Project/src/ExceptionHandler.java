/***
 * Exception Handler Class
 * @author Keivan Ipchi Hagh
 * @version 1.0.0
 */
public class ExceptionHandler {

    /**
     * Handler method
     * All errors have three things: Code (Identifier), Content (Description) and Level (Indicator)
     * @param code  Err/Warning Code
     */
    public static void handle(int code) {
        String exceptionInfo = null;
        String errorLvl = null;

        switch (code) {
            case 0:
                exceptionInfo = "Cross platform look and feel where not found on this device. Reversing to system look and feel configurations.";
                errorLvl = "Warning";
                break;

            case 1:
                exceptionInfo = "Logo file could not be found.";
                errorLvl = "Warning";
                break;
            case 2:
                exceptionInfo = "There was an error while initializing the frame.";
                errorLvl = "Error";
                break;
            case 3:
                exceptionInfo = "There was an error saving your settings.";
                errorLvl = "Error";
                break;
            case 4:
                exceptionInfo = "There was an error loading your settings.";
                errorLvl = "Error";
                break;
            case 5:
                exceptionInfo = "Data files not found. Setting default frame properties'";
                errorLvl = "Warning";
                break;
            case 6:
                exceptionInfo = "Invalid cURL Command";
                errorLvl = "Error";
                break;
            case 7:
                exceptionInfo = "There was an error Saving your Command.";
                errorLvl = "Error";
                break;
            case 8:
                exceptionInfo = "No Saved Records Where Found. Save At least One Record To Use This Command";
                errorLvl = "Error";
                break;
            case 9:
                exceptionInfo = "There Was An Error Creating Your WorkSpace";
                errorLvl = "Error";
                break;
            case 10:
                exceptionInfo = "There Was An Error While Processing Your Thread";
                errorLvl = "Error";
                break;
            case 11:
                exceptionInfo = "There Was An Error Writing Your File";
                errorLvl = "Error";
                break;
            case 12:
                exceptionInfo = "Connection Failed";
                errorLvl = "Error";
                break;
            case 13:
                exceptionInfo = "Both -d & -j identifiers have been used. JSON has higher priority.";
                errorLvl = "Warning";
                break;
            case 14:
                exceptionInfo = "Both formData & uploadFile are used. UploadFile has higher priority.";
                errorLvl = "Warning";
                break;
            case 15:
                exceptionInfo = "There was an error firing your command";
                errorLvl = "Error";
                break;
            case 16:
                exceptionInfo = "An error occurred while loading your image.";
                errorLvl = "Error";
                break;
            case 17:
                exceptionInfo = "Server returned HTTP response code: 400 - Bad Request";
                errorLvl = "Error";
                break;
            default:
                exceptionInfo = "Exception Handler cannot provide description for the following error/warning exception.";
                errorLvl = "Error";
        }

        // Print out the costume error description
        if (errorLvl.equals("Error"))
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Error Code " + code + ": " + exceptionInfo + " " + ConsoleColors.RESET);
        else
            System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Warning Code " + code + ": " + exceptionInfo + " " + ConsoleColors.RESET);
    }
}
package big.company.input;

public class EmployeeFileNameArgumentExtractor {
    private static final String NO_FILE_ADDED_AS_ARG = "No file was added as arg!";
    private static final String ONLY_ONE_FILE_CAN_BE_ADDED_AS_ARG = "Only one file can be added as arg!";

    public String extractFileName(String[] args) {
        int argsLength = args.length;
        if (argsLength == 0) {
            throw new IllegalArgumentException(NO_FILE_ADDED_AS_ARG);
        } else if (argsLength > 1) {
            throw new IllegalArgumentException(ONLY_ONE_FILE_CAN_BE_ADDED_AS_ARG);
        } else {
            return args[0];
        }
    }
}

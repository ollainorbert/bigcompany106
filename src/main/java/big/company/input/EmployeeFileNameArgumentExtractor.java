package big.company.input;

/**
 * Extracts the filename from the input arguments.
 */
public class EmployeeFileNameArgumentExtractor {
    private static final String NO_FILE_ADDED_AS_ARG = "No file was added as arg!";
    private static final String ONLY_ONE_FILE_CAN_BE_ADDED_AS_ARG = "Only one file can be added as arg!";
    private static final int MAXIMUM_NUMBER_OF_ARG_ALLOWED = 1;

    public String extractFileName(String[] args) {
        int argsLength = args.length;
        if (argsLength == 0) {
            throw new IllegalArgumentException(NO_FILE_ADDED_AS_ARG);
        } else if (argsLength > MAXIMUM_NUMBER_OF_ARG_ALLOWED) {
            throw new IllegalArgumentException(ONLY_ONE_FILE_CAN_BE_ADDED_AS_ARG);
        } else {
            return args[0];
        }
    }
}

package big.company.domain;

/**
 * Custom exception to indicate the employee data related issues.
 */
public class IncorrectEmployeeDataException extends RuntimeException {

    public IncorrectEmployeeDataException(String message) {
        super(message);
    }

    public IncorrectEmployeeDataException(String message, Exception cause) {
        super(message, cause);
    }
}

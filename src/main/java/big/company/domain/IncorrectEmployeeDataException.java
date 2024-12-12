package big.company.domain;

public class IncorrectEmployeeDataException extends RuntimeException {

    public IncorrectEmployeeDataException(String message) {
        super(message);
    }

    public IncorrectEmployeeDataException(String message, Throwable cause) {
        super(message, cause);
    }
}

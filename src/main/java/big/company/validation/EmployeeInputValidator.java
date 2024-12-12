package big.company.validation;

import big.company.domain.Employee;
import big.company.domain.IncorrectEmployeeDataException;

import java.util.List;

public class EmployeeInputValidator {
    private static final String NO_CEO_PRESENT = "No CEO present!";
    private static final String MULTIPLE_CEO_PRESENT = "Multiple CEO present, instead of 1!";

    public void validate(List<Employee> employees) {
        validateCeoRole(employees);
    }

    private void validateCeoRole(List<Employee> employees) {
        var numberOfCeo = employees.stream().filter(employee -> employee.managerId() == null).count();
        if (numberOfCeo == 0) {
            throw new IncorrectEmployeeDataException(NO_CEO_PRESENT);
        } else if (numberOfCeo > 1) {
            throw new IncorrectEmployeeDataException(MULTIPLE_CEO_PRESENT);
        }
    }
}

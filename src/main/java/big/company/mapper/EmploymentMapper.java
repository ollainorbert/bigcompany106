package big.company.mapper;

import big.company.domain.Employee;
import big.company.domain.Employment;
import big.company.domain.IncorrectEmployeeDataException;

import java.util.List;

/**
 * Maps the employees into a structured format.
 */
public class EmploymentMapper {
    private static final String NO_CEO_PRESENT = "No CEO present!";
    private static final String MULTIPLE_CEO_PRESENT = "Multiple CEO present, instead of 1!";
    private static final int MAXIMUM_NUMBER_OF_CEO_ALLOWED = 1;

    public Employment map(List<Employee> employees) {
        return new Employment(
            findTheCeo(employees),
            employees
        );
    }

    private Employee findTheCeo(List<Employee> employees) {
        var ceos = employees.stream().filter(employee -> employee.managerId() == null).toList();
        if (ceos.isEmpty()) {
            throw new IncorrectEmployeeDataException(NO_CEO_PRESENT);
        } else if (ceos.size() > MAXIMUM_NUMBER_OF_CEO_ALLOWED) {
            throw new IncorrectEmployeeDataException(MULTIPLE_CEO_PRESENT);
        } else {
            return ceos.get(0);
        }
    }
}

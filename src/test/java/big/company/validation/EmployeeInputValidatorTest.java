package big.company.validation;

import big.company.domain.Employee;
import big.company.domain.IncorrectEmployeeDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmployeeInputValidatorTest {
    private static final String NO_CEO_PRESENT = "No CEO present!";
    private static final String MULTIPLE_CEO_PRESENT = "Multiple CEO present, instead of 1!";
    private static final List<Employee> NO_CEO_EMPLOYEE_LIST = List.of(
            new Employee(123, "Joe", "Doe", 60000, 1),
            new Employee(124, "Martin", "Chekov", 45000, 123),
            new Employee(125, "Bob", "Ronstad", 47000, 123),
            new Employee(300, "Alice", "Hasacat", 50000, 124),
            new Employee(305, "Brett", "Hardleaf", 34000, 300)
    );
    private static final List<Employee> MULTIPLE_CEO_EMPLOYEE_LIST = List.of(
            new Employee(123, "Joe", "Doe", 60000, null),
            new Employee(124, "Martin", "Chekov", 45000, null),
            new Employee(125, "Bob", "Ronstad", 47000, 123),
            new Employee(300, "Alice", "Hasacat", 50000, 124),
            new Employee(305, "Brett", "Hardleaf", 34000, 300)
    );
    private static final List<Employee> CORRECT_EMPLOYEE_LIST = List.of(
            new Employee(123, "Joe", "Doe", 60000, null),
            new Employee(124, "Martin", "Chekov", 45000, 123),
            new Employee(125, "Bob", "Ronstad", 47000, 123),
            new Employee(300, "Alice", "Hasacat", 50000, 124),
            new Employee(305, "Brett", "Hardleaf", 34000, 300)
    );

    private EmployeeInputValidator validator;

    @BeforeEach
    void setUp() {
        validator = new EmployeeInputValidator();
    }

    @Test
    void shouldBeExceptionDueNoCeoTest() {
        IncorrectEmployeeDataException thrown = assertThrows(
                IncorrectEmployeeDataException.class,
                () -> validator.validate(NO_CEO_EMPLOYEE_LIST)
        );

        assertEquals(NO_CEO_PRESENT, thrown.getMessage());
    }

    @Test
    void shouldBeExceptionDueMultipleCeoTest() {
        IncorrectEmployeeDataException thrown = assertThrows(
                IncorrectEmployeeDataException.class,
                () -> validator.validate(MULTIPLE_CEO_EMPLOYEE_LIST)
        );

        assertEquals(MULTIPLE_CEO_PRESENT, thrown.getMessage());
    }

    @Test
    void shouldBeValidTest() {
        validator.validate(CORRECT_EMPLOYEE_LIST);
    }
}

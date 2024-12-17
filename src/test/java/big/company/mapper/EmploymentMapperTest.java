package big.company.mapper;

import big.company.domain.Employee;
import big.company.domain.Employment;
import big.company.domain.IncorrectEmployeeDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmploymentMapperTest {
    private static final String NO_CEO_PRESENT = "No CEO present!";
    private static final String MULTIPLE_CEO_PRESENT = "Multiple CEO present, instead of 1!";
    private static final Employee A_REAL_CEO = new Employee(123, "Joe", "Doe", BigDecimal.valueOf(60000), null);
    private static final List<Employee> NO_CEO_EMPLOYEE_LIST = List.of(
        new Employee(123, "Joe", "Doe", BigDecimal.valueOf(60000), 1),
        new Employee(124, "Martin", "Chekov", BigDecimal.valueOf(45000), 123),
        new Employee(125, "Bob", "Ronstad", BigDecimal.valueOf(47000), 123),
        new Employee(300, "Alice", "Hasacat", BigDecimal.valueOf(50000), 124),
        new Employee(305, "Brett", "Hardleaf", BigDecimal.valueOf(34000), 300)
    );
    private static final List<Employee> MULTIPLE_CEO_EMPLOYEE_LIST = List.of(
        A_REAL_CEO,
        new Employee(124, "Martin", "Chekov", BigDecimal.valueOf(45000), null),
        new Employee(125, "Bob", "Ronstad", BigDecimal.valueOf(47000), 123),
        new Employee(300, "Alice", "Hasacat", BigDecimal.valueOf(50000), 124),
        new Employee(305, "Brett", "Hardleaf", BigDecimal.valueOf(34000), 300)
    );
    private static final List<Employee> CORRECT_EMPLOYEE_LIST = List.of(
        A_REAL_CEO,
        new Employee(124, "Martin", "Chekov", BigDecimal.valueOf(45000), 123),
        new Employee(125, "Bob", "Ronstad", BigDecimal.valueOf(47000), 123),
        new Employee(300, "Alice", "Hasacat", BigDecimal.valueOf(50000), 124),
        new Employee(305, "Brett", "Hardleaf", BigDecimal.valueOf(34000), 300)
    );
    private static final Employment EXPECTED_EMPLOYMENT = new Employment(
        A_REAL_CEO,
        CORRECT_EMPLOYEE_LIST
    );

    private EmploymentMapper employmentMapper;

    @BeforeEach
    void setUp() {
        employmentMapper = new EmploymentMapper();
    }

    @Test
    void shouldBeExceptionDueNoCeoTest() {
        IncorrectEmployeeDataException thrown = assertThrows(
            IncorrectEmployeeDataException.class,
            () -> employmentMapper.map(NO_CEO_EMPLOYEE_LIST)
        );

        assertEquals(NO_CEO_PRESENT, thrown.getMessage());
    }

    @Test
    void shouldBeExceptionDueMultipleCeoTest() {
        IncorrectEmployeeDataException thrown = assertThrows(
            IncorrectEmployeeDataException.class,
            () -> employmentMapper.map(MULTIPLE_CEO_EMPLOYEE_LIST)
        );

        assertEquals(MULTIPLE_CEO_PRESENT, thrown.getMessage());
    }

    @Test
    void shouldBeValidTest() {
        assertEquals(EXPECTED_EMPLOYMENT, employmentMapper.map(CORRECT_EMPLOYEE_LIST));
    }
}

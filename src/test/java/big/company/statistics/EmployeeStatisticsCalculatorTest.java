package big.company.statistics;

import big.company.domain.Employee;
import big.company.domain.EmployeeStatistics;
import big.company.domain.IncorrectEmployeeDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmployeeStatisticsCalculatorTest {
    private static final String MANAGER_NOT_EXIST_PATTERN = "There is a managerId=%s with no employee that has that id!";
    private static final String EMPLOYEE_WITH_NON_EXISTENT_MANAGER_OR_INCORRECT_REFERENCE_PATTERN = "An employee employeeId=%s has a managerId=%s, which doesn't exist or references backward or in a circle!";
    private static final List<Employee> EMPLOYEE_LIST_WITH_NON_EXISTENT_MANAGER = List.of(
        new Employee(123, "Joe", "Doe", 60000, null),
        new Employee(124, "Martin", "Chekov", 45000, 1231),
        new Employee(125, "Bob", "Ronstad", 47000, 123),
        new Employee(300, "Alice", "Hasacat", 50000, 124),
        new Employee(305, "Brett", "Hardleaf", 34000, 300)
    );
    private static final List<Employee> EMPLOYEE_LIST_WITH_INCORRECT_MANAGER_REFERENCING = List.of(
            new Employee(123, "Joe", "Doe", 60000, null),
            new Employee(124, "Martin", "Chekov", 45000, 123),
            new Employee(125, "Bob", "Ronstad", 47000, 123),
            new Employee(300, "Alice", "Hasacat", 50000, 305),
            new Employee(305, "Brett", "Hardleaf", 34000, 300)
    );
    private static final List<Employee> EMPLOYEE_LIST = List.of(
        new Employee(123, "Joe", "Doe", 60000, null),
        new Employee(124, "Martin", "Chekov", 450000, 123),
        new Employee(125, "Bob", "Ronstad", 47000, 123),
        new Employee(300, "Alice", "Hasacat", 50000, 124),
        new Employee(305, "Brett", "Hardleaf", 34000, 300),
        new Employee(306, "Brett1", "Hardleaf1", 35000, 305),
        new Employee(307, "Brett2", "Hardleaf2", 36000, 306),
        new Employee(308, "Brett3", "Hardleaf3", 37000, 307)
    );
    private static final EmployeeStatistics EXPECTED_EMPLOYEE_STATISTICS = new EmployeeStatistics(
        Map.of(
            new Employee(123, "Joe", "Doe", 60000, null), 238200.0,
            new Employee(305, "Brett", "Hardleaf", 34000, 300), 8000.0,
            new Employee(306, "Brett1", "Hardleaf1", 35000, 305), 8200.0,
            new Employee(307, "Brett2", "Hardleaf2", 36000, 306), 8400.0
        ),
        Map.of(new Employee(124, "Martin", "Chekov", 450000, 123), 375000.0),
        Map.of(new Employee(308, "Brett3", "Hardleaf3", 37000, 307), 5)
    );

    private EmployeeStatisticsCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new EmployeeStatisticsCalculator();
    }

    @Test
    void shouldThrowExceptionInCaseOfNonExistentMangerIdPresentTest() {
        IncorrectEmployeeDataException thrown = assertThrows(
            IncorrectEmployeeDataException.class,
            () -> calculator.calculate(EMPLOYEE_LIST_WITH_NON_EXISTENT_MANAGER)
        );

        assertEquals(String.format(MANAGER_NOT_EXIST_PATTERN, "1231"), thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionInCaseOfEmployeeWithNonExistentMangerTest() {
        IncorrectEmployeeDataException thrown = assertThrows(
                IncorrectEmployeeDataException.class,
                () -> calculator.calculate(EMPLOYEE_LIST_WITH_INCORRECT_MANAGER_REFERENCING)
        );

        // TODO This will maybe break, check back later
        assertEquals(String.format(EMPLOYEE_WITH_NON_EXISTENT_MANAGER_OR_INCORRECT_REFERENCE_PATTERN, "305", "300"), thrown.getMessage());
    }

    @Test
    void calculateTest() {
        assertEquals(EXPECTED_EMPLOYEE_STATISTICS, calculator.calculate(EMPLOYEE_LIST));
    }
}

package big.company.statistics;

import big.company.domain.Employee;
import big.company.domain.EmployeeStatistics;
import big.company.domain.Employment;
import big.company.domain.IncorrectEmployeeDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmployeeStatisticsCalculatorTest {
    private static final String MANAGER_NOT_EXIST_PATTERN = "There is a managerId=%s with no employee that has that id!";
    private static final String EMPLOYEE_WITH_NON_EXISTENT_MANAGER_OR_INCORRECT_REFERENCE_PATTERN = "An employee employeeId=%s has a managerId=%s, which doesn't exist or references backward or in a circle!";
    private static final Employee CEO = new Employee(123, "Joe", "Doe", BigDecimal.valueOf(60000), null);
    private static final Employment EMPLOYMENT_WITH_NON_EXISTENT_MANAGER = new Employment(
        CEO,
        List.of(
            CEO,
            new Employee(124, "Martin", "Chekov", BigDecimal.valueOf(45000), 1231),
            new Employee(125, "Bob", "Ronstad", BigDecimal.valueOf(47000), 123),
            new Employee(300, "Alice", "Hasacat", BigDecimal.valueOf(50000), 124),
            new Employee(305, "Brett", "Hardleaf", BigDecimal.valueOf(34000), 300)
        )
    );
    private static final Employment EMPLOYMENT_WITH_INCORRECT_MANAGER_REFERENCING = new Employment(
        CEO,
        List.of(
            CEO,
            new Employee(124, "Martin", "Chekov", BigDecimal.valueOf(45000), 123),
            new Employee(125, "Bob", "Ronstad", BigDecimal.valueOf(47000), 123),
            new Employee(300, "Alice", "Hasacat", BigDecimal.valueOf(50000), 305),
            new Employee(305, "Brett", "Hardleaf", BigDecimal.valueOf(34000), 300)
        )
    );
    private static final Employment EMPLOYEE_LIST = new Employment(
        CEO,
        List.of(
            CEO,
            new Employee(124, "Martin", "Chekov", BigDecimal.valueOf(450000), 123),
            new Employee(125, "Bob", "Ronstad", BigDecimal.valueOf(47000), 123),
            new Employee(300, "Alice", "Hasacat", BigDecimal.valueOf(50000), 124),
            new Employee(305, "Brett", "Hardleaf", BigDecimal.valueOf(34000), 300),
            new Employee(306, "Brett1", "Hardleaf1", BigDecimal.valueOf(35000), 305),
            new Employee(307, "Brett2", "Hardleaf2", BigDecimal.valueOf(36000), 306),
            new Employee(308, "Brett3", "Hardleaf3", BigDecimal.valueOf(37000), 307)
        )
    );
    private static final EmployeeStatistics EXPECTED_EMPLOYEE_STATISTICS = new EmployeeStatistics(
        Map.of(
            CEO, BigDecimal.valueOf(238200.0),
            new Employee(305, "Brett", "Hardleaf", BigDecimal.valueOf(34000), 300), BigDecimal.valueOf(8000.0),
            new Employee(306, "Brett1", "Hardleaf1", BigDecimal.valueOf(35000), 305), BigDecimal.valueOf(8200.0),
            new Employee(307, "Brett2", "Hardleaf2", BigDecimal.valueOf(36000), 306), BigDecimal.valueOf(8400.0)
        ),
        Map.of(new Employee(124, "Martin", "Chekov", BigDecimal.valueOf(450000), 123), BigDecimal.valueOf(375000.0)),
        Map.of(new Employee(308, "Brett3", "Hardleaf3", BigDecimal.valueOf(37000), 307), 5)
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
            () -> calculator.calculate(EMPLOYMENT_WITH_NON_EXISTENT_MANAGER)
        );

        assertEquals(String.format(MANAGER_NOT_EXIST_PATTERN, "1231"), thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionInCaseOfEmployeeWithNonExistentMangerTest() {
        IncorrectEmployeeDataException thrown = assertThrows(
                IncorrectEmployeeDataException.class,
                () -> calculator.calculate(EMPLOYMENT_WITH_INCORRECT_MANAGER_REFERENCING)
        );

        assertEquals(String.format(EMPLOYEE_WITH_NON_EXISTENT_MANAGER_OR_INCORRECT_REFERENCE_PATTERN, "305", "300"), thrown.getMessage());
    }

    @Test
    void calculateTest() {
        assertEquals(EXPECTED_EMPLOYEE_STATISTICS, calculator.calculate(EMPLOYEE_LIST));
    }
}

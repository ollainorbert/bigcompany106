package big.company.report;

import big.company.domain.Employee;
import big.company.domain.EmployeeStatistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeeStatisticsConsoleReportCreatorTest {
    private static final EmployeeStatistics EMPLOYEE_STATISTICS = new EmployeeStatistics(
        Map.of(
            new Employee(123, "Joe", "Doe", 60000, null), 238200.0,
            new Employee(305, "Brett", "Hardleaf", 34000, 300), 8000.0,
            new Employee(306, "Brett1", "Hardleaf1", 35000, 305), 8200.0,
            new Employee(307, "Brett2", "Hardleaf2", 36000, 306), 8400.0
        ),
        Map.of(new Employee(124, "Martin", "Chekov", 450000, 123), 375000.0),
        Map.of(
            new Employee(308, "Brett3", "Hardleaf3", 37000, 307), 5,
            new Employee(309, "Brett4", "Hardleaf4", 38000, 308), 6
        )
    );
    private static final String EXPECTED_REPORT =
        """
        Managers who earn less than they should:
        Employee: 123, with 238200.0 amount.
        Employee: 307, with 8400.0 amount.
        Employee: 306, with 8200.0 amount.
        Employee: 305, with 8000.0 amount.
        
        Managers who earn more than they should:
        Employee: 124, with 375000.0 amount.
        
        Employees with too long reporting line:
        Employee: 309, with 6 number of manager.
        Employee: 308, with 5 number of manager.
        """;

    private EmployeeStatisticsConsoleReportCreator reportCreator;

    @BeforeEach
    void setUp() {
        reportCreator = new EmployeeStatisticsConsoleReportCreator();
    }

    @Test
    void createReport() {
        assertEquals(EXPECTED_REPORT, reportCreator.createReport(EMPLOYEE_STATISTICS));
    }
}

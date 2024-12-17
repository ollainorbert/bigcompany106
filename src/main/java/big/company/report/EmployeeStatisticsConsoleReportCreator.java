package big.company.report;

import big.company.domain.Employee;
import big.company.domain.EmployeeStatistics;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

/**
 * Implementation for creating report by using the console logging.
 */
public class EmployeeStatisticsConsoleReportCreator implements EmployeeStatisticsReportCreator {
    private static final String MANAGER_WITH_DIFFERENT_EXPECTED_EARNINGS_PATTERN = "Employee: %s, with %s amount.\n";
    private static final String EMPLOYEE_WITH_TOO_LONG_REPORTING_LINE_PATTERN = "Employee: %s, with %s number of manager.\n";
    private static final String REPORT_MESSAGE_HEADER_FOR_WHO_EARN_LESS = "Managers who earn less than they should:\n";
    private static final String REPORT_MESSAGE_HEADER_FOR_WHO_EARN_MORE = "\nManagers who earn more than they should:\n";
    private static final String REPORT_MESSAGE_HEADER_FOR_WHO_HAS_TOO_LONG_REPORTING_LINE = "\nEmployees with too long reporting line:\n";

    @Override
    public String createReport(EmployeeStatistics employeeStatistics) {
        return new StringBuilder()
            .append(REPORT_MESSAGE_HEADER_FOR_WHO_EARN_LESS)
            .append(createManagersWithAmountReport(employeeStatistics.managersWithLessExpectedEarning()))
            .append(REPORT_MESSAGE_HEADER_FOR_WHO_EARN_MORE)
            .append(createManagersWithAmountReport(employeeStatistics.managersWithMoreExpectedEarning()))
            .append(REPORT_MESSAGE_HEADER_FOR_WHO_HAS_TOO_LONG_REPORTING_LINE)
            .append(createEmployeesWithTooLongReportingLineReport(employeeStatistics.employeesWithLongReportingLine()))
            .toString();
    }

    private String createManagersWithAmountReport(Map<Employee, BigDecimal> managersWithAmountEarning) {
        StringBuilder stringBuilder = new StringBuilder();
        managersWithAmountEarning.entrySet().stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .forEach(entry ->
                stringBuilder.append(String.format(MANAGER_WITH_DIFFERENT_EXPECTED_EARNINGS_PATTERN, entry.getKey().id(), entry.getValue()))
            );
        return stringBuilder.toString();
    }

    private String createEmployeesWithTooLongReportingLineReport(Map<Employee, Integer> employeesWithLongReportingLine) {
        StringBuilder stringBuilder = new StringBuilder();
        employeesWithLongReportingLine.entrySet().stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .forEach(entry ->
                stringBuilder.append(String.format(EMPLOYEE_WITH_TOO_LONG_REPORTING_LINE_PATTERN, entry.getKey().id(), entry.getValue()))
            );
        return stringBuilder.toString();
    }
}

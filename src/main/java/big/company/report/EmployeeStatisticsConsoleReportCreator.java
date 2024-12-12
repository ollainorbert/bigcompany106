package big.company.report;

import big.company.domain.Employee;
import big.company.domain.EmployeeStatistics;

import java.util.Collections;
import java.util.Map;

public class EmployeeStatisticsConsoleReportCreator implements EmployeeStatisticsReportCreator {
    private static final String MANAGER_WITH_DIFFERENT_EXPECTED_EARNINGS_PATTERN = "Employee: %s, with %s amount.\n";
    private static final String EMPLOYEE_WITH_TOO_LONG_REPORTING_LINE_PATTERN = "Employee: %s, with %s number of manager.\n";

    @Override
    public String createReport(EmployeeStatistics employeeStatistics) {
        StringBuilder stringBuilder = new StringBuilder()
            .append("Managers who earn less than they should:\n")
            .append(createManagersWithAmountReport(employeeStatistics.managersWithLessExpectedEarning()))
            .append("\nManagers who earn more than they should:\n")
            .append(createManagersWithAmountReport(employeeStatistics.managersWithMoreExpectedEarning()))
            .append("\nEmployees with too long reporting line:\n")
            .append(createEmployeesWithTooLongReportingLineReport(employeeStatistics.employeesWithLongReportingLine()));
        return stringBuilder.toString();
    }

    private String createManagersWithAmountReport(Map<Employee, Double> managersWithAmountEarning) {
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

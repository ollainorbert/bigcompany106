package big.company.report;

import big.company.domain.Employee;
import big.company.domain.EmployeeStatistics;

import java.util.Collections;
import java.util.Map;

public class EmployeeStatisticsConsoleReportCreator implements EmployeeStatisticsReportCreator {

    @Override
    public String createReport(EmployeeStatistics employeeStatistics) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
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
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).forEach(entry -> {
                stringBuilder.append("Employee: ").append(entry.getKey().id())
                    .append(", with ").append(entry.getValue()).append(" amount.\n");
        });
        /*managersWithAmountEarning.forEach((employee, amount) -> {
            stringBuilder.append("Employee: ").append(employee.id())
                .append(", with ").append(amount).append(" amount.\n");
        });*/
        return stringBuilder.toString();
    }

    private String createEmployeesWithTooLongReportingLineReport(Map<Employee, Integer> employeesWithLongReportingLine) {
        StringBuilder stringBuilder = new StringBuilder();
        employeesWithLongReportingLine.entrySet().stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).forEach(entry -> {
                stringBuilder.append("Employee: ").append(entry.getKey().id())
                    .append(", with ").append(entry.getValue()).append(" number of manager.\n");
        });
        /*employeesWithLongReportingLine.forEach((employee, numberOfManager) -> {

        });*/
        return stringBuilder.toString();
    }
}

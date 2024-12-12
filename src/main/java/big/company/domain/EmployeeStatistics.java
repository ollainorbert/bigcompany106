package big.company.domain;

import java.util.Map;

public record EmployeeStatistics(Map<Employee, Double> managersWithLessExpectedEarning,
                                 Map<Employee, Double> managersWithMoreExpectedEarning,
                                 Map<Employee, Integer> employeesWithLongReportingLine) {}

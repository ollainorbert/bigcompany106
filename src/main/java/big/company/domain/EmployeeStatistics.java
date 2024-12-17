package big.company.domain;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Holds the data of the calculated statistics.
 */
public record EmployeeStatistics(Map<Employee, BigDecimal> managersWithLessExpectedEarning,
                                 Map<Employee, BigDecimal> managersWithMoreExpectedEarning,
                                 Map<Employee, Integer> employeesWithLongReportingLine) {}

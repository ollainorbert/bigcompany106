package big.company.report;

import big.company.domain.EmployeeStatistics;

/**
 * Interface for creating report from the calculated statistics.
 */
public interface EmployeeStatisticsReportCreator {

    String createReport(EmployeeStatistics employeeStatistics);
}

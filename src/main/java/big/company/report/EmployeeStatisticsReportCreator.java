package big.company.report;

import big.company.domain.EmployeeStatistics;

public interface EmployeeStatisticsReportCreator {

    String createReport(EmployeeStatistics employeeStatistics);
}

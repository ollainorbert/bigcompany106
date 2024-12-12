package big.company;

import big.company.input.EmployeeFileNameArgumentExtractor;
import big.company.input.EmployeeInfoCsvFileReader;
import big.company.input.EmployeeInfoFileReader;
import big.company.report.EmployeeStatisticsConsoleReportCreator;
import big.company.report.EmployeeStatisticsReportCreator;
import big.company.statistics.EmployeeStatisticsCalculator;
import big.company.validation.EmployeeInputValidator;

/**
 * Start the program with adding the file name as argument,
 * Then see the desired report.
 */
public class Main {
    public static void main(String[] args) {
        EmployeeFileNameArgumentExtractor extractor = new EmployeeFileNameArgumentExtractor();
        var fileName = extractor.extractFileName(args);

        EmployeeInfoFileReader fileReader = new EmployeeInfoCsvFileReader();
        var employees = fileReader.read(fileName);

        EmployeeInputValidator validator = new EmployeeInputValidator();
        validator.validate(employees);

        EmployeeStatisticsCalculator calculator = new EmployeeStatisticsCalculator();
        var statisticsResult = calculator.calculate(employees);

        EmployeeStatisticsReportCreator reporter = new EmployeeStatisticsConsoleReportCreator();
        var report = reporter.createReport(statisticsResult);

        System.out.println(report);
    }
}

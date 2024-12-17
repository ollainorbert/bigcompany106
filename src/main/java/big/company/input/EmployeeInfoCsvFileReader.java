package big.company.input;

import big.company.domain.Employee;
import big.company.domain.IncorrectEmployeeDataException;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation for reading the employees' data from a csv file.
 */
public class EmployeeInfoCsvFileReader implements EmployeeInfoFileReader {
    private static final String FILE_NOT_EXIST_PATTERN = "File=%s, not exist or not a file!";
    private static final String ERROR_WHILE_READING_PATTERN = "Error while reading the file=%s!";
    private static final String FILE_CONTAINS_NO_EMPLOYEE_DATA = "The file contains no employee data!";
    private static final String INCORRECT_EMPLOYEE_DATA_LINE_PATTERN = "Incorrect employee data line=%s";
    private static final String ERROR_DUE_INCORRECT_DATA_TYPE_IN_THE_FILES_RAW_DATA = "Error during mapping the file's raw data, due incorrect data type!";
    private static final String LINE_DELIMITER = ",";
    private static final int EMPLOYEE_DATA_STARTS_FROM_NTH_LINE = 1;
    private static final int NUMBER_OF_ROW_PARTS_IN_CASE_OF_CEO = 5;
    private static final int NUMBER_OF_ROW_PARTS_IN_CASE_OF_NOT_CEO = 4;
    private static final int ROW_PART_FOR_ID_INDEX = 0;
    private static final int ROW_PART_FOR_FIRST_NAME_INDEX = 1;
    private static final int ROW_PART_FOR_LAST_NAME_INDEX = 2;
    private static final int ROW_PART_FOR_SALARY_INDEX = 3;
    private static final int ROW_PART_FOR_MANAGER_ID_INDEX = 4;

    @Override
    public List<Employee> read(String fileName) {
        validateFile(fileName);
        var linesRead = readFile(fileName);
        return mapEmployees(linesRead);
    }

    private void validateFile(String fileName) {
        File file = new File(fileName);
        if (!file.isFile()) {
            throw new IllegalArgumentException(String.format(FILE_NOT_EXIST_PATTERN, fileName));
        }
    }

    private List<String> readFile(String fileName) {
        try {
            Path path = Paths.get(fileName);
            return Files.readAllLines(path).stream().toList();
        } catch (IOException exception) {
            throw new IllegalArgumentException(String.format(ERROR_WHILE_READING_PATTERN, fileName), exception);
        }
    }

    private List<Employee> mapEmployees(List<String> rawLines) {
        List<String> lines = rawLines.stream().toList();

        if (lines.size() <= EMPLOYEE_DATA_STARTS_FROM_NTH_LINE) {
            throw new IncorrectEmployeeDataException(FILE_CONTAINS_NO_EMPLOYEE_DATA);
        }

        return rawLines.stream().skip(EMPLOYEE_DATA_STARTS_FROM_NTH_LINE).map(line -> {
            var parts = line.split(LINE_DELIMITER);
            if (parts.length < NUMBER_OF_ROW_PARTS_IN_CASE_OF_NOT_CEO || NUMBER_OF_ROW_PARTS_IN_CASE_OF_CEO < parts.length) {
                throw new IncorrectEmployeeDataException(String.format(INCORRECT_EMPLOYEE_DATA_LINE_PATTERN, Arrays.stream(parts).toList()));
            }
            try {
                return new Employee(
                    Integer.parseInt(parts[ROW_PART_FOR_ID_INDEX]),
                    parts[ROW_PART_FOR_FIRST_NAME_INDEX],
                    parts[ROW_PART_FOR_LAST_NAME_INDEX],
                    new BigDecimal(parts[ROW_PART_FOR_SALARY_INDEX]),
                    retrieveManagerId(parts)
                );
            } catch (Exception throwable) {
                throw new IncorrectEmployeeDataException(ERROR_DUE_INCORRECT_DATA_TYPE_IN_THE_FILES_RAW_DATA, throwable);
            }
        }).toList();
    }

    private Integer retrieveManagerId(String[] rowParts) {
        return isTheRowContainsManagerId(rowParts.length) ? null : Integer.parseInt(rowParts[ROW_PART_FOR_MANAGER_ID_INDEX]);
    }

    private boolean isTheRowContainsManagerId(int rowLength) {
        return rowLength == ROW_PART_FOR_MANAGER_ID_INDEX;
    }
}

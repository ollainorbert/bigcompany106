package big.company.input;

import big.company.domain.Employee;
import big.company.domain.IncorrectEmployeeDataException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class EmployeeInfoCsvFileReader implements EmployeeInfoFileReader {
    private static final String FILE_NOT_EXIST_PATTERN = "File=%s, not exist or not a file!";
    private static final String ERROR_WHILE_READING_PATTERN = "Error while reading the file=%s!";
    private static final String FILE_CONTAINS_NO_EMPLOYEE_DATA = "The file contains no employee data!";
    private static final String INCORRECT_EMPLOYEE_DATA_LINE_PATTERN = "Incorrect employee data line=%s";
    private static final String ERROR_DUE_INCORRECT_DATA_TYPE_IN_THE_FILES_RAW_DATA = "Error during mapping the file's raw data, due incorrect data type!";
    private static final String LINE_DELIMITER = ",";

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
            throw new RuntimeException(String.format(ERROR_WHILE_READING_PATTERN, fileName), exception);
        }
    }

    private List<Employee> mapEmployees(List<String> rawLines) {
        List<String> lines = rawLines.stream().toList();

        if (lines.size() <= 1) {
            throw new IncorrectEmployeeDataException(FILE_CONTAINS_NO_EMPLOYEE_DATA);
        }

        return rawLines.stream().skip(1).map(line -> {
            var parts = line.split(LINE_DELIMITER);
            if (parts.length < 4 || 5 < parts.length) {
                throw new IncorrectEmployeeDataException(String.format(INCORRECT_EMPLOYEE_DATA_LINE_PATTERN, Arrays.stream(parts).toList()));
            }
            try {
                return new Employee(
                    Integer.parseInt(parts[0]),
                    parts[1],
                    parts[2],
                    Integer.parseInt(parts[3]),
                    parts.length == 4 ? null : Integer.parseInt(parts[4])
                );
            } catch (Throwable throwable) {
                throw new IncorrectEmployeeDataException(ERROR_DUE_INCORRECT_DATA_TYPE_IN_THE_FILES_RAW_DATA, throwable);
            }
        }).toList();
    }
}

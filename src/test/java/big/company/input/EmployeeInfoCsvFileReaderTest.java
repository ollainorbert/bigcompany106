package big.company.input;

import big.company.domain.Employee;
import big.company.domain.IncorrectEmployeeDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmployeeInfoCsvFileReaderTest {
    private static final String FILE_NOT_EXIST_PATTERN = "File=%s, not exist or not a file!";
    private static final String FILE_CONTAINS_NO_EMPLOYEE_DATA = "The file contains no employee data!";
    private static final String INCORRECT_EMPLOYEE_DATA_LINE_PATTERN = "Incorrect employee data line=%s";
    private static final String ERROR_DUE_INCORRECT_DATA_TYPE_IN_THE_FILES_RAW_DATA = "Error during mapping the file's raw data, due incorrect data type!";
    private static final List<Employee> EXPECTED_EMPLOYEES = List.of(
        new Employee(123, "Joe", "Doe", 60000, null),
        new Employee(124, "Martin", "Chekov", 45000, 123),
        new Employee(125, "Bob", "Ronstad", 47000, 123),
        new Employee(300, "Alice", "Hasacat", 50000, 124),
        new Employee(305, "Brett", "Hardleaf", 34000, 300)
    );

    private EmployeeInfoCsvFileReader reader;

    @BeforeEach
    void setUp() {
        reader = new EmployeeInfoCsvFileReader();
    }

    @Test
    void readErrorDueFileNotExistTest() {
        String notExistFileName = "src/test/resources/test111111.csv";

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> reader.read(notExistFileName)
        );

        assertEquals(String.format(FILE_NOT_EXIST_PATTERN, notExistFileName), thrown.getMessage());
    }

    @Test
    void readErrorDueEmptyFileTest() {
        String fileName = "src/test/resources/empty.csv";

        IncorrectEmployeeDataException thrown = assertThrows(
            IncorrectEmployeeDataException.class,
            () -> reader.read(fileName)
        );

        assertEquals(FILE_CONTAINS_NO_EMPLOYEE_DATA, thrown.getMessage());
    }

    @Test
    void readErrorDueIncorrectLinesAsLessTest() {
        String fileName = "src/test/resources/incorrectLineWithLess.csv";

        IncorrectEmployeeDataException thrown = assertThrows(
            IncorrectEmployeeDataException.class,
            () -> reader.read(fileName)
        );

        assertEquals(String.format(INCORRECT_EMPLOYEE_DATA_LINE_PATTERN, "[123, Joe, Doe]"), thrown.getMessage());
    }

    @Test
    void readErrorDueIncorrectLineAsMoreTest() {
        String fileName = "src/test/resources/incorrectLineWithMore.csv";

        IncorrectEmployeeDataException thrown = assertThrows(
            IncorrectEmployeeDataException.class,
            () -> reader.read(fileName)
        );

        assertEquals(String.format(INCORRECT_EMPLOYEE_DATA_LINE_PATTERN, "[123, Joe, Doe, 60000, 1, 1]"), thrown.getMessage());
    }

    @Test
    void readErrorDueIncorrectDataTypeDueMappingTest() {
        String fileName = "src/test/resources/incorrectLineWithDifferentTypeOfData.csv";

        IncorrectEmployeeDataException thrown = assertThrows(
            IncorrectEmployeeDataException.class,
            () -> reader.read(fileName)
        );

        assertEquals(ERROR_DUE_INCORRECT_DATA_TYPE_IN_THE_FILES_RAW_DATA, thrown.getMessage());
        assertEquals("java.lang.NumberFormatException: For input string: \"Joe\"", thrown.getCause().toString());
    }

    @Test
    void readTest() {
        var res = reader.read("src/test/resources/test.csv");
        assertEquals(EXPECTED_EMPLOYEES, res);
    }
}

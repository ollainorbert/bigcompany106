package big.company.input;

import big.company.domain.Employee;

import java.util.List;

/**
 * Interface for reading the employees' data from a file.
 */
public interface EmployeeInfoFileReader {

    List<Employee> read(String fileName);
}

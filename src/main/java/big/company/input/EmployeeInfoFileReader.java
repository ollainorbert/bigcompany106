package big.company.input;

import big.company.domain.Employee;

import java.util.List;

public interface EmployeeInfoFileReader {

    List<Employee> read(String fileName);
}

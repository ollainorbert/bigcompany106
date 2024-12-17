package big.company.domain;

import java.util.List;

/**
 * This class holds a reference to the CEO (to help certain logics), the list of employees, including the CEO!
 */
public record Employment(Employee Ceo, List<Employee> employees) {}

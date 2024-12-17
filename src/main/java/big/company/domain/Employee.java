package big.company.domain;

import java.math.BigDecimal;

/**
 * Holds the data of an employee.
 */
public record Employee(int id, String firstName, String lastName, BigDecimal salary, Integer managerId) {}

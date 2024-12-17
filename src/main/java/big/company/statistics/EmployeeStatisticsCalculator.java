package big.company.statistics;

import big.company.domain.Employee;
import big.company.domain.EmployeeStatistics;
import big.company.domain.Employment;
import big.company.domain.IncorrectEmployeeDataException;
import big.company.domain.ManagersWithEarningDifferenceMapContainer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Calculate the required statistics, based on the employees' data.
 */
public class EmployeeStatisticsCalculator {
    private static final String MANAGER_NOT_EXIST_PATTERN = "There is a managerId=%s with no employee that has that id!";
    private static final String EMPLOYEE_WITH_NON_EXISTENT_MANAGER_OR_INCORRECT_REFERENCE_PATTERN = "An employee employeeId=%s has a managerId=%s, which doesn't exist or references backward or in a circle!";
    private static final BigDecimal MIN_SALARY_PERCENTAGE = BigDecimal.valueOf(1.2);
    private static final BigDecimal MAX_SALARY_PERCENTAGE = BigDecimal.valueOf(1.5);
    private static final int MAX_NUMBER_OF_FEASIBLE_REPORTING_LINE = 4;

    public EmployeeStatistics calculate(Employment employment) {
        Map<Integer, Employee> idWithEmployeeMap = buildIdWithEmployeeMap(employment.employees());
        Map<Integer, List<Employee>> managerIdWithSubordinatesMap = buildManagerIdWithSubordinatesMap(employment.employees());
        Map<Integer, BigDecimal> averageEarningsForManagersTeam = calculateAverageEarningsForManagersTeam(managerIdWithSubordinatesMap);

        var managersWithEarningDifferenceMapContainer = calculateManagersWithEarningDifferences(averageEarningsForManagersTeam, idWithEmployeeMap);
        return new EmployeeStatistics(
            managersWithEarningDifferenceMapContainer.managersWithLessExpectedEarning(),
            managersWithEarningDifferenceMapContainer.managersWithMoreExpectedEarning(),
            calculateEmployeesWithLongReportingLine(employment.Ceo().id(), idWithEmployeeMap)
        );
    }

    private Map<Integer, Employee> buildIdWithEmployeeMap(List<Employee> employees) {
        return employees.stream().collect(Collectors.toMap(Employee::id, employee -> employee));
    }

    private Map<Integer, List<Employee>> buildManagerIdWithSubordinatesMap(List<Employee> employees) {
        Map<Integer, List<Employee>> map = new HashMap<>();
        employees.forEach(employee -> {
            var managerId = employee.managerId();
            if (managerId != null) {
                if (map.containsKey(managerId)) {
                    map.get(managerId).add(employee);
                } else {
                    var list = new ArrayList<Employee>();
                    list.add(employee);
                    map.put(managerId, list);
                }
            }
        });

        return map;
    }

    private Map<Integer, BigDecimal> calculateAverageEarningsForManagersTeam(Map<Integer, List<Employee>> managerIdWithSubordinatesMap) {
        return managerIdWithSubordinatesMap.entrySet().stream().collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> {
                BigDecimal sum = entry.getValue().stream().map(Employee::salary).reduce(BigDecimal.ZERO, BigDecimal::add);
                return sum.divide(BigDecimal.valueOf(entry.getValue().size()));
            }
        ));
    }

    private ManagersWithEarningDifferenceMapContainer calculateManagersWithEarningDifferences(Map<Integer, BigDecimal> averageEarningsForManagersTeam, Map<Integer, Employee> idWithEmployeeMap) {
        Map<Employee, BigDecimal> managersWithLess = new HashMap<>();
        Map<Employee, BigDecimal> managersWithMore = new HashMap<>();
        averageEarningsForManagersTeam.forEach((managerId, teamsAverageEarning) -> {
            if (idWithEmployeeMap.containsKey(managerId)) {
                Employee manager = idWithEmployeeMap.get(managerId);
                BigDecimal managerEarning = manager.salary();

                BigDecimal managerShouldEarnAtLeast = teamsAverageEarning.multiply(MIN_SALARY_PERCENTAGE);
                if (managerEarning.compareTo(managerShouldEarnAtLeast) < 0) {
                    managersWithLess.put(manager, managerShouldEarnAtLeast.subtract(managerEarning));
                }
                BigDecimal managerShouldEarnAtLast = teamsAverageEarning.multiply(MAX_SALARY_PERCENTAGE);
                if (managerEarning.compareTo(managerShouldEarnAtLast) > 0) {
                    managersWithMore.put(manager, managerEarning.subtract(managerShouldEarnAtLast));
                }
            } else {
                throw new IncorrectEmployeeDataException(String.format(MANAGER_NOT_EXIST_PATTERN, managerId));
            }
        });
        return new ManagersWithEarningDifferenceMapContainer(managersWithLess, managersWithMore);
    }

    private Map<Employee, Integer> calculateEmployeesWithLongReportingLine(int ceoId, Map<Integer, Employee> idWithEmployeeMap) {
        Map<Employee, Integer> res = new HashMap<>();
        var mapWithoutCeo = new HashMap<>(idWithEmployeeMap);
        mapWithoutCeo.remove(ceoId);
        mapWithoutCeo.forEach((id, employee) -> {
            Map<Integer, Employee> copyIdWithEmployeeMapForAvoidCirculating = new HashMap<>(idWithEmployeeMap);
            int numberOfManagerInChain = lookForNumberOfManagerWithDecreasingEmployeeMap(employee, copyIdWithEmployeeMapForAvoidCirculating, 0);
            if (numberOfManagerInChain > MAX_NUMBER_OF_FEASIBLE_REPORTING_LINE) {
                res.put(employee, numberOfManagerInChain);
            }
        });
        return res;
    }

    private int lookForNumberOfManagerWithDecreasingEmployeeMap(final Employee employee, final Map<Integer, Employee> mutableIdWithEmployeeMap, final int counter) {
        int managerId = employee.managerId();
        if (mutableIdWithEmployeeMap.containsKey(managerId)) {
            var manager = mutableIdWithEmployeeMap.get(managerId);
            if (manager.managerId() == null) {
                return counter;
            } else {
                mutableIdWithEmployeeMap.remove(managerId);
                return lookForNumberOfManagerWithDecreasingEmployeeMap(manager, mutableIdWithEmployeeMap, counter + 1);
            }
        } else {
            throw new IncorrectEmployeeDataException(String.format(EMPLOYEE_WITH_NON_EXISTENT_MANAGER_OR_INCORRECT_REFERENCE_PATTERN, employee.id(), managerId));
        }
    }
}

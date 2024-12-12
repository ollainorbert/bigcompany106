package big.company.statistics;

import big.company.domain.Employee;
import big.company.domain.EmployeeStatistics;
import big.company.domain.IncorrectEmployeeDataException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmployeeStatisticsCalculator {
    private static final String MANAGER_NOT_EXIST_PATTERN = "There is a managerId=%s with no employee that has that id!";
    private static final String EMPLOYEE_WITH_NON_EXISTENT_MANAGER_OR_INCORRECT_REFERENCE_PATTERN = "An employee employeeId=%s has a managerId=%s, which doesn't exist or references backward or in a circle!";
    private static final double MIN_SALARY_PERCENTAGE = 1.2;
    private static final double MAX_SALARY_PERCENTAGE = 1.5;
    private static final int MAX_NUMBER_OF_FEASIBLE_REPORTING_LINE = 4;

    public EmployeeStatistics calculate(List<Employee> employees) {
        Map<Integer, Employee> idWithEmployeeMap = buildIdWithEmployeeMap(employees);
        Map<Integer, List<Employee>> managerIdWithSubordinatesMap = buildManagerIdWithSubordinatesMap(employees);
        Map<Integer, Double> averageEarningsForManagersTeam = calculateAverageEarningsForManagersTeam(managerIdWithSubordinatesMap);

        // When calculating the earning related data, I'll assume the CEO is a Manager as well
        return new EmployeeStatistics(
            calculateManagersWithLessExpectedEarning(averageEarningsForManagersTeam, idWithEmployeeMap),
            calculateManagersWithMoreExpectedEarning(averageEarningsForManagersTeam, idWithEmployeeMap),
            calculateEmployeesWithLongReportingLine(idWithEmployeeMap)
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

    private Map<Integer, Double> calculateAverageEarningsForManagersTeam(Map<Integer, List<Employee>> managerIdWithSubordinatesMap) {
        return managerIdWithSubordinatesMap.entrySet().stream().collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> {
                int sum = entry.getValue().stream().map(Employee::salary).mapToInt(Integer::intValue).sum();
                return (double) sum / entry.getValue().size();
            }
        ));
    }

    // TODO these two method probably could get refactored
    private Map<Employee, Double> calculateManagersWithLessExpectedEarning(Map<Integer, Double> averageEarningsForManagersTeam, Map<Integer, Employee> idWithEmployeeMap) {
        Map<Employee, Double> res = new HashMap<>();
        averageEarningsForManagersTeam.forEach((managerId, teamsAverageEarning) -> {
            if (idWithEmployeeMap.containsKey(managerId)) {
                Employee manager = idWithEmployeeMap.get(managerId);
                int managerEarning = manager.salary();
                double managerShouldEarnAtLeast = teamsAverageEarning * MIN_SALARY_PERCENTAGE;
                if (managerEarning < managerShouldEarnAtLeast) {
                    res.put(manager, managerShouldEarnAtLeast - managerEarning);
                }
            } else {
                // TODO maybe this validation can go somewhere else
                throw new IncorrectEmployeeDataException(String.format(MANAGER_NOT_EXIST_PATTERN, managerId));
            }
        });
        return res;
    }

    private Map<Employee, Double> calculateManagersWithMoreExpectedEarning(Map<Integer, Double> averageEarningsForManagersTeam, Map<Integer, Employee> idWithEmployeeMap) {
        Map<Employee, Double> res = new HashMap<>();
        averageEarningsForManagersTeam.forEach((managerId, teamsAverageEarning) -> {
            if (idWithEmployeeMap.containsKey(managerId)) {
                Employee manager = idWithEmployeeMap.get(managerId);
                int managerEarning = manager.salary();
                double managerShouldEarnAtLast = teamsAverageEarning * MAX_SALARY_PERCENTAGE;
                if (managerEarning > managerShouldEarnAtLast) {
                    res.put(manager, managerEarning - managerShouldEarnAtLast);
                }
            } else {
                // TODO maybe this validation can go somewhere else
                throw new IncorrectEmployeeDataException(String.format(MANAGER_NOT_EXIST_PATTERN, managerId));
            }
        });
        return res;
    }

    private Map<Employee, Integer> calculateEmployeesWithLongReportingLine(Map<Integer, Employee> idWithEmployeeMap) {
        Map<Employee, Integer> res = new HashMap<>();
        int ceoId = retrieveCeoId(idWithEmployeeMap);
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

    private int retrieveCeoId(Map<Integer, Employee> idWithEmployeeMap) {
        return idWithEmployeeMap.entrySet().stream().filter(entry -> entry.getValue().managerId() == null).findFirst().get().getKey();
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

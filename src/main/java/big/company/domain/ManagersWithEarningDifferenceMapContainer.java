package big.company.domain;

import java.util.Map;

public record ManagersWithEarningDifferenceMapContainer(Map<Employee, Double> managersWithLessExpectedEarning,
                                                        Map<Employee, Double> managersWithMoreExpectedEarning) {}

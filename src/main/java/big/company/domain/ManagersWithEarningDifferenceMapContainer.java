package big.company.domain;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Holds data for managers earning.
 */
public record ManagersWithEarningDifferenceMapContainer(Map<Employee, BigDecimal> managersWithLessExpectedEarning,
                                                        Map<Employee, BigDecimal> managersWithMoreExpectedEarning) {}

package org.example;

import java.util.Map;

public class ContentComparer {
    public static ComparisonResult compareContent(Map<String, String> expectedContent, Map<String, String> actualContent) {
        ComparisonResult result = new ComparisonResult();
        int passCount = 0;
        int failCount = 0;
        int notInLocalization = 0;

        for (String key : expectedContent.keySet()) {
            String expectedValue = expectedContent.get(key);
            String actualValue = actualContent.get(key);

            if (actualValue != null) {
                if (expectedValue.equals(actualValue)) {
                    passCount++;
                } else {
                    failCount++;
                    result.addFailure(key, expectedValue, actualValue);
                }
            } else {
                notInLocalization++;
                result.addNotInLocalization(key);
            }
        }

        result.setPassCount(passCount);
        result.setFailCount(failCount);
        result.setNotInLocalization(notInLocalization);

        return result;
    }
}

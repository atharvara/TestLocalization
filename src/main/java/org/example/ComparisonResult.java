package org.example;

import java.util.ArrayList;
import java.util.List;

public class ComparisonResult {
    private int passCount;
    private int failCount;
    private int notInLocalization;
    private List<String> failures;
    private List<String> notInLocalizationList;

    public ComparisonResult() {
        passCount = 0;
        failCount = 0;
        notInLocalization = 0;
        failures = new ArrayList<>();
        notInLocalizationList = new ArrayList<>();
    }

    public int getPassCount() {
        return passCount;
    }

    public void setPassCount(int passCount) {
        this.passCount = passCount;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public int getNotInLocalization() {
        return notInLocalization;
    }

    public void setNotInLocalization(int notInLocalization) {
        this.notInLocalization = notInLocalization;
    }

    public void addFailure(String key, String expectedValue, String actualValue) {
        failures.add("Key: " + key + ", Expected: " + expectedValue + ", Actual: " + actualValue);
    }

    public void addNotInLocalization(String key) {
        notInLocalizationList.add("Key: " + key);
    }

    public void printSummary() {
        System.out.println("####################  Result Summary ####################");
        System.out.println("Total Pass: " + passCount);
        System.out.println("Total Fail: " + failCount);
        System.out.println("Not in Localization: " + notInLocalization);

        if (!failures.isEmpty()) {
            System.out.println("\nFailures:");
            for (String failure : failures) {
                System.out.println(failure);
            }
        }

        if (!notInLocalizationList.isEmpty()) {
            System.out.println("\nKeys Not in Localization:");
            for (String key : notInLocalizationList) {
                System.out.println(key);
            }
        }
    }
}
package com.github.bogdanlivadariu.reporting.rspec.builder;

import java.util.List;

import com.github.bogdanlivadariu.reporting.rspec.xml.models.TestSuiteModel;

public class AllRSpecJUnitReports {
    private List<TestSuiteModel> allTestSuites;

    private int suitesCount;

    private int totalTests;

    private int totalFailures;

    private int totalErrors;

    private int totalSkipped;

    private String pageTitle;

    private Double totalTime = 0.0;

    public AllRSpecJUnitReports(String pageTitle, List<TestSuiteModel> allTestSuites) {
        this.pageTitle = pageTitle;
        this.allTestSuites = allTestSuites;
        this.suitesCount = allTestSuites.size();

        for (TestSuiteModel ts : allTestSuites) {
            totalTests += Integer.parseInt(ts.getTests());
            totalFailures += Integer.parseInt(ts.getFailures());
            totalErrors += Integer.parseInt(ts.getErrors());
            totalSkipped += Integer.parseInt(ts.getSkipped());
            totalTime += Double.parseDouble(ts.getTime());
        }

    }

    public List<TestSuiteModel> getAllTestSuites() {
        return allTestSuites;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public String getTotalTime() {
        return totalTime.toString();
    }

    public int getSuitesCount() {
        return suitesCount;
    }

    public int getTotalTests() {
        return totalTests;
    }

    public int getTotalFailures() {
        return totalFailures;
    }

    public int getTotalErrors() {
        return totalErrors;
    }

    public int getTotalSkipped() {
        return totalSkipped;
    }
}

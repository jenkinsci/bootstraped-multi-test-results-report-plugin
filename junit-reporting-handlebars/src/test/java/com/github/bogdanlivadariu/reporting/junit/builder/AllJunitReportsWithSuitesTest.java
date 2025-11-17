package com.github.bogdanlivadariu.reporting.junit.builder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AllJunitReportsWithSuitesTest {

    private final String reportPath = this.getClass().getClassLoader().getResource("valid-report-2.xml").getPath();
    private List<String> xmlReports;
    private JUnitReportBuilder builder;

    private AllJUnitReports reports;

    @BeforeEach
    void setUp() throws IOException {
        xmlReports = new ArrayList<>();
        xmlReports.add(reportPath);
        builder = new JUnitReportBuilder(xmlReports, "out");
        reports = new AllJUnitReports("title", builder.getProcessedTestSuites());
    }

    @Test
    void restSuitesSizeTest() {
        assertEquals(2, reports.getAllTestSuites().size(), "reports count is not right");
        assertEquals(2, reports.getSuitesCount());
        assertEquals(0, reports.getTotalErrors());
    }

    @Test
    void pageTitleTest() {
        assertEquals("title", reports.getPageTitle());
    }

    @Test
    void totalErrorsTest() {
        assertEquals(2, reports.getSuitesCount());
    }

    @Test
    void totalFailuresTest() {
        assertEquals(2, reports.getTotalFailures());
    }

    @Test
    void totalSkippedTest() {
        assertEquals(0, reports.getTotalSkipped());
    }

    @Test
    void totalTestsTest() {
        assertEquals(8, reports.getTotalTests());
    }

    @Test
    void totalTimeTest() {
        assertEquals("8.1", reports.getTotalTime());
    }
}

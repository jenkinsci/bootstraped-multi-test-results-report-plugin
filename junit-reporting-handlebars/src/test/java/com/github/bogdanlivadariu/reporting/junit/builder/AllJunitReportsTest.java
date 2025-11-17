package com.github.bogdanlivadariu.reporting.junit.builder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AllJunitReportsTest {

    private final String reportPath = this.getClass().getClassLoader().getResource("valid-report-1.xml").getPath();
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
        assertEquals(1, reports.getAllTestSuites().size(), "reports count is not right");
        assertEquals(1, reports.getSuitesCount());
        assertEquals(0, reports.getTotalErrors());
    }

    @Test
    void pageTitleTest() {
        assertEquals("title", reports.getPageTitle());
    }

    @Test
    void totalErrorsTest() {
        assertEquals(0, reports.getTotalErrors());
    }

    @Test
    void totalFailuresTest() {
        assertEquals(7, reports.getTotalFailures());
    }

    @Test
    void totalSkippedTest() {
        assertEquals(0, reports.getTotalSkipped());
    }

    @Test
    void totalTestsTest() {
        assertEquals(13, reports.getTotalTests());
    }

    @Test
    void totalTimeTest() {
        assertEquals("0.813", reports.getTotalTime());
    }
}

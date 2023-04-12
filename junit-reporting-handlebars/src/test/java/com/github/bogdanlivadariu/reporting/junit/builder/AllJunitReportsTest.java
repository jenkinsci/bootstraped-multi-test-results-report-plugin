package com.github.bogdanlivadariu.reporting.junit.builder;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AllJunitReportsTest {
    private List<String> xmlReports;

    private String reportPath = this.getClass().getClassLoader().getResource("valid-report-1.xml").getPath();

    private JUnitReportBuilder builder;

    private AllJUnitReports reports;

    @BeforeEach
    public void setUp() throws FileNotFoundException, JAXBException {
        xmlReports = new ArrayList<>();
        xmlReports.add(reportPath);
        builder = new JUnitReportBuilder(xmlReports, "out");
        reports = new AllJUnitReports("title", builder.getProcessedTestSuites());
    }

    @Test
    public void restSuitesSizeTest() throws FileNotFoundException, JAXBException {
        assertEquals(reports.getAllTestSuites().size(), 1, "reports count is not right");
        assertEquals(reports.getSuitesCount(), 1);
        assertEquals(reports.getTotalErrors(), 0);
    }

    @Test
    public void pageTitleTest() {
        assertEquals(reports.getPageTitle(), "title");
    }

    @Test
    public void totalErrorsTest() {
        assertEquals(reports.getSuitesCount(), 1);
    }

    @Test
    public void totalFailuresTest() {
        assertEquals(reports.getTotalFailures(), 7);
    }

    @Test
    public void totalSkippedTest() {
        assertEquals(reports.getTotalSkipped(), 0);
    }

    @Test
    public void totalTestsTest() {
        assertEquals(reports.getTotalTests(), 13);
    }

    @Test
    public void totalTimeTest() {
        assertEquals(reports.getTotalTime(), "0.813");
    }
}

package com.github.bogdanlivadariu.reporting.testng;

import com.github.bogdanlivadariu.reporting.testng.builder.AllTestNgReports;
import com.github.bogdanlivadariu.reporting.testng.builder.TestNgReportBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AllTestNgReportsTest {

    private final String reportPath = this.getClass().getClassLoader().getResource("testng-results.xml").getPath();
    private List<String> xmlReports;
    private TestNgReportBuilder builder;

    private AllTestNgReports reports;

    @BeforeEach
    void setUp() throws Exception {
        xmlReports = new ArrayList<>();
        xmlReports.add(reportPath);
        builder = new TestNgReportBuilder(xmlReports, "out");
        reports = new AllTestNgReports("title", builder.getProcessedTestNgReports());

        builder.writeReportsOnDisk();
    }

    @Test
    void ceva() {
        System.out.println(reports);
    }

    @Test
    void pageTitleTest() {
        assertEquals("title", reports.getPageTitle());
    }

    @Test
    void totalFailuresTest() {
        assertEquals(2, reports.getTotalFailed());
    }

    @Test
    void totalSkippedTest() {
        assertEquals(1, reports.getTotalSkipped());
    }

    @Test
    void totalTestsTest() {
        assertEquals(41, reports.getTotalTests());
    }

    @Test
    void totalTimeTest() {
        assertEquals(280060L, reports.getTotalTime());
    }
}

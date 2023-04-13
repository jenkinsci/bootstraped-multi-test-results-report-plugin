package com.github.bogdanlivadariu.reporting.testng;

import com.github.bogdanlivadariu.reporting.testng.builder.AllTestNgReports;
import com.github.bogdanlivadariu.reporting.testng.builder.TestNgReportBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AllTestNgReportsTest {

    private final String reportPath = this.getClass().getClassLoader().getResource("testng-results.xml").getPath();
    private List<String> xmlReports;
    private TestNgReportBuilder builder;

    private AllTestNgReports reports;

    @BeforeEach
    public void setUp() throws IOException, XMLStreamException, NoSuchAlgorithmException {
        xmlReports = new ArrayList<>();
        xmlReports.add(reportPath);
        builder = new TestNgReportBuilder(xmlReports, "out");
        reports = new AllTestNgReports("title", builder.getProcessedTestNgReports());

        builder.writeReportsOnDisk();
    }

    @Test
    public void ceva() {
        System.out.println(reports);
    }

    @Test
    public void pageTitleTest() {
        assertEquals(reports.getPageTitle(), "title");
    }

    @Test
    public void totalFailuresTest() {
        assertEquals(reports.getTotalFailed(), 2);
    }

    @Test
    public void totalSkippedTest() {
        assertEquals(reports.getTotalSkipped(), 1);
    }

    @Test
    public void totalTestsTest() {
        assertEquals(reports.getTotalTests(), 41);
    }

    @Test
    public void totalTimeTest() {
        assertEquals(reports.getTotalTime(), 280060L);
    }
}

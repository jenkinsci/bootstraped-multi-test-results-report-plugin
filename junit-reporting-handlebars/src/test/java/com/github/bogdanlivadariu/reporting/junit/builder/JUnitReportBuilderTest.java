package com.github.bogdanlivadariu.reporting.junit.builder;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test for simple App.
 */
class JUnitReportBuilderTest {

    @Test
    void npeTest() {
        List<String> xmlReports = null;
        assertThrows(NullPointerException.class, () -> new JUnitReportBuilder(xmlReports, "out"));
    }

    @Test
    void processedReportsTest() throws IOException {
        List<String> xmlReports = new ArrayList<>();
        String report = this.getClass().getClassLoader().getResource("valid-report-1.xml").getPath();
        xmlReports.add(report);
        JUnitReportBuilder builder = new JUnitReportBuilder(xmlReports, "out");
        assertEquals(1, builder.getProcessedTestSuites().size(), "reports count is not right");
        xmlReports.clear();
        builder = new JUnitReportBuilder(xmlReports, "out");
        assertEquals(0, builder.getProcessedTestSuites().size(), "reports count is not right");
    }

    @Test
    void processedReportsSuitesTest() throws IOException {
        List<String> xmlReports = new ArrayList<>();
        String report = this.getClass().getClassLoader().getResource("valid-report-2.xml").getPath();
        xmlReports.add(report);
        JUnitReportBuilder builder = new JUnitReportBuilder(xmlReports, "out");
        assertEquals(2, builder.getProcessedTestSuites().size(), "reports count is not right");
        xmlReports.clear();
        builder = new JUnitReportBuilder(xmlReports, "out");
        assertEquals(0, builder.getProcessedTestSuites().size(), "reports count is not right");
    }
}

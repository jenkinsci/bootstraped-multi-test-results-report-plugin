package com.github.bogdanlivadariu.reporting.junit.builder;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test for simple App.
 */
public class JUnitReportBuilderTest {

    @Test
    public void npeTest() throws Exception {
        assertThrows(NullPointerException.class, () -> {
            List<String> xmlReports = null;
            new JUnitReportBuilder(xmlReports, "out");
        });
    }

    @Test
    public void processedReportsTest() throws FileNotFoundException, JAXBException {
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
    public void processedReportsSuitesTest() throws FileNotFoundException, JAXBException {
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

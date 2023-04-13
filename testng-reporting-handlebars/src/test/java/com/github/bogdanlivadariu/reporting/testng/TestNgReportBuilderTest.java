package com.github.bogdanlivadariu.reporting.testng;

import com.github.bogdanlivadariu.reporting.testng.builder.TestNgReportBuilder;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestNgReportBuilderTest {
    @Test
    public void npeTest() {
        assertThrows(NullPointerException.class, () -> {
            List<String> xmlReports = null;
            new TestNgReportBuilder(xmlReports, "out");
        });
    }

    @Test
    public void processedReportsTest() throws IOException, XMLStreamException {
        List<String> xmlReports = new ArrayList<>();
        String report = this.getClass().getClassLoader().getResource("testng-results.xml").getPath();
        xmlReports.add(report);


        TestNgReportBuilder builder = new TestNgReportBuilder(xmlReports, "out");

        System.out.println(builder);

        assertEquals(1, builder.getProcessedTestNgReports().size(), "reports count is not correct");

        xmlReports.clear();
        builder = new TestNgReportBuilder(xmlReports, "out");

        assertEquals(0, builder.getProcessedTestNgReports().size(), "reports count is not correct");
    }
}

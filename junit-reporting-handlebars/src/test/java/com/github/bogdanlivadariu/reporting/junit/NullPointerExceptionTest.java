package com.github.bogdanlivadariu.reporting.junit;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.github.bogdanlivadariu.reporting.junit.builder.JUnitReportBuilder;

/**
 * Unit test for simple App.
 */
public class NullPointerExceptionTest {

    @Test(expected = NullPointerException.class)
    public void npeTest() throws IOException, JAXBException {
    	List<String> xmlReports = null;
		JUnitReportBuilder repo = new JUnitReportBuilder(xmlReports, "out");
    }
}

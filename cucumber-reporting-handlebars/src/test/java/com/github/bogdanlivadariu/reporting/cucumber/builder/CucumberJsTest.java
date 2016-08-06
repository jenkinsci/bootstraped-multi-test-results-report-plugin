package com.github.bogdanlivadariu.reporting.cucumber.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;

import com.github.bogdanlivadariu.reporting.cucumber.helpers.SpecialProperties;

public class CucumberJsTest {

    private CucumberReportBuilder builder;

    private AllFeatureReports reports;

    @Before
    public void setUp() throws JAXBException, IOException {
        List<String> jsonReports = new ArrayList<>();

        String cucumberJsReport = AllFeatureReportsTest.class.getResource("/cucumber-js.json").getPath();

        jsonReports.add(cucumberJsReport);

        builder = new CucumberReportBuilder(jsonReports, "output", new SpecialProperties());
        reports = new AllFeatureReports("title", builder.getProcessedFeatures());

    }

    @Test
    public void featureSizeTest() {
        assertEquals(1, reports.getFeatures().size());
        reports.getPageTitle();
        reports.getScenariosTotal();
        reports.getScenariosTotalFailed();
        reports.getScenariosTotalPassed();
        reports.getStepsTotal();
    }

    @Test
    public void stepsTest() {
        assertEquals(Integer.valueOf(6), reports.getStepsTotal());
        assertEquals(Integer.valueOf(0), reports.getStepsTotalFailed());
        assertEquals(Integer.valueOf(4), reports.getStepsTotalPassed());
        assertEquals(Integer.valueOf(1), reports.getStepsTotalSkipped());
        assertEquals(Integer.valueOf(1), reports.getStepsTotalUndefined());
    }

    @Test
    public void builderShouldFail() throws IOException {
        assertFalse(builder.writeReportsOnDisk());
    }
}

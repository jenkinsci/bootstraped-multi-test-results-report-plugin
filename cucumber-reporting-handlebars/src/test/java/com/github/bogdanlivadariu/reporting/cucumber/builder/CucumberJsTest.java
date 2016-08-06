package com.github.bogdanlivadariu.reporting.cucumber.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;

import com.github.bogdanlivadariu.reporting.cucumber.helpers.SpecialProperties;
import com.github.bogdanlivadariu.reporting.cucumber.json.models.Element;
import com.github.bogdanlivadariu.reporting.cucumber.json.models.Step;

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
        assertEquals("title", reports.getPageTitle());
    }

    @Test
    public void scenariosTest() {
        assertEquals(Integer.valueOf(1), reports.getScenariosTotal());
        assertEquals(Integer.valueOf(1), reports.getScenariosTotalFailed());
        assertEquals(Integer.valueOf(0), reports.getScenariosTotalPassed());
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
    public void hidenTest() {
        assertTrue(reports.getFeatures().get(0).getElements()[0].getSteps()[5].isHidden());
    }

    @Test
    public void builderShouldFail() throws IOException {
        assertFalse(builder.writeReportsOnDisk());
    }
}

package com.github.bogdanlivadariu.reporting.cucumber.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;

import com.github.bogdanlivadariu.reporting.cucumber.helpers.SpecialProperties;
import com.github.bogdanlivadariu.reporting.cucumber.json.models.Element;
import com.github.bogdanlivadariu.reporting.cucumber.json.models.Feature;
import com.github.bogdanlivadariu.reporting.cucumber.json.models.Step;

public class AllFeatureReportsTest {

    private CucumberReportBuilder builder;

    private AllFeatureReports reports;

    @Before
    public void setUp() throws JAXBException, IOException {
        List<String> jsonReports = new ArrayList<>();

        String firstReport = AllFeatureReportsTest.class.getResource("/result.json").getPath();
        String secondReport = AllFeatureReportsTest.class.getResource("/cucumber.json").getPath();

        jsonReports.add(firstReport);
        jsonReports.add(secondReport);

        builder = new CucumberReportBuilder(jsonReports, "output", new SpecialProperties());
        reports = new AllFeatureReports("title", builder.getProcessedFeatures());
    }

    @Test
    public void tagSizeTest() {
        assertEquals(9, reports.getAllTags().size());
    }

    @Test
    public void featureSizeTest() {
        assertEquals(4, reports.getFeatures().size());
        reports.getPageTitle();
        reports.getScenariosTotal();
        reports.getScenariosTotalFailed();
        reports.getScenariosTotalPassed();
        reports.getStepsTotal();
    }

    @Test
    public void scenariosTest() {
        assertEquals(Integer.valueOf(21), reports.getScenariosTotal());
        assertEquals(Integer.valueOf(1), reports.getScenariosTotalFailed());
        assertEquals(Integer.valueOf(20), reports.getScenariosTotalPassed());
    }

    @Test
    public void stepsTest() {
        assertEquals(Integer.valueOf(92), reports.getStepsTotal());
        assertEquals(Integer.valueOf(1), reports.getStepsTotalFailed());
        assertEquals(Integer.valueOf(88), reports.getStepsTotalPassed());
        assertEquals(Integer.valueOf(3), reports.getStepsTotalSkipped());
        assertEquals(Integer.valueOf(0), reports.getStepsTotalUndefined());
    }

    @Test
    public void totalDurationTest() {
        long duration = Long.parseLong("206919001170");
        assertEquals(duration, reports.getTotalDuration());
    }

    @Test
    public void elementEmbeddingTest() {
        List<Integer> expected = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0);
        List<Integer> actualEmbeddingsCount = new ArrayList<>();

        for (Feature f : reports.getFeatures()) {
            for (Element e : f.getElements()) {
                actualEmbeddingsCount.add(e.getEmbeddings().size());
            }
        }
        assertEquals(expected, actualEmbeddingsCount);
    }

    @Test
    public void stepEmbeddingTest() {
        List<Integer> expected = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        List<Integer> actualEmbeddingsCount = new ArrayList<>();

        for (Feature f : reports.getFeatures()) {
            for (Element e : f.getElements()) {
                for (Step s : e.getSteps()) {
                    actualEmbeddingsCount.add(s.getEmbeddings().length);
                }
            }
        }
        assertEquals(expected, actualEmbeddingsCount);
    }

    @Test
    public void builderShouldFail() throws IOException {
        assertFalse(builder.writeReportsOnDisk());
    }
}

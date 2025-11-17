package com.github.bogdanlivadariu.reporting.cucumber.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.bogdanlivadariu.reporting.cucumber.helpers.SpecialProperties;
import com.github.bogdanlivadariu.reporting.cucumber.json.models.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CucumberJsTest {

    private CucumberReportBuilder builder;

    private AllFeatureReports reports;

    @BeforeEach
    void setUp() throws IOException {
        List<String> jsonReports = new ArrayList<>();

        String cucumberJsReport = AllFeatureReportsTest.class.getResource("/cucumber-js.json").getPath();

        jsonReports.add(cucumberJsReport);

        builder = new CucumberReportBuilder(jsonReports, "output", new SpecialProperties());
        reports = new AllFeatureReports("title", builder.getProcessedFeatures());
    }

    @Test
    void featureSizeTest() {
        assertEquals(1, reports.getFeatures().size());
        assertEquals("title", reports.getPageTitle());
    }

    @Test
    void scenariosTest() {
        assertEquals(Integer.valueOf(1), reports.getScenariosTotal());
        assertEquals(Integer.valueOf(1), reports.getScenariosTotalFailed());
        assertEquals(Integer.valueOf(0), reports.getScenariosTotalPassed());
    }

    @Test
    void featureTagTest() {
        List<String> existingTags = new ArrayList<>();
        for (Tag t : reports.getFeatures().get(0).getTags()) {
            existingTags.add(t.getName() + t.getLine());
        }
        assertEquals(List.of("@letter_tag3"), existingTags);
    }

    @Test
    void scenariosTagTest() {
        List<String> existingTags = new ArrayList<>();
        for (Tag t : reports.getFeatures().get(0).getElements()[0].getTags()) {
            existingTags.add(t.getName() + t.getLine());
        }
        assertEquals(List.of("@PROD3"), existingTags);
    }

    @Test
    void stepsTest() {
        assertEquals(Integer.valueOf(6), reports.getStepsTotal());
        assertEquals(Integer.valueOf(0), reports.getStepsTotalFailed());
        assertEquals(Integer.valueOf(4), reports.getStepsTotalPassed());
        assertEquals(Integer.valueOf(1), reports.getStepsTotalSkipped());
        assertEquals(Integer.valueOf(1), reports.getStepsTotalUndefined());
    }

    @Test
    void hiddenTest() {
        assertTrue(reports.getFeatures().get(0).getElements()[0].getSteps()[5].isHidden());
    }

    @Test
    void builderShouldFail() throws IOException {
        assertFalse(builder.writeReportsOnDisk());
    }
}

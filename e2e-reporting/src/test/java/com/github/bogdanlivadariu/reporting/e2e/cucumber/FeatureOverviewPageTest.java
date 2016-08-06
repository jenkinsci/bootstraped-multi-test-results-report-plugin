package com.github.bogdanlivadariu.reporting.e2e.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.github.bogdanlivadariu.reporting.cucumber.builder.AllFeatureReports;
import com.github.bogdanlivadariu.reporting.cucumber.helpers.Helpers;
import com.github.bogdanlivadariu.reporting.cucumber.json.models.Feature;
import com.github.bogdanlivadariu.reporting.e2e.components.cucumber.FeatureFooterRowComponent;
import com.github.bogdanlivadariu.reporting.e2e.components.cucumber.FeatureRowComponent;
import com.github.bogdanlivadariu.reporting.e2e.page.cucumber.FeatureOverviewPage;
import com.github.jknack.handlebars.Handlebars;

public class FeatureOverviewPageTest extends BaseTestCase {
    private static FeatureOverviewPage page = new FeatureOverviewPage();

    @Test
    public void featureCountTest() {
        assertEquals(cucumberReportBuilder.getProcessedFeatures().size(), page.getFeatureRows().size());
    }

    public void featureFooterCountTest() {
        assertEquals(1, page.getFeatureFooterRows().size());
    }

    @Test
    public void featureCellsTest() throws IOException {
        for (int i = 0; i < cucumberReportBuilder.getProcessedFeatures().size(); i++) {
            Feature expectedFeature = cucumberReportBuilder.getProcessedFeatures().get(i);
            FeatureRowComponent actualFeatureRow = page.getFeatureRows().get(i);
            actualFeatureRow.scrollIntoView();

            assertTrue(
                "Feature unique id from href is not correct",
                actualFeatureRow.buttonFeatureName().getAttributeValue("href")
                    .contains(expectedFeature.getUniqueID()));
            assertEquals(
                "Feature name do not match",
                expectedFeature.getName(),
                actualFeatureRow.buttonFeatureName().getText());

            assertEquals(
                "Scenarios total count do not match",
                expectedFeature.getScenariosCount().toString(),
                actualFeatureRow.labelScenariosTotal().getText());

            assertEquals(
                "Scenarios passed count do not match",
                expectedFeature.getScenariosPassedCount().toString(),
                actualFeatureRow.labelScenariosPassed().getText());

            assertEquals(
                "Scenarios failed count do not match",
                expectedFeature.getScenariosFailedCount().toString(),
                actualFeatureRow.labelScenariosFailed().getText());

            assertEquals(
                "Steps total count do not match",
                expectedFeature.getStepsTotalCount().toString(),
                actualFeatureRow.labelStepsTotal().getText());

            assertEquals(
                "Steps passed count do not match",
                expectedFeature.getStepsPassedCount().toString(),
                actualFeatureRow.labelStepsPassed().getText());

            assertEquals(
                "Steps failed count do not match",
                expectedFeature.getStepsFailedCount().toString(),
                actualFeatureRow.labelStepsFailed().getText());

            assertEquals(
                "Steps skipped count do not match",
                expectedFeature.getStepsSkippedCount().toString(),
                actualFeatureRow.labelStepsSkipped().getText());

            assertEquals(
                "Steps undefined count do not match",
                expectedFeature.getStepsUndefinedCount().toString(),
                actualFeatureRow.labelStepsUndefined().getText());

            assertEquals(
                "Duration do not match",
                new Helpers(new Handlebars()).registerHelpers().helper("date")
                    .apply(expectedFeature.getTotalDuration(), null),
                actualFeatureRow.labelDuration().getText());
        }
    }

    @Test
    public void featureFooterCellsTest() throws IOException {
        FeatureFooterRowComponent actualRow = page.getFeatureFooterRows().get(0);
        AllFeatureReports features = new AllFeatureReports("test", cucumberReportBuilder.getProcessedFeatures());
        actualRow.scrollIntoView();

        assertEquals(features.getFeaturesCount().toString(), actualRow.labelFeaturesTotal().getText());

        assertEquals(
            "Scenarios total count do not match",
            features.getScenariosTotal().toString(),
            actualRow.labelScenariosTotal().getText());

        assertEquals(
            "Scenarios passed count do not match",
            features.getScenariosTotalPassed().toString(),
            actualRow.labelScenariosPassed().getText());

        assertEquals(
            "Scenarios failed count do not match",
            features.getScenariosTotalFailed().toString(),
            actualRow.labelScenariosFailed().getText());

        assertEquals(
            "Steps total count do not match",
            features.getStepsTotal().toString(),
            actualRow.labelStepsTotal().getText());

        assertEquals(
            "Steps passed count do not match",
            features.getStepsTotalPassed().toString(),
            actualRow.labelStepsPassed().getText());

        assertEquals(
            "Steps failed count do not match",
            features.getStepsTotalFailed().toString(),
            actualRow.labelStepsFailed().getText());

        assertEquals(
            "Steps skipped count do not match",
            features.getStepsTotalSkipped().toString(),
            actualRow.labelStepsSkipped().getText());

        assertEquals(
            "Steps undefined count do not match",
            features.getStepsTotalUndefined().toString(),
            actualRow.labelStepsUndefined().getText());

        assertEquals(
            "Duration do not match",
            new Helpers(new Handlebars()).registerHelpers().helper("date")
                .apply(features.getTotalDuration(), null),
            actualRow.labelDuration().getText());

    }
}

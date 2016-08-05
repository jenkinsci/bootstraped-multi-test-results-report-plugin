package com.github.bogdanlivadariu.reporting.e2e.cucumber;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import com.github.bogdanlivadariu.reporting.cucumber.json.models.Feature;
import com.github.bogdanlivadariu.reporting.e2e.components.cucumber.FeatureRowComponent;
import com.github.bogdanlivadariu.reporting.e2e.page.cucumber.FeatureOverviewPage;

public class FeatureOverviewPageTest extends BaseTestCase {
    private FeatureOverviewPage page = new FeatureOverviewPage();

    @Test
    public void featureCountTest() {
        assertEquals(cucumberReportBuilder.getProcessedFeatures().size(), page.getFeatureRows().size());
    }

    @Test
    public void featureNameTest() {
        Comparator<Feature> featureNameComparator = new Comparator<Feature>() {
            @Override
            public int compare(Feature first, Feature second) {
                return first.getName().compareToIgnoreCase(second.getName());
            }
        };

        Comparator< ? super FeatureRowComponent> rowFeatureNameComparator = new Comparator<FeatureRowComponent>() {
            @Override
            public int compare(FeatureRowComponent first, FeatureRowComponent second) {
                return first.buttonFeatureName().getText().compareToIgnoreCase(second.buttonFeatureName().getText());
            }
        };

        List<Feature> sortedFeatures = new ArrayList<>(cucumberReportBuilder.getProcessedFeatures());
        List<FeatureRowComponent> sortedRows = new ArrayList<>(page.getFeatureRows());
        Collections.sort(sortedFeatures, featureNameComparator);
        Collections.sort(sortedRows, rowFeatureNameComparator);

        for (int i = 0; i < sortedFeatures.size(); i++) {
            String expected = sortedRows.get(i).buttonFeatureName().getText();
            String actual = sortedRows.get(i).buttonFeatureName().getText();
            assertEquals(expected, actual);
        }
    }

}

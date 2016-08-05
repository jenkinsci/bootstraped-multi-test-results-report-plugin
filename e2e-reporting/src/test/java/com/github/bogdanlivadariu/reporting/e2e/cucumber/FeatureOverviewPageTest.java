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
        assertEquals(cucumberReportBuilder.getProcessedFeatures().size(), page.getRows().size());
    }

    @Test
    public void featureNameTest() {
        Comparator<Feature> c = new Comparator<Feature>() {

            @Override
            public int compare(Feature o1, Feature o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        };
        Comparator< ? super FeatureRowComponent> d = new Comparator<FeatureRowComponent>() {
            @Override
            public int compare(FeatureRowComponent o1, FeatureRowComponent o2) {
                return o1.buttonFeatureName().getText().compareToIgnoreCase(o2.buttonFeatureName().getText());
            }
        };

        List<Feature> sortedFeatures = new ArrayList<>(cucumberReportBuilder.getProcessedFeatures());
        List<FeatureRowComponent> sortedRows = new ArrayList<>(page.getRows());
        Collections.sort(sortedFeatures, c);
        Collections.sort(sortedRows, d);

        for (int i = 0; i < sortedFeatures.size(); i++) {
            String expected = sortedRows.get(i).buttonFeatureName().getText();
            String actual = sortedRows.get(i).buttonFeatureName().getText();
            assertEquals(expected, actual);
        }
    }

}

package com.github.bogdanlivadariu.reporting.e2e.cucumber;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.github.bogdanlivadariu.java.automation.framework.webdriver.WebDriverInstance;
import com.github.bogdanlivadariu.reporting.cucumber.builder.CucumberReportBuilder;
import com.github.bogdanlivadariu.reporting.cucumber.helpers.SpecialProperties;
import com.github.bogdanlivadariu.reporting.cucumber.json.models.Feature;
import com.github.bogdanlivadariu.reporting.e2e.components.cucumber.IndexRowComponent;
import com.github.bogdanlivadariu.reporting.e2e.page.cucumber.IndexPage;

public class SampleTest {
    static CucumberReportBuilder re;

    static IndexPage page;

    @BeforeClass
    public static void beforeSetup() throws IOException {
        String reportPath = "./tes";
        List<String> jsonReports = new ArrayList<>();
        String reportFile = SampleTest.class.getResource("/result.json").getPath();
        jsonReports.add(reportFile);

        re = new CucumberReportBuilder(jsonReports, reportPath, new SpecialProperties());
        re.writeReportsOnDisk();

        WebDriver we = new FirefoxDriver();
        WebDriverInstance.setDriver(we);

        File overviewReport = new File(reportPath + File.separator + CucumberReportBuilder.FEATURES_OVERVIEW_HTML);
        we.get("file:///" + overviewReport.getAbsolutePath());

        page = new IndexPage();
    }

    @AfterClass
    public static void afterSetup() {
        WebDriverInstance.getDriver().quit();
    }

    @Test
    public void featureCountTest() {
        assertEquals(re.getProcessedFeatures().size(), page.getRows().size());
    }

    @Test
    public void featureNameTest() {
        Comparator<Feature> c = new Comparator<Feature>() {

            @Override
            public int compare(Feature o1, Feature o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        };
        Comparator< ? super IndexRowComponent> d = new Comparator<IndexRowComponent>() {
            @Override
            public int compare(IndexRowComponent o1, IndexRowComponent o2) {
                return o1.buttonFeatureName().getText().compareToIgnoreCase(o2.buttonFeatureName().getText());
            }
        };

        List<Feature> sortedFeatures = new ArrayList<>(re.getProcessedFeatures());
        List<IndexRowComponent> sortedRows = new ArrayList<>(page.getRows());
        Collections.sort(sortedFeatures, c);
        Collections.sort(sortedRows, d);

        for (int i = 0; i < sortedFeatures.size(); i++) {
            String expected = sortedRows.get(i).buttonFeatureName().getText();
            String actual = sortedRows.get(i).buttonFeatureName().getText();
            assertEquals(expected, actual);
        }
    }

}

package com.github.bogdanlivadariu.reporting.e2e.cucumber;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.github.bogdanlivadariu.java.automation.framework.webdriver.WebDriverInstance;
import com.github.bogdanlivadariu.reporting.cucumber.builder.CucumberReportBuilder;
import com.github.bogdanlivadariu.reporting.cucumber.helpers.SpecialProperties;

public class BaseTestCase {
    protected static CucumberReportBuilder cucumberReportBuilder;

    @BeforeClass
    public static void beforeSetup() throws IOException {
        String reportPath = "./tes";
        List<String> jsonReports = new ArrayList<>();
        String reportFile = FeatureOverviewPageTest.class.getResource("/result.json").getPath();
        jsonReports.add(reportFile);

        cucumberReportBuilder = new CucumberReportBuilder(jsonReports, reportPath, new SpecialProperties());
        cucumberReportBuilder.writeReportsOnDisk();

        WebDriver we = new FirefoxDriver();
        WebDriverInstance.setDriver(we);

        File overviewReport = new File(reportPath + File.separator + CucumberReportBuilder.FEATURES_OVERVIEW_HTML);
        we.get("file:///" + overviewReport.getAbsolutePath());

    }

    @AfterClass
    public static void afterSetup() {
        WebDriverInstance.getDriver().quit();
    }
}

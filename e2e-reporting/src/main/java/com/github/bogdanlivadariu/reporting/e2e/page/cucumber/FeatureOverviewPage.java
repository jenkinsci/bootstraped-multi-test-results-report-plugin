package com.github.bogdanlivadariu.reporting.e2e.page.cucumber;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.github.bogdanlivadariu.java.automation.framework.webdriver.WebDriverInstance;
import com.github.bogdanlivadariu.reporting.e2e.components.cucumber.FeatureFooterRowComponent;
import com.github.bogdanlivadariu.reporting.e2e.components.cucumber.FeatureRowComponent;

public class FeatureOverviewPage {

    @FindBy(css = "#feature-container tbody > tr")
    private List<WebElement> featureRows;

    @FindBy(css = "#feature-container tfoot > tr")
    private List<WebElement> footerRows;

    public List<FeatureRowComponent> getFeatureRows() {
        List<FeatureRowComponent> rows = new ArrayList<>();
        for (WebElement row : featureRows) {
            rows.add(new FeatureRowComponent(row));
        }
        return rows;
    }

    public List<FeatureFooterRowComponent> getFeatureFooterRows() {
        List<FeatureFooterRowComponent> rows = new ArrayList<>();
        for (WebElement row : footerRows) {
            rows.add(new FeatureFooterRowComponent(row));
        }
        return rows;
    }

    public FeatureOverviewPage() {
        PageFactory.initElements(WebDriverInstance.getDriver(), this);
    }
}

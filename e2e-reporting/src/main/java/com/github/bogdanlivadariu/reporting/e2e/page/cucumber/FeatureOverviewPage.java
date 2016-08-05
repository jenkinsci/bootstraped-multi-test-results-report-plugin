package com.github.bogdanlivadariu.reporting.e2e.page.cucumber;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.github.bogdanlivadariu.java.automation.framework.webdriver.WebDriverInstance;
import com.github.bogdanlivadariu.reporting.e2e.components.cucumber.FeatureRowComponent;

public class FeatureOverviewPage {

    @FindBy(css = "#feature-container tbody > tr")
    private List<WebElement> rows;

    public List<FeatureRowComponent> getFeatureRows() {
        List<FeatureRowComponent> rows = new ArrayList<>();
        for (WebElement row : this.rows) {
            rows.add(new FeatureRowComponent(row));
        }
        return rows;
    }

    public FeatureOverviewPage() {
        PageFactory.initElements(WebDriverInstance.getDriver(), this);
    }
}

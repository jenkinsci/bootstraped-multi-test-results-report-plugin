package com.github.bogdanlivadariu.reporting.e2e.page.cucumber;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.github.bogdanlivadariu.java.automation.framework.webdriver.WebDriverInstance;
import com.github.bogdanlivadariu.reporting.e2e.components.cucumber.IndexRowComponent;

public class IndexPage {

    @FindBy(css = "#feature-container tbody > tr")
    private List<WebElement> rows;

    public List<IndexRowComponent> getRows() {
        List<IndexRowComponent> rows = new ArrayList<>();
        for (WebElement row : this.rows) {
            rows.add(new IndexRowComponent(row));
        }
        return rows;
    }

    public IndexPage() {
        PageFactory.initElements(WebDriverInstance.getDriver(), this);
    }
}

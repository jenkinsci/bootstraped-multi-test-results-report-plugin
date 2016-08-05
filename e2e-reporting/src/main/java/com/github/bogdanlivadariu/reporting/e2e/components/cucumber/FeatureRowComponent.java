package com.github.bogdanlivadariu.reporting.e2e.components.cucumber;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.github.bogdanlivadariu.java.automation.framework.components.common.BaseComponent;
import com.github.bogdanlivadariu.java.automation.framework.components.common.ButtonComponent;

public class FeatureRowComponent extends BaseComponent {

    private By featureName = By.xpath("td[1]");

    public FeatureRowComponent(WebElement element) {
        super(element);
    }

    public ButtonComponent buttonFeatureName() {
        return new ButtonComponent(getDescendant(featureName));
    }

}

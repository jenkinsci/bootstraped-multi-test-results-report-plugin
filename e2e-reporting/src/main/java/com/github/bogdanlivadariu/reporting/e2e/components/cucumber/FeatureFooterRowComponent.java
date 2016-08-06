package com.github.bogdanlivadariu.reporting.e2e.components.cucumber;

import org.openqa.selenium.WebElement;

import com.github.bogdanlivadariu.java.automation.framework.components.common.LabelComponent;

public class FeatureFooterRowComponent extends FeatureRowComponent {

    public FeatureFooterRowComponent(WebElement element) {
        super(element);
    }

    public LabelComponent labelFeaturesTotal() {
        return new LabelComponent(cells.get(1));
    }

}

package com.github.bogdanlivadariu.reporting.e2e.components.cucumber;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.github.bogdanlivadariu.java.automation.framework.components.common.BaseComponent;
import com.github.bogdanlivadariu.java.automation.framework.components.common.ButtonComponent;
import com.github.bogdanlivadariu.java.automation.framework.components.common.LabelComponent;

public class FeatureRowComponent extends BaseComponent {

    private By cellsLocator = By.tagName("td");

    private List<WebElement> cells;

    public FeatureRowComponent(WebElement element) {
        super(element);
        cells = getDescendants(cellsLocator);
    }

    public ButtonComponent buttonFeatureName() {
        return new ButtonComponent(getDescendant(cells.get(0), By.tagName("a")));
    }

    public LabelComponent labelScenariosTotal() {
        return new LabelComponent(cells.get(2));
    }

    public LabelComponent labelScenariosPassed() {
        return new LabelComponent(cells.get(3));
    }

    public LabelComponent labelScenariosFailed() {
        return new LabelComponent(cells.get(4));
    }

    public LabelComponent labelStepsTotal() {
        return new LabelComponent(cells.get(5));
    }

    public LabelComponent labelStepsPassed() {
        return new LabelComponent(cells.get(6));
    }

    public LabelComponent labelStepsFailed() {
        return new LabelComponent(cells.get(7));
    }

    public LabelComponent labelStepsSkipped() {
        return new LabelComponent(cells.get(8));
    }

    public LabelComponent labelStepsUndefined() {
        return new LabelComponent(cells.get(9));
    }

    public LabelComponent labelDuration() {
        return new LabelComponent(cells.get(10));
    }

}

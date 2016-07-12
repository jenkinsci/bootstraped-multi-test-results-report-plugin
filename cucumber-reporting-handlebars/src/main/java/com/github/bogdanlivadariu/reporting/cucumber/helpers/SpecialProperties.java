package com.github.bogdanlivadariu.reporting.cucumber.helpers;

import java.util.HashMap;
import java.util.Map;

public class SpecialProperties {

    public enum SpecialKeyProperties {
        IGNORE_UNDEFINED_STEPS
    }

    private Map<SpecialProperties.SpecialKeyProperties, Boolean> specialProperties =
        new HashMap<>();

    public Map<SpecialKeyProperties, Boolean> getProperties() {
        return specialProperties;
    }
}

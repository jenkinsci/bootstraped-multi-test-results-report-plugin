package com.github.bogdanlivadariu.reporting.cucumber.helpers;

import java.util.HashMap;
import java.util.Map;

public class SpecialProperties {

    public enum SpecialKeyProperties {
        IGNORE_UNDEFINED_STEPS
    }

    private Map<SpecialProperties.SpecialKeyProperties, Boolean> specialProperties =
        new HashMap<>();

    public void addProperty(SpecialKeyProperties key, Boolean value) {
        specialProperties.put(key, value);
    }

    public Map<SpecialKeyProperties, Boolean> getProperties() {
        return specialProperties;
    }
}

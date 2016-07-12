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

    public boolean getPropertyValue(SpecialKeyProperties key) {
        try {
            return getProperties().get(key);
        } catch (NullPointerException e) {
            return false;
        }
    }
}

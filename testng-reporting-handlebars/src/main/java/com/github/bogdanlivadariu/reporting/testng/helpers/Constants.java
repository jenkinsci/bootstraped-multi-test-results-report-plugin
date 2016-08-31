package com.github.bogdanlivadariu.reporting.testng.helpers;

import org.testng.reporters.XMLReporterConfig;

public class Constants {
    public static final String SKIPPED = XMLReporterConfig.TEST_SKIPPED;

    public static final String PASSED = XMLReporterConfig.TEST_PASSED;

    public static final String FAILED = XMLReporterConfig.TEST_FAILED;

    public static final String UNDEFINED = "UNDEFINED";

    private Constants() {
    }
}

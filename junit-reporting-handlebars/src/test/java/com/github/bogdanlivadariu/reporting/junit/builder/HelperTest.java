package com.github.bogdanlivadariu.reporting.junit.builder;

import com.github.bogdanlivadariu.reporting.junit.helpers.Helpers;
import com.github.jknack.handlebars.Handlebars;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.bogdanlivadariu.reporting.junit.helpers.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class HelperTest {

    private static Handlebars instance;

    @BeforeAll
    static void setup() {
        Helpers a = new Helpers(new Handlebars());
        instance = a.registerHelpers();
    }

    @Test
    void resolveTooltipTest() throws IOException {
        String helperName = "resolve-tooltip";
        assertEquals("This test has Failed",
            instance.helper(helperName).apply("Failed", null));

        assertEquals("This test has 123!@",
            instance.helper(helperName).apply("123!@", null));
    }

    @Test
    void isCollapsedTest() throws IOException {
        String helperName = "is-collapsed";
        assertEquals("collapse",
            instance.helper(helperName).apply(PASSED, null));

        assertEquals("collapse in",
            instance.helper(helperName).apply(FAILED, null));

        assertNull(instance.helper(helperName).apply(SKIPPED, null));

        assertNull(instance.helper(helperName).apply(ERRORED, null));
    }

    @Test
    void resolveTitleTest() throws IOException {
        String helperName = "resolve-title";
        assertEquals("This step has passed",
            instance.helper(helperName).apply(PASSED, null));

        assertEquals("This step has failed",
            instance.helper(helperName).apply(FAILED, null));

        assertEquals("This step has been skipped",
            instance.helper(helperName).apply(SKIPPED, null));

        assertNull(instance.helper(helperName).apply(ERRORED, null));
    }

    @Test
    void resultColorTest() throws IOException {
        String helperName = "result-color";
        assertEquals("success",
            instance.helper(helperName).apply(PASSED, null));

        assertEquals("danger",
            instance.helper(helperName).apply(FAILED, null));

        assertEquals("info",
            instance.helper(helperName).apply(SKIPPED, null));

        assertEquals("warning",
            instance.helper(helperName).apply(ERRORED, null));
    }

}

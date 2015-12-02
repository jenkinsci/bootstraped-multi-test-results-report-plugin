package com.github.bogdanlivadariu.reporting.testng.helpers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.testng.reporters.XMLReporterConfig;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

public class Helpers {
    private Handlebars handlebar;

    public Helpers(Handlebars handlebar) {
        this.handlebar = handlebar;
    }

    public Handlebars registerHelpers() {
        handlebar.registerHelper("date", new Helper<Long>() {
            public CharSequence apply(Long arg0, Options arg1) throws IOException {
                int totalSecs = (int) arg0.doubleValue() / 1000;
                int hours = totalSecs / 3600;
                int minutes = (totalSecs % 3600) / 60;
                int seconds = totalSecs % 60;
                int miliSec = (int) arg0.doubleValue() % 1000;

                String timeString =
                    String.format("%02d h : %02d m : %02d s : %02d ms", hours, minutes, seconds, miliSec);
                return timeString;
            }
        });

        handlebar.registerHelper("result-color", new Helper<String>() {
            @Override
            public CharSequence apply(String arg0, Options arg1) throws IOException {
                switch (arg0.toUpperCase()) {
                    case XMLReporterConfig.TEST_SKIPPED:
                        return "info";
                    case XMLReporterConfig.TEST_PASSED:
                        return "success";
                    case XMLReporterConfig.TEST_FAILED:
                        return "danger";
                }
                return "undefined";
            }
        });

        handlebar.registerHelper("is-config", new Helper<Boolean>() {
            @Override
            public CharSequence apply(Boolean arg0, Options arg1) throws IOException {
                if (arg0) {
                    return "collapseMagic2 collapse";
                } else {
                    return "nada";
                }
            }
        });

        handlebar.registerHelper("resolve-tooltip", new Helper<String>() {
            @Override
            public CharSequence apply(String arg0, Options arg1) throws IOException {
                switch (arg0.toUpperCase()) {
                    case XMLReporterConfig.TEST_PASSED:
                        return "This test has passed";
                    case XMLReporterConfig.TEST_FAILED:
                        return "This test has failed";
                    case XMLReporterConfig.TEST_SKIPPED:
                        return "This test has been skipped";
                }
                return "there was a problem setting the tooltip of the test, status might differ, investigate";
            }
        });

        handlebar.registerHelper("resolve-title", new Helper<String>() {
            @Override
            public CharSequence apply(String arg0, Options arg1) throws IOException {
                switch (arg0.toLowerCase()) {
                    case "skipped":
                        return "This step has been skipped";
                    case "passed":
                        return "This step has passed";
                    case "failed":
                        return "This step has failed";
                }
                return "undefined";
            }
        });

        handlebar.registerHelper("is-collapsed", new Helper<String>() {
            @Override
            public CharSequence apply(String arg0, Options arg1) throws IOException {
                switch (arg0.toLowerCase()) {
                    case "passed":
                        return "collapse";
                    case "failed":
                        return "collapse in";
                }
                return "undefined";
            }
        });

        handlebar.registerHelper("now", new Helper<Object>() {
            @Override
            public CharSequence apply(Object context, Options options) throws IOException {
                Calendar cal = Calendar.getInstance();
                Date date = cal.getTime();
                String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(date);
                TimeZone tz = cal.getTimeZone();
                return now + " " + tz.getID();
            }
        });

        return handlebar;
    }
}

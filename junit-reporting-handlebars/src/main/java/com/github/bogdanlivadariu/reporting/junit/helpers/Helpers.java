package com.github.bogdanlivadariu.reporting.junit.helpers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

public class Helpers {
    private Handlebars handlebar;

    public Helpers(Handlebars handlebar) {
        this.handlebar = handlebar;
    }

    public Handlebars registerHelpers() {
        handlebar.registerHelper("date", new Helper<String>() {
            public CharSequence apply(String arg0, Options arg1) throws IOException {
                int totalSecs = (int) Double.parseDouble(arg0);
                int hours = totalSecs / 3600;
                int minutes = (totalSecs % 3600) / 60;
                int seconds = totalSecs % 60;
                int miliSec = (int) ((Double.parseDouble(arg0) - totalSecs) * 1000);

                String timeString =
                    String.format("%02d h : %02d m : %02d s : %02d ms", hours, minutes, seconds, miliSec);
                return timeString;
            }
        });

        handlebar.registerHelper("result-color", new Helper<String>() {
            @Override
            public CharSequence apply(String arg0, Options arg1) throws IOException {
                switch (arg0.toLowerCase()) {
                    case Constants.SKIPPED:
                        return "info";
                    case Constants.ERRORED:
                        return "warning";
                    case Constants.PASSED:
                        return "success";
                    case Constants.FAILED:
                        return "danger";
                }
                return "undefined";
            }
        });

        handlebar.registerHelper("resolve-tooltip", new Helper<String>() {
            @Override
            public CharSequence apply(String arg0, Options arg1) throws IOException {
                return "This test has " + arg0;
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

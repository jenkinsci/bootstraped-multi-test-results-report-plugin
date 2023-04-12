package com.github.bogdanlivadariu.reporting.junit.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;

public class Helpers {
    private Handlebars handlebar;

    public Helpers(Handlebars handlebar) {
        this.handlebar = handlebar;
    }

    public Handlebars registerHelpers() {
        handlebar.registerHelper("date", (Helper<String>) (arg0, arg1) -> {
            int totalSecs = (int) Double.parseDouble(arg0);
            int hours = totalSecs / 3600;
            int minutes = (totalSecs % 3600) / 60;
            int seconds = totalSecs % 60;
            int miliSec = (int) ((Double.parseDouble(arg0) - totalSecs) * 1000);

            return String.format("%02d h : %02d m : %02d s : %02d ms", hours, minutes, seconds, miliSec);
        });

        handlebar.registerHelper("result-color", (Helper<String>) (arg0, arg1) -> checkStatus(arg0, "info", "warning", "success", "danger"));

        handlebar.registerHelper("resolve-tooltip", (Helper<String>) (arg0, arg1) -> "This test has " + arg0);

        handlebar.registerHelper("resolve-title", (Helper<String>) (arg0, arg1) -> checkStatus(arg0, "This step has been skipped", null, "This step has passed", "This step has failed"));

        handlebar.registerHelper("is-collapsed", (Helper<String>) (arg0, arg1) -> checkStatus(arg0, null, null, "collapse", "collapse in"));

        handlebar.registerHelper("now", (context, options) -> {
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(date);
            TimeZone tz = cal.getTimeZone();
            return now + " " + tz.getID();
        });

        return handlebar;
    }

    private CharSequence checkStatus(String arg0, String retValue1, String retValue2, String retValue3, String retValue4) {
        switch (arg0.toLowerCase()) {
            case Constants.SKIPPED:
                return retValue1;
            case Constants.ERRORED:
                return retValue2;
            case Constants.PASSED:
                return retValue3;
            case Constants.FAILED:
                return retValue4;
            default:
                break;
        }
        return Constants.UNDEFINED;
    }
    public Handlebars getInstance() {
        return handlebar;
    }
}

package com.github.bogdanlivadariu.reporting.testng.helpers;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.github.bogdanlivadariu.reporting.testng.helpers.Constants.*;

public class Helpers {
    public static final String PROBLEM_SETTING_STATUS =
            "there was a problem setting the tooltip of the test, status might differ, investigate";

    private final Handlebars handlebar;

    public Helpers(Handlebars handlebar) {
        this.handlebar = handlebar;
    }

    public Handlebars registerHelpers() {
        handlebar.registerHelper("date", (Helper<Long>) (arg0, arg1) -> {
            int totalSecs = (int) arg0.doubleValue() / 1000;
            int hours = totalSecs / 3600;
            int minutes = (totalSecs % 3600) / 60;
            int seconds = totalSecs % 60;
            int miliSec = (int) arg0.doubleValue() % 1000;

            return String.format("%02d h : %02d m : %02d s : %02d ms", hours, minutes, seconds, miliSec);
        });

        handlebar.registerHelper("result-color", (Helper<String>) (arg0, arg1) -> checkStatus(arg0.toLowerCase(), "info", "success", "danger", null));

        handlebar.registerHelper("is-config", (Helper<Boolean>) (arg0, arg1) -> getIsConfigApplyResult(arg0));

        handlebar.registerHelper("resolve-tooltip", (Helper<String>) (arg0, arg1) -> checkStatus(arg0.toLowerCase(), "This test has been skipped", "This test has passed",
                "This test has failed", PROBLEM_SETTING_STATUS));

        handlebar.registerHelper("resolve-title", (Helper<String>) (arg0, arg1) -> checkStatus(arg0.toLowerCase(), "This step has been skipped", "This step has passed",
                "This step has failed", null));

        handlebar.registerHelper("is-collapsed", (Helper<String>) (arg0, arg1) -> checkStatus(arg0.toLowerCase(), null, "collapse", "collapse in", null));

        handlebar.registerHelper("now", (context, options) -> {
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(date);
            TimeZone tz = cal.getTimeZone();
            return now + " " + tz.getID();
        });

        handlebar.registerHelper("md5", md5Helper());

        return handlebar;
    }

    private Helper<String> md5Helper() {
        return (arg0, arg1) -> {
            try {
                return StringUtils.getMd5From(arg0 + arg1.param(0));
            } catch (NoSuchAlgorithmException e) {
                return "ERR";
            }
        };
    }

    private CharSequence getIsConfigApplyResult(Boolean arg0) {
        if (arg0) {
            return "collapseMagic2 collapse";
        } else {
            return "nada";
        }
    }

    private CharSequence checkStatus(String arg0, String retValue1, String retValue2, String retValue3,
                                     String retValue4) {
        switch (arg0.toUpperCase()) {
            case SKIPPED:
                return retValue1;
            case PASSED:
                return retValue2;
            case FAILED:
                return retValue3;
            default:
                break;
        }
        return retValue4 == null ? UNDEFINED : retValue4;
    }
}

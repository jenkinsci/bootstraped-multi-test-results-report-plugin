package com.github.bogdanlivadariu.reporting.cucumber.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.github.bogdanlivadariu.reporting.cucumber.json.models.Row;
import com.github.bogdanlivadariu.reporting.cucumber.json.models.StepRow;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;

public class Helpers {
    private Handlebars handlebar;

    public Helpers(Handlebars handlebar) {
        this.handlebar = handlebar;
    }

    public Handlebars registerHelpers() {
        handlebar.registerHelper("date", dateHelper());
        handlebar.registerHelper("embedding", embeddingHelper());
        handlebar.registerHelper("result-color", resultColorHelper());
        handlebar.registerHelper("resolve-title", resolveTitleHelper());
        handlebar.registerHelper("is-collapsed", isCollapsedHelper());
        handlebar.registerHelper("now", nowHelper());
        handlebar.registerHelper("do_table", doTableHelper());
        handlebar.registerHelper("do_table_step", doTableHelperForStep());
        return handlebar;
    }

    private Helper<Long> dateHelper() {
        return (arg0, arg1) -> {
            PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendDays()
                .appendSuffix(" d : ")
                .appendHours()
                .appendSuffix(" h : ")
                .appendMinutes()
                .appendSuffix(" m : ")
                .appendSeconds()
                .appendSuffix(" s : ")
                .appendMillis()
                .appendSuffix(" ms")
                .toFormatter();
            return formatter.print(new Period((arg0 * 1) / 1000000));
        };
    }

    private Helper<String> embeddingHelper() {
        return (arg0, arg1) -> {
            String toReturn;
            String id = UUID.randomUUID().toString();
            int index = arg1.param(1);
            if (arg1.param(0).toString().contains("image")) {
                toReturn =
                    "<button 'type='button'"
                        + "class='btn btn-primary'"
                        + "data-toggle='modal' "
                        + "data-target='#" + id + "'>"
                        + "  Screenshot " + ++index + ""
                        + "</button>";
                toReturn +=
                    "<div id='" + id + "'"
                        + "class='modal fade'"
                        + "tabindex='-1'"
                        + "role='dialog'"
                        + "aria-labelledby='myModalLabel'"
                        + "aria-hidden='true'>"
                        + "  <div style='width:90%;height:90%;'"
                        + "  class='modal-dialog'>"
                        + "    <div class='modal-content'>"
                        + "      <div class='modal-body'>"
                        + "        <img "
                        + "        src='data:image/png;base64," + arg0 + "'"
                        + "        class='img-responsive'>"
                        + "      </div>"
                        + "    </div>"
                        + "  </div>"
                        + "</div>";
            } else {
                toReturn = "<pre>" + arg0 + "</pre>";
            }
            return toReturn;
        };
    }

    private Helper<String> resultColorHelper() {
        return (arg0, arg1) -> checkState(
            arg0.toLowerCase(),
            Constants.INFO,
            Constants.SUCCESS,
            Constants.DANGER,
            Constants.WARNING);
    }

    private Helper<String> resolveTitleHelper() {
        return (arg0, arg1) -> checkState(
            arg0.toLowerCase(),
            Constants.THIS_STEP_HAS_BEEN_SKIPPED,
            Constants.THIS_STEP_HAS_PASSED,
            Constants.THIS_STEP_HAS_FAILED,
            Constants.THIS_STEP_HAS_NOT_BEEN_DEFINED);
    }

    private Helper<String> isCollapsedHelper() {
        return (arg0, arg1) -> checkState(
            arg0.toLowerCase(),
            Constants.COLLAPSE_IN,
            Constants.COLLAPSE,
            Constants.COLLAPSE_IN,
            Constants.COLLAPSE_IN);
    }

    private Helper<Object> nowHelper() {
        return (context, options) -> {
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(date);
            TimeZone tz = cal.getTimeZone();
            return now + " " + tz.getID();
        };
    }

    private Helper<List<StepRow>> doTableHelperForStep() {
        return (rows, arg1) -> {
            String tableContent = "<table  class='table table-condensed table-hover'>";
            int indexRow = 0;
            for (StepRow row : rows) {
                indexRow++;
                if (indexRow == 1) {
                    tableContent += "<thead><tr>";
                } else if (indexRow == 2) {
                    tableContent += "<tbody><tr>";
                } else {
                    tableContent += "<tr>";
                }
                for (String cell : row.getCells()) {
                    if (indexRow == 1) {
                        tableContent += "<th>" + cell + "</th>";
                    } else {
                        tableContent += "<td>" + cell + "</td>";
                    }
                }
                if (indexRow == 1) {
                    tableContent += "</tr></thead>";
                } else {
                    tableContent += "</tr>";
                }
            }
            tableContent += "</tbody></table>";
            return tableContent;
        };
    }
    
    
    private Helper<List<Row>> doTableHelper() {
        return (rows, arg1) -> {
            String tableContent = "<table  class='table table-condensed table-hover'>";
            int indexRow = 0;
            for (Row row : rows) {
                indexRow++;
                if (indexRow == 1) {
                    tableContent += "<thead><tr>";
                } else if (indexRow == 2) {
                    tableContent += "<tbody><tr>";
                } else {
                    tableContent += "<tr>";
                }
                for (String cell : row.getCells()) {
                    if (indexRow == 1) {
                        tableContent += "<th>" + cell + "</th>";
                    } else {
                        tableContent += "<td>" + cell + "</td>";
                    }
                }
                if (indexRow == 1) {
                    tableContent += "</tr></thead>";
                } else {
                    tableContent += "</tr>";
                }
            }
            tableContent += "</tbody></table>";
            return tableContent;
        };
    }

    private CharSequence checkState(String arg0, String retValue1, String retValue2, String retValue3,
        String retValue4) {
        switch (arg0.toLowerCase()) {
            case Constants.SKIPPED:
                return retValue1;
            case Constants.PASSED:
                return retValue2;
            case Constants.FAILED:
                return retValue3;
            case Constants.UNDEFINED:
                return retValue4;
            default:
        }
        return Constants.UNDEFINED;
    }
}

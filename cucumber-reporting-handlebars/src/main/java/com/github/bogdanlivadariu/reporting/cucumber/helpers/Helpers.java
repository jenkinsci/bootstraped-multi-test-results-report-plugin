package com.github.bogdanlivadariu.reporting.cucumber.helpers;

import java.io.IOException;
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
                String formatted = formatter.print(new Period((arg0 * 1) / 1000000));
                return formatted;
            }
        });

        handlebar.registerHelper("embedding", new Helper<String>() {
            @Override
            public CharSequence apply(String arg0, Options arg1) throws IOException {
                String toReturn = "";
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
            }
        });

        handlebar.registerHelper("result-color", new Helper<String>() {
            @Override
            public CharSequence apply(String arg0, Options arg1) throws IOException {
                switch (arg0.toLowerCase()) {
                    case "skipped":
                        return "info";
                    case "passed":
                        return "success";
                    case "failed":
                        return "danger";
                }
                return "undefined";
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

        handlebar.registerHelper("latest-lib", new Helper<Object>() {
            @Override
            public CharSequence apply(Object context, Options options) throws IOException {
                return Constants.LATEST_VERSION;
            }
        });

        handlebar.registerHelper("do_table", new Helper<List<Row>>() {
            @Override
            public CharSequence apply(List<Row> rows, Options arg1) throws IOException {
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
            }
        });
        return handlebar;
    }
}

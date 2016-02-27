package com.github.bogdanlivadariu.reporting.commons;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class ResourceUtil {

    public static enum WEB_RESOURCE {
        BOOTSTRAP_THEME_CSS("bootstrap-theme.min.css"),
        BOOTSTRAP_TOGGLE_JS("bootstrap-toggle.min.js"),
        BOOTSTRAP_TOGGLE_CSS("bootstrap-toggle.min.css"),
        BOOTSTRAP_MIN_CSS("bootstrap.min.css"),
        BOOTSTRAP_MIN_JS("bootstrap.min.js"),
        HIGHCHARTS_EXPORTING("exporting.js"),
        HIGHCHARTS_3D("highcharts-3d.js"),
        HIGHCHARTS_JS("highcharts.js"),
        JQUERY("jquery.min.js");

        private final String name;

        private WEB_RESOURCE(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public static void copyResource(WEB_RESOURCE source, String destPath) throws IOException {
        File sourceFile =
            new File(ResourceUtil.class.getResource("/web/" + source.toString()).getPath());
        File destFile = new File(destPath);
        File tmpFile = new File(destFile.getAbsoluteFile() + File.separator + sourceFile.getName());

        FileUtils.copyFile(sourceFile, tmpFile);
    }
}

package com.github.bogdanlivadariu.reporting.cucumber.builder;

import static com.github.bogdanlivadariu.reporting.cucumber.helpers.Constants.FEATURES_FAILED_OVERVIEW;
import static com.github.bogdanlivadariu.reporting.cucumber.helpers.Constants.FEATURES_OVERVIEW;
import static com.github.bogdanlivadariu.reporting.cucumber.helpers.Constants.FEATURES_PASSED_OVERVIEW;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.github.bogdanlivadariu.reporting.cucumber.helpers.Constants;
import com.github.bogdanlivadariu.reporting.cucumber.helpers.Helpers;
import com.github.bogdanlivadariu.reporting.cucumber.json.models.Feature;
import com.github.bogdanlivadariu.reporting.cucumber.json.models.Tag;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.google.gson.Gson;

public class CucumberReportBuilder {

    private final String FEATURE_SUMMARY_REPORT = "cucumber-reporting/featureSummaryReport";

    private final String FEATURE_OVERVIEW_REPORT = "cucumber-reporting/featureOverviewReport";

    private final String FEATURE_TAG_REPORT;

    private final String REPORTS_SUMMARY_PATH;

    private final String REPORTS_OVERVIEW_PATH;

    private final String TARGET_BUILD_PATH;

    private Gson gs = new Gson();

    private Handlebars bars = new Helpers(new Handlebars()).registerHelpers();

    private List<Feature> processedFeatures = null;

    public CucumberReportBuilder(List<String> jsonReports, String targetBuildPath) throws FileNotFoundException,
        IOException {
        this.TARGET_BUILD_PATH = targetBuildPath;
        this.REPORTS_SUMMARY_PATH = targetBuildPath + "/feature-reports/";

        this.REPORTS_OVERVIEW_PATH = targetBuildPath + "/";
        this.FEATURE_TAG_REPORT = targetBuildPath + "/tag-reports/";
        processedFeatures = prepareData(jsonReports);
    }

    private void writeFeatureSummaryReports() throws IOException {
        Template template = bars.compile(FEATURE_SUMMARY_REPORT);
        for (Feature feature : processedFeatures) {
            String generatedFeatureHtmlContent = template.apply(feature);
            // generatedFeatureSummaryReports.put(feature.getUniqueID(), generatedFeatureHtmlContent);
            FileUtils.writeStringToFile(new File(REPORTS_SUMMARY_PATH + feature.getUniqueID() + ".html"),
                generatedFeatureHtmlContent);
        }
    }

    private void writeFeatureOverviewReport() throws IOException {
        Template template = bars.compile(FEATURE_OVERVIEW_REPORT);
        AllFeatureReports allFeatures = new AllFeatureReports(FEATURES_OVERVIEW, processedFeatures);
        FileUtils.writeStringToFile(new File(REPORTS_OVERVIEW_PATH + "featuresOverview.html"),
            template.apply(allFeatures));
    }

    private void writeFeaturePassedReport() throws IOException {
        Template template = bars.compile(FEATURE_OVERVIEW_REPORT);
        Predicate<Feature> p = new Predicate<Feature>() {
            @Override
            public boolean test(Feature t) {
                return t.getOverall_status().equalsIgnoreCase(Constants.PASSED);
            }
        };
        List<Feature> onlyPassed = cast(processedFeatures.stream()
            .filter(p)
            .collect(Collectors.toList()));

        AllFeatureReports allFeatures = new AllFeatureReports(FEATURES_PASSED_OVERVIEW, onlyPassed);
        FileUtils.writeStringToFile(new File(REPORTS_OVERVIEW_PATH + "featuresPassed.html"),
            template.apply(allFeatures));
    }

    private void writeFeatureFailedReport() throws IOException {
        Template template = bars.compile(FEATURE_OVERVIEW_REPORT);
        Predicate<Feature> p = new Predicate<Feature>() {
            @Override
            public boolean test(Feature t) {
                return t.getOverall_status().equalsIgnoreCase(Constants.FAILED);
            }
        };
        List<Feature> onlyFailed = cast(processedFeatures.stream()
            .filter(p)
            .collect(Collectors.toList()));

        AllFeatureReports allFeatures = new AllFeatureReports(FEATURES_FAILED_OVERVIEW, onlyFailed);
        FileUtils.writeStringToFile(new File(REPORTS_OVERVIEW_PATH + "featuresFailed.html"),
            template.apply(allFeatures));
    }

    private void writeFeatureTagsReport() throws IOException {
        LinkedHashMap<String, List<Feature>> allTags = new LinkedHashMap<>();
        for (Feature feature : processedFeatures) {
            // move the outputFileLocation one folder up for all the features that will be built
            feature.setOutputFileLocation("../" + feature.getOutputFileLocation());
            // put the tags in the proper place
            if (feature.getTags().length < 1) {
                if (allTags.containsKey(Constants.UNTAGGED)) {
                    allTags.get(Constants.UNTAGGED).add(feature);
                } else {
                    List<Feature> tagFeatures = new ArrayList<Feature>();
                    tagFeatures.add(feature);
                    allTags.put(Constants.UNTAGGED, tagFeatures);
                }
                continue;
            }
            for (Tag tag : feature.getTags()) {
                String tagName = tag.getName();
                if (allTags.containsKey(tagName)) {
                    allTags.get(tagName).add(feature);
                } else {
                    List<Feature> tagFeatures = new ArrayList<Feature>();
                    tagFeatures.add(feature);
                    allTags.put(tagName, tagFeatures);
                }
            }
            Template template = bars.compile(FEATURE_OVERVIEW_REPORT);

            for (Entry<String, List<Feature>> entry : allTags.entrySet()) {
                AllFeatureReports specificTagFeatures =
                    new AllFeatureReports(entry.getKey(), entry.getValue());
                FileUtils.writeStringToFile(new File(FEATURE_TAG_REPORT + entry.getKey() + ".html"),
                    template.apply(specificTagFeatures));
            }
        }
    }

    private List<Feature> prepareData(List<String> jsonReports) throws FileNotFoundException, IOException {
        List<Feature> processedFeatures = new ArrayList<>();
        for (String jsonReport : jsonReports) {
            File jsonFileReport = new File(jsonReport);
            String gson = null;
            FileInputStream fis = new FileInputStream(jsonFileReport);
            gson = IOUtils.toString(fis);
            fis.close();

            if (gson.isEmpty()) {
                continue;
            }
            Feature[] features = gs.fromJson(gson, Feature[].class);

            for (Feature feature : features) {
                processedFeatures.add(feature.postProcess());
            }

        }
        return processedFeatures;
    }

    /**
     * @return true if all the features have passed, false if at least one has failed.
     * @throws IOException
     */
    public boolean writeReportsOnDisk() throws IOException {
        copyResources();
        writeFeatureSummaryReports();
        writeFeatureOverviewReport();
        writeFeaturePassedReport();
        writeFeatureFailedReport();
        writeFeatureTagsReport();
        for (Feature feature : processedFeatures) {
            if (feature.getOverall_status().equals(Constants.FAILED)) {
                return false;
            }
        }
        return true;
    }

    private void copyResources() throws IOException {

        File dest = new File(TARGET_BUILD_PATH + "/resources/style.css");
        FileUtils.forceMkdir(dest.getParentFile());
        InputStream style = CucumberReportBuilder.class.getResourceAsStream("/cucumber-reporting/style.css");

        Files.copy(style, dest.toPath());

    }

    @SuppressWarnings("unchecked")
    public static <T extends List< ? >> T cast(Object obj) {
        return (T) obj;
    }
}

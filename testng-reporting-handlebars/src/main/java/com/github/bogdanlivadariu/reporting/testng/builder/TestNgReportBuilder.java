package com.github.bogdanlivadariu.reporting.testng.builder;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.bogdanlivadariu.reporting.testng.helpers.Helpers;
import com.github.bogdanlivadariu.reporting.testng.helpers.StringUtils;
import com.github.bogdanlivadariu.reporting.testng.xml.models.ClassModel;
import com.github.bogdanlivadariu.reporting.testng.xml.models.SuiteModel;
import com.github.bogdanlivadariu.reporting.testng.xml.models.TestModel;
import com.github.bogdanlivadariu.reporting.testng.xml.models.TestngResultsModel;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.apache.commons.io.FileUtils;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class TestNgReportBuilder {
    public static final String TESTS_BY_CLASS_OVERVIEW = "testsByClassOverview.html";

    private final String testOverviewPath;

    private final String classesSummaryPath;

    private final String testSummaryReport = "testng-reporting/testCaseSummaryReport";

    private final String testOverviewReport = "testng-reporting/testsByClassOverview";

    private final String testNameOverviewReport = "testng-reporting/testsByNameOverview";

    private final List<TestngResultsModel> processedTestNgReports;


    public TestNgReportBuilder(List<String> xmlReports, String targetBuildPath)
            throws XMLStreamException, FactoryConfigurationError, IOException {
        testOverviewPath = targetBuildPath + "/";
        classesSummaryPath = targetBuildPath + "/classes-summary/";
        processedTestNgReports = new ArrayList<>();

        XmlMapper mapper = new XmlMapper();

        for (String xml : xmlReports) {
            InputStream inputStream = new FileInputStream(xml);
            XMLStreamReader xmlStream = XMLInputFactory.newInstance().createXMLStreamReader(inputStream);
            TestngResultsModel ts = mapper.readValue(xmlStream, TestngResultsModel.class);
            ts.postProcess();
            processedTestNgReports.add(ts);
            inputStream.close();
            xmlStream.close();
        }
    }

    public List<TestngResultsModel> getProcessedTestNgReports() {
        return processedTestNgReports;
    }

    private void writeTestsByClassOverview() throws IOException {
        Template template = new Helpers(new Handlebars()).registerHelpers().compile(testOverviewReport);
        AllTestNgReports allTestNgReports =
                new AllTestNgReports("Tests by class overview report", processedTestNgReports);
        FileUtils.writeStringToFile(new File(testOverviewPath + TESTS_BY_CLASS_OVERVIEW),
                template.apply(allTestNgReports), StandardCharsets.UTF_8);
    }

    private void writeTestsByNameOverview() throws IOException {
        Template template = new Helpers(new Handlebars()).registerHelpers().compile(testNameOverviewReport);
        AllTestNgReports allTestNgReports =
                new AllTestNgReports("Tests by name overview report", processedTestNgReports);
        FileUtils.writeStringToFile(new File(testOverviewPath + "testsByNameOverview.html"),
                template.apply(allTestNgReports), StandardCharsets.UTF_8);
    }

    private void writeTestCaseSummaryReport() throws IOException, NoSuchAlgorithmException {
        Template templateTestClassReport =
                new Helpers(new Handlebars()).registerHelpers().compile(testSummaryReport);
        for (TestngResultsModel tngr : processedTestNgReports) {
            for (SuiteModel sm : tngr.getSuites()) {
                for (TestModel tm : sm.getTests()) {
                    generateHtmlReport(templateTestClassReport, tm);
                }
            }
        }
    }

    private void generateHtmlReport(Template templateTestClassReport, TestModel tm) throws IOException, NoSuchAlgorithmException {
        for (ClassModel cm : tm.getClasses()) {

            File file = new File(classesSummaryPath + StringUtils.getMd5From(tm.getName() + cm.getName()) + ".html");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            OutputStream os =
                    new FileOutputStream(file);

            PrintWriter rw = new PrintWriter(os);
            rw.print(templateTestClassReport.apply(cm));
            rw.close();
            os.close();
        }
    }

    private String getMd5(String source) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(StandardCharsets.UTF_8.encode(source));
        return String.format("%032x", new BigInteger(1, md5.digest()));
    }

    public boolean writeReportsOnDisk() throws IOException, NoSuchAlgorithmException {
        writeTestsByClassOverview();
        writeTestsByNameOverview();
        writeTestCaseSummaryReport();
        for (TestngResultsModel ts : processedTestNgReports) {
            if (ts.getTotalClassesFailed() >= 1
                    || ts.getTotalClassesSkipped() >= 1) {
                return false;
            }
        }
        return processedTestNgReports.size() > 0;
    }
}

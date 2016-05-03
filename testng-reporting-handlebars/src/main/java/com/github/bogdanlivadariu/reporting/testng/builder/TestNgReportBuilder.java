package com.github.bogdanlivadariu.reporting.testng.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.io.FileUtils;

import com.github.bogdanlivadariu.reporting.testng.helpers.Helpers;
import com.github.bogdanlivadariu.reporting.testng.xml.models.ClassModel;
import com.github.bogdanlivadariu.reporting.testng.xml.models.SuiteModel;
import com.github.bogdanlivadariu.reporting.testng.xml.models.TestModel;
import com.github.bogdanlivadariu.reporting.testng.xml.models.TestngResultsModel;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

public class TestNgReportBuilder {
    private String testSummaryReport = "testng-reporting/testCaseSummaryReport";

    private String testOverviewReport = "testng-reporting/testsByClassOverview";

    private String testNameOverviewReport = "testng-reporting/testsByNameOverview";

    private final String testOverviewPath;

    private final String classesSummaryPath;

    private List<TestngResultsModel> processedTestNgReports;

    public TestNgReportBuilder(List<String> xmlReports, String targetBuildPath)
        throws JAXBException, XMLStreamException, FactoryConfigurationError, IOException {
        testOverviewPath = targetBuildPath + "/";
        classesSummaryPath = targetBuildPath + "/classes-summary/";
        processedTestNgReports = new ArrayList<>();

        JAXBContext cntx = JAXBContext.newInstance(TestngResultsModel.class);

        Unmarshaller unm = cntx.createUnmarshaller();

        for (String xml : xmlReports) {
            InputStream inputStream = new FileInputStream(xml);
            XMLStreamReader xmlStream = XMLInputFactory.newInstance().createXMLStreamReader(inputStream);
            TestngResultsModel ts = (TestngResultsModel) unm.unmarshal(xmlStream);
            ts.postProcess();
            processedTestNgReports.add(ts);
            inputStream.close();
            xmlStream.close();
        }
    }

    private void writeTestsByClassOverview() throws IOException {
        Template template = new Helpers(new Handlebars()).registerHelpers().compile(testOverviewReport);
        AllTestNgReports allTestNgReports =
            new AllTestNgReports("Tests by class overview report", processedTestNgReports);
        FileUtils.writeStringToFile(new File(testOverviewPath + "testsByClassOverview.html"),
            template.apply(allTestNgReports));
    }

    private void writeTestsByNameOverview() throws IOException {
        Template template = new Helpers(new Handlebars()).registerHelpers().compile(testNameOverviewReport);
        AllTestNgReports allTestNgReports =
            new AllTestNgReports("Tests by name overview report", processedTestNgReports);
        FileUtils.writeStringToFile(new File(testOverviewPath + "testsByNameOverview.html"),
            template.apply(allTestNgReports));
    }

    private void writeTestCaseSummaryReport() throws IOException {
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

    private void generateHtmlReport(Template templateTestClassReport, TestModel tm) throws IOException {
        for (ClassModel cm : tm.getClasses()) {

            File file = new File(classesSummaryPath + tm.getName() + cm.getName() + ".html");
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

    public boolean writeReportsOnDisk() throws IOException {
        writeTestsByClassOverview();
        writeTestsByNameOverview();
        writeTestCaseSummaryReport();
        for (TestngResultsModel ts : processedTestNgReports) {
            if (ts.getTotalClassesFailed() > 1 || ts.getTotalClassesSkipped() > 1) {
                return false;
            }
        }
        return true;
    }

}

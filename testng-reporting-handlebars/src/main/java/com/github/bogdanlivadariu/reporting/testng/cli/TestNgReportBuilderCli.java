package com.github.bogdanlivadariu.reporting.testng.cli;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.FileUtils;

import com.github.bogdanlivadariu.reporting.testng.builder.TestNgReportBuilder;

public class TestNgReportBuilderCli {

    public static void main(String[] args) throws JAXBException, IOException, XMLStreamException, FactoryConfigurationError {
        List<String> xmlReports = new ArrayList<String>();
        String[] extensions = {"xml"};
        String xmlPath = System.getProperty("xmlPath");
        String outputPath = System.getProperty("reportsOutputPath");
        if (xmlPath == null || outputPath == null) {
            throw new RuntimeException("xmlPath or reportsOutputPath variables have not been set");
        }
        Object[] files = FileUtils.listFiles(new File(xmlPath), extensions, false).toArray();
        System.out.println("Found " + files.length + " xml files");
        for (Object absFilePath : files) {
            System.out.println("Found an xml: " + absFilePath);
            xmlReports.add(((File) absFilePath).getAbsolutePath());
        }

        TestNgReportBuilder repo = new TestNgReportBuilder(xmlReports, outputPath);
        repo.writeReportsOnDisk();
    }
}

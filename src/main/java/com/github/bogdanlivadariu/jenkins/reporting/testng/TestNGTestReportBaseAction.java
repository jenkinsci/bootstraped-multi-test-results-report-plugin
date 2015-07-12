package com.github.bogdanlivadariu.jenkins.reporting.testng;

import hudson.FilePath;
import hudson.model.Action;
import hudson.model.DirectoryBrowserSupport;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

public abstract class TestNGTestReportBaseAction implements Action {

    public String getUrlName() {
        return "testng-reports-with-handlebars";
    }

    public String getDisplayName() {
        return "View TestNG Reports";
    }

    public String getIconFileName() {
        return "/plugin/bootstraped-multi-test-results-report/testng.png";
    }

    public void doDynamic(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        DirectoryBrowserSupport dbs =
            new DirectoryBrowserSupport(this, new FilePath(this.dir()), this.getTitle(),
                "graph.gif", false);
        dbs.setIndexFileName("testsByClassOverview.html");
        dbs.generateResponse(req, rsp, this);
    }

    protected abstract String getTitle();

    protected abstract File dir();
}

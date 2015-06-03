package com.github.bogdanlivadariu.jenkins.reporting.cucumber;

import hudson.FilePath;
import hudson.model.Action;
import hudson.model.DirectoryBrowserSupport;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

public abstract class CucumberTestReportBaseAction implements Action {

    public String getUrlName() {
        return "cucumber-reports-with-handlebars";
    }

    public String getDisplayName() {
        return "View Reports";
    }

    public String getIconFileName() {
        return "/plugin/bootstraped-multi-test-results-report/logo.png";
    }

    public void doDynamic(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        DirectoryBrowserSupport dbs =
            new DirectoryBrowserSupport(this, new FilePath(this.dir()), this.getTitle(),
                "graph.gif", false);
        dbs.setIndexFileName("featuresOverview.html");
        dbs.generateResponse(req, rsp, this);
    }

    protected abstract String getTitle();

    protected abstract File dir();
}

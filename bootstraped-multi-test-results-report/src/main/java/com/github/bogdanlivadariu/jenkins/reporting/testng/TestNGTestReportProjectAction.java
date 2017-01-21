package com.github.bogdanlivadariu.jenkins.reporting.testng;

import hudson.model.AbstractProject;
import hudson.model.ProminentProjectAction;

public class TestNGTestReportProjectAction extends TestNGTestReportBaseAction implements ProminentProjectAction {

    private final AbstractProject<?, ?> project;

    public TestNGTestReportProjectAction(AbstractProject<?, ?> project) {
        this.project = project;
    }

    public String getUrlName() {
        return project != null
            ? project.getLastBuild().getId() + "/testng-reports-with-handlebars/testsByClassOverview.html"
            : "if-this-happens-contact-dev";
    }
}

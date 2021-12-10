package com.github.bogdanlivadariu.jenkins.reporting.testng;

import hudson.model.AbstractProject;
import hudson.model.ProminentProjectAction;
import hudson.model.Run;

import java.util.Optional;

public class TestNGTestReportProjectAction extends TestNGTestReportBaseAction implements ProminentProjectAction {

    private final AbstractProject<?, ?> project;

    public TestNGTestReportProjectAction(AbstractProject<?, ?> project) {
        this.project = project;
    }

    public String getUrlName() {
        return Optional.ofNullable(project)
                .map(AbstractProject::getLastBuild)
                .map(Run::getId)
                .map(it -> it + "/testng-reports-with-handlebars/testsByClassOverview.html")
                .orElse("if-this-happens-contact-dev");
    }
}

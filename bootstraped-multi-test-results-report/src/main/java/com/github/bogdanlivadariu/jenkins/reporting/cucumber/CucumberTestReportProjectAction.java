package com.github.bogdanlivadariu.jenkins.reporting.cucumber;

import hudson.model.AbstractProject;
import hudson.model.ProminentProjectAction;

public class CucumberTestReportProjectAction extends CucumberTestReportBaseAction implements ProminentProjectAction {

    private final AbstractProject<?, ?> project;

    public CucumberTestReportProjectAction(AbstractProject<?, ?> project) {
        this.project = project;
    }

    public String getUrlName() {
        return project != null
            ? project.getLastBuild().getId() + "/cucumber-reports-with-handlebars/featuresOverview.html"
            : "if-this-happens-contact-dev";
    }
}
package com.github.bogdanlivadariu.jenkins.reporting.cucumber;

import hudson.model.AbstractProject;
import hudson.model.ProminentProjectAction;
import hudson.model.Run;

import java.util.Optional;

public class CucumberTestReportProjectAction extends CucumberTestReportBaseAction implements ProminentProjectAction {

    private final AbstractProject<?, ?> project;

    public CucumberTestReportProjectAction(AbstractProject<?, ?> project) {
        this.project = project;
    }

    public String getUrlName() {
        return Optional.ofNullable(project)
                .map(AbstractProject::getLastBuild)
                .map(Run::getId)
                .map(it -> it + "/cucumber-reports-with-handlebars/featuresOverview.html")
                .orElse("if-this-happens-contact-dev");
    }
}

package com.github.bogdanlivadariu.jenkins.reporting.rspec;

import hudson.model.AbstractProject;
import hudson.model.ProminentProjectAction;
import hudson.model.Run;

import java.util.Optional;

public class RSpecTestReportProjectAction extends RSpecTestReportBaseAction implements ProminentProjectAction {

    private final AbstractProject<?, ?> project;

    public RSpecTestReportProjectAction(AbstractProject<?, ?> project) {
        this.project = project;
    }

    public String getUrlName() {
        return Optional.ofNullable(project)
                .map(AbstractProject::getLastBuild)
                .map(Run::getId)
                .map(it -> it + "/rspec-reports-with-handlebars/testsByClassOverview.html")
                .orElse("if-this-happens-contact-dev");
    }
}

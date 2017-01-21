package com.github.bogdanlivadariu.jenkins.reporting.rspec;

import hudson.model.AbstractProject;
import hudson.model.ProminentProjectAction;

public class RSpecTestReportProjectAction extends RSpecTestReportBaseAction implements ProminentProjectAction {

    private final AbstractProject<?, ?> project;

    public RSpecTestReportProjectAction(AbstractProject<?, ?> project) {
        this.project = project;
    }

    public String getUrlName() {
        return project != null
            ? project.getLastBuild().getId() + "/rspec-reports-with-handlebars/testsByClassOverview.html"
            : "if-this-happens-contact-dev";
    }
}

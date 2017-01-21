package com.github.bogdanlivadariu.jenkins.reporting.junit;

import hudson.model.AbstractProject;
import hudson.model.ProminentProjectAction;

public class JUnitTestReportProjectAction extends JUnitTestReportBaseAction implements ProminentProjectAction {

    private final AbstractProject<?, ?> project;

    public JUnitTestReportProjectAction(AbstractProject<?, ?> project) {
        this.project = project;
    }

    public String getUrlName() {
        return project != null
            ? project.getLastBuild().getId() + "/junit-reports-with-handlebars/testSuitesOverview.html"
            : "if-this-happens-contact-dev";
    }
}
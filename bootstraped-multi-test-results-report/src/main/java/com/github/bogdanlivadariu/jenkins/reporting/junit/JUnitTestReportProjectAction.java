package com.github.bogdanlivadariu.jenkins.reporting.junit;

import hudson.model.AbstractProject;
import hudson.model.ProminentProjectAction;
import hudson.model.Run;

import java.util.Optional;

public class JUnitTestReportProjectAction extends JUnitTestReportBaseAction implements ProminentProjectAction {

    private final AbstractProject<?, ?> project;

    public JUnitTestReportProjectAction(AbstractProject<?, ?> project) {
        this.project = project;
    }

    public String getUrlName() {
        return Optional.ofNullable(project)
                .map(AbstractProject::getLastBuild)
                .map(Run::getId)
                .map(it -> it + "/junit-reports-with-handlebars/testSuitesOverview.html")
                .orElse("if-this-happens-contact-dev");
    }
}

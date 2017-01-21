package com.github.bogdanlivadariu.jenkins.reporting.rspec;

import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;

public class RSpecTestReportBuildStepDescriptor extends BuildStepDescriptor<Publisher> {

    @Override
    public String getDisplayName() {
        return "Publish RSpec reports generated with handlebars";
    }

    @Override public boolean isApplicable(Class<? extends AbstractProject> aClass) {
        return true;
    }
}

package com.github.bogdanlivadariu.jenkins.reporting.junit;

import hudson.model.ProminentProjectAction;
import hudson.model.AbstractItem;
import hudson.model.AbstractProject;
import hudson.model.Run;

import java.io.File;

public class JUnitTestReportProjectAction extends JUnitTestReportBaseAction implements ProminentProjectAction {

    private final AbstractItem project;

    public JUnitTestReportProjectAction(AbstractItem project) {
        this.project = project;
    }

    @Override
    protected File dir() {
        if (this.project instanceof AbstractProject) {
            AbstractProject abstractProject = (AbstractProject) this.project;

            Run run = abstractProject.getLastCompletedBuild();
            if (run != null) {
                File javadocDir = getBuildArchiveDir(run);

                if (javadocDir.exists()) {
                    return javadocDir;
                }
            }
        }

        return getProjectArchiveDir(this.project);
    }

    private File getProjectArchiveDir(AbstractItem project) {
        return new File(project.getRootDir(), "junit-reports-with-handlebars");
    }

    /**
     * Gets the directory where the HTML report is stored for the given build.
     */
    private File getBuildArchiveDir(Run run) {
        return new File(run.getRootDir(), "junit-reports-with-handlebars");
    }

    @Override
    protected String getTitle() {
        return this.project.getDisplayName() + " html2";
    }
}

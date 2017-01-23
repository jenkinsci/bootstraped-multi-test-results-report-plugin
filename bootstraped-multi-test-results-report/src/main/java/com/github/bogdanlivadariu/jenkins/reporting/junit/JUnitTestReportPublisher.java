package com.github.bogdanlivadariu.jenkins.reporting.junit;

import com.github.bogdanlivadariu.jenkins.reporting.SafeArchiveServingRunAction;
import com.github.bogdanlivadariu.reporting.junit.builder.JUnitReportBuilder;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.*;
import hudson.slaves.SlaveComputer;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import jenkins.tasks.SimpleBuildStep;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

import static com.github.bogdanlivadariu.jenkins.reporting.Helper.findFiles;
import static com.github.bogdanlivadariu.jenkins.reporting.Helper.fullPathToFiles;

@SuppressWarnings("unchecked")
public class JUnitTestReportPublisher extends Publisher implements SimpleBuildStep {

    private static final String DEFAULT_FILE_INCLUDE_PATTERN = "**/*.xml";

    private final String reportsDirectory;

    private final String fileIncludePattern;

    private final String fileExcludePattern;

    private final boolean markAsUnstable;

    private final boolean copyHTMLInWorkspace;

    @DataBoundConstructor
    public JUnitTestReportPublisher(String reportsDirectory, String fileIncludePattern, String fileExcludePattern,
        boolean markAsUnstable, boolean copyHTMLInWorkspace) {
        this.reportsDirectory = reportsDirectory;
        this.fileIncludePattern = fileIncludePattern;
        this.fileExcludePattern = fileExcludePattern;
        this.markAsUnstable = markAsUnstable;
        this.copyHTMLInWorkspace = copyHTMLInWorkspace;
    }

    public String getReportsDirectory() {
        return reportsDirectory;
    }

    public String getFileIncludePattern() {
        return fileIncludePattern;
    }

    public String getFileExcludePattern() {
        return fileExcludePattern;
    }

    public boolean isMarkAsUnstable() {
        return markAsUnstable;
    }

    public boolean isCopyHTMLInWorkspace() {
        return copyHTMLInWorkspace;
    }

    public boolean generateReport(Run<?, ?> build, FilePath workspace, TaskListener listener)
        throws IOException, InterruptedException {

        listener.getLogger().println("[JUnitReportPublisher] Compiling JUnit Html Reports ...");

        // source directory (possibly on slave)
        FilePath workspaceJsonReportDirectory;
        if (getReportsDirectory().isEmpty()) {
            workspaceJsonReportDirectory = workspace;
        } else {
            workspaceJsonReportDirectory = new FilePath(workspace, getReportsDirectory());
        }

        // target directory (always on master)
        File targetBuildDirectory = new File(build.getRootDir(), "junit-reports-with-handlebars");
        if (!targetBuildDirectory.exists()) {
            if (!targetBuildDirectory.mkdirs()) {
                listener.getLogger().println("target dir was not created !!!");
            }

        }

        if (Computer.currentComputer() instanceof SlaveComputer) {
            listener.getLogger().println(
                "[JUnit test report builder] Copying XML files from slave: "
                    + workspaceJsonReportDirectory.getRemote() + " to master reports directory: "
                    + targetBuildDirectory);
        } else {
            listener.getLogger().println(
                "[JUnit test report builder] Copying XML files from: "
                    + workspaceJsonReportDirectory.getRemote()
                    + " to reports directory: " + targetBuildDirectory);
        }
        File targetBuildJsonDirectory = new File(targetBuildDirectory.getAbsolutePath() + "/xmlData");
        if (!targetBuildJsonDirectory.exists()) {
            targetBuildJsonDirectory.mkdirs();
        }
        String includePattern = (fileIncludePattern == null || fileIncludePattern.isEmpty()) ?
            DEFAULT_FILE_INCLUDE_PATTERN : fileIncludePattern;
        workspaceJsonReportDirectory.copyRecursiveTo(includePattern, new FilePath(
            targetBuildJsonDirectory));

        // generate the reports from the targetBuildDirectory
        Result result = Result.NOT_BUILT;
        String[] reportFiles =
            findFiles(targetBuildJsonDirectory, getFileIncludePattern(),
                getFileExcludePattern(), DEFAULT_FILE_INCLUDE_PATTERN);
        if (reportFiles.length > 0) {
            listener.getLogger().println(
                String.format("[JUnitReportPublisher] Found %d xml files.", reportFiles.length));
            int jsonIndex = 0;
            for (String reportFile : reportFiles) {
                listener.getLogger().println(
                    "[JUnit test report builder] " + jsonIndex + ". Found a xml file: " + reportFile);
                jsonIndex++;
            }
            listener.getLogger().println("[JUnit test report builder] Generating HTML reports");

            try {
                for (String ss : fullPathToFiles(reportFiles, targetBuildJsonDirectory)) {
                    listener.getLogger().println("processing: " + ss);
                }
                JUnitReportBuilder rep =
                    new JUnitReportBuilder(fullPathToFiles(reportFiles, targetBuildJsonDirectory),
                        targetBuildDirectory.getAbsolutePath());

                boolean featuresResult = rep.writeReportsOnDisk();
                if (featuresResult) {
                    result = Result.SUCCESS;
                } else {
                    result = isMarkAsUnstable() ? Result.UNSTABLE : Result.FAILURE;
                }

                // finally copy to workspace, if needed
                if (isCopyHTMLInWorkspace()) {
                    FilePath workspaceCopyDirectory =
                        new FilePath(workspace, "junit-reports-with-handlebars");
                    if (workspaceCopyDirectory.exists()) {
                        workspaceCopyDirectory.deleteRecursive();
                    }
                    listener.getLogger().println(
                        "[JUnit test report builder] Copying report to workspace directory: " + workspaceCopyDirectory
                            .toURI());
                    new FilePath(targetBuildDirectory).copyRecursiveTo("**/*.html", workspaceCopyDirectory);
                }
            } catch (Exception e) {
                result = Result.FAILURE;
                listener.getLogger().println(
                    "[JUnit test report builder] there was an error generating the reports: " + e);
                for (StackTraceElement error : e.getStackTrace()) {
                    listener.getLogger().println(error);
                }
            }
        } else {
            result = Result.SUCCESS;
            listener.getLogger().println(
                "[JUnit test report builder] xml path for the reports might be wrong, " + targetBuildDirectory);
        }
        build.setResult(result);

        return true;
    }

    @Override public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath filePath, @Nonnull Launcher launcher,
        @Nonnull TaskListener taskListener) throws InterruptedException, IOException {
        generateReport(run, filePath, taskListener);

        SafeArchiveServingRunAction caa = new SafeArchiveServingRunAction(
            new File(run.getRootDir(), "junit-reports-with-handlebars"),
            "junit-reports-with-handlebars",
            JUnitReportBuilder.SUITE_OVERVIEW,
            JUnitTestReportBaseAction.ICON_LOCATON,
            JUnitTestReportBaseAction.DISPLAY_NAME);
        run.addAction(caa);
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override
    public Action getProjectAction(AbstractProject<?, ?> project) {
        return new JUnitTestReportProjectAction(project);
    }

    @Extension
    public static class DescriptorImpl extends JUnitTestReportBuildStepDescriptor {
    }
}
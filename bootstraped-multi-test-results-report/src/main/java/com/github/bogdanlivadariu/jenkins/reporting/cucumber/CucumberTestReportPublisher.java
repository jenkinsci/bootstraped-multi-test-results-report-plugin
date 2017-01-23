package com.github.bogdanlivadariu.jenkins.reporting.cucumber;

import com.github.bogdanlivadariu.jenkins.reporting.SafeArchiveServingRunAction;
import com.github.bogdanlivadariu.reporting.cucumber.builder.CucumberReportBuilder;
import com.github.bogdanlivadariu.reporting.cucumber.helpers.SpecialProperties;
import com.github.bogdanlivadariu.reporting.cucumber.helpers.SpecialProperties.SpecialKeyProperties;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.*;
import hudson.slaves.SlaveComputer;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import jenkins.tasks.SimpleBuildStep;
import org.apache.tools.ant.DirectoryScanner;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

import static com.github.bogdanlivadariu.jenkins.reporting.Helper.fullPathToFiles;

@SuppressWarnings("unchecked")
public class CucumberTestReportPublisher extends Publisher implements SimpleBuildStep {

    private static final String DEFAULT_FILE_INCLUDE_PATTERN = "**/*.json";

    private final String reportsDirectory;

    private final String fileIncludePattern;

    private final String fileExcludePattern;

    private final boolean markAsUnstable;

    private final boolean copyHTMLInWorkspace;

    private final boolean ignoreUndefinedSteps;

    private final SpecialProperties props;

    @DataBoundConstructor
    public CucumberTestReportPublisher(String reportsDirectory, String fileIncludePattern, String fileExcludePattern,
        boolean markAsUnstable, boolean copyHTMLInWorkspace, boolean ignoreUndefinedSteps) {
        this.reportsDirectory = reportsDirectory;
        this.fileIncludePattern = fileIncludePattern;
        this.fileExcludePattern = fileExcludePattern;
        this.markAsUnstable = markAsUnstable;
        this.copyHTMLInWorkspace = copyHTMLInWorkspace;
        this.ignoreUndefinedSteps = ignoreUndefinedSteps;

        SpecialProperties props = new SpecialProperties();
        props.getProperties().put(SpecialKeyProperties.IGNORE_UNDEFINED_STEPS, isIgnoreUndefinedSteps());
        this.props = props;

    }

    public String getReportsDirectory() {
        return this.reportsDirectory;
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

    public boolean isIgnoreUndefinedSteps() {
        return ignoreUndefinedSteps;
    }

    private String[] findJsonFiles(File targetDirectory, String fileIncludePattern, String fileExcludePattern) {
        DirectoryScanner scanner = new DirectoryScanner();
        if (fileIncludePattern == null || fileIncludePattern.isEmpty()) {
            scanner.setIncludes(new String[] {DEFAULT_FILE_INCLUDE_PATTERN});
        } else {
            scanner.setIncludes(new String[] {fileIncludePattern});
        }
        if (fileExcludePattern != null) {
            scanner.setExcludes(new String[] {fileExcludePattern});
        }
        scanner.setBasedir(targetDirectory);
        scanner.scan();
        return scanner.getIncludedFiles();
    }

    public boolean generateReport(Run<?, ?> build, FilePath workspace, TaskListener listener)
        throws IOException, InterruptedException {

        listener.getLogger().println("[CucumberReportPublisher] Compiling Cucumber Html Reports ...");

        // source directory (possibly on slave)
        FilePath workspaceJsonReportDirectory;
        if (getReportsDirectory().isEmpty()) {
            workspaceJsonReportDirectory = workspace;
        } else {
            workspaceJsonReportDirectory = new FilePath(workspace, getReportsDirectory());
        }

        // target directory (always on master)
        File targetBuildDirectory = new File(build.getRootDir(), "cucumber-reports-with-handlebars");
        if (!targetBuildDirectory.exists()) {
            if (!targetBuildDirectory.mkdirs()) {
                listener.getLogger().println("target dir was not created !!!");
            }
        }

        if (Computer.currentComputer() instanceof SlaveComputer) {
            listener.getLogger()
                .println("[Cucumber test report builder] Copying JSON files from slave: "
                    + workspaceJsonReportDirectory.getRemote() + " to master reports directory: "
                    + targetBuildDirectory);
        } else {
            listener.getLogger().println("[Cucumber test report builder] Copying JSON files from: "
                + workspaceJsonReportDirectory.getRemote() + " to reports directory: " + targetBuildDirectory);
        }
        File targetBuildJsonDirectory = new File(targetBuildDirectory.getAbsolutePath() + "/jsonData");
        if (!targetBuildJsonDirectory.exists()) {
            targetBuildJsonDirectory.mkdirs();
        }
        String includePattern = (fileIncludePattern == null || fileIncludePattern.isEmpty())
            ? DEFAULT_FILE_INCLUDE_PATTERN : fileIncludePattern;
        workspaceJsonReportDirectory.copyRecursiveTo(includePattern,
            new FilePath(targetBuildJsonDirectory));

        // generate the reports from the targetBuildDirectory
        Result result = Result.NOT_BUILT;
        String[] jsonReportFiles =
            findJsonFiles(targetBuildJsonDirectory, getFileIncludePattern(), getFileExcludePattern());
        if (jsonReportFiles.length > 0) {
            listener.getLogger()
                .println(String.format("[CucumberReportPublisher] Found %d json files.", jsonReportFiles.length));
            int jsonIndex = 0;
            for (String jsonReportFile : jsonReportFiles) {
                listener.getLogger().println(
                    "[Cucumber test report builder] " + jsonIndex + ". Found a json file: " + jsonReportFile);
                jsonIndex++;
            }
            listener.getLogger().println("[Cucumber test report builder] Generating HTML reports");

            try {
                for (String ss : fullPathToFiles(jsonReportFiles, targetBuildJsonDirectory)) {
                    listener.getLogger().println("processing: " + ss);
                }
                CucumberReportBuilder rep = new CucumberReportBuilder(
                    fullPathToFiles(jsonReportFiles, targetBuildJsonDirectory),
                    targetBuildDirectory.getAbsolutePath(), props);
                boolean featuresResult = rep.writeReportsOnDisk();
                if (featuresResult) {
                    result = Result.SUCCESS;
                } else {
                    result = isMarkAsUnstable() ? Result.UNSTABLE : Result.FAILURE;
                }

                // finally copy to workspace, if needed
                if (isCopyHTMLInWorkspace()) {
                    FilePath workspaceCopyDirectory =
                        new FilePath(workspace, "cucumber-reports-with-handlebars");
                    if (workspaceCopyDirectory.exists()) {
                        workspaceCopyDirectory.deleteRecursive();
                    }
                    listener.getLogger().println(
                        "[Cucumber test report builder] Copying report to workspace directory: "
                            + workspaceCopyDirectory.toURI());
                    new FilePath(targetBuildDirectory).copyRecursiveTo("**/*.html", workspaceCopyDirectory);
                }

            } catch (Exception e) {
                result = Result.FAILURE;
                listener.getLogger()
                    .println("[Cucumber test report builder] there was an error generating the reports: " + e);
                for (StackTraceElement error : e.getStackTrace()) {
                    listener.getLogger().println(error);
                }
            }
        } else {
            result = Result.SUCCESS;
            listener.getLogger().println(
                "[Cucumber test report builder] json path for the reports might be wrong, " + targetBuildDirectory);
        }
        build.setResult(result);

        return true;
    }

    @Override
    public Action getProjectAction(AbstractProject<?, ?> project) {
        return new CucumberTestReportProjectAction(project);
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath filePath, @Nonnull Launcher launcher,
        @Nonnull TaskListener taskListener) throws InterruptedException, IOException {
        generateReport(run, filePath, taskListener);

        SafeArchiveServingRunAction caa = new SafeArchiveServingRunAction(
            new File(run.getRootDir(), "cucumber-reports-with-handlebars"),
            "cucumber-reports-with-handlebars",
            CucumberReportBuilder.FEATURES_OVERVIEW_HTML,
            CucumberTestReportBaseAction.ICON_LOCATON,
            CucumberTestReportBaseAction.DISPLAY_NAME);
        run.addAction(caa);
    }

    @Extension
    public static class DescriptorImpl extends CucumberTestReportBuildStepDescriptor {
    }
}

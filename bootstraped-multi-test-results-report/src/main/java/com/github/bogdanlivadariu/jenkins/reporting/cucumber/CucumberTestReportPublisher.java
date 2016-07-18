package com.github.bogdanlivadariu.jenkins.reporting.cucumber;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.tools.ant.DirectoryScanner;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import com.github.bogdanlivadariu.reporting.cucumber.builder.CucumberReportBuilder;
import com.github.bogdanlivadariu.reporting.cucumber.helpers.SpecialProperties;
import com.github.bogdanlivadariu.reporting.cucumber.helpers.SpecialProperties.SpecialKeyProperties;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.Computer;
import hudson.model.Result;
import hudson.slaves.SlaveComputer;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.FormValidation;

@SuppressWarnings("unchecked")
public class CucumberTestReportPublisher extends Recorder {

    private static final String DEFAULT_FILE_INCLUDE_PATTERN = "**/*.json";

    private final String jsonReportDirectory;

    private final String fileIncludePattern;

    private final String fileExcludePattern;

    private final boolean markAsUnstable;

    private final boolean copyHTMLInWorkspace;

    private final boolean ignoreUndefinedSteps;

    private final SpecialProperties props;

    @DataBoundConstructor
    public CucumberTestReportPublisher(String jsonReportDirectory, String fileIncludePattern, String fileExcludePattern,
        boolean markAsUnstable, boolean copyHTMLInWorkspace, boolean ignoreUndefinedSteps) {
        this.jsonReportDirectory = jsonReportDirectory;
        this.fileIncludePattern = fileIncludePattern;
        this.fileExcludePattern = fileExcludePattern;
        this.markAsUnstable = markAsUnstable;
        this.copyHTMLInWorkspace = copyHTMLInWorkspace;
        this.ignoreUndefinedSteps = ignoreUndefinedSteps;

        SpecialProperties props = new SpecialProperties();
        props.getProperties().put(SpecialKeyProperties.IGNORE_UNDEFINED_STEPS, isIgnoreUndefinedSteps());
        this.props = props;

    }

    public String getJsonReportDirectory() {
        return this.jsonReportDirectory;
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

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener)
        throws IOException, InterruptedException {

        listener.getLogger().println("[CucumberReportPublisher] Compiling Cucumber Html Reports ...");

        // source directory (possibly on slave)
        FilePath workspaceJsonReportDirectory;
        if (getJsonReportDirectory().isEmpty()) {
            workspaceJsonReportDirectory = build.getWorkspace();
        } else {
            workspaceJsonReportDirectory = new FilePath(build.getWorkspace(), getJsonReportDirectory());
        }

        // target directory (always on master)
        File targetBuildDirectory = new File(build.getRootDir(), "cucumber-reports-with-handlebars");
        if (!targetBuildDirectory.exists()) {
            targetBuildDirectory.mkdirs();
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
                List<String> fullJsonPaths = new ArrayList<String>();
                // reportBuilder.generateReports();
                for (String fi : jsonReportFiles) {
                    fullJsonPaths.add(targetBuildJsonDirectory + "/" + fi);
                }
                for (String ss : fullPathToJsonFiles(jsonReportFiles, targetBuildJsonDirectory)) {
                    listener.getLogger().println("processing: " + ss);
                }
                CucumberReportBuilder rep = new CucumberReportBuilder(
                    fullPathToJsonFiles(jsonReportFiles, targetBuildJsonDirectory),
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
                        new FilePath(build.getWorkspace(), "cucumber-reports-with-handlebars");
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

        build.addAction(new CucumberTestReportBuildAction(build));
        build.setResult(result);

        return true;
    }

    private List<String> fullPathToJsonFiles(String[] jsonFiles, File targetBuildDirectory) {
        List<String> fullPathList = new ArrayList<String>();
        for (String file : jsonFiles) {
            fullPathList.add(new File(targetBuildDirectory, file).getAbsolutePath());
        }
        return fullPathList;
    }

    @Override
    public Action getProjectAction(AbstractProject< ? , ? > project) {
        return new CucumberTestReportProjectAction(project);
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {
        @Override
        public String getDisplayName() {
            return "Publish cucumber reports generated with handlebars";
        }

        // Performs on-the-fly validation on the file mask wildcard.
        public FormValidation doCheck(@AncestorInPath AbstractProject project, @QueryParameter String value)
            throws IOException, ServletException {
            FilePath ws = project.getSomeWorkspace();
            return ws != null ? ws.validateRelativeDirectory(value) : FormValidation.ok();
        }

        @Override
        public boolean isApplicable(Class< ? extends AbstractProject> jobType) {
            return true;
        }
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }
}

package com.github.bogdanlivadariu.jenkins.reporting.rspec;

import com.github.bogdanlivadariu.jenkins.reporting.SafeArchiveServingRunAction;
import com.github.bogdanlivadariu.reporting.rspec.builder.RSpecReportBuilder;
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
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class RSpecTestReportPublisher extends Publisher implements SimpleBuildStep {

    private static final String DEFAULT_FILE_INCLUDE_PATTERN = "**/*.xml";

    private final String jsonReportDirectory;

    private final String fileIncludePattern;

    private final String fileExcludePattern;

    private final boolean markAsUnstable;

    private final boolean copyHTMLInWorkspace;

    @DataBoundConstructor
    public RSpecTestReportPublisher(String jsonReportDirectory, String fileIncludePattern, String fileExcludePattern,
        boolean markAsUnstable, boolean copyHTMLInWorkspace) {
        this.jsonReportDirectory = jsonReportDirectory;
        this.fileIncludePattern = fileIncludePattern;
        this.fileExcludePattern = fileExcludePattern;
        this.markAsUnstable = markAsUnstable;
        this.copyHTMLInWorkspace = copyHTMLInWorkspace;
    }

    public String getJsonReportDirectory() {
        return jsonReportDirectory;
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

    public boolean generateReports(Run<?, ?> build, FilePath workspace, TaskListener listener)
        throws IOException, InterruptedException {

        listener.getLogger().println("[RSpecReportPublisher] Compiling RSpec Html Reports ...");

        // source directory (possibly on slave)
        FilePath workspaceJsonReportDirectory;
        if (getJsonReportDirectory().isEmpty()) {
            workspaceJsonReportDirectory = workspace;
        } else {
            workspaceJsonReportDirectory = new FilePath(workspace, getJsonReportDirectory());
        }

        // target directory (always on master)
        File targetBuildDirectory = new File(build.getRootDir(), "rspec-reports-with-handlebars");
        if (!targetBuildDirectory.exists()) {
            if (!targetBuildDirectory.mkdirs()) {
                listener.getLogger().println("target dir was not created !!!");
            }
        }
        String remoteWS = workspaceJsonReportDirectory != null ? workspaceJsonReportDirectory.getRemote() : "";

        if (Computer.currentComputer() instanceof SlaveComputer) {
            listener.getLogger().println(
                "[RSpec test report builder] Copying XML files from slave: "
                    + remoteWS + " to master reports directory: " + targetBuildDirectory);
        } else {
            listener.getLogger().println(
                "[RSpec test report builder] Copying XML files from: "
                    + remoteWS + " to reports directory: " + targetBuildDirectory);
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
        String[] jsonReportFiles =
            findJsonFiles(targetBuildJsonDirectory, getFileIncludePattern(), getFileExcludePattern());
        if (jsonReportFiles.length > 0) {
            listener.getLogger().println(
                String.format("[RSpecReportPublisher] Found %d xml files.", jsonReportFiles.length));
            int jsonIndex = 0;
            for (String jsonReportFile : jsonReportFiles) {
                listener.getLogger().println(
                    "[RSpec test report builder] " + jsonIndex + ". Found a xml file: " + jsonReportFile);
                jsonIndex++;
            }
            listener.getLogger().println("[RSpec test report builder] Generating HTML reports");

            try {
                for (String ss : fullPathToXmlFiles(jsonReportFiles, targetBuildJsonDirectory)) {
                    listener.getLogger().println("processing: " + ss);
                }
                RSpecReportBuilder rep =
                    new RSpecReportBuilder(fullPathToXmlFiles(jsonReportFiles, targetBuildJsonDirectory),
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
                        new FilePath(workspace, "rspec-reports-with-handlebars");
                    if (workspaceCopyDirectory.exists()) {
                        workspaceCopyDirectory.deleteRecursive();
                    }
                    listener.getLogger().println(
                        "[RSpec test report builder] Copying report to workspace directory: " + workspaceCopyDirectory
                            .toURI());
                    new FilePath(targetBuildDirectory).copyRecursiveTo("**/*.html", workspaceCopyDirectory);
                }

            } catch (Exception e) {
                result = Result.FAILURE;
                listener.getLogger().println(
                    "[RSpec test report builder] there was an error generating the reports: " + e);
                for (StackTraceElement error : e.getStackTrace()) {
                    listener.getLogger().println(error);
                }
            }
        } else {
            result = Result.SUCCESS;
            listener.getLogger().println(
                "[RSpec test report builder] xml path for the reports might be wrong, " + targetBuildDirectory);
        }
        build.setResult(result);

        return true;
    }

    private List<String> fullPathToXmlFiles(String[] xmlFiles, File targetBuildDirectory) {
        List<String> fullPathList = new ArrayList<String>();
        for (String file : xmlFiles) {
            fullPathList.add(new File(targetBuildDirectory, file).getAbsolutePath());
        }
        return fullPathList;
    }

    @Override
    public Action getProjectAction(AbstractProject<?, ?> project) {
        return new RSpecTestReportProjectAction(project);
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath workspace, @Nonnull Launcher launcher,
        @Nonnull TaskListener listener) throws InterruptedException, IOException {
        listener.getLogger().println("[RSpecReportPublisher] searching for files ...");
        generateReports(run, workspace, listener);
        SafeArchiveServingRunAction caa = new SafeArchiveServingRunAction(
            new File(run.getRootDir(), "rspec-reports-with-handlebars"),
            "rspec-reports-with-handlebars",
            RSpecReportBuilder.SUITES_OVERVIEW,
            RSpecTestReportBaseAction.ICON_LOCATON,
            "Publish Rspec reports generated with handlebars");
        run.addAction(caa);
    }

    @Extension
    public static class DescriptorImpl extends RSpecTestReportBuildStepDescriptor {
    }
}

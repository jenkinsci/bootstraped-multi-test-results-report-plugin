package com.github.bogdanlivadariu.jenkins.reporting.testng;

import com.github.bogdanlivadariu.jenkins.reporting.SafeArchiveServingRunAction;
import com.github.bogdanlivadariu.reporting.testng.builder.TestNgReportBuilder;
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


public class TestNGTestReportPublisher extends Publisher implements SimpleBuildStep {

    private static final String DEFAULT_FILE_INCLUDE_PATTERN = "**/*.xml";

    private final String jsonReportDirectory;

    private final String fileIncludePattern;

    private final String fileExcludePattern;

    private final boolean markAsUnstable;

    private final boolean copyHTMLInWorkspace;

    @DataBoundConstructor
    public TestNGTestReportPublisher(String jsonReportDirectory, String fileIncludePattern, String fileExcludePattern,
        boolean markAsUnstable, boolean copyHTMLInWorkspace) {
        this.jsonReportDirectory = jsonReportDirectory;
        this.fileIncludePattern = fileIncludePattern;
        this.fileExcludePattern = fileExcludePattern;
        this.markAsUnstable = markAsUnstable;
        this.copyHTMLInWorkspace = copyHTMLInWorkspace;
    }

    @Override public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath workspace, @Nonnull Launcher launcher,
        @Nonnull TaskListener listener) throws InterruptedException, IOException {
        listener.getLogger().println("[TestNGReportPublisher] searching for files ...");
        generateReports(run, workspace, listener);
        SafeArchiveServingRunAction caa = new SafeArchiveServingRunAction(
            new File(run.getRootDir(), "testng-reports-with-handlebars"),
            "testng-reports-with-handlebars",
            "testsByClassOverview.html",
            TestNGTestReportBaseAction.ICON_LOCATON,
            "TestNG Reports");
        run.addAction(caa);
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override
    public Action getProjectAction(AbstractProject<?, ?> project) {
        return new TestNGTestReportProjectAction(project);
    }

    private String[] findFiles(File targetDirectory, String fileIncludePattern, String fileExcludePattern) {
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

    public String getJsonReportDirectory() {
        return jsonReportDirectory;
    }

    public boolean isCopyHTMLInWorkspace() {
        return copyHTMLInWorkspace;
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

    private void generateReports(Run<?, ?> build, FilePath workspace, TaskListener listener)
        throws IOException, InterruptedException {
        // source directory (possibly on slave)
        FilePath workspaceJsonReportDirectory;
        if (getJsonReportDirectory().isEmpty()) {
            workspaceJsonReportDirectory = workspace;
        } else {
            workspaceJsonReportDirectory = new FilePath(workspace, getJsonReportDirectory());
        }

        // target directory (always on master)
        File targetBuildDirectory = new File(build.getRootDir(), "testng-reports-with-handlebars");
        if (!targetBuildDirectory.exists()) {
            targetBuildDirectory.mkdirs();
        }

        if (Computer.currentComputer() instanceof SlaveComputer) {
            listener.getLogger().println(
                "[TestNG test report builder] Copying XML files from slave: "
                    + workspaceJsonReportDirectory.getRemote() + " to master reports directory: "
                    + targetBuildDirectory);
        } else {
            listener.getLogger().println(
                "[TestNG test report builder] Copying XML files from: "
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
        String[] jsonReportFiles =
            findFiles(targetBuildJsonDirectory, getFileIncludePattern(), getFileExcludePattern());
        if (jsonReportFiles.length > 0) {
            listener.getLogger().println(
                String.format("[TestNGReportPublisher] Found %d xml files.", jsonReportFiles.length));
            int jsonIndex = 0;
            for (String jsonReportFile : jsonReportFiles) {
                listener.getLogger().println(
                    "[TestNG test report builder] " + jsonIndex + ". Found a xml file: " + jsonReportFile);
                jsonIndex++;
            }
            listener.getLogger().println("[TestNG test report builder] Generating HTML reports");

            try {
                List<String> fullJsonPaths = new ArrayList<String>();
                // reportBuilder.generateReports();
                for (String fi : jsonReportFiles) {
                    fullJsonPaths.add(targetBuildJsonDirectory + "/" + fi);
                }
                for (String ss : fullPathToXmlFiles(jsonReportFiles, targetBuildJsonDirectory)) {
                    listener.getLogger().println("processing: " + ss);
                }
                TestNgReportBuilder rep =
                    new TestNgReportBuilder(fullPathToXmlFiles(jsonReportFiles, targetBuildJsonDirectory),
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
                        new FilePath(workspace, "testng-reports-with-handlebars");
                    if (workspaceCopyDirectory.exists()) {
                        workspaceCopyDirectory.deleteRecursive();
                    }
                    listener.getLogger().println(
                        "[TestNG test report builder] Copying report to workspace directory: " + workspaceCopyDirectory
                            .toURI());
                    new FilePath(targetBuildDirectory).copyRecursiveTo("**/*.html", workspaceCopyDirectory);
                }

            } catch (Exception e) {
                result = Result.FAILURE;
                listener.getLogger().println(
                    "[TestNG test report builder] there was an error generating the reports: " + e);
                for (StackTraceElement error : e.getStackTrace()) {
                    listener.getLogger().println(error);
                }
            }
        } else {
            result = Result.SUCCESS;
            listener.getLogger().println(
                "[TestNG test report builder] xml path for the reports might be wrong, " + targetBuildDirectory);
        }
        build.setResult(result);
    }

    private List<String> fullPathToXmlFiles(String[] xmlFiles, File targetBuildDirectory) {
        List<String> fullPathList = new ArrayList<String>();
        for (String file : xmlFiles) {
            fullPathList.add(new File(targetBuildDirectory, file).getAbsolutePath());
        }
        return fullPathList;
    }

    @Extension
    public static class DescriptorImpl extends TestNGTestReportBuildStepDescriptor {
    }
}

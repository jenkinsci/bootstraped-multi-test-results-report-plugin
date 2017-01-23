package com.github.bogdanlivadariu.jenkins.reporting;

import org.apache.tools.ant.DirectoryScanner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bobo on 24/01/2017.
 */
public class Helper {
    public static List<String> fullPathToFiles(String[] files, File targetBuildDirectory) {
        List<String> fullPathList = new ArrayList<String>();
        for (String file : files) {
            fullPathList.add(new File(targetBuildDirectory, file).getAbsolutePath());
        }
        return fullPathList;
    }

    public static String[] findFiles(File targetDirectory, String fileIncludePattern, String fileExcludePattern,
        final String DEFAULT_FILE_INCLUDE_PATTERN) {
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
}

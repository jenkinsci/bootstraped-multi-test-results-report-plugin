package com.github.bogdanlivadariu.jenkins.reporting;

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
}

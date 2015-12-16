package com.github.bogdanlivadariu.reporting.commons;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class ResourceUtils {
    public static String unzipToFile(File sourceZipFile, String destPath) throws ZipException {
        ZipFile zipFile = new ZipFile(sourceZipFile);
        zipFile.extractAll(destPath);

        return destPath;
    }

    public static void unzipFileToDest(String zipName, String destFolder)
        throws IOException, URISyntaxException, ZipException {
        File tmpDir = new File(destFolder);
        File tempFile = new File(tmpDir.getAbsoluteFile(), zipName);
        InputStream fileIntputStream =
            ResourceUtils.class.getResourceAsStream("/zip-files/" + zipName);

        FileUtils.copyInputStreamToFile(fileIntputStream, tempFile);
        unzipToFile(tempFile, tmpDir.getAbsolutePath());
        tempFile.delete();
    }
}

package com.github.bogdanlivadariu.reporting.commons;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.github.bogdanlivadariu.reporting.commons.ResourceUtil.WEB_RESOURCE;
import org.junit.jupiter.api.Test;

class ResourceUtilTest {

    @Test
    void testResourceCopy() throws IOException {
        File target = new File("tmpDir");
        WEB_RESOURCE resource = WEB_RESOURCE.JQUERY;

        ResourceUtil.copyResource(resource, target.getAbsolutePath());
        assertTrue(target.listFiles()[0].getName().contains(resource.toString()));

        FileUtils.deleteDirectory(target);
    }
}

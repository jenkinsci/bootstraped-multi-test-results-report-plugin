import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import com.github.bogdanlivadariu.reporting.commons.ResourceUtils;

import net.lingala.zip4j.exception.ZipException;

public class UnzipTest {

    @Test
    public void unzipTest() throws IOException, URISyntaxException, ZipException {
        File destFile = new File("tmpResFile");
        ResourceUtils.unzipFileToDest("bootstrap-3.3.6.zip", destFile.getAbsolutePath());
        Assert.assertTrue(destFile.listFiles()[0].getName().contains("bootstrap-3.3.6-dist"));
        FileUtils.deleteDirectory(destFile);
    }
}

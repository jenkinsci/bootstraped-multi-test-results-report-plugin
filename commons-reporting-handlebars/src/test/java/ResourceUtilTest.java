import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.github.bogdanlivadariu.reporting.commons.ResourceUtil;
import com.github.bogdanlivadariu.reporting.commons.ResourceUtil.WEB_RESOURCE;

public class ResourceUtilTest {
    @Test
    public void testResourceCopy() throws IOException, InstantiationException, IllegalAccessException {
        File target = new File("tmpDir");
        WEB_RESOURCE resource = WEB_RESOURCE.JQUERY;

        ResourceUtil.copyResource(resource, target.getAbsolutePath());
        assertTrue(target.listFiles()[0].getName().contains(resource.toString()));

        FileUtils.deleteDirectory(target);
    }
}

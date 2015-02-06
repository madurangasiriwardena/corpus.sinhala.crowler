package corpus.sinhala.crawler;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import junit.framework.TestCase;

public class LakbimaParserTest extends TestCase {

	private static LakbimaParser parser;
    @BeforeClass
    public void setUp() throws Exception {
        
        String content = Files.toString(new File("/home/chamila/semester7/fyp/corpus.sinhala.crowler/crawlers/corpus.sinhala.crawler.lakbima/src/test/resources/web/news1.html"), Charsets.UTF_8);
//      System.out.println("hhhhhhhhhh");
        parser = new LakbimaParser(content, "http://www.lakbima.lk/index.php?option=com_content&view=article&id=43577&catid=62&Itemid=84");
    }

    @After
    public void after() throws Exception {
        //server.stop();
    }

    @Test
	public void testGetTitle() throws IOException {

		assertEquals("අගමැතිත් විපක්ෂනායකත් පත් කරන මෙහෙම ආණ්ඩුවක් ලෝකෙ කොහෙවත් නෑ", parser.getTitle());
	}

    @Test
	public void testGetAuthor() {
    	assertEquals("", parser.getAuthor());
	}

    @Test
	public void testGetContent() throws IOException {
    	String content = Files.toString(new File("/home/chamila/semester7/fyp/corpus.sinhala.crowler/crawlers/corpus.sinhala.crawler.lakbima/src/test/resources/Content.txt"), Charsets.UTF_8);
    	assertEquals(content, parser.getContent());
	}

    @Test
	public void testGetUrl() {
    	assertEquals("http://www.lakbima.lk/index.php?option=com_content&view=article&id=43577&catid=62&Itemid=84", parser.getUrl());
	}

    @Test
	public void testGetYear() {
    	assertEquals("", parser.getYear());
	}

    @Test
	public void testGetMonth() {
    	assertEquals("", parser.getMonth());
	}

    @Test
	public void testGetDate() {
    	assertEquals("", parser.getDate());
	}

    @Test
	public void testGetCategory() {
    	assertEquals("NEWS", parser.getCategory());
	}

}

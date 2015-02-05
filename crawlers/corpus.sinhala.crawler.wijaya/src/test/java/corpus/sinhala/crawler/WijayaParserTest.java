package corpus.sinhala.crawler;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import junit.framework.TestCase;

public class WijayaParserTest extends TestCase {

	private static WijayaParser parser;
    @BeforeClass
    public void setUp() throws Exception {
        
        String content = Files.toString(new File("/home/chamila/semester7/fyp/corpus.sinhala.crowler/crawlers/corpus.sinhala.crawler.wijaya/src/test/resources/web/archives/3000/index.html"), Charsets.UTF_8);
//      System.out.println("hhhhhhhhhh");
        parser = new WijayaParser(content, "http://www.wijeya.lk/archives/3300");
    }

    @After
    public void after() throws Exception {
        //server.stop();
    }

    @Test
	public void testGetTitle() throws IOException {

		assertEquals("ස්ටෝන්හෙජ් පරයන අබිරහසක්", parser.getTitle());
	}

    @Test
	public void testGetAuthor() {
    	assertEquals("", parser.getAuthor());
	}

    @Test
	public void testGetContent() throws IOException {
    	String content = Files.toString(new File("/home/chamila/semester7/fyp/corpus.sinhala.crowler/crawlers/corpus.sinhala.crawler.wijaya/src/test/resources/Content.txt"), Charsets.UTF_8);
    	assertEquals(content, parser.getContent());
	}

    @Test
	public void testGetUrl() {
    	assertEquals("http://www.wijeya.lk/archives/3300", parser.getUrl());
	}

    @Test
	public void testGetYear() {
    	assertEquals("2014", parser.getYear());
	}

    @Test
	public void testGetMonth() {
    	assertEquals("6", parser.getMonth());
	}

    @Test
	public void testGetDate() {
    	assertEquals("2", parser.getDate());
	}

    @Test
	public void testGetCategory() {
    	assertEquals("ACADEMIC", parser.getCategory());
	}

}

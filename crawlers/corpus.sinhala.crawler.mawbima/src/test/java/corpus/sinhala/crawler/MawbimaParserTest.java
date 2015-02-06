package corpus.sinhala.crawler;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import junit.framework.TestCase;

public class MawbimaParserTest extends TestCase {

	private static MawbimaParser parser;
    @BeforeClass
    public void setUp() throws Exception {
        
        String content = Files.toString(new File("/home/chamila/semester7/fyp/corpus.sinhala.crowler/crawlers/corpus.sinhala.crawler.mawbima/src/test/resources/web/mawbima/86-78569-news-detail.html"), Charsets.UTF_8);
//      System.out.println("hhhhhhhhhh");
        parser = new MawbimaParser(content, "http://www.mawbima.lk/86-78569-news-detail.html");
    }

    @After
    public void after() throws Exception {
        //server.stop();
    }

    @Test
	public void testGetTitle() throws IOException {

		assertEquals("වරාය නගරයේ ඉදිකිරීම් නොනවත්වන්න", parser.getTitle());
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

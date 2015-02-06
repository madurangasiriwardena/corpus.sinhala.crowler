package corpus.sinhala.crawler;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import junit.framework.TestCase;

public class AdaderanaParserTest extends TestCase {

	private static AdaderanaParser parser;
    @BeforeClass
    public void setUp() throws Exception {
        
        String content = Files.toString(new File("/home/chamila/semester7/fyp/corpus.sinhala.crowler/crawlers/corpus.sinhala.crawler.adaderana/src/test/resources/web/news1.html"), Charsets.UTF_8);
//      System.out.println("hhhhhhhhhh");
        parser = new AdaderanaParser(content, "http://sinhala.adaderana.lk/news.php?nid=42879");
    }

    @After
    public void after() throws Exception {
        //server.stop();
    }

    @Test
	public void testGetTitle() throws IOException {

		assertEquals("දිද්දෙණිය කර්මාන්තශාලාවක ගින්නක්", parser.getTitle());
	}

    @Test
	public void testGetAuthor() {
    	assertEquals("", parser.getAuthor());
	}

    @Test
	public void testGetContent() throws IOException {
    	String content = Files.toString(new File("/home/chamila/semester7/fyp/corpus.sinhala.crowler/crawlers/corpus.sinhala.crawler.adaderana/src/test/resources/Content.txt"), Charsets.UTF_8);
    	assertEquals(content, parser.getContent());
	}

    @Test
	public void testGetUrl() {
    	assertEquals("http://sinhala.adaderana.lk/news.php?nid=42879", parser.getUrl());
	}

    @Test
	public void testGetYear() {
    	assertEquals("2014", parser.getYear());
	}

    @Test
	public void testGetMonth() {
    	assertEquals("04", parser.getMonth());
	}

    @Test
	public void testGetDate() {
    	assertEquals("30", parser.getDate());
	}

    @Test
	public void testGetCategory() {
    	assertEquals("NEWS", parser.getCategory());
	}

}
